package com.airbnb.deeplinkdispatch.metadata

import androidx.annotation.VisibleForTesting
import androidx.room.compiler.processing.XMessager
import androidx.room.compiler.processing.XProcessingEnv
import com.airbnb.deeplinkdispatch.DeepLinkAnnotatedElement
import com.airbnb.deeplinkdispatch.metadata.writers.GenericDocumentationWriter
import com.airbnb.deeplinkdispatch.metadata.writers.MarkdownDocumentationWriter
import com.airbnb.deeplinkdispatch.metadata.writers.Writer
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

/**
 * Documents deep links.
 *
 *
 * The output format is:
 * {DeepLink1}\n|#|\n[Description part of java doc]\n|#|\n{ClassName}#[MethodName]\n|##|\n
 * {DeepLink2}\n|#|\n[Description part of java doc]\n|#|\n{ClassName}#[MethodName]\n|##|\n
 * ...
 *
 *
 * The output location is specified in the project's gradle file through
 * [ProcessingEnvironment]'s compiler argument options by deepLinkDoc.output.
 */
internal class Documentor(
    private val processingEnv: XProcessingEnv,
) {
    private val messager: XMessager = processingEnv.messager

    @get:VisibleForTesting
    var file: File? = initFile()

    fun write(elements: List<DeepLinkAnnotatedElement>) {
        val file =
            file ?: run {
                messager.printMessage(
                    Diagnostic.Kind.NOTE,
                    "Output file is null, DeepLink doc not generated.",
                )
                return
            }
        if (elements.isNullOrEmpty()) {
            messager.printMessage(
                Diagnostic.Kind.NOTE,
                "No deep link, DeepLink doc not generated.",
            )
            return
        }
        try {
            PrintWriter(FileWriter(file), true).use { writer ->
                val fileName = file.name
                val extIndex = fileName.lastIndexOf(".")

                // markdown writer if .md file extension is used
                val docWriter: Writer =
                    if (extIndex >= 0 &&
                        extIndex < fileName.length &&
                        fileName.substring(extIndex + 1).equals("md", ignoreCase = true)
                    ) {
                        MarkdownDocumentationWriter()
                    } else {
                        // else default writer
                        GenericDocumentationWriter()
                    }
                docWriter.write(processingEnv, writer, elements)
            }
        } catch (e: IOException) {
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                "DeepLink doc not generated: " + e.message,
            )
        }
        messager.printMessage(Diagnostic.Kind.NOTE, "DeepLink doc generated at: " + file.path)
    }

    private fun initFile(): File? {
        val path = processingEnv.options[DOC_OUTPUT_PROPERTY_NAME]
        if (path == null || path.trim { it <= ' ' }.isEmpty()) {
            messager.printMessage(
                Diagnostic.Kind.NOTE,
                "Output path not specified, DeepLink doc is not going to be generated.",
            )
            return null
        }
        val file = File(path)
        if (file.isDirectory) {
            messager.printMessage(
                Diagnostic.Kind.NOTE,
                "Specify a file path at $DOC_OUTPUT_PROPERTY_NAME to generate deep link doc.",
            )
            return null
        }
        val parentDir = file.parentFile
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            messager.printMessage(
                Diagnostic.Kind.NOTE,
                "Cannot create file specified at ${file.canonicalPath}.",
            )
        }
        return file
    }

    companion object {
        const val DOC_OUTPUT_PROPERTY_NAME = "deepLinkDoc.output"
    }
}
