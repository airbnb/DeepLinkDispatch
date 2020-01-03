package com.airbnb.deeplinkdispatch;

import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Name;
import javax.lang.model.util.Elements;

/**
 * GitHub markdown format.
 *
 * @see <a href="https://guides.github.com/features/mastering-markdown/">Mastering Markdown</a>
 * @see <a href="https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet">Markdown
 * Cheatsheet</a>
 * @see <a href="https://github.com/JFormDesigner/markdown-writer-fx">Markdown Writer FX</a>
 * @see <a href="https://stackedit.io/">Stack Edit</a>
 */
final class MarkdownWriter implements Documentor.DocumetationWriter {

    @Override
    public void write(final ProcessingEnvironment env,
                      final PrintWriter writer,
                      final List<DeepLinkAnnotatedElement> elements) {

        final Elements utils = env.getElementUtils();

        // header
        writer.println("| URI | JavaDoc | Simple Name | Method |");
        writer.println("| --- | ------- | ----------- | ------ |");
        final String format = "| %1$s | %2$s | %3$s | %4$s |";

        // publish lines
        for (DeepLinkAnnotatedElement element : elements) {
            String uri = element.getUri();
            String doc = Documentor.formatJavaDoc(utils.getDocComment(element.getElement()));
            String embeddedComments = null == doc ? "" : doc;
            boolean isMethod = element.getAnnotationType().equals(DeepLinkEntry.Type.METHOD);
            String methodName = isMethod ? element.getMethod() : "";
            Name simpleName = element.getAnnotatedElement().getSimpleName();

            String line = String.format(Locale.US, format,
                    uri, embeddedComments, simpleName, methodName);

            writer.println(line);
        }

        writer.flush();
    }
}
