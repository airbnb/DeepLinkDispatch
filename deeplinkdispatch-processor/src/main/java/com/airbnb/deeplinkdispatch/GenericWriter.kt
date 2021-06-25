package com.airbnb.deeplinkdispatch

import androidx.room.compiler.processing.XProcessingEnv
import com.airbnb.deeplinkdispatch.Documentor.DocumetationWriter
import java.io.PrintWriter

/**
 * Old documentation format.
 */
internal class GenericWriter : DocumetationWriter {
    override fun write(
        env: XProcessingEnv,
        writer: PrintWriter,
        elements: List<DeepLinkAnnotatedElement>
    ) {
        writer.apply {
            for (element in elements) {
                print(element.uri + Documentor.PROPERTY_DELIMITER)
                Documentor.formatJavaDoc(element.element.docComment)
                    ?.let { print(it) }
                print(Documentor.PROPERTY_DELIMITER)
                print(element.annotatedClass?.qualifiedName ?: "")
                if (element.annotationType == DeepLinkEntry.Type.METHOD) {
                    print(Documentor.CLASS_METHOD_NAME_DELIMITER)
                    print(element.method)
                }
                print(Documentor.ELEMENT_DELIMITER)
            }
            flush()
        }
    }
}
