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

import com.google.auto.common.AnnotationMirrors;
import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.google.auto.service.AutoService;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import static com.airbnb.deeplinkdispatch.MoreAnnotationMirrors.asAnnotationValues;
import static com.airbnb.deeplinkdispatch.MoreAnnotationMirrors.getTypeValue;
import static com.airbnb.deeplinkdispatch.Utils.decapitalize;
import static com.google.auto.common.MoreElements.getAnnotationMirror;

@AutoService(Processor.class)
public class DeepLinkProcessor extends AbstractProcessor {
  private static final ClassName ANDROID_BUNDLE = ClassName.get("android.os", "Bundle");
  private static final String DLD_PACKAGE_NAME = "com.airbnb.deeplinkdispatch";
  private static final ClassName ANDROID_INTENT = ClassName.get("android.content", "Intent");
  private static final ClassName ANDROID_CONTEXT = ClassName.get("android.content", "Context");
  private static final ClassName ANDROID_ACTIVITY = ClassName.get("android.app", "Activity");
  private static final ClassName TASK_STACK_BUILDER = ClassName.get(
      "android.support.v4.app", "TaskStackBuilder");
  private static final ClassName ANDROID_URI = ClassName.get("android.net", "Uri");
  private static final ClassName CLASS_DLD_ENTRY = ClassName.get(DeepLinkEntry.class);
  private static final ClassName CLASS_DLD_URI = ClassName.get(DeepLinkUri.class);
  private static final ClassName CLASS_ARRAYS = ClassName.get(Arrays.class);
  private static final ClassName CLASS_COLLECTIONS = ClassName.get(Collections.class);
  private static final Class<DeepLink> DEEP_LINK_CLASS = DeepLink.class;
  private static final Class<DeepLinkSpec> DEEP_LINK_SPEC_CLASS = DeepLinkSpec.class;

  private Filer filer;
  private Messager messager;

