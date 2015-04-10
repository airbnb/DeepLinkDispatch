package com.airbnb.deeplinkdispatch.internal;


import com.airbnb.deeplinkdispatch.javawriter.JavaWriter;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

public class DeepLinkRegistry {

  private List<DeepLinkAnnotatedMethod> registry = new ArrayList<>();

  public void add(DeepLinkAnnotatedMethod annotatedMethod) {
    registry.add(annotatedMethod);
  }

  public void generateCode(Elements elementUtils, Filer filer) throws IOException {
    JavaFileObject jfo = filer.createSourceFile("DeepLinkDispatch");
    Writer writer = jfo.openWriter();
    JavaWriter jw = new JavaWriter(writer);

    jw.emitPackage("com.airbnb.deeplinkdispatch.sample");

    jw.emitImports("android.content.Context");
    jw.emitImports("android.content.Intent");
    jw.emitEmptyLine();

    jw.beginType("DeepLinkDispatch", "class", EnumSet.of(Modifier.PUBLIC));
    jw.emitEmptyLine();

    jw.beginMethod("void", "handleDeepLink", EnumSet.of(Modifier.PUBLIC), "Context", "context",
                   "Intent", "intent");
    jw.emitStatement("String uri = intent.getData().toString()");
    jw.emitStatement("Intent dispatchIntent = null");

    generateRegistryCode(jw);
    jw.emitEmptyLine();

    jw.emitStatement("context.startActivity(dispatchIntent)");
    jw.endMethod();

    jw.endType();

    jw.close();
  }

  private void generateRegistryCode(JavaWriter jw) throws IOException {
    for (int i = 0; i < registry.size(); i++) {
      DeepLinkAnnotatedMethod annotatedMethod = registry.get(i);

      String registeredUri = annotatedMethod.getUri();
      String registeredActivity = annotatedMethod.getActivity();
      String registeredMethod = annotatedMethod.getMethod();

      if (i == 0) {
        jw.beginControlFlow("if (\"%s\".equals(uri))", registeredUri);
        jw.emitStatement("dispatchIntent = %s.%s(context)", registeredActivity, registeredMethod);
      } else {
        jw.nextControlFlow("else if (\"%s\".equals(uri))", registeredUri);
        jw.emitStatement("dispatchIntent = %s.%s(context)", registeredActivity, registeredMethod);
      }
    }

    if (registry.size() > 0) {
      jw.endControlFlow();
    }
  }

  public List<DeepLinkAnnotatedMethod> getRegistry() {
    return registry;
  }
}
