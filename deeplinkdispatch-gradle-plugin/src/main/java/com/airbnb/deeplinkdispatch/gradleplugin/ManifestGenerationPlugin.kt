package com.airbnb.deeplinkdispatch.gradleplugin

import com.airbnb.deeplinkdispatch.base.ManifestGeneration
import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.HasHostTests
import com.android.manifmerger.ManifestMerger2
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Automatic manifest and asset generation plugin for DeepLinkDispatch.
 *
 * This plugin handles two main features for KSP-based builds:
 *
 * ## 1. Manifest Generation
 * For deep links that have activityClassFqn set, the DeepLinkDispatch annotation processor generates
 * an AndroidManifest.xml that contains the intent filters for the deeplinks. This plugin uses AGP's
 * Artifacts API to transform the MERGED_MANIFEST artifact, merging in the KSP-generated manifest.
 *
 * ## 2. Asset-Based Match Index (KSP only)
 * When using KSP, the processor generates a binary match index file as an Android asset instead of
 * encoding it as strings in the generated registry class. This provides:
 * - Faster build times (no string chunking/encoding)
 * - Faster app startup (direct binary loading, no string decoding)
 * - Smaller APK size (binary assets compress better than dex strings)
 *
 * IMPORTANT: This plugin requires AGP 8.0+ and only works with KSP (Kotlin Symbol Processing).
 * It does NOT work with KAPT. If you're using KAPT:
 * - You must manually add intent filters to your AndroidManifest.xml
 * - The match index will use the legacy string-based approach (still works, just less efficient)
 *
 * How it works (AGP 8.0+):
 * 1. KSP processes annotations and generates:
 *    - A manifest file with intent filters (if activityClassFqn is set)
 *    - A binary match index asset file (assets/deeplinkdispatch/<module>.bin)
 * 2. This plugin uses variant.artifacts.use().toTransform() to intercept:
 *    - SingleArtifact.MERGED_MANIFEST - adds the KSP-generated intent filters
 *    - SingleArtifact.ASSETS - adds the binary match index files
 * 3. The transformed artifacts are used by all subsequent tasks in the build pipeline
 * 4. The final APK/AAR includes both the merged manifest and the binary match index
 *
 * This ensures that generated content is included on the first build without requiring
 * two build passes or manual modifications.
 */
class ManifestGenerationPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        // Validate that this plugin is only applied to library modules
        if (project.plugins.hasPlugin("com.android.application")) {
            throw org.gradle.api.GradleException(
                """
                DeepLinkDispatch manifest generation plugin cannot be applied to application modules.

                This plugin only works with library modules due to AGP 8.x circular dependency limitations.

                To use automatic manifest generation:
                1. Remove this plugin from your app module's build.gradle
                2. Apply the plugin only to library modules that contain deep link activities
                3. Activities with deep link annotations (with activityClassFqn parameter) must be in library modules
                4. Your app module will automatically inherit the intent filters from library AARs through normal manifest merging

                For more details, see the DeepLinkDispatch documentation.
                """.trimIndent()
            )
        }

        // Configure KSP to use asset-based match index when this plugin is applied.
        // This enables the efficient binary asset loading instead of the legacy string-based approach.
        project.afterEvaluate {
            project.extensions.findByName("ksp")?.let { kspExtension ->
                // Use reflection to call arg() method since we don't want to add KSP as a compile dependency
                try {
                    val argMethod = kspExtension::class.java.getMethod("arg", String::class.java, String::class.java)
                    argMethod.invoke(kspExtension, ManifestGeneration.OPTION_USE_ASSET_BASED_MATCH_INDEX, "true")
                } catch (e: Exception) {
                    project.logger.warn("DeepLinkDispatch: Could not configure KSP argument: ${e.message}")
                }
            }
        }

        val androidComponents = project.extensions.getByType(
            AndroidComponentsExtension::class.java
        )

        androidComponents.onVariants { variant ->
            // Merge type is always library as this does not support application modules
            val mergeType = ManifestMerger2.MergeType.LIBRARY

            // Path where KSP writes the manifest via filer API (this is cached by Gradle)
            val kspGeneratedManifestPath = "generated/ksp/${variant.name}/resources/${ManifestGeneration.MANIFEST_RESOURCE_PATH}"
            // Safe location where we move the manifest to prevent it from being picked up by Java resource merge
            val safeManifestPath = "intermediates/deeplinkdispatch/${variant.name}/AndroidManifest.xml"

            val manifestMergeTask = project.tasks.register(
                GenerateManifestIntentFiltersForDeeplinkDispatchTask.taskName(variant),
                GenerateManifestIntentFiltersForDeeplinkDispatchTask::class.java
            ) {
                // Read manifest from safe location (moved there by KSP doLast action)
                generatedManifestPath.set(project.layout.buildDirectory.file(safeManifestPath))
                // Set KSP output directory as an input to establish dependency on KSP task
                kspOutputDirectory.set(project.layout.buildDirectory.dir(
                    "generated/ksp/${variant.name}/kotlin"
                ))
                // Set merge type determined during configuration phase
                this.mergeType.set(mergeType)
                group = "deeplinkdispatch"
                description = "Merges KSP-generated manifest for ${variant.name}"
            }

            // Transform MERGED_MANIFEST
            // By making our task depend on KSP output directory, we establish that KSP must run first
            // This might create a circular dependency in app modules but could work for libraries
            variant.artifacts.use(manifestMergeTask)
                .wiredWithFiles(
                    GenerateManifestIntentFiltersForDeeplinkDispatchTask::mergedManifest,
                    GenerateManifestIntentFiltersForDeeplinkDispatchTask::updatedManifest
                )
                .toTransform(SingleArtifact.MERGED_MANIFEST)

            // Register a task to move the manifest from KSP resources to a safe location.
            // This must be a separate task (not doLast on KSP) because doLast doesn't run
            // when KSP is restored FROM-CACHE.
            val relocateManifestTask = project.tasks.register(
                "relocateDeepLinkManifest${variant.name.replaceFirstChar { it.uppercase() }}",
                RelocateDeepLinkManifestTask::class.java
            ) {
                kspManifestFile.set(project.layout.buildDirectory.file(kspGeneratedManifestPath))
                safeManifestFile.set(project.layout.buildDirectory.file(safeManifestPath))
                group = "deeplinkdispatch"
                description = "Moves DeepLinkDispatch manifest from KSP resources to safe location for ${variant.name}"
            }

            // === Asset handling for binary match index ===
            // Path where KSP writes the asset files via filer API
            val kspGeneratedAssetsPath = "generated/ksp/${variant.name}/resources/assets/${ManifestGeneration.MATCH_INDEX_ASSET_PATH_PREFIX}"
            // Safe location where we move the assets to prevent them from being picked up by Java resource merge
            val safeAssetsPath = "intermediates/deeplinkdispatch/${variant.name}/assets"

            // Register task to relocate assets from KSP output
            val relocateAssetsTask = project.tasks.register(
                "relocateDeepLinkAssets${variant.name.replaceFirstChar { it.uppercase() }}",
                RelocateDeepLinkAssetsTask::class.java
            ) {
                kspAssetsDir.set(project.layout.buildDirectory.dir(kspGeneratedAssetsPath))
                safeAssetsDir.set(project.layout.buildDirectory.dir(safeAssetsPath))
                group = "deeplinkdispatch"
                description = "Moves DeepLinkDispatch assets from KSP resources to safe location for ${variant.name}"
            }

            // Register task to merge assets into the final asset directory
            val mergeAssetsTask = project.tasks.register(
                MergeDeepLinkAssetsTask.taskName(variant),
                MergeDeepLinkAssetsTask::class.java
            ) {
                additionalAssetsDir.set(project.layout.buildDirectory.dir(safeAssetsPath))
                group = "deeplinkdispatch"
                description = "Merges DeepLinkDispatch assets for ${variant.name}"
            }

            // Transform ASSETS artifact to include our binary match index files
            variant.artifacts.use(mergeAssetsTask)
                .wiredWithDirectories(
                    MergeDeepLinkAssetsTask::inputAssets,
                    MergeDeepLinkAssetsTask::outputAssets
                )
                .toTransform(SingleArtifact.ASSETS)

            // Configure task ordering AFTER evaluation when KSP task exists
            project.afterEvaluate {
                val kspTaskName = "ksp${variant.name.replaceFirstChar { it.uppercase() }}Kotlin"
                val kspTask = project.tasks.findByName(kspTaskName)

                if (kspTask != null) {
                    // Relocate tasks run after KSP
                    relocateManifestTask.configure {
                        dependsOn(kspTask)
                    }
                    relocateAssetsTask.configure {
                        dependsOn(kspTask)
                    }
                }

                // Manifest merge task always runs after relocate (regardless of KSP presence)
                manifestMergeTask.configure {
                    dependsOn(relocateManifestTask)
                }

                // Asset merge task depends on asset relocate task
                mergeAssetsTask.configure {
                    dependsOn(relocateAssetsTask)
                }

                // Wire relocate tasks into the library's Java resource processing pipeline.
                // This ensures the manifest and asset files are moved before the library's resources are processed,
                // so when an app depends on this library, the files won't be in the resources.
                // processJavaRes is critical - it's the task that collects resources from source
                // directories and is what consuming app modules depend on.
                val variantCapitalized = variant.name.replaceFirstChar { it.uppercase() }
                listOf(
                    "process${variantCapitalized}JavaRes",
                    "merge${variantCapitalized}JavaResource",
                    "bundleLibCompileToJar${variantCapitalized}",
                    "bundleLibRuntimeToJar${variantCapitalized}",
                    "sync${variantCapitalized}LibJars"
                ).forEach { taskName ->
                    project.tasks.findByName(taskName)?.let { task ->
                        task.dependsOn(relocateManifestTask)
                        task.dependsOn(relocateAssetsTask)
                    }
                }
            }

            // Also transform the manifest for host tests (unit tests) so that
            // Robolectric tests can access the merged intent filters via PackageManager
            (variant as? HasHostTests)?.hostTests?.forEach { (_, hostTest) ->
                val hostTestManifestMergeTask = project.tasks.register(
                    "${hostTest.name}GenerateManifestIntentFiltersForDeepLinkDispatch",
                    GenerateManifestIntentFiltersForDeeplinkDispatchTask::class.java
                ) {
                    // Read manifest from safe location (moved there by KSP doLast action)
                    generatedManifestPath.set(project.layout.buildDirectory.file(safeManifestPath))
                    kspOutputDirectory.set(project.layout.buildDirectory.dir(
                        "generated/ksp/${variant.name}/kotlin"
                    ))
                    this.mergeType.set(mergeType)
                    group = "deeplinkdispatch"
                    description = "Merges KSP-generated manifest for ${hostTest.name}"
                }

                hostTest.artifacts.use(hostTestManifestMergeTask)
                    .wiredWithFiles(
                        GenerateManifestIntentFiltersForDeeplinkDispatchTask::mergedManifest,
                        GenerateManifestIntentFiltersForDeeplinkDispatchTask::updatedManifest
                    )
                    .toTransform(SingleArtifact.MERGED_MANIFEST)

                // Configure task ordering for host test tasks
                project.afterEvaluate {
                    val kspTaskName = "ksp${variant.name.replaceFirstChar { it.uppercase() }}Kotlin"
                    val kspTask = project.tasks.findByName(kspTaskName)

                    if (kspTask != null) {
                        hostTestManifestMergeTask.configure {
                            dependsOn(kspTask)
                        }
                    }

                    // Host test manifest merge task always depends on relocate task
                    hostTestManifestMergeTask.configure {
                        dependsOn(relocateManifestTask)
                    }
                }
            }
        }
    }
}
