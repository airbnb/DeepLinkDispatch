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
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
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
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
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
@SupportedOptions(Documentor.DOC_OUTPUT_PROPERTY_NAME)
public class DeepLinkProcessor extends AbstractProcessor {
  private static final ClassName CLASS_BASE_DEEP_LINK_DELEGATE
          = ClassName.get("com.airbnb.deeplinkdispatch", "BaseDeepLinkDelegate");
  private static final ClassName CLASS_ARRAYS = ClassName.get(Arrays.class);
  private static final ClassName CLASS_COLLECTIONS = ClassName.get(Collections.class);
  private static final Class<DeepLink> DEEP_LINK_CLASS = DeepLink.class;
  private static final Class<DeepLinkSpec> DEEP_LINK_SPEC_CLASS = DeepLinkSpec.class;

  private Filer filer;
  private Messager messager;
  private Documentor documentor;

  @Override public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    filer = processingEnv.getFiler();
    messager = processingEnv.getMessager();
    documentor = new Documentor(processingEnv);
  }

  @Override public Set<String> getSupportedAnnotationTypes() {
    return Sets.newHashSet("*");
  }

  @Override public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override public Set<String> getSupportedOptions() {
    return Collections.singleton(Documentor.DOC_OUTPUT_PROPERTY_NAME);
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
      customAnnotatedElements.addAll(
          roundEnv.getElementsAnnotatedWith(MoreElements.asType(customAnnotation)));
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
        ExecutableElement executableElement = MoreElements.asExecutable(element);
        TypeElement returnType = MoreTypes.asTypeElement(executableElement.getReturnType());
        String qualifiedName = returnType.getQualifiedName().toString();
        if (!qualifiedName.equals("android.content.Intent")
            && !qualifiedName.equals("androidx.core.app.TaskStackBuilder")) {
          error(element, "Only `Intent` or `androidx.core.app.TaskStackBuilder` are supported."
                  + " Please double check your imports and try again.");
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
    Collections.sort(elements, new Comparator<DeepLinkAnnotatedElement>() {
      @Override
      public int compare(DeepLinkAnnotatedElement element1, DeepLinkAnnotatedElement element2) {
        DeepLinkUri uri1 = DeepLinkUri.parse(element1.getUri());
        DeepLinkUri uri2 = DeepLinkUri.parse(element2.getUri());
        int comparisonResult = uri2.pathSegments().size() - uri1.pathSegments().size();
        if (comparisonResult == 0) {
          comparisonResult = uri2.queryParameterNames().size() - uri1.queryParameterNames().size();
        }
        if (comparisonResult == 0) {
          comparisonResult =
                  uri1.encodedPath().split("%7B").length - uri2.encodedPath().split("%7B").length;
        }
        if (comparisonResult == 0) {
          String element1Representation =
                  element1.getUri() + element1.getMethod() + element1.getAnnotationType();
          String element2Representation =
                  element2.getUri() + element2.getMethod() + element2.getAnnotationType();
          comparisonResult = element1Representation.compareTo(element2Representation);
        }
        return comparisonResult;
      }
    });
    documentor.write(elements);
    int totalElements = elements.size();
    for (int i = 0; i < totalElements; i++) {
      DeepLinkAnnotatedElement element = elements.get(i);
      String type = "DeepLinkEntry.Type." + element.getAnnotationType().toString();
      ClassName activity = ClassName.get(element.getAnnotatedElement());
      Object method = element.getMethod();
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
    CodeBlock.Builder loadersInitializer = CodeBlock.builder()
        .add("super($T.asList(\n", ClassName.get(Arrays.class))
        .indent();
    int totalElements = loaderClasses.size();
    for (int i = 0; i < totalElements; i++) {
      loadersInitializer.add("$L$L",
          decapitalize(moduleNameToLoaderName(loaderClasses.get(i))),
          i < totalElements - 1 ? ",\n" : "\n");
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
        .addCode(loadersInitializer.unindent().add("));\n").build())
        .build();

    TypeSpec deepLinkDelegate = TypeSpec.classBuilder("DeepLinkDelegate")
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .superclass(CLASS_BASE_DEEP_LINK_DELEGATE)
        .addMethod(constructor)
        .build();

    JavaFile.builder(packageName, deepLinkDelegate)
        .build()
        .writeTo(filer);
  }
}
