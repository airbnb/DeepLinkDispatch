package com.airbnb.deeplinkdispatch.gradleplugin

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import com.android.manifmerger.ManifestMerger2
import com.android.manifmerger.MergingReport
import com.android.utils.StdLogger
import com.google.devtools.ksp.gradle.KspExtension
import com.google.devtools.ksp.gradle.KspGradleSubplugin

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project


import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * Automatic manifest generation plugin for DeepLinkDispatch.
 *
 * For deep links that have activityClasFqn set, the DeepLinkDispatch annotation processor generates
 * an AndroidManifest.xml that contains the intent filters for the deeplinks.
 *
 * This plugin uses AGP's Artifacts API to transform the MERGED_MANIFEST artifact,
 * merging in the KSP-generated manifest with intent filters.
 *
 * This means that for any deep links that have activityClasFqn set it is not necessary to add the
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
                3. Activities with deep link annotations (with activityClasFqn parameter) must be in library modules
                4. Your app module will automatically inherit the intent filters from library AARs through normal manifest merging

                For more details, see the DeepLinkDispatch documentation.
                """.trimIndent()
            )
        }

        // If the KSP plugin was already applied, we need to update the args for the manifest
        // generation.
        if (project.plugins.hasPlugin(KspGradleSubplugin::class.java)) {
            updateKspArgsForManifestGeneration(project)
        }

        // Just in case listen to when the KSP plugin is applied and update the args for the
        // manifest generation. This can happen if the KSP plugin is applied after this plugin.
        project.plugins.whenPluginAdded {
            when (this) {
                is KspGradleSubplugin -> {
                    updateKspArgsForManifestGeneration(project)
                }
            }
        }

        val androidComponents = project.extensions.getByType(
            AndroidComponentsExtension::class.java
        )

        androidComponents.onVariants { variant ->
            val generatedManifestFile = generatedManifestFile(project)

            val manifestMergeTask = project.tasks.register(
                GenerateManifestIntentFiltersForDeeplinkDispatchTask.taskName(variant),
                GenerateManifestIntentFiltersForDeeplinkDispatchTask::class.java
            ) {
                // Don't set manifestIntentFiltersFile during configuration
                // Instead, the task will look for it at the expected location during execution
                generatedManifestPath.set(project.layout.buildDirectory.file(
                    "intermediates/deeplinkdispatch/AndroidManifest.xml"
                ))
                // Set KSP output directory as an input to establish dependency on KSP task
                kspOutputDirectory.set(project.layout.buildDirectory.dir(
                    "generated/ksp/${variant.name}/kotlin"
                ))
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

            // Configure task ordering AFTER evaluation when KSP task exists
            project.afterEvaluate {
                val kspTaskName = "ksp${variant.name.replaceFirstChar { it.uppercase() }}Kotlin"
                val kspTask = project.tasks.findByName(kspTaskName)

                if (kspTask != null) {
                    // Explicitly depend on KSP task to ensure it runs first
                    manifestMergeTask.configure {
                        dependsOn(kspTask)
                    }
                    println("Configured ${manifestMergeTask.name} to depend on $kspTaskName")
                } else {
                    println("Warning: KSP task $kspTaskName not found")
                }
            }

            println("Configured manifest generation for ${variant.name} with generated manifest at: ${generatedManifestFile.absolutePath}")
        }

    }

    private fun generatedManifestFile(project: Project) =
        File(project.layout.buildDirectory.asFile.get(), "intermediates/deeplinkdispatch/AndroidManifest.xml")

    /**
     * Configures KSP to generate the manifest file with intent filters.
     *
     * This tells the DeepLinkDispatch annotation processor (running via KSP) where to write
     * the generated AndroidManifest.xml file containing intent filters for deeplinks with
     * activityClasFqn set.
     *
     * Note: This only works with KSP. KAPT does not support this feature.
     */
    private fun updateKspArgsForManifestGeneration(project: Project) {
        val kspExtension = project.extensions.getByType(KspExtension::class.java)
        kspExtension.arg(
            "deepLinkManifestGenMetadata.output",
            generatedManifestFile(project).canonicalPath
        )
    }
}

/**
 * Gradle task that merges KSP-generated manifest with AGP's merged manifest.
 *
 * This task is wired into AGP's Artifacts API as a MERGED_MANIFEST transformer.
 *
 * IMPORTANT: Due to circular dependencies in the build graph, this approach
 * may not work for app modules but might work for library modules.
 */
abstract class GenerateManifestIntentFiltersForDeeplinkDispatchTask: DefaultTask() {

    /**
     * The path where KSP will generate the manifest file.
     * We use @Internal instead of @InputFile to avoid Gradle validation errors
     * when the file doesn't exist yet.
     */
    @get:org.gradle.api.tasks.Internal
    abstract val generatedManifestPath: RegularFileProperty

    /**
     * The KSP output directory. By declaring this as an input, we establish
     * a dependency on the KSP task, ensuring KSP runs before our task.
     */
    @get:InputFiles
    @get:Optional
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val kspOutputDirectory: DirectoryProperty

    /**
     * The merged manifest file created by AGP.
     */
    @get:InputFile
    abstract val mergedManifest: RegularFileProperty

    /**
     * The output location for the merged manifest.
     */
    @get:OutputFile
    abstract val updatedManifest: RegularFileProperty

    @TaskAction
    fun taskAction() {
        // The input manifest (the merged manifest as created by the Android Gradle Plugin)
        val inputManifest = mergedManifest.asFile.get()
        val outputManifest = updatedManifest.asFile.get()

        // Check if the generated manifest file exists and has content
        val generatedManifestFile = generatedManifestPath.asFile.get()
        val hasGeneratedManifest = generatedManifestFile.exists() &&
                                   generatedManifestFile.length() > 0

        if (!hasGeneratedManifest) {
            println("No DeepLinkDispatch generated manifest found or manifest is empty. " +
                    "Copying input manifest to output without modifications.")
            inputManifest.copyTo(outputManifest, overwrite = true)
            return
        }

        // Determine merge type based on plugin
        val mergeType = if (project.plugins.hasPlugin("com.android.application")) {
            ManifestMerger2.MergeType.APPLICATION
        } else if (project.plugins.hasPlugin("com.android.library")) {
            ManifestMerger2.MergeType.LIBRARY
        } else {
            error("Unsupported plugin type ${this::class.java}. You can only apply this plugin" +
                    " to an Android application or library modules")
        }

        // Merge the manifests using AGP's manifest merger
        // Use USES_SDK_IN_MANIFEST_LENIENT_HANDLING feature to allow <uses-sdk> elements
        // AGP injects <uses-sdk> into MERGED_MANIFEST but AGP 9.0+ validation rejects it
        // This feature flag makes it a warning instead of an error
        val invoker: ManifestMerger2.Invoker = ManifestMerger2.newMerger(
            inputManifest,
            StdLogger(StdLogger.Level.VERBOSE),
            mergeType
        ).withFeatures(ManifestMerger2.Invoker.Feature.USES_SDK_IN_MANIFEST_LENIENT_HANDLING)

        // generatedManifestFile is guaranteed to be non-null here due to hasGeneratedManifest check
        invoker.addFlavorAndBuildTypeManifest(generatedManifestFile)

        val merge = invoker.merge()
        if (merge.result.isSuccess) {
            val mergedDocument = merge.getMergedDocument(MergingReport.MergedManifestKind.MERGED)
                ?: error("Failed to get merged document")
            outputManifest.writeText(mergedDocument)
            println("Successfully merged DeepLinkDispatch manifest. Output written to: ${outputManifest.absolutePath}")
        } else {
            val errorMessage = buildString {
                appendLine("Manifest merge for DeepLinkDispatch failed with error: ${merge.result}")
                merge.loggingRecords.forEach { record ->
                    appendLine("  ${record.severity}: ${record.message}")
                }
            }
            error(errorMessage)
        }
    }

    companion object {
        internal fun taskName(variant: Variant) =
            variant.name + "GenerateManifestIntentFiltersForDeepLinkDispatch"
    }
}
