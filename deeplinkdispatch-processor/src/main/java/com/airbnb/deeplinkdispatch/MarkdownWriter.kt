package com.airbnb.deeplinkdispatch

import androidx.room.compiler.processing.XProcessingEnv
import com.airbnb.deeplinkdispatch.Documentor.DocumetationWriter
import java.io.PrintWriter
import java.util.Locale

/**
 * GitHub markdown format.
 *
 * @see [Mastering Markdown](https://guides.github.com/features/mastering-markdown/)
 *
 * @see [Markdown
 * Cheatsheet](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)
 *
 * @see [Markdown Writer FX](https://github.com/JFormDesigner/markdown-writer-fx)
 *
 * @see [Stack Edit](https://stackedit.io/)
 */
internal class MarkdownWriter : DocumetationWriter {
    override fun write(
        env: XProcessingEnv,
        writer: PrintWriter,
        elements: List<DeepLinkAnnotatedElement>
    ) {
        // header
        writer.println("| URI | JavaDoc | Simple Name | Method |")
        writer.println("| --- | ------- | ----------- | ------ |")
        val format = "| %1\$s | %2\$s | %3\$s | %4\$s |"

        // publish lines
        for (element in elements) {
            val embeddedComments =
                Documentor.formatJavaDoc(element.element.docComment) ?: ""
            val methodName = when (element) {
                is DeepLinkAnnotatedElement.MethodAnnotatedElement -> element.method
                else -> ""
            }
            val simpleName = element.annotatedClass.qualifiedName
            writer.println(
                String.format(
                    Locale.US, format,
                    element.uri, embeddedComments, simpleName, methodName
                )
            )
        }
        writer.flush()
    }
}
