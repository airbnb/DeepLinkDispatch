package com.airbnb.deeplinkdispatch.internal;

import com.google.auto.service.AutoService;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.javawriter.JavaWriter;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import static com.airbnb.deeplinkdispatch.internal.DeepLinkEntry.Type;

@AutoService(Processor.class)
public class DeepLinkProcessor extends AbstractProcessor {

  private Filer filer;
  private Messager messager;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    filer = processingEnv.getFiler();
    messager = processingEnv.getMessager();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> annotations = new LinkedHashSet<String>();
    annotations.add(DeepLink.class.getCanonicalName());
    return annotations;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    List<DeepLinkAnnotatedElement> deepLinkElements = new ArrayList<>();

    for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(DeepLink.class)) {
      ElementKind kind = annotatedElement.getKind();
      if (kind != ElementKind.METHOD && kind != ElementKind.CLASS) {
        error(annotatedElement, "Only classes and methods can be annotated with @%s", DeepLink.class.getSimpleName());
      }

      Type type = kind == ElementKind.CLASS ? Type.CLASS : Type.METHOD;
      DeepLinkAnnotatedElement element = new DeepLinkAnnotatedElement(annotatedElement, type);
      deepLinkElements.add(element);
    }

    if (deepLinkElements.size() > 0) {
      try {
        generateCode(deepLinkElements);
      } catch (IOException e) {
        messager.printMessage(Diagnostic.Kind.ERROR, "Error creating file");
      }
    }

    return true;
  }

  private void error(Element e, String msg, Object... args) {
    messager.printMessage(
        Diagnostic.Kind.ERROR,
        String.format(msg, args),
        e);
  }

  private void generateCode(List<DeepLinkAnnotatedElement> elements) throws IOException {
    JavaFileObject jfo = filer.createSourceFile("DeepLinkLoader");
    Writer writer = jfo.openWriter();
    JavaWriter jw = new JavaWriter(writer);

    jw.emitPackage("com.airbnb.deeplinkdispatch");

    jw.emitImports("com.airbnb.deeplinkdispatch.internal.DeepLinkEntry.Type");
    jw.emitEmptyLine();

    jw.beginType("DeepLinkLoader", "class", EnumSet.of(Modifier.PUBLIC), null, "Loader");
    jw.emitEmptyLine();

    jw.beginConstructor(EnumSet.of(Modifier.PUBLIC));
    jw.endConstructor();
    jw.emitEmptyLine();

    jw.beginMethod("void", "load", EnumSet.of(Modifier.PUBLIC), "DeepLinkRegistry",
                   "registry");
    for (DeepLinkAnnotatedElement element: elements) {
      String uri = "\"" + element.getUri() + "\"";
      String type = "Type." + element.getAnnotationType().toString();
      String activity = "\"" + element.getActivity() + "\"";
      String method = element.getMethod() == null ? "null" : "\"" + element.getMethod() + "\"";
      jw.emitStatement(String.format("registry.registerDeepLink(%s, %s, %s, %s)", uri, type, activity, method));
    }
    jw.endMethod();

    jw.endType();

    jw.close();
  }
}
