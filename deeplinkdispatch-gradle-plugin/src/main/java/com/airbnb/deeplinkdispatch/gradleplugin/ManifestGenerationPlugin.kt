package com.airbnb.deeplinkdispatch.gradleplugin

import com.airbnb.deeplinkdispatch.base.ManifestGeneration
import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.HasHostTests
import com.android.build.api.variant.Variant
import com.android.manifmerger.ManifestMerger2
import com.android.manifmerger.MergingReport
import com.android.utils.StdLogger
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
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
import org.gradle.api.tasks.TaskAction

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
                    // Manifest merge task runs after relocate
                    manifestMergeTask.configure {
                        dependsOn(relocateManifestTask)
                    }
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
                }
            }
        }
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
            println("No DeepLinkDispatch generated manifest found or manifest is empty. " +
                    "Copying input manifest to output without modifications.")
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

/**
 * Task that relocates the KSP-generated manifest from the resources directory to a safe location.
 *
 * This task must run after KSP and before any Java resource merge tasks. It moves the manifest
 * file out of the KSP resources directory to prevent it from being included when this library
 * is used as a project dependency.
 *
 * This is a separate task (rather than a doLast on KSP) because doLast doesn't run when
 * KSP is restored FROM-CACHE. This task will always run after KSP, even if KSP was cached.
 */
abstract class RelocateDeepLinkManifestTask : DefaultTask() {

    @get:InputFiles
    @get:Optional
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val kspManifestFile: RegularFileProperty

    @get:OutputFile
    abstract val safeManifestFile: RegularFileProperty

    @TaskAction
    fun taskAction() {
        val sourceFile = kspManifestFile.get().asFile
        val destFile = safeManifestFile.get().asFile

        if (sourceFile.exists()) {
            // Ensure parent directory exists
            destFile.parentFile?.mkdirs()
            // Copy to safe location
            sourceFile.copyTo(destFile, overwrite = true)
            // Delete from KSP resources to prevent Java resource merge conflict
            if (sourceFile.delete()) {
                println("Moved DeepLinkDispatch manifest to safe location: ${destFile.absolutePath}")
                // Delete empty parent directories up to but not including "resources"
                var parentDir = sourceFile.parentFile
                while (parentDir != null && parentDir.name != "resources" && parentDir.isDirectory && parentDir.list()?.isEmpty() == true) {
                    val nextParent = parentDir.parentFile
                    parentDir.delete()
                    parentDir = nextParent
                }
            }
        } else if (destFile.exists()) {
            // Source doesn't exist but dest does - this can happen on incremental builds
            // where KSP was UP-TO-DATE and we already moved the file previously.
            // The dest file is still valid, so nothing to do.
            println("DeepLinkDispatch manifest already at safe location: ${destFile.absolutePath}")
        } else {
            // Neither file exists - no manifest was generated
            println("No DeepLinkDispatch manifest found to relocate")
        }
    }
}
