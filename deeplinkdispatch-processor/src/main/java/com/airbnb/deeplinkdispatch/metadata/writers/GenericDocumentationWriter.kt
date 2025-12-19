package com.airbnb.deeplinkdispatch.metadata.writers

import androidx.room.compiler.codegen.toJavaPoet
import androidx.room.compiler.processing.XProcessingEnv
import com.airbnb.deeplinkdispatch.DeepLinkAnnotatedElement
import com.airbnb.deeplinkdispatch.metadata.writers.Writer.Companion.formatJavaDoc
import com.squareup.kotlinpoet.javapoet.KotlinPoetJavaPoetPreview
import java.io.PrintWriter

/**
 * Old documentation format.
 */
@KotlinPoetJavaPoetPreview
internal class GenericDocumentationWriter : Writer {
    override fun write(
        env: XProcessingEnv,
        writer: PrintWriter,
        elements: List<DeepLinkAnnotatedElement>,
    ) {
        writer.apply {
            for (element in elements) {
                print(element.uriTemplate + PROPERTY_DELIMITER)
                formatJavaDoc(element.element.docComment)
                    ?.let { print(it) }
                print(PROPERTY_DELIMITER)
                print(
                    element.annotatedClass
                        .asClassName()
                        .toJavaPoet()
                        .reflectionName(),
                )
                when (element) {
                    is DeepLinkAnnotatedElement.MethodAnnotatedElement -> {
                        print(CLASS_METHOD_NAME_DELIMITER)
                        print(element.method)
                    }
                    else -> { // Nothing
                    }
                }
                print(ELEMENT_DELIMITER)
            }
            flush()
        }
    }

    companion object {
        private const val PROPERTY_DELIMITER = "\\n|#|\\n"
        private const val ELEMENT_DELIMITER = "\\n|##|\\n"
        private const val CLASS_METHOD_NAME_DELIMITER = "#"
    }
}
