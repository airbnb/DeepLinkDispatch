package com.airbnb.deeplinkdispatch;

import com.google.auto.service.AutoService;

import com.airbnb.deeplinkdispatch.internal.DeepLinkAnnotatedMethod;
import com.airbnb.deeplinkdispatch.internal.DeepLinkRegistry;

import java.io.IOException;
import java.util.LinkedHashSet;
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
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class DeepLinkProcessor extends AbstractProcessor {

  private Elements elementUtils;
  private Filer filer;
  private Messager messager;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    elementUtils = processingEnv.getElementUtils();
    filer = processingEnv.getFiler();
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
    DeepLinkRegistry registry = new DeepLinkRegistry();

    for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(DeepLink.class)) {
      if (annotatedElement.getKind() != ElementKind.METHOD) {
        error(annotatedElement, "Only methods can be annotated with @%s", DeepLink.class.getSimpleName());
        return true;
      }

      DeepLinkAnnotatedMethod annotatedMethod = new DeepLinkAnnotatedMethod(annotatedElement);
      registry.add(annotatedMethod);
    }

    if (registry.getRegistry().size() > 0) {
      try {
        registry.generateCode(elementUtils, filer);
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
}
