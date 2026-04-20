package com.airbnb.deeplinkdispatch.gradleplugin

import com.airbnb.deeplinkdispatch.base.ManifestGeneration
import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.HasHostTests
import com.android.manifmerger.ManifestMerger2
import com.google.devtools.ksp.gradle.KspExtension
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
        // Using pluginManager.withPlugin ensures we apply the arg whenever KSP is present,
        // regardless of whether it is applied before or after this plugin.
        project.pluginManager.withPlugin("com.google.devtools.ksp") {
            val ksp = project.extensions.getByType(KspExtension::class.java)
            ksp.arg(ManifestGeneration.OPTION_USE_ASSET_BASED_MATCH_INDEX, "true")
        }

        val androidComponents = project.extensions.getByType(
            AndroidComponentsExtension::class.java
        )

        androidComponents.onVariants { variant ->
            val mergeType = ManifestMerger2.MergeType.LIBRARY
            val variantName = variant.name
            val variantCapitalized = variantName.replaceFirstChar { it.uppercase() }
            val kspTaskName = "ksp${variantCapitalized}Kotlin"

            // Build-dir-relative paths (all centralized in ManifestGeneration)
            val kspGeneratedManifestPath = ManifestGeneration.kspGeneratedManifestPath(variantName)
            val kspGeneratedAssetsPath = ManifestGeneration.kspGeneratedAssetsDir(variantName)
            val kspKotlinOutputPath = "generated/ksp/$variantName/kotlin"
            val safeManifestPath = "intermediates/deeplinkdispatch/$variantName/AndroidManifest.xml"
            val safeAssetsPath = "intermediates/deeplinkdispatch/$variantName/assets"

            val manifestMergeTask = project.tasks.register(
                GenerateManifestIntentFiltersForDeeplinkDispatchTask.taskName(variant),
                GenerateManifestIntentFiltersForDeeplinkDispatchTask::class.java
            ) {
                generatedManifestPath.set(project.layout.buildDirectory.file(safeManifestPath))
                kspOutputDirectory.set(project.layout.buildDirectory.dir(kspKotlinOutputPath))
                this.mergeType.set(mergeType)
                group = "deeplinkdispatch"
                description = "Merges KSP-generated manifest for $variantName"
            }

            variant.artifacts.use(manifestMergeTask)
                .wiredWithFiles(
                    GenerateManifestIntentFiltersForDeeplinkDispatchTask::mergedManifest,
                    GenerateManifestIntentFiltersForDeeplinkDispatchTask::updatedManifest
                )
                .toTransform(SingleArtifact.MERGED_MANIFEST)

            // Moves the manifest out of the KSP resources dir so it isn't picked up as a Java
            // resource. Must be a separate task (rather than a KSP `doLast`) because `doLast`
            // doesn't run when KSP is restored FROM-CACHE.
            val relocateManifestTask = project.tasks.register(
                "relocateDeepLinkManifest$variantCapitalized",
                RelocateDeepLinkManifestTask::class.java
            ) {
                kspManifestFile.set(project.layout.buildDirectory.file(kspGeneratedManifestPath))
                safeManifestFile.set(project.layout.buildDirectory.file(safeManifestPath))
                group = "deeplinkdispatch"
                description = "Moves DeepLinkDispatch manifest from KSP resources to safe location for $variantName"
            }

            val relocateAssetsTask = project.tasks.register(
                "relocateDeepLinkAssets$variantCapitalized",
                RelocateDeepLinkAssetsTask::class.java
            ) {
                kspAssetsDir.set(project.layout.buildDirectory.dir(kspGeneratedAssetsPath))
                safeAssetsDir.set(project.layout.buildDirectory.dir(safeAssetsPath))
                group = "deeplinkdispatch"
                description = "Moves DeepLinkDispatch assets from KSP resources to safe location for $variantName"
            }

            val mergeAssetsTask = project.tasks.register(
                MergeDeepLinkAssetsTask.taskName(variant),
                MergeDeepLinkAssetsTask::class.java
            ) {
                additionalAssetsDir.set(project.layout.buildDirectory.dir(safeAssetsPath))
                group = "deeplinkdispatch"
                description = "Merges DeepLinkDispatch assets for $variantName"
            }

            variant.artifacts.use(mergeAssetsTask)
                .wiredWithDirectories(
                    MergeDeepLinkAssetsTask::inputAssets,
                    MergeDeepLinkAssetsTask::outputAssets
                )
                .toTransform(SingleArtifact.ASSETS)

            // Merge tasks always run after their respective relocate task; these are in-plugin
            // task providers so they can be wired lazily without `findByName`.
            manifestMergeTask.configure { dependsOn(relocateManifestTask) }
            mergeAssetsTask.configure { dependsOn(relocateAssetsTask) }

            // Wire KSP -> relocate only when the KSP plugin is applied. We use `tasks.matching`
            // (not `named`) because KSP registers its variant-specific tasks lazily during
            // project evaluation, possibly after this block runs.
            project.pluginManager.withPlugin("com.google.devtools.ksp") {
                project.tasks.matching { it.name == kspTaskName }.configureEach {
                    // no-op; ensures the task is realized
                }
                relocateManifestTask.configure {
                    dependsOn(project.tasks.matching { it.name == kspTaskName })
                }
                relocateAssetsTask.configure {
                    dependsOn(project.tasks.matching { it.name == kspTaskName })
                }
            }

            // Make the library's Java-resource and jar-bundling tasks run after our relocate
            // tasks so the KSP-generated files aren't copied into the AAR as Java resources.
            val javaResourceTaskNames = setOf(
                "process${variantCapitalized}JavaRes",
                "merge${variantCapitalized}JavaResource",
                "bundleLibCompileToJar$variantCapitalized",
                "bundleLibRuntimeToJar$variantCapitalized",
                "sync${variantCapitalized}LibJars",
            )
            project.tasks.matching { it.name in javaResourceTaskNames }.configureEach {
                dependsOn(relocateManifestTask, relocateAssetsTask)
            }

            // Also transform the manifest for host tests (unit tests) so that Robolectric tests
            // can access the merged intent filters via PackageManager.
            (variant as? HasHostTests)?.hostTests?.forEach { (_, hostTest) ->
                val hostTestManifestMergeTask = project.tasks.register(
                    "${hostTest.name}GenerateManifestIntentFiltersForDeepLinkDispatch",
                    GenerateManifestIntentFiltersForDeeplinkDispatchTask::class.java
                ) {
                    generatedManifestPath.set(project.layout.buildDirectory.file(safeManifestPath))
                    kspOutputDirectory.set(project.layout.buildDirectory.dir(kspKotlinOutputPath))
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

                hostTestManifestMergeTask.configure { dependsOn(relocateManifestTask) }
                project.pluginManager.withPlugin("com.google.devtools.ksp") {
                    hostTestManifestMergeTask.configure {
                        dependsOn(project.tasks.matching { it.name == kspTaskName })
                    }
                }
            }
        }
    }
}
