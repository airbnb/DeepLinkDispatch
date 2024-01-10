package com.airbnb.deeplinkdispatch.metadata

import androidx.annotation.VisibleForTesting
import androidx.room.compiler.processing.XMessager
import androidx.room.compiler.processing.XProcessingEnv
import com.airbnb.deeplinkdispatch.DeepLinkAnnotatedElement
import com.airbnb.deeplinkdispatch.metadata.writers.ManifestWriter
import com.airbnb.deeplinkdispatch.metadata.writers.Writer
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

/**
 * This generates the inpput manifest for the mahifest merger in the Gradle plugin.
 *
 * It creates a manifest file that contains all the intent filters from the annotated elements.
 *
 */
internal class ManifestGenerator(private val processingEnv: XProcessingEnv) {
    private val messager: XMessager = processingEnv.messager

    @get:VisibleForTesting
    var file: File? = initFile()

    fun write(elements: List<DeepLinkAnnotatedElement>) {
        val file = file ?: run {
            messager.printMessage(
                Diagnostic.Kind.NOTE,
                "Output file is null, Manifest generation metadata doc not generated."
            )
            return
        }
        if (elements.isNullOrEmpty()) {
            messager.printMessage(
                Diagnostic.Kind.NOTE,
                "No deep links, Manifest generation metadata doc not generated."
            )
            file.delete()
            return
        }
        try {
            PrintWriter(FileWriter(file), true).use { writer ->
                val docWriter: Writer = ManifestWriter()
                docWriter.write(processingEnv, writer, elements)
            }
        } catch (e: IOException) {
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                " Manifest generation metadata doc not generated: " + e.message
            )
        }
        messager.printMessage(Diagnostic.Kind.NOTE, " Manifest generation metadata docc generated at: " + file.path)
    }

    private fun initFile(): File? {
        val path = processingEnv.options[MANIFEST_GEN_METADATA_OUTPUT_FILE]
        if (path == null || path.trim { it <= ' ' }.isEmpty()) {
            messager.printMessage(
                Diagnostic.Kind.NOTE,
                "Output path not specified, manifest generation metadata not generated."
            )
            return null
        }
        val file = File(path)
        if (file.isDirectory) {
            messager.printMessage(
                Diagnostic.Kind.NOTE,
                "Specify a file path at $MANIFEST_GEN_METADATA_OUTPUT_FILE to generate manifest generation metadata."
            )
            return null
        }
        val parentDir = file.parentFile
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            messager.printMessage(
                Diagnostic.Kind.NOTE,
                "Cannot create file specified at ${file.canonicalPath}."
            )
        }
        return file
    }

    companion object {
        const val MANIFEST_GEN_METADATA_OUTPUT_FILE = "deepLinkManifestGenMetadata.output"
    }
}
