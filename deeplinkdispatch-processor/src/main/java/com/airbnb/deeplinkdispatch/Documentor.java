package com.airbnb.deeplinkdispatch;

import com.google.common.annotations.VisibleForTesting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Name;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Documents deep links.
 * <p>
 * The output format is:
 * {DeepLink1}\n|#|\n[Description part of java doc]\n|#|\n{ClassName}#[MethodName]\n|##|\n
 * {DeepLink2}\n|#|\n[Description part of java doc]\n|#|\n{ClassName}#[MethodName]\n|##|\n
 * ...
 * <p>
 * The output location is specified in the project's gradle file through
 * {@link ProcessingEnvironment}'s compiler argument options by deepLinkDoc.output.
 */
final class Documentor {
  private static final String PROPERTY_DELIMITER = "\\n|#|\\n";
  private static final String ELEMENT_DELIMITER = "\\n|##|\\n";
  private static final String CLASS_METHOD_NAME_DELIMITER = "#";
  private static final String PARAM = "@param";
  private static final String RETURN = "@return";
  @VisibleForTesting
  static final String DOC_OUTPUT_PROPERTY_NAME = "deepLinkDoc.output";
  /**  */
  private static final Map<String, DocumetationWriter> EXTENSIONS = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  private final ProcessingEnvironment processingEnv;
  private final Messager messager;

  @Nullable
  private File file;

  static {
    EXTENSIONS.put("md", new MarkdownWriter());
    EXTENSIONS.put("", new GenericWriter());
  }

  Documentor(@Nonnull final ProcessingEnvironment processingEnv) {
    this.processingEnv = processingEnv;
    messager = processingEnv.getMessager();
    initFile();
  }

  void write(final List<DeepLinkAnnotatedElement> elements) {
    if (file == null) {
      messager.printMessage(Diagnostic.Kind.NOTE,
          "Output file is null, DeepLink doc not generated.");
      return;
    }
    if (elements == null || elements.isEmpty()) {
      messager.printMessage(Diagnostic.Kind.NOTE,
          "No deep link, DeepLink doc not generated.");
      return;
    }
    try (PrintWriter writer = new PrintWriter(new FileWriter(file), true)) {
      String key = "";
      final String fileName = file.getName();
      final int extIndex = fileName.lastIndexOf(".");

      // find writer by file name extension
      if (extIndex >= 0 && extIndex < fileName.length()) {
        final String ext = fileName.substring(extIndex + 1);
        key = EXTENSIONS.containsKey(ext) ? ext : "";
      }

      EXTENSIONS.get(key).write(processingEnv, writer, elements);
    } catch (IOException e) {
      messager.printMessage(Diagnostic.Kind.ERROR,
          "DeepLink doc not generated: " + e.getMessage());
    }
    messager.printMessage(Diagnostic.Kind.NOTE, "DeepLink doc generated at: " + file.getPath());
  }

  private void initFile() {
    String path = processingEnv.getOptions().get(DOC_OUTPUT_PROPERTY_NAME);
    if (path == null || path.trim().isEmpty()) {
      messager.printMessage(Diagnostic.Kind.NOTE,
          "Output path not specified, DeepLink doc is not going to be generated.");
      return;
    }

    file = new File(path);
    if (file.isDirectory()) {
      messager.printMessage(Diagnostic.Kind.NOTE,
          String.format("Specify a file path at %s to generate deep link doc.",
              DOC_OUTPUT_PROPERTY_NAME));
      return;
    }

    File parentDir = file.getParentFile();
    if (!parentDir.exists() && !parentDir.mkdirs()) {
      messager.printMessage(Diagnostic.Kind.NOTE,
          String.format("Cannot create file specified at %s.", file));
    }
  }

  /* Strips off {@link #PARAM} and {@link #RETURN}. */
  private static String formatJavaDoc(String str) {
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

  /**
   * Implement this interface if you want to provide own documentation writer.
   */
  interface DocumetationWriter {
    /**
     * Compose documentation with help of provided environment, writer and collection of found deeplink elements.
     */
    void write(@Nonnull final ProcessingEnvironment env,
               @Nonnull final PrintWriter writer,
               @Nonnull final List<DeepLinkAnnotatedElement> elements);
  }

  /**
   * GitHub markdown format.
   *
   * @see <a href="https://guides.github.com/features/mastering-markdown/">Mastering Markdown</a>
   * @see <a href="https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet">Markdown Cheatsheet</a>
   * @see <a href="https://github.com/JFormDesigner/markdown-writer-fx">Markdown Writer FX</a>
   * @see <a href="https://stackedit.io/">Stack Edit</a>
   */
  static class MarkdownWriter implements DocumetationWriter {

    @Override
    public void write(@Nonnull final ProcessingEnvironment env, @Nonnull final PrintWriter writer, @Nonnull final List<DeepLinkAnnotatedElement> elements) {

      final Elements utils = env.getElementUtils();

      // header
      writer.println("| URI | JavaDoc | Simple Name | Method |");
      writer.println("| --- | ------- | ----------- | ------ |");
      final String FORMAT = "| %1$s | %2$s | %3$s | %4$s |";

      // publish lines
      for (DeepLinkAnnotatedElement element : elements) {
        final String uri = element.getUri();
        final String doc = formatJavaDoc(utils.getDocComment(element.getElement()));
        final String embeddedComments = null == doc ? "" : doc;
        final boolean isMethod = element.getAnnotationType().equals(DeepLinkEntry.Type.METHOD);
        final String methodName = isMethod ? element.getMethod() : "";
        final Name simpleName = element.getAnnotatedElement().getSimpleName();

        final String line = String.format(Locale.US, FORMAT, uri, embeddedComments, simpleName, methodName);

        writer.println(line);
      }

      writer.flush();
    }
  }

  /**
   * Old documentation format.
   */
  static class GenericWriter implements DocumetationWriter {
    @Override
    public void write(@Nonnull final ProcessingEnvironment env, @Nonnull final PrintWriter writer, @Nonnull final List<DeepLinkAnnotatedElement> elements) {
      final Elements utils = env.getElementUtils();

      for (DeepLinkAnnotatedElement element : elements) {
        writer.print(element.getUri() + PROPERTY_DELIMITER);

        String doc = formatJavaDoc(utils.getDocComment(element.getElement()));
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
    }
  }
}
