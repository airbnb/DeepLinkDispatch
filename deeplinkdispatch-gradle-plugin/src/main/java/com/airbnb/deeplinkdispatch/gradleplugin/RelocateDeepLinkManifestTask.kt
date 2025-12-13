package com.airbnb.deeplinkdispatch.gradleplugin

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

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
                while (parentDir != null &&
                    parentDir.name != "resources" &&
                    parentDir.isDirectory &&
                    parentDir.list()?.isEmpty() == true
                ) {
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
