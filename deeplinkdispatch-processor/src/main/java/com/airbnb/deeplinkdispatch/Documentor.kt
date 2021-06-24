package com.airbnb.deeplinkdispatch

import androidx.annotation.VisibleForTesting
import androidx.room.compiler.processing.XMessager
import androidx.room.compiler.processing.XProcessingEnv
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
internal class Documentor(private val processingEnv: XProcessingEnv) {
    private val messager: XMessager = processingEnv.messager

    @get:VisibleForTesting
    var file: File? = initFile()

    fun write(elements: List<DeepLinkAnnotatedElement>) {
        val file = file ?: run {
            messager.printMessage(
                Diagnostic.Kind.NOTE,
                "Output file is null, DeepLink doc not generated."
            )
            return
        }
        if (elements.isNullOrEmpty()) {
            messager.printMessage(
                Diagnostic.Kind.NOTE,
                "No deep link, DeepLink doc not generated."
            )
            return
        }
        try {
            PrintWriter(FileWriter(file), true).use { writer ->
                val fileName = file.name
                val extIndex = fileName.lastIndexOf(".")

                // markdown writer if .md file extension is used
                val docWriter: DocumetationWriter =
                    if (extIndex >= 0 && extIndex < fileName.length
                        && fileName.substring(extIndex + 1).equals("md", ignoreCase = true)
                    ) {
                        MarkdownWriter()
                    } else {
                        // else default writer
                        GenericWriter()
                    }
                docWriter.write(processingEnv, writer, elements)
            }
        } catch (e: IOException) {
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                "DeepLink doc not generated: " + e.message
            )
        }
        messager.printMessage(Diagnostic.Kind.NOTE, "DeepLink doc generated at: " + file.path)
    }

    private fun initFile(): File? {
        val path = processingEnv.options[DOC_OUTPUT_PROPERTY_NAME]
        if (path == null || path.trim { it <= ' ' }.isEmpty()) {
            messager.printMessage(
                Diagnostic.Kind.NOTE,
                "Output path not specified, DeepLink doc is not going to be generated."
            )
            return null
        }
        val file = File(path)
        if (file.isDirectory) {
            messager.printMessage(
                Diagnostic.Kind.NOTE,
                "Specify a file path at $DOC_OUTPUT_PROPERTY_NAME to generate deep link doc.",
            )
            return file
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

    /**
     * Implement this interface if you want to provide own documentation writer.
     */
    internal interface DocumetationWriter {
        /**
         * Compose documentation with help of provided environment, writer and collection of
         * found deeplink elements.
         */
        fun write(
            env: XProcessingEnv,
            writer: PrintWriter,
            elements: List<DeepLinkAnnotatedElement>
        )
    }

    companion object {
        internal const val PROPERTY_DELIMITER = "\\n|#|\\n"
        internal const val ELEMENT_DELIMITER = "\\n|##|\\n"
        internal const val CLASS_METHOD_NAME_DELIMITER = "#"
        private const val PARAM = "@param"
        private const val RETURN = "@return"
        const val DOC_OUTPUT_PROPERTY_NAME = "deepLinkDoc.output"

        /* Strips off {@link #PARAM} and {@link #RETURN}. */
        internal fun formatJavaDoc(str: String?): String? {
            var result = str
            if (result != null) {
                val paramPos = result.indexOf(PARAM)
                if (paramPos != -1) {
                    result = result.substring(0, paramPos)
                }
                val returnPos = result.indexOf(RETURN)
                if (returnPos != -1) {
                    result = result.substring(0, returnPos)
                }
                result = result.trim { it <= ' ' }
            }
            return result
        }
    }
}