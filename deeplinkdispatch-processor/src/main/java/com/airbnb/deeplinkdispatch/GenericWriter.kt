package com.airbnb.deeplinkdispatch;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;

/**
 * Old documentation format.
 */
final class GenericWriter implements Documentor.DocumetationWriter {
    @Override
    public void write(final ProcessingEnvironment env,
                      final PrintWriter writer,
                      final List<DeepLinkAnnotatedElement> elements) {
        final Elements utils = env.getElementUtils();

        for (DeepLinkAnnotatedElement element : elements) {
            writer.print(element.getUri() + Documentor.PROPERTY_DELIMITER);

            String doc = Documentor.formatJavaDoc(utils.getDocComment(element.getElement()));
            if (doc != null) {
                writer.print(doc);
            }

            writer.print(Documentor.PROPERTY_DELIMITER);
            writer.print(element.getAnnotatedElement().getSimpleName());

            if (element.getAnnotationType().equals(DeepLinkEntry.Type.METHOD)) {
                writer.print(Documentor.CLASS_METHOD_NAME_DELIMITER);
                writer.print(element.getMethod());
            }

            writer.print(Documentor.ELEMENT_DELIMITER);
        }

        writer.flush();
    }
}
