package com.airbnb.deeplinkdispatch;

import com.google.common.annotations.VisibleForTesting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;

/**
 * Documents deep links.
 *
 * The output format is:
 * {DeepLink1}\n|#|\n[Description part of java doc]\n|#|\n{ClassName}#[MethodName]\n|##|\n
 * {DeepLink2}\n|#|\n[Description part of java doc]\n|#|\n{ClassName}#[MethodName]\n|##|\n
 * ...
 *
 * The output location is specified in the project's gradle file through
 * {@link ProcessingEnvironment}'s compiler argument options by deepLinkDoc.output.
 */

final class Documentor {
    private static final String PROPERTY_DELIMITER = "\\n|#|\\n";
    private static final String ELEMENT_DELIMITER = "\\n|##|\\n";
    private static final String CLASS_METHOD_NAME_DELIMITER = "#";
    private static final String PARAM = "@param";
    private static final String RETURN = "@return";
    @VisibleForTesting static final String DOC_OUTPUT_PROPERTY_NAME = "deepLinkDoc.output";

    private final ProcessingEnvironment processingEnv;
    private final Messager messager;

    @Nullable private File file;

    Documentor(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        messager = processingEnv.getMessager();
        initFile();
    }

    void write(List<DeepLinkAnnotatedElement> elements) {
        if (file == null) {
            messager.printMessage(Diagnostic.Kind.WARNING,
                    "Output file is null, DeepLink doc not generated.");
            return;
        }
        if (elements == null || elements.isEmpty()) {
            messager.printMessage(Diagnostic.Kind.WARNING,
                    "No deep link, DeepLink doc not generated.");
            return;
        }
        PrintWriter writer;
        try {
            writer = new PrintWriter(new FileWriter(file), true);
            for (DeepLinkAnnotatedElement element : elements) {
                writer.print(element.getUri() + PROPERTY_DELIMITER);
                String doc = formatJavaDoc(
                        processingEnv.getElementUtils().getDocComment(element.getElement()));
                if (doc != null) {
                    writer.print(doc);
                }
                writer.print(PROPERTY_DELIMITER);
                writer.print(element.getAnnotatedElement().getSimpleName());
                if (element.getAnnotationType().equals(DeepLinkEntry.Type.METHOD)) {
                    writer.print(CLASS_METHOD_NAME_DELIMITER);
                    writer.print(element.getMethod());
                }
                writer.print(ELEMENT_DELIMITER);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            messager.printMessage(Diagnostic.Kind.WARNING, "DeepLink doc not generated, ");
        }
        messager.printMessage(Diagnostic.Kind.NOTE,
                "DeepLink doc generated at: " + file.getPath());

    }

    private void initFile() {
        String path = processingEnv.getOptions().get(DOC_OUTPUT_PROPERTY_NAME);
        if (path != null && path.trim().length() > 0) {
            file = new File(path);
            if (file.isDirectory()) {
                messager.printMessage(Diagnostic.Kind.WARNING,
                        String.format("Specify a file path at %s to generate deep link doc.",
                                DOC_OUTPUT_PROPERTY_NAME));
            } else {
                final File parentDir = file.getParentFile();
                if (!parentDir.exists() && !parentDir.mkdirs()) {
                    messager.printMessage(Diagnostic.Kind.WARNING,
                            String.format("Cannot create file specified at %s.", file));
                }
            }
        } else {
            messager.printMessage(Diagnostic.Kind.WARNING,
                    "DeepLink doc is not going to be generated.");
        }
    }

    /* Strips off {@link #PARAM} and {@link #RETURN}. */
    private String formatJavaDoc(String str) {
        if (str != null) {
            int paramPos = str.indexOf(PARAM);
            if (paramPos != -1) {
                str = str.substring(0, paramPos);
            }
            int returnPos = str.indexOf(RETURN);
            if (returnPos != -1) {
                str = str.substring(0, returnPos);
            }
            str = str.trim();
        }
        return str;
    }

    @Nullable
    @VisibleForTesting
    File getFile() {
        return file;
    }
}
