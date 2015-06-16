/*
 * Copyright (C) 2015 Airbnb, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.airbnb.deeplinkdispatch.internal;

import com.google.auto.service.AutoService;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.DeepLinkEntry;
import com.airbnb.deeplinkdispatch.DeepLinkRegistry;
import com.airbnb.deeplinkdispatch.DeepLinks;
import com.airbnb.deeplinkdispatch.Loader;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
    Set<String> annotations = new LinkedHashSet<>();
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

    for (Element element : roundEnv.getElementsAnnotatedWith(DeepLinks.class)) {
      ElementKind kind = element.getKind();
      if (kind != ElementKind.METHOD && kind != ElementKind.CLASS) {
        error(element, "Only classes and methods can be annotated with @%s",
            DeepLinks.class.getSimpleName());
      }

      String[] deepLinks = element.getAnnotation(DeepLinks.class).value();
      DeepLinkEntry.Type type = kind == ElementKind.CLASS
                                ? DeepLinkEntry.Type.CLASS : DeepLinkEntry.Type.METHOD;
      for (String deepLink : deepLinks) {
        deepLinkElements.add(new DeepLinkAnnotatedElement(deepLink, element, type));
      }
    }

    for (Element element : roundEnv.getElementsAnnotatedWith(DeepLink.class)) {
      ElementKind kind = element.getKind();
      if (kind != ElementKind.METHOD && kind != ElementKind.CLASS) {
        error(element, "Only classes and methods can be annotated with @%s",
            DeepLink.class.getSimpleName());
      }

      DeepLink deepLink = element.getAnnotation(DeepLink.class);
      DeepLinkEntry.Type type = kind == ElementKind.CLASS
                                ? DeepLinkEntry.Type.CLASS : DeepLinkEntry.Type.METHOD;
      deepLinkElements.add(new DeepLinkAnnotatedElement(deepLink.value(), element, type));
    }

    if (!deepLinkElements.isEmpty()) {
      try {
        generateRegistry(deepLinkElements);
        generateDeepLinkActivity();
      } catch (IOException e) {
        messager.printMessage(Diagnostic.Kind.ERROR, "Error creating file");
      } catch (Exception e) {
        messager.printMessage(Diagnostic.Kind.ERROR,
            "Internal error during annotation processing: " + e.getClass().getSimpleName());
      }
    }

    return true;
  }

  private void error(Element e, String msg, Object... args) {
    messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
  }

  private void generateRegistry(List<DeepLinkAnnotatedElement> elements) throws IOException {
    MethodSpec.Builder loadMethod = MethodSpec.methodBuilder("load")
        .addModifiers(Modifier.PUBLIC)
        .returns(void.class)
        .addParameter(DeepLinkRegistry.class, "registry");

    for (DeepLinkAnnotatedElement element : elements) {
      String hostPath = element.getPath().equals("")
        ? element.getHost() : element.getHost() + "/" + element.getPath();
      String type = "DeepLinkEntry.Type." + element.getAnnotationType().toString();
      String activity = element.getActivity();
      Object method = element.getMethod() == null ? null : element.getMethod();
      loadMethod.addStatement("registry.registerDeepLink($S, $L, $S, $S)",
          hostPath, type, activity, method);
    }

    TypeSpec deepLinkLoader = TypeSpec.classBuilder("DeepLinkLoader")
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addSuperinterface(Loader.class)
        .addMethod(loadMethod.build())
        .build();

    JavaFile.builder("com.airbnb.deeplinkdispatch", deepLinkLoader)
        .build()
        .writeTo(filer);
  }

  private void generateDeepLinkActivity() throws IOException {
    FieldSpec tag = FieldSpec
        .builder(String.class, "TAG", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
        .initializer("DeepLinkActivity.class.getSimpleName()")
        .build();

    MethodSpec notifyListenerMethod = MethodSpec.methodBuilder("notifyListener")
        .addModifiers(Modifier.PRIVATE)
        .returns(void.class)
        .addParameter(boolean.class, "isError")
        .addParameter(ClassName.get("android.net", "Uri"), "uri")
        .addParameter(String.class, "errorMessage")
        .beginControlFlow("if (getApplication() instanceof DeepLinkCallback)")
        .addStatement("DeepLinkCallback listener = (DeepLinkCallback) getApplication()")
        .beginControlFlow("if (!isError)")
        .addStatement("listener.onSuccess(uri.toString())")
        .nextControlFlow("else")
        .addStatement("listener.onError(new DeepLinkError(uri.toString(), errorMessage))")
        .endControlFlow()
        .endControlFlow()
        .build();

    MethodSpec onCreateMethod = MethodSpec.methodBuilder("onCreate")
        .addModifiers(Modifier.PROTECTED)
        .addAnnotation(Override.class)
        .returns(void.class)
        .addParameter(ClassName.get("android.os", "Bundle"), "savedInstanceState")
        .addStatement("super.onCreate(savedInstanceState)")
        .addStatement("Loader loader = new com.airbnb.deeplinkdispatch.DeepLinkLoader()")
        .addStatement("DeepLinkRegistry registry = new DeepLinkRegistry(loader)")
        .addStatement("$T uri = getIntent().getData()", ClassName.get("android.net", "Uri"))
        .addStatement("String hostPath = uri.getHost() + uri.getPath()")
        .addStatement("DeepLinkEntry entry = registry.parseUri(hostPath)")
        .beginControlFlow("if (entry != null)")
        .addStatement("$T<String, String> parameterMap = entry.getParameters(hostPath)", Map.class)
        .beginControlFlow("for (String queryParameter : uri.getQueryParameterNames())")
        .beginControlFlow("if (parameterMap.containsKey(queryParameter))")
        .addStatement(
            "$T.w(TAG, \"Duplicate parameter name in path and query param: \" + queryParameter)",
            ClassName.get("android.util", "Log"))
        .endControlFlow()
        .addStatement("parameterMap.put(queryParameter, uri.getQueryParameter(queryParameter))")
        .endControlFlow()
        .beginControlFlow("try")
        .addStatement("Class<?> c = Class.forName(entry.getActivity())")
        .addStatement("$T intent", ClassName.get("android.content", "Intent"))
        .beginControlFlow("if (entry.getType() == DeepLinkEntry.Type.CLASS)")
        .addStatement("intent = new Intent(this, c)")
        .nextControlFlow("else")
        .addStatement("$T method = c.getMethod(entry.getMethod(), $T.class)", Method.class,
            ClassName.get("android.content", "Context"))
        .addStatement("intent = (Intent) method.invoke(c, this)")
        .endControlFlow()
        .beginControlFlow("if (intent.getAction() == null)")
        .addStatement("intent.setAction(getIntent().getAction())")
        .endControlFlow()
        .beginControlFlow("if (intent.getData() == null)")
        .addStatement("intent.setData(getIntent().getData())")
        .endControlFlow()
        .addStatement("Bundle parameters")
        .beginControlFlow("if (getIntent().getExtras() != null)")
        .addStatement("parameters = new Bundle(getIntent().getExtras())")
        .nextControlFlow("else")
        .addStatement("parameters = new Bundle()")
        .endControlFlow()
        .beginControlFlow(
            "for (Map.Entry<String, String> parameterEntry : parameterMap.entrySet())")
        .addStatement("parameters.putString(parameterEntry.getKey(), parameterEntry.getValue())")
        .endControlFlow()
        .addStatement("intent.putExtras(parameters)")
        .addStatement("intent.putExtra(DeepLink.IS_DEEP_LINK, true)")
        .addStatement("startActivity(intent)")
        .addStatement("notifyListener(false, uri, null)")
        .nextControlFlow("catch (ClassNotFoundException exception)")
        .addStatement(
            "notifyListener(true, uri, \"Deep link to non-existent class: \" + entry.getActivity())")
        .nextControlFlow("catch (NoSuchMethodException exception)")
        .addStatement(
            "notifyListener(true, uri, \"Deep link to non-existent method: \" + entry.getMethod())")
        .nextControlFlow("catch (IllegalAccessException exception)")
        .addStatement(
            "notifyListener(true, uri, \"Could not deep link to method: \" + entry.getMethod())")
        .nextControlFlow("catch($T  exception)", InvocationTargetException.class)
        .addStatement(
            "notifyListener(true, uri, \"Could not deep link to method: \" + entry.getMethod())")
        .nextControlFlow("finally")
        .addStatement("finish()")
        .endControlFlow()
        .nextControlFlow("else")
        .addStatement(
            "notifyListener(true, uri, \"No registered entity to handle deep link: \" + uri.toString())")
        .addStatement("finish()")
        .endControlFlow()
        .build();

    TypeSpec deepLinkActivity = TypeSpec.classBuilder("DeepLinkActivity")
        .addModifiers(Modifier.PUBLIC)
        .superclass(ClassName.get("android.app", "Activity"))
        .addField(tag)
        .addMethod(onCreateMethod)
        .addMethod(notifyListenerMethod)
        .build();

    JavaFile.builder("com.airbnb.deeplinkdispatch", deepLinkActivity)
        .build()
        .writeTo(filer);
  }
}
