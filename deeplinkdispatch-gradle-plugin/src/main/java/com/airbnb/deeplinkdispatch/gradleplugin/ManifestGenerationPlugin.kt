package com.airbnb.deeplinkdispatch.gradleplugin

import com.airbnb.deeplinkdispatch.base.ManifestGeneration
import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.HasHostTests
import com.android.manifmerger.ManifestMerger2
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Automatic manifest generation plugin for DeepLinkDispatch.
 *
 * For deep links that have activityClassFqn set, the DeepLinkDispatch annotation processor generates
 * an AndroidManifest.xml that contains the intent filters for the deeplinks.
 *
 * This plugin uses AGP's Artifacts API to transform the MERGED_MANIFEST artifact,
 * merging in the KSP-generated manifest with intent filters.
 *
 * This means that for any deep links that have activityClassFqn set it is not necessary to add the
 * intent filters to the AndroidManifest.xml manually.
 *
 * IMPORTANT: This plugin requires AGP 8.0+ and only works with KSP (Kotlin Symbol Processing).
 * It does NOT work with KAPT. If you're using KAPT, you must manually add intent filters
 * to your AndroidManifest.xml.
 *
 * How it works (AGP 8.0+):
 * 1. KSP processes annotations and generates a manifest file with intent filters
 * 2. This plugin uses variant.artifacts.use().toTransform(SingleArtifact.MERGED_MANIFEST)
 *    to intercept the merged manifest and add the KSP-generated intent filters
 * 3. The transformed manifest is used by all subsequent tasks in the build pipeline
 * 4. The final merged manifest with intent filters is packaged into the APK/AAR
 *
 * This ensures that generated manifest entries are included on the first build without requiring
 * two build passes or manual manifest modifications.
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

        val androidComponents = project.extensions.getByType(
            AndroidComponentsExtension::class.java
        )

        androidComponents.onVariants { variant ->
            // Determine merge type during configuration phase
            val mergeType = if (project.plugins.hasPlugin("com.android.application")) {
                ManifestMerger2.MergeType.APPLICATION
            } else if (project.plugins.hasPlugin("com.android.library")) {
                ManifestMerger2.MergeType.LIBRARY
            } else {
                error("Unsupported plugin type. You can only apply this plugin to an Android application or library modules")
            }

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

            // Configure task ordering AFTER evaluation when KSP task exists
            project.afterEvaluate {
                val kspTaskName = "ksp${variant.name.replaceFirstChar { it.uppercase() }}Kotlin"
                val kspTask = project.tasks.findByName(kspTaskName)

                if (kspTask != null) {
                    // Relocate task runs after KSP
                    relocateManifestTask.configure {
                        dependsOn(kspTask)
                    }
                }

                // Manifest merge task always runs after relocate (regardless of KSP presence)
                manifestMergeTask.configure {
                    dependsOn(relocateManifestTask)
                }

                // Wire relocate task into the library's Java resource processing pipeline.
                // This ensures the manifest file is moved before the library's resources are processed,
                // so when an app depends on this library, the file won't be in the resources.
                // processJavaRes is critical - it's the task that collects resources from source
                // directories and is what consuming app modules depend on.
                val variantCapitalized = variant.name.replaceFirstChar { it.uppercase() }
                listOf(
                    "process${variantCapitalized}JavaRes",
                    "merge${variantCapitalized}JavaResource",
                    "bundleLibCompileToJar${variantCapitalized}",
                    "sync${variantCapitalized}LibJars"
                ).forEach { taskName ->
                    project.tasks.findByName(taskName)?.let { task ->
                        task.dependsOn(relocateManifestTask)
                    }
                }
            }

            // Also transform the manifest for host tests (unit tests) so that
            // Robolectric tests can access the merged intent filters via PackageManager
            (variant as? HasHostTests)?.hostTests?.forEach { (testType, hostTest) ->
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
