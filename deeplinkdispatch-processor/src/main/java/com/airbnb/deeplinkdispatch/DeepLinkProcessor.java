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
package com.airbnb.deeplinkdispatch;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
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

  private static final ClassName ANDROID_INTENT = ClassName.get("android.content", "Intent");
  private static final ClassName ANDROID_CONTEXT = ClassName.get("android.content", "Context");
  private static final ClassName ANDROID_URI = ClassName.get("android.net", "Uri");
  private static final ClassName DEEPLINKRESULT
          = ClassName.get("com.airbnb.deeplinkdispatch", "DeepLinkResult");

  private Filer filer;
  private Messager messager;

  @Override public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    filer = processingEnv.getFiler();
    messager = processingEnv.getMessager();
  }

  @Override public Set<String> getSupportedAnnotationTypes() {
    return new HashSet<>(
        Arrays.asList(
            DeepLink.class.getCanonicalName(),
            DeepLinkHandler.class.getCanonicalName()));
  }

  @Override public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    List<DeepLinkAnnotatedElement> deepLinkElements = new ArrayList<>();

    Class<DeepLink> deepLinkClass = DeepLink.class;
    for (Element element : roundEnv.getElementsAnnotatedWith(deepLinkClass)) {
      ElementKind kind = element.getKind();
      if (kind != ElementKind.METHOD && kind != ElementKind.CLASS) {
        error(element, "Only classes and methods can be annotated with @%s",
            deepLinkClass.getSimpleName());
      }

      if (kind == ElementKind.METHOD) {
        Set<Modifier> methodModifiers = element.getModifiers();
        if (!methodModifiers.contains(Modifier.STATIC)) {
          error(element, "Only static methods can be annotated with @%s",
              deepLinkClass.getSimpleName());
        }
      }

      String[] deepLinks = element.getAnnotation(deepLinkClass).value();
      DeepLinkEntry.Type type = kind == ElementKind.CLASS
          ? DeepLinkEntry.Type.CLASS : DeepLinkEntry.Type.METHOD;
      for (String deepLink : deepLinks) {
        try {
          deepLinkElements.add(new DeepLinkAnnotatedElement(deepLink, element, type));
        } catch (MalformedURLException e) {
          messager.printMessage(Diagnostic.Kind.ERROR, "Malformed Deep Link URL " + deepLink);
        }
      }
    }

    boolean hasSpecifiedDeepLinkActivity
        = !roundEnv.getElementsAnnotatedWith(DeepLinkHandler.class).isEmpty();

    if (!deepLinkElements.isEmpty()) {
      try {
        generateDeepLinkResult();
        generateDeepLinkLoader(deepLinkElements);
        generateDeepLinkDelegate();

        if (!hasSpecifiedDeepLinkActivity) {
          generateDeepLinkActivity();
        }
      } catch (IOException e) {
        messager.printMessage(Diagnostic.Kind.ERROR, "Error creating file");
      } catch (RuntimeException e) {
        messager.printMessage(Diagnostic.Kind.ERROR,
            "Internal error during annotation processing: " + e.getClass().getSimpleName());
      }
    }

    return true;
  }

  private void error(Element e, String msg, Object... args) {
    messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
  }

  /**
   * Generates a DeepLinkResult class for us to use. Must be here because it uses Android's Uri
   * class and our api is a java lib, not Android
   */
  private void generateDeepLinkResult() throws IOException {
    TypeSpec deepLinkResult = TypeSpec.classBuilder("DeepLinkResult")
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addField(TypeName.BOOLEAN, "successful", Modifier.PRIVATE, Modifier.FINAL)
        .addField(ANDROID_URI, "uri", Modifier.PRIVATE, Modifier.FINAL)
        .addField(ClassName.get(String.class), "error", Modifier.PRIVATE, Modifier.FINAL)
        .addMethod(MethodSpec.constructorBuilder()
            .addParameter(ParameterSpec.builder(TypeName.BOOLEAN, "successful").build())
            .addParameter(ParameterSpec.builder(ANDROID_URI, "uri").build())
            .addParameter(ParameterSpec.builder(ClassName.get(String.class), "error")
                .build())
            .addStatement("this.successful = successful")
            .addStatement("this.uri = uri")
            .addStatement("this.error = error")
            .build())
        .addMethod(MethodSpec.methodBuilder("isSuccessful")
            .addModifiers(Modifier.PUBLIC)
            .addJavadoc("@return whether or not the dispatch was a success.\n")
            .returns(TypeName.BOOLEAN)
            .addStatement("return successful")
            .build())
        .addMethod(MethodSpec.methodBuilder("uri")
            .addModifiers(Modifier.PUBLIC)
            .addJavadoc("@return this result's uri, or {@code null} if there is none.\n")
            .returns(ANDROID_URI)
            .addStatement("return uri")
            .build())
        .addMethod(MethodSpec.methodBuilder("error")
            .addModifiers(Modifier.PUBLIC)
            .addJavadoc("@return this result's error message, or {@code null} if there is none.\n")
            .returns(ClassName.get(String.class))
            .addStatement("return error")
            .build())
        .addMethod(MethodSpec.methodBuilder("equals")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .addParameter(ClassName.get(Object.class), "o")
            .returns(TypeName.BOOLEAN)
            .addCode("if (this == o) { return true; }")
            .addCode("\n")
            .addCode("if (o == null || getClass() != o.getClass()) { return false; }")
            .addCode("\n")
            .addCode("\n")
            .addCode("DeepLinkResult that = (DeepLinkResult) o;")
            .addCode("\n")
            .addCode("\n")
            .addCode("if (successful != that.successful) { return false; }")
            .addCode("\n")
            .addCode("if (uri != null ? !uri.equals(that.uri) : that.uri != null) { return false; "
                + "}")
            .addCode("\n")
            .addCode("return error != null ? error.equals(that.error) "
                + ": that.error == null;")
            .build())
        .addMethod(MethodSpec.methodBuilder("hashCode")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(TypeName.INT)
            .addStatement("int result = (successful ? 1 : 0)")
            .addStatement("result = 31 * result + (uri != null ? uri.hashCode() : 0)")
            .addStatement("result = 31 * result + (error != null ? error.hashCode() "
                + ": 0)")
            .addStatement("return result")
            .build())
        .addMethod(MethodSpec.methodBuilder("toString")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(ClassName.get(String.class))
            .addCode("return \"DeepLinkResult{\" +")
            .addCode("\n")
            .addCode("    \"successful=\" + successful +")
            .addCode("\n")
            .addCode("    \", uri=\" + uri +")
            .addCode("\n")
            .addCode("    \", error='\" + error + '\\'' +")
            .addCode("\n")
            .addCode("    '}';")
            .build())
        .build();

    JavaFile.builder("com.airbnb.deeplinkdispatch", deepLinkResult)
        .build()
        .writeTo(filer);
  }

  private void generateDeepLinkLoader(List<DeepLinkAnnotatedElement> elements)
      throws IOException {
    FieldSpec registry = FieldSpec
        .builder(ParameterizedTypeName.get(List.class, DeepLinkEntry.class), "registry",
            Modifier.PRIVATE, Modifier.FINAL)
        .initializer("new $T<>()", TypeName.get(LinkedList.class))
        .build();

    MethodSpec.Builder loadMethod = MethodSpec.methodBuilder("load")
        .returns(void.class);

    for (DeepLinkAnnotatedElement element : elements) {
      String type = "DeepLinkEntry.Type." + element.getAnnotationType().toString();
      ClassName activity = ClassName.get(element.getActivityElement());
      Object method = element.getMethod() == null ? null : element.getMethod();
      String uri = element.getUri();
      loadMethod.addStatement("registry.add(new DeepLinkEntry($S, $L, $T.class, $S))",
          uri, type, activity, method);
    }

    MethodSpec parseMethod = MethodSpec.methodBuilder("parseUri")
        .addParameter(String.class, "uri")
        .returns(DeepLinkEntry.class)
        .beginControlFlow("for (DeepLinkEntry entry : registry)")
        .beginControlFlow("if (entry.matches(uri))")
        .addStatement("return entry")
        .endControlFlow()
        .endControlFlow()
        .addStatement("return null")
        .build();

    TypeSpec deepLinkLoader = TypeSpec.classBuilder("DeepLinkLoader")
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addField(registry)
        .addMethod(loadMethod.build())
        .addMethod(parseMethod)
        .build();

    JavaFile.builder("com.airbnb.deeplinkdispatch", deepLinkLoader)
        .build()
        .writeTo(filer);
  }

  private void generateDeepLinkDelegate() throws IOException {

    MethodSpec notifyListenerMethod = MethodSpec.methodBuilder("notifyListener")
        .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
        .returns(void.class)
        .addParameter(ANDROID_CONTEXT, "context")
        .addParameter(boolean.class, "isError")
        .addParameter(ClassName.get("android.net", "Uri"), "uri")
        .addParameter(String.class, "errorMessage")
        .addStatement("$T intent = new Intent()", ANDROID_INTENT)
        .addStatement("intent.setAction($T.ACTION)", DeepLinkHandler.class)
        .addStatement("intent.putExtra($T.EXTRA_URI, uri.toString())", DeepLinkHandler.class)
        .addStatement("intent.putExtra($T.EXTRA_SUCCESSFUL, !isError)", DeepLinkHandler.class)
        .beginControlFlow("if (isError)")
        .addStatement("intent.putExtra($T.EXTRA_ERROR_MESSAGE, errorMessage)",
                DeepLinkHandler.class)
        .endControlFlow()
        .addStatement("$T.getInstance(context).sendBroadcast(intent)",
            ClassName.get("android.support.v4.content", "LocalBroadcastManager"))
        .build();

    MethodSpec createResultAndNotifyMethod = MethodSpec.methodBuilder("createResultAndNotify")
        .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
        .returns(DEEPLINKRESULT)
        .addParameter(ANDROID_CONTEXT, "context")
        .addParameter(TypeName.BOOLEAN, "successful", Modifier.FINAL)
        .addParameter(ANDROID_URI, "uri", Modifier.FINAL)
        .addParameter(ClassName.get(String.class), "error", Modifier.FINAL)
        .addStatement("$T result = new $T(successful, uri, error)", DEEPLINKRESULT, DEEPLINKRESULT)
        .addStatement("notifyListener(context, !successful, uri, error)")
        .addStatement("return result")
        .build();

    FieldSpec tag = FieldSpec
        .builder(String.class, "TAG", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
        .initializer("DeepLinkDelegate.class.getSimpleName()")
        .build();

    MethodSpec constructor = MethodSpec.constructorBuilder()
        .addModifiers(Modifier.PRIVATE)
        .addStatement("throw new $T($S)", AssertionError.class, "No instances.")
        .build();

    MethodSpec dispatchFromMethod = MethodSpec.methodBuilder("dispatchFrom")
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .returns(DEEPLINKRESULT)
        .addParameter(ClassName.get("android.app", "Activity"), "activity")
        .beginControlFlow("if (activity == null)")
        .addStatement("throw new $T($S)", NullPointerException.class, "activity == null")
        .endControlFlow()
        .addStatement("$T sourceIntent = activity.getIntent()", ANDROID_INTENT)
        .addStatement("$T uri = sourceIntent.getData()", ANDROID_URI)
        .beginControlFlow("if (uri == null)")
        .addStatement("return createResultAndNotify(activity, false, null, $S)",
            "No Uri in given activity's intent.")
        .endControlFlow()
        .addStatement("DeepLinkLoader loader = new DeepLinkLoader()")
        .addStatement("loader.load()")
        .addStatement("String uriString = uri.toString()")
        .addStatement("DeepLinkEntry entry = loader.parseUri(uriString)")
        .beginControlFlow("if (entry != null)")
        .addStatement("DeepLinkUri deepLinkUri = DeepLinkUri.parse(uriString)")
        .addStatement("$T<String, String> parameterMap = entry.getParameters(uriString)", Map.class)
        .beginControlFlow("for (String queryParameter : deepLinkUri.queryParameterNames())")
        .beginControlFlow(
            "for (String queryParameterValue : deepLinkUri.queryParameterValues(queryParameter))")
        .beginControlFlow("if (parameterMap.containsKey(queryParameter))")
        .addStatement(
            "$T.w(TAG, \"Duplicate parameter name in path and query param: \" + queryParameter)",
            ClassName.get("android.util", "Log"))
        .endControlFlow()
        .addStatement("parameterMap.put(queryParameter, queryParameterValue)")
        .endControlFlow()
        .endControlFlow()
        .addStatement("parameterMap.put(DeepLink.URI, uri.toString())")
        .beginControlFlow("try")
        .addStatement("Class<?> c = entry.getActivityClass()")
        .addStatement("$T newIntent", ANDROID_INTENT)
        .beginControlFlow("if (entry.getType() == DeepLinkEntry.Type.CLASS)")
        .addStatement("newIntent = new Intent(activity, c)")
        .nextControlFlow("else")
        .addStatement("$T method = c.getMethod(entry.getMethod(), $T.class)", Method.class,
            ClassName.get("android.content", "Context"))
        .addStatement("newIntent = (Intent) method.invoke(c, activity)")
        .endControlFlow()
        .beginControlFlow("if (newIntent.getAction() == null)")
        .addStatement("newIntent.setAction(sourceIntent.getAction())")
        .endControlFlow()
        .beginControlFlow("if (newIntent.getData() == null)")
        .addStatement("newIntent.setData(sourceIntent.getData())")
        .endControlFlow()
        .addStatement("$T parameters", ClassName.get("android.os", "Bundle"))
        .beginControlFlow("if (sourceIntent.getExtras() != null)")
        .addStatement("parameters = new Bundle(sourceIntent.getExtras())")
        .nextControlFlow("else")
        .addStatement("parameters = new Bundle()")
        .endControlFlow()
        .beginControlFlow(
            "for (Map.Entry<String, String> parameterEntry : parameterMap.entrySet())")
        .addStatement("parameters.putString(parameterEntry.getKey(), parameterEntry.getValue())")
        .endControlFlow()
        .addStatement("newIntent.putExtras(parameters)")
        .addStatement("newIntent.putExtra(DeepLink.IS_DEEP_LINK, true)")
        .beginControlFlow("if (activity.getCallingActivity() != null)")
        .addStatement("newIntent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)")
        .endControlFlow()
        .addStatement("activity.startActivity(newIntent)")
        .addStatement("return createResultAndNotify(activity, true, uri, null)")
        .nextControlFlow("catch (NoSuchMethodException exception)")
        .addStatement(
            "return createResultAndNotify(activity, false, uri, \"Deep link to "
                + "non-existent method: \" + entry.getMethod())")
        .nextControlFlow("catch (IllegalAccessException exception)")
        .addStatement(
            "return createResultAndNotify(activity, false, uri, \"Could not deep "
                + "link to method: \" + entry.getMethod())")
        .nextControlFlow("catch ($T  exception)", InvocationTargetException.class)
        .addStatement(
            "return createResultAndNotify(activity, false, uri, \"Could not deep "
                + "link to method: \" + entry.getMethod())")
        .endControlFlow()
        .nextControlFlow("else")
        .addStatement("return createResultAndNotify(activity, false, uri, "
            + "\"No registered entity to handle deep link: \" + uri.toString())")
        .endControlFlow()
        .build();

    TypeSpec deepLinkDelegate = TypeSpec.classBuilder("DeepLinkDelegate")
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addField(tag)
        .addMethod(constructor)
        .addMethod(dispatchFromMethod)
        .addMethod(createResultAndNotifyMethod)
        .addMethod(notifyListenerMethod)
        .build();

    JavaFile.builder("com.airbnb.deeplinkdispatch", deepLinkDelegate)
        .build()
        .writeTo(filer);
  }

  private void generateDeepLinkActivity() throws IOException {
    MethodSpec onCreateMethod = MethodSpec.methodBuilder("onCreate")
        .addModifiers(Modifier.PROTECTED)
        .addAnnotation(Override.class)
        .returns(void.class)
        .addParameter(ClassName.get("android.os", "Bundle"), "savedInstanceState")
        .addStatement("super.onCreate(savedInstanceState)")
        .addStatement("$T.dispatchFrom(this)",
            ClassName.get("com.airbnb.deeplinkdispatch", "DeepLinkDelegate"))
        .build();

    TypeSpec deepLinkActivity = TypeSpec.classBuilder("DeepLinkActivity")
        .addModifiers(Modifier.PUBLIC)
        .superclass(ClassName.get("android.app", "Activity"))
        .addMethod(onCreateMethod)
        .build();

    JavaFile.builder("com.airbnb.deeplinkdispatch", deepLinkActivity)
        .build()
        .writeTo(filer);
  }
}
