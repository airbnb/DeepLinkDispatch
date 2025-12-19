package com.airbnb.deeplinkdispatch.gradleplugin

import com.android.build.api.variant.Variant
import com.android.manifmerger.ManifestMerger2
import com.android.manifmerger.MergingReport
import com.android.utils.StdLogger
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.TaskAction

/**
 * Gradle task that merges KSP-generated manifest with AGP's merged manifest.
 *
 * This task is wired into AGP's Artifacts API as a MERGED_MANIFEST transformer.
 *
 * IMPORTANT: Due to circular dependencies in the build graph, this approach
 * will not work for app modules but does work for library modules.
 */
@CacheableTask
abstract class GenerateManifestIntentFiltersForDeeplinkDispatchTask : DefaultTask() {

    /**
     * The path where KSP will generate the manifest file.
     * Marked as optional InputFiles so Gradle tracks changes to this file for up-to-date checking.
     * Note: Using InputFiles instead of InputFile because @Optional doesn't work correctly with
     * @InputFile - Gradle validates file existence even when marked optional, causing failures
     * on clean builds where the KSP-generated file doesn't exist yet.
     */
    @get:InputFiles
    @get:Optional
    @get:PathSensitive(PathSensitivity.RELATIVE)
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
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val mergedManifest: RegularFileProperty

    /**
     * The output location for the merged manifest.
     */
    @get:OutputFile
    abstract val updatedManifest: RegularFileProperty

    /**
     * The merge type to use (APPLICATION or LIBRARY).
     * Determined during configuration phase to support configuration cache.
     */
    @get:Input
    abstract val mergeType: Property<ManifestMerger2.MergeType>

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
            println(
                "No DeepLinkDispatch generated manifest found or manifest is empty. Copying input" +
                        " manifest to output without modifications. You might have applied the" +
                        " deep link dispatch gradle plugin to a module that does not have deep" +
                        " links defined or you forgot the set the `activityClassFqn` on your deep" +
                        " links."
            )
            inputManifest.copyTo(outputManifest, overwrite = true)
            return
        }

        // Use merge type set during configuration phase
        val mergeTypeValue = mergeType.get()

        // Merge the manifests using AGP's manifest merger
        // Use USES_SDK_IN_MANIFEST_LENIENT_HANDLING feature to allow <uses-sdk> elements
        // AGP injects <uses-sdk> into MERGED_MANIFEST but AGP 9.0+ validation rejects it
        // This feature flag makes it a warning instead of an error
        val invoker: ManifestMerger2.Invoker = ManifestMerger2.newMerger(
            inputManifest,
            StdLogger(StdLogger.Level.VERBOSE),
            mergeTypeValue
        ).withFeatures(ManifestMerger2.Invoker.Feature.USES_SDK_IN_MANIFEST_LENIENT_HANDLING)

        // generatedManifestFile is guaranteed to be non-null here due to hasGeneratedManifest check
        invoker.addFlavorAndBuildTypeManifest(generatedManifestFile)

        val merge = invoker.merge()
        if (merge.result.isSuccess) {
            val mergedDocument = merge.getMergedDocument(MergingReport.MergedManifestKind.MERGED)
                ?: error("Failed to get merged document")
            outputManifest.writeText(mergedDocument)
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