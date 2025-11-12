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
import javax.tools.Diagnostic

/**
 * This generates the input manifest for the manifest merger in the Gradle plugin.
 *
 * It creates a manifest file that contains all the intent filters from the annotated elements.
 *
 */
internal class ManifestGenerator(
    private val processingEnv: XProcessingEnv,
) {
    private val messager: XMessager = processingEnv.messager

    @get:VisibleForTesting
    var file: File? = initFile()

    fun write(elements: List<DeepLinkAnnotatedElement>) {
        val file =
            file ?: run {
                messager.printMessage(
                    Diagnostic.Kind.WARNING,
                    "Output file is null. Manifest generation: Manifest not generated.",
                )
                return
            }
        if (elements.isNullOrEmpty()) {
            messager.printMessage(
                Diagnostic.Kind.WARNING,
                "No deep links. Manifest generation: Manifest not generated.",
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
                " Manifest generation metadata doc not generated: " + e.message,
            )
        }
        messager.printMessage(Diagnostic.Kind.WARNING, " Manifest generation: Generated at: " + file.path)
    }

    private fun initFile(): File? {
        val path = processingEnv.options[MANIFEST_GEN_METADATA_OUTPUT_FILE]
        if (path == null || path.trim { it <= ' ' }.isEmpty()) {
            messager.printMessage(
                Diagnostic.Kind.WARNING,
                "Output path not specified. Manifest generation: Manifest not generated. Output path must be specified via $MANIFEST_GEN_METADATA_OUTPUT_FILE ksp option.",
            )
            return null
        }
        val file = File(path)
        if (file.isDirectory) {
            messager.printMessage(
                Diagnostic.Kind.WARNING,
                "Specify a file path at $MANIFEST_GEN_METADATA_OUTPUT_FILE to generate manifest generation metadata.",
            )
            return null
        }
        val parentDir = file.parentFile
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            messager.printMessage(
                Diagnostic.Kind.WARNING,
                "Cannot create file specified at ${file.canonicalPath}.",
            )
        }
        return file
    }

    companion object {
        const val MANIFEST_GEN_METADATA_OUTPUT_FILE = "deepLinkManifestGenMetadata.output"
    }
}