  @Override public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    filer = processingEnv.getFiler();
    messager = processingEnv.getMessager();
  }

  @Override public Set<String> getSupportedAnnotationTypes() {
    return Sets.newHashSet("*");
  }

  @Override public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    Set<Element> customAnnotations = new HashSet<>();
    for (Element annotation : annotations) {
      if (annotation.getAnnotation(DEEP_LINK_SPEC_CLASS) != null) {
        customAnnotations.add(annotation);
      }
    }

    Map<Element, String[]> prefixes = new HashMap<>();
    Set<Element> customAnnotatedElements = new HashSet<>();
    for (Element customAnnotation : customAnnotations) {
      ElementKind kind = customAnnotation.getKind();
      if (kind != ElementKind.ANNOTATION_TYPE) {
        error(customAnnotation, "Only annotation types can be annotated with @%s",
            DEEP_LINK_SPEC_CLASS.getSimpleName());
      }
      String[] prefix = customAnnotation.getAnnotation(DEEP_LINK_SPEC_CLASS).prefix();
      if (Utils.hasEmptyOrNullString(prefix)) {
        error(customAnnotation, "Prefix property cannot have null or empty strings");
      }
      if (prefix.length == 0) {
        error(customAnnotation, "Prefix property cannot be empty");
      }
      prefixes.put(customAnnotation, prefix);
      for (Element customAnnotatedElement
          : roundEnv.getElementsAnnotatedWith(MoreElements.asType(customAnnotation))) {
        customAnnotatedElements.add(customAnnotatedElement);
      }
    }

    Set<Element> elementsToProcess = new HashSet<>(customAnnotatedElements);
    elementsToProcess.addAll(roundEnv.getElementsAnnotatedWith(DEEP_LINK_CLASS));

    List<DeepLinkAnnotatedElement> deepLinkElements = new ArrayList<>();
    for (Element element : elementsToProcess) {
      ElementKind kind = element.getKind();
      if (kind != ElementKind.METHOD && kind != ElementKind.CLASS) {
        error(element, "Only classes and methods can be annotated with @%s",
            DEEP_LINK_CLASS.getSimpleName());
      }

      if (kind == ElementKind.METHOD) {
        Set<Modifier> methodModifiers = element.getModifiers();
        if (!methodModifiers.contains(Modifier.STATIC)) {
          error(element, "Only static methods can be annotated with @%s",
              DEEP_LINK_CLASS.getSimpleName());
        }
      }

      DeepLink deepLinkAnnotation = element.getAnnotation(DEEP_LINK_CLASS);
      List<String> deepLinks = new ArrayList<>();
      if (deepLinkAnnotation != null) {
        deepLinks.addAll(Arrays.asList(deepLinkAnnotation.value()));
      }
      if (customAnnotatedElements.contains(element)) {
        deepLinks.addAll(enumerateCustomDeepLinks(element, prefixes));
      }
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
    Set<? extends Element> deepLinkHandlerElements =
        roundEnv.getElementsAnnotatedWith(DeepLinkHandler.class);
    for (Element deepLinkHandlerElement : deepLinkHandlerElements) {
      Optional<AnnotationMirror> annotationMirror =
          getAnnotationMirror(deepLinkHandlerElement, DeepLinkHandler.class);
      if (annotationMirror.isPresent()) {
        Iterable<TypeMirror> klasses = getTypeValue(annotationMirror.get(), "value");
        List<TypeElement> typeElements = FluentIterable.from(klasses).transform(
            new Function<TypeMirror, TypeElement>() {
              @Override public TypeElement apply(TypeMirror klass) {
                return MoreTypes.asTypeElement(klass);
              }
            }).toList();
        String packageName = processingEnv.getElementUtils()
            .getPackageOf(deepLinkHandlerElement).getQualifiedName().toString();
        try {
          generateDeepLinkDelegate(packageName, typeElements);
        } catch (IOException e) {
          messager.printMessage(Diagnostic.Kind.ERROR, "Error creating file");
        } catch (RuntimeException e) {
          messager.printMessage(Diagnostic.Kind.ERROR,
              "Internal error during annotation processing: " + e.getClass().getSimpleName());
        }
      }
    }

    Set<? extends Element> deepLinkModuleElements =
        roundEnv.getElementsAnnotatedWith(DeepLinkModule.class);
    for (Element deepLinkModuleElement : deepLinkModuleElements) {
      String packageName = processingEnv.getElementUtils()
          .getPackageOf(deepLinkModuleElement).getQualifiedName().toString();
      try {
        generateDeepLinkLoader(packageName, deepLinkModuleElement.getSimpleName().toString(),
            deepLinkElements);
      } catch (IOException e) {
        messager.printMessage(Diagnostic.Kind.ERROR, "Error creating file");
      } catch (RuntimeException e) {
        messager.printMessage(Diagnostic.Kind.ERROR,
            "Internal error during annotation processing: " + e.getClass().getSimpleName());
      }
    }

    return false;
  }

  private static List<String> enumerateCustomDeepLinks(Element element,
      Map<Element, String[]> prefixesMap) {
    Set<? extends AnnotationMirror> annotationMirrors =
        AnnotationMirrors.getAnnotatedAnnotations(element, DEEP_LINK_SPEC_CLASS);
    final List<String> deepLinks = new ArrayList<>();
    for (AnnotationMirror customAnnotation : annotationMirrors) {
      List<? extends AnnotationValue> suffixes =
          asAnnotationValues(AnnotationMirrors.getAnnotationValue(customAnnotation, "value"));
      String[] prefixes = prefixesMap.get(customAnnotation.getAnnotationType().asElement());
      for (String prefix : prefixes) {
        for (AnnotationValue suffix : suffixes) {
          deepLinks.add(prefix + suffix.getValue());
        }
      }
    }
    return deepLinks;
  }

  private void error(Element e, String msg, Object... args) {
    messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
  }

  private void generateDeepLinkLoader(String packageName, String className,
      List<DeepLinkAnnotatedElement> elements)
      throws IOException {
    CodeBlock.Builder initializer = CodeBlock.builder()
        .add("$T.unmodifiableList($T.asList(\n", CLASS_COLLECTIONS, CLASS_ARRAYS)
        .indent();
    int totalElements = elements.size();
    Collections.sort(elements, new Comparator<DeepLinkAnnotatedElement>() {
      @Override
      public int compare(DeepLinkAnnotatedElement element1, DeepLinkAnnotatedElement element2) {
        DeepLinkUri uri1 = DeepLinkUri.parse(element1.getUri());
        DeepLinkUri uri2 = DeepLinkUri.parse(element2.getUri());
        if (uri1.pathSegments().size() != uri2.pathSegments().size()) {
          return uri2.pathSegments().size() - uri1.pathSegments().size();
        } else {
          if (uri1.queryParameterNames().size() != uri2.queryParameterNames().size()) {
            return uri2.queryParameterNames().size() - uri1.queryParameterNames().size();
          } else {
            if (!uri1.encodedPath().contains("%7B") && uri2.encodedPath().contains("%7B")) {
              return -1;
            } else if (uri1.encodedPath().contains("%7B") && !uri2.encodedPath().contains("%7B")) {
              return 1;
            }
          }
        }
        return 0;
      }
    });
    for (int i = 0; i < totalElements; i++) {
      DeepLinkAnnotatedElement element = elements.get(i);
      String type = "DeepLinkEntry.Type." + element.getAnnotationType().toString();
      ClassName activity = ClassName.get(element.getAnnotatedElement());
      Object method = element.getMethod() == null ? null : element.getMethod();
      String uri = element.getUri();
      initializer.add("new DeepLinkEntry($S, $L, $T.class, $S)$L\n",
          uri, type, activity, method, (i < totalElements - 1) ? "," : "");
    }
    FieldSpec registry = FieldSpec
        .builder(ParameterizedTypeName.get(List.class, DeepLinkEntry.class), "REGISTRY",
            Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
        .initializer(initializer.unindent().add("))").build())
        .build();

    MethodSpec parseMethod = MethodSpec.methodBuilder("parseUri")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(AnnotationSpec.builder(Override.class).build())
        .addParameter(String.class, "uri")
        .returns(DeepLinkEntry.class)
        .beginControlFlow("for (DeepLinkEntry entry : REGISTRY)")
        .beginControlFlow("if (entry.matches(uri))")
        .addStatement("return entry")
        .endControlFlow()
        .endControlFlow()
        .addStatement("return null")
        .build();

    TypeSpec deepLinkLoader = TypeSpec.classBuilder(className + "Loader")
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addSuperinterface(ClassName.get(Parser.class))
        .addField(registry)
        .addMethod(parseMethod)
        .build();

    JavaFile.builder(packageName, deepLinkLoader)
        .build()
        .writeTo(filer);
  }

  private static String moduleNameToLoaderName(TypeElement typeElement) {
    return typeElement.getSimpleName().toString() + "Loader";
  }

  private static ClassName moduleElementToLoaderClassName(TypeElement element) {
    return ClassName.get(getPackage(element).getQualifiedName().toString(),
        element.getSimpleName().toString() + "Loader");
  }

  private static PackageElement getPackage(Element type) {
    while (type.getKind() != ElementKind.PACKAGE) {
      type = type.getEnclosingElement();
    }
    return (PackageElement) type;
  }

  private void generateDeepLinkDelegate(String packageName, List<TypeElement> loaderClasses)
      throws IOException {
    MethodSpec notifyListenerMethod = MethodSpec.methodBuilder("notifyListener")
        .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
        .returns(void.class)
        .addParameter(ANDROID_CONTEXT, "context")
        .addParameter(boolean.class, "isError")
        .addParameter(ClassName.get("android.net", "Uri"), "uri")
        .addParameter(String.class, "errorMessage")
        .addStatement("$T intent = new Intent()", ANDROID_INTENT)
        .addStatement("intent.setAction($T.ACTION)", DeepLinkHandler.class)
        .addStatement("intent.putExtra($T.EXTRA_URI, uri != null ? uri.toString() : $S)",
            DeepLinkHandler.class, "")
        .addStatement("intent.putExtra($T.EXTRA_SUCCESSFUL, !isError)", DeepLinkHandler.class)
        .beginControlFlow("if (isError)")
        .addStatement("intent.putExtra($T.EXTRA_ERROR_MESSAGE, errorMessage)",
            DeepLinkHandler.class)
        .endControlFlow()
        .addStatement("$T.getInstance(context).sendBroadcast(intent)",
            ClassName.get("android.support.v4.content", "LocalBroadcastManager"))
        .build();
    ClassName deepLinkResult = ClassName.get(DLD_PACKAGE_NAME, "DeepLinkResult");
    MethodSpec createResultAndNotifyMethod = MethodSpec.methodBuilder("createResultAndNotify")
        .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
        .returns(deepLinkResult)
        .addParameter(ANDROID_CONTEXT, "context")
        .addParameter(TypeName.BOOLEAN, "successful", Modifier.FINAL)
        .addParameter(ANDROID_URI, "uri", Modifier.FINAL)
        .addParameter(ClassName.get(String.class), "error", Modifier.FINAL)
        .addStatement("notifyListener(context, !successful, uri, error)")
        .addStatement("return new $T(successful, uri != null ? uri.toString() : null, error)",
            deepLinkResult)
        .build();

    FieldSpec tag = FieldSpec
        .builder(String.class, "TAG", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
        .initializer("DeepLinkDelegate.class.getSimpleName()")
        .build();

    FieldSpec loaders = FieldSpec
        .builder(ParameterizedTypeName.get(ClassName.get(List.class),
            WildcardTypeName.subtypeOf(Parser.class)), "loaders", Modifier.PRIVATE,
            Modifier.FINAL)
        .build();

    CodeBlock.Builder loadersInitializer = CodeBlock.builder()
        .add("this.loaders = $T.asList(\n", ClassName.get(Arrays.class))
        .indent();
    int totalElements = loaderClasses.size();
    for (int i = 0; i < totalElements; i++) {
      loadersInitializer.add("$L$L",
          decapitalize(moduleNameToLoaderName(loaderClasses.get(i))),
          i < totalElements - 1 ? "," : "");
    }
    MethodSpec constructor = MethodSpec.constructorBuilder()
        .addModifiers(Modifier.PUBLIC)
        .addParameters(FluentIterable.from(loaderClasses).transform(
            new Function<TypeElement, ParameterSpec>() {
              @Override public ParameterSpec apply(TypeElement typeElement) {
                return ParameterSpec.builder(moduleElementToLoaderClassName(typeElement),
                    decapitalize(moduleNameToLoaderName(typeElement))).build();
              }
            }).toList())
        .addCode(loadersInitializer.unindent().add(");\n").build())
        .build();

    MethodSpec supportsUri = MethodSpec.methodBuilder("supportsUri")
        .addModifiers(Modifier.PUBLIC)
        .returns(TypeName.BOOLEAN)
        .addParameter(String.class, "uriString")
        .addStatement("return findEntry(uriString) != null")
        .build();

    MethodSpec findEntry = MethodSpec.methodBuilder("findEntry")
        .addModifiers(Modifier.PRIVATE)
        .returns(CLASS_DLD_ENTRY)
        .addParameter(String.class, "uriString")
        .beginControlFlow("for (Parser loader : loaders)")
        .addStatement("$T entry = loader.parseUri(uriString)", CLASS_DLD_ENTRY)
        .beginControlFlow("if (entry != null)")
        .addStatement("return entry")
        .endControlFlow()
        .endControlFlow()
        .addStatement("return null")
        .build();

    MethodSpec dispatchFromMethod = MethodSpec.methodBuilder("dispatchFrom")
        .addModifiers(Modifier.PUBLIC)
        .returns(deepLinkResult)
        .addParameter(ANDROID_ACTIVITY, "activity")
        .beginControlFlow("if (activity == null)")
        .addStatement("throw new $T($S)", NullPointerException.class, "activity == null")
        .endControlFlow()
        .addStatement("return dispatchFrom(activity, activity.getIntent())")
        .build();

    MethodSpec dispatchFromMethodWithIntent = MethodSpec.methodBuilder("dispatchFrom")
        .addModifiers(Modifier.PUBLIC)
        .returns(deepLinkResult)
        .addParameter(ANDROID_ACTIVITY, "activity")
        .addParameter(ANDROID_INTENT, "sourceIntent")
        .beginControlFlow("if (activity == null)")
        .addStatement("throw new $T($S)", NullPointerException.class, "activity == null")
        .endControlFlow()
        .beginControlFlow("if (sourceIntent == null)")
        .addStatement("throw new $T($S)", NullPointerException.class, "sourceIntent == null")
        .endControlFlow()
        .addStatement("$T uri = sourceIntent.getData()", ANDROID_URI)
        .beginControlFlow("if (uri == null)")
        .addStatement("return createResultAndNotify(activity, false, null, $S)",
            "No Uri in given activity's intent.")
        .endControlFlow()
        .addStatement("String uriString = uri.toString()")
        .addStatement("$T entry = findEntry(uriString)", CLASS_DLD_ENTRY)
        .beginControlFlow("if (entry != null)")
        .addStatement("$T deepLinkUri = DeepLinkUri.parse(uriString)", CLASS_DLD_URI)
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
        .addStatement("parameterMap.put($T.URI, uri.toString())",
            ClassName.get(DLD_PACKAGE_NAME, "DeepLink"))
        .addStatement("$T parameters", ANDROID_BUNDLE)
        .beginControlFlow("if (sourceIntent.getExtras() != null)")
        .addStatement("parameters = new Bundle(sourceIntent.getExtras())")
        .nextControlFlow("else")
        .addStatement("parameters = new Bundle()")
        .endControlFlow()
        .beginControlFlow(
            "for (Map.Entry<String, String> parameterEntry : parameterMap.entrySet())")
        .addStatement("parameters.putString(parameterEntry.getKey(), parameterEntry.getValue())")
        .endControlFlow()
        .beginControlFlow("try")
        .addStatement("Class<?> c = entry.getActivityClass()")
        .addStatement("$T newIntent", ANDROID_INTENT)
        .addStatement("$T taskStackBuilder = null", TASK_STACK_BUILDER)
        .beginControlFlow("if (entry.getType() == DeepLinkEntry.Type.CLASS)")
        .addStatement("newIntent = new Intent(activity, c)")
        .nextControlFlow("else")
        .addStatement("$T method", Method.class)
        .beginControlFlow("try")
        .addStatement("method = c.getMethod(entry.getMethod(), $T.class)", ANDROID_CONTEXT)
        .beginControlFlow("if (method.getReturnType().equals($T.class))", TASK_STACK_BUILDER)
        .addStatement("taskStackBuilder = (TaskStackBuilder) method.invoke(c, activity)")
        .beginControlFlow("if (taskStackBuilder.getIntentCount() == 0)")
        .addStatement("return createResultAndNotify(activity, false, uri, \"Could not deep "
            + "link to method: \" + entry.getMethod() + \" intents length == 0\" )")
        .endControlFlow()
        .addStatement("newIntent = taskStackBuilder."
            + "editIntentAt(taskStackBuilder.getIntentCount()-1)")
        .nextControlFlow("else")
        .addStatement("newIntent = (Intent) method.invoke(c, activity)")
        .endControlFlow()
        .nextControlFlow("catch ($T exception)", NoSuchMethodException.class)
        .addStatement("method = c.getMethod(entry.getMethod(), $T.class, $T.class)",
            ANDROID_CONTEXT, ANDROID_BUNDLE)
        .beginControlFlow("if (method.getReturnType().equals($T.class))", TASK_STACK_BUILDER)
        .addStatement("taskStackBuilder = "
            + "(TaskStackBuilder) method.invoke(c, activity, parameters)")
        .beginControlFlow("if (taskStackBuilder.getIntentCount() == 0)")
        .addStatement("return createResultAndNotify(activity, false, uri, \"Could not deep "
            + "link to method: \" + entry.getMethod() + \" intents length == 0\" )")
        .endControlFlow()
        .addStatement("newIntent = taskStackBuilder."
            + "editIntentAt(taskStackBuilder.getIntentCount()-1)")
        .nextControlFlow("else")
        .addStatement("newIntent = (Intent) method.invoke(c, activity, parameters)")
        .endControlFlow()
        .endControlFlow()
        .endControlFlow()
        .beginControlFlow("if (newIntent.getAction() == null)")
        .addStatement("newIntent.setAction(sourceIntent.getAction())")
        .endControlFlow()
        .beginControlFlow("if (newIntent.getData() == null)")
        .addStatement("newIntent.setData(sourceIntent.getData())")
        .endControlFlow()
        .addStatement("newIntent.putExtras(parameters)")
        .addStatement("newIntent.putExtra(DeepLink.IS_DEEP_LINK, true)")
        .addStatement("newIntent.putExtra(DeepLink.REFERRER_URI, uri)")
        .beginControlFlow("if (activity.getCallingActivity() != null)")
        .addStatement("newIntent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)")
        .endControlFlow()
        .beginControlFlow("if (taskStackBuilder != null)")
        .addStatement("taskStackBuilder.startActivities()")
        .nextControlFlow("else")
        .addStatement("activity.startActivity(newIntent)")
        .endControlFlow()
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
        .addField(loaders)
        .addMethod(constructor)
        .addMethod(findEntry)
        .addMethod(dispatchFromMethod)
        .addMethod(dispatchFromMethodWithIntent)
        .addMethod(createResultAndNotifyMethod)
        .addMethod(notifyListenerMethod)
        .addMethod(supportsUri)
        .build();

    JavaFile.builder(packageName, deepLinkDelegate)
        .build()
        .writeTo(filer);
  }
}
