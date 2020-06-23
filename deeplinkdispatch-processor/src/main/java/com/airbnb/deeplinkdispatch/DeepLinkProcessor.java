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

import com.airbnb.deeplinkdispatch.base.Utils;
import com.google.auto.common.AnnotationMirrors;
import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
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
import static com.airbnb.deeplinkdispatch.ProcessorUtils.decapitalize;
import static com.airbnb.deeplinkdispatch.ProcessorUtils.hasEmptyOrNullString;
import static com.airbnb.deeplinkdispatch.UrlTreeKt.configurablePathSegmentSuffix;
import static com.airbnb.deeplinkdispatch.UrlTreeKt.configurablePathSegmentPrefix;
import static com.airbnb.deeplinkdispatch.base.Utils.isConfigurablePathSegment;
import static com.google.auto.common.MoreElements.getAnnotationMirror;

public class DeepLinkProcessor extends AbstractProcessor {
  private static final String PACKAGE_NAME = "com.airbnb.deeplinkdispatch";
  private static final String OPTION_CUSTOM_ANNOTATIONS = "deepLink.customAnnotations";
  private static final String OPTION_INCREMENTAL = "deepLink.incremental";
  private static final ClassName CLASS_BASE_DEEP_LINK_DELEGATE
    = ClassName.get(PACKAGE_NAME, "BaseDeepLinkDelegate");
  private static final ClassName CLASS_ARRAYS = ClassName.get(Arrays.class);
  private static final ClassName CLASS_COLLECTIONS = ClassName.get(Collections.class);
  private static final ClassName CLASS_UTILS = ClassName.get(Utils.class);
  private static final ClassName CLASS_DEEP_LINK_ENTRY
    = ClassName.get(PACKAGE_NAME, DeepLinkEntry.class.getSimpleName());
  private static final Class<DeepLink> DEEP_LINK_CLASS = DeepLink.class;
  private static final Class<DeepLinkSpec> DEEP_LINK_SPEC_CLASS = DeepLinkSpec.class;
  public static final String REGISTRY_CLASS_SUFFIX = "Registry";

  private Filer filer;
  private Messager messager;
  private Documentor documentor;
  private IncrementalMetadata incrementalMetadata;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    filer = processingEnv.getFiler();
    messager = processingEnv.getMessager();
    documentor = new Documentor(processingEnv);
    incrementalMetadata = getIncrementalMetadata();
  }

  private IncrementalMetadata getIncrementalMetadata() {
    if (!"true".equalsIgnoreCase(processingEnv.getOptions().get(OPTION_INCREMENTAL))) {
      return null;
    }
    Map<String, String> options = processingEnv.getOptions();
    String customAnnotationOption = options.get(OPTION_CUSTOM_ANNOTATIONS);
    if (customAnnotationOption == null) {
      return new IncrementalMetadata(new String[0]);
    }
    return new IncrementalMetadata(customAnnotationOption.split(","));
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    if (incrementalMetadata != null) {
      HashSet<String> annotationTypes = Sets.newHashSet(incrementalMetadata.customAnnotations);
      annotationTypes.add(DeepLink.class.getCanonicalName());
      annotationTypes.add(DeepLinkHandler.class.getCanonicalName());
      annotationTypes.add(DeepLinkModule.class.getCanonicalName());
      return annotationTypes;

    } else {
      return Sets.newHashSet("*");
    }
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public Set<String> getSupportedOptions() {
    HashSet<String> supportedOptions = Sets.newHashSet(
      Documentor.DOC_OUTPUT_PROPERTY_NAME
    );
    if (incrementalMetadata != null) {
      supportedOptions.add("org.gradle.annotation.processing.aggregating");
    }
    return supportedOptions;
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    try {
      processInternal(annotations, roundEnv);
    } catch (DeepLinkProcessorException e) {
      error(e.getElement(), e.getMessage());
    }
    return false;
  }

  private void processInternal(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    Set<Element> customAnnotations = new HashSet<>();
    for (Element annotation : annotations) {
      if (annotation.getAnnotation(DEEP_LINK_SPEC_CLASS) != null) {
        customAnnotations.add(annotation);
      }
    }

    Map<Element, String[]> prefixes = new HashMap<>();
    Set<Element> elementsToProcess = new HashSet<>();
    for (Element customAnnotation : customAnnotations) {
      ElementKind kind = customAnnotation.getKind();
      if (kind != ElementKind.ANNOTATION_TYPE) {
        error(customAnnotation, "Only annotation types can be annotated with @%s",
          DEEP_LINK_SPEC_CLASS.getSimpleName());
      }
      String[] prefix = customAnnotation.getAnnotation(DEEP_LINK_SPEC_CLASS).prefix();
      if (hasEmptyOrNullString(prefix)) {
        error(customAnnotation, "Prefix property cannot have null or empty strings");
      }
      if (prefix.length == 0) {
        error(customAnnotation, "Prefix property cannot be empty");
      }
      prefixes.put(customAnnotation, prefix);
      elementsToProcess.addAll(
        roundEnv.getElementsAnnotatedWith(MoreElements.asType(customAnnotation)));
    }

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
      deepLinks.addAll(enumerateCustomDeepLinks(element, prefixes));
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
            @Override
            public TypeElement apply(TypeMirror klass) {
              return MoreTypes.asTypeElement(klass);
            }
          }).toList();
        String packageName = processingEnv.getElementUtils()
          .getPackageOf(deepLinkHandlerElement).getQualifiedName().toString();
        try {
          generateDeepLinkDelegate(packageName, typeElements, deepLinkHandlerElement);
        } catch (IOException e) {
          messager.printMessage(Diagnostic.Kind.ERROR, "Error creating file");
        } catch (RuntimeException e) {
          StringWriter sw = new StringWriter();
          PrintWriter pw = new PrintWriter(sw);
          e.printStackTrace(pw);
          messager.printMessage(Diagnostic.Kind.ERROR,
            "Internal error during annotation processing: " + sw.toString());
        }
      }
    }

    Set<? extends Element> deepLinkModuleElements =
      roundEnv.getElementsAnnotatedWith(DeepLinkModule.class);
    for (Element deepLinkModuleElement : deepLinkModuleElements) {
      String packageName = processingEnv.getElementUtils()
        .getPackageOf(deepLinkModuleElement).getQualifiedName().toString();
      Set<Element> originatingElements = new HashSet<Element>(elementsToProcess);
      originatingElements.add(deepLinkModuleElement);
      try {
        generateDeepLinkRegistry(packageName, deepLinkModuleElement.getSimpleName().toString(),
          deepLinkElements, originatingElements);
      } catch (IOException e) {
        messager.printMessage(Diagnostic.Kind.ERROR, "Error creating file");
      } catch (RuntimeException e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        messager.printMessage(Diagnostic.Kind.ERROR,
          "Internal error during annotation processing: " + sw.toString());
      }
    }
  }

  private static List<String> enumerateCustomDeepLinks(Element element,
                                                       Map<Element, String[]> prefixesMap) {
    Set<? extends AnnotationMirror> annotationMirrors =
      AnnotationMirrors.getAnnotatedAnnotations(element, DEEP_LINK_SPEC_CLASS);
    final List<String> deepLinks = new ArrayList<>();
    for (AnnotationMirror customAnnotation : annotationMirrors) {
      List<? extends AnnotationValue> suffixes =
        asAnnotationValues(AnnotationMirrors.getAnnotationValue(customAnnotation, "value"));

      Element customElement = customAnnotation.getAnnotationType().asElement();
      String[] prefixes = prefixesMap.get(customElement);

      if (prefixes == null) {
        throw new DeepLinkProcessorException(
          "Unable to find annotation '"
            + customElement
            + "' you must update "
            + "'deepLink.customAnnotations' within the build.gradle",
          customElement);
      }

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

  private void generateDeepLinkRegistry(String packageName, String className,
                                        List<DeepLinkAnnotatedElement> elements,
                                        Set<Element> originatingElements)
    throws IOException {
    CodeBlock.Builder deeplinks = CodeBlock.builder()
      .add("super($T.unmodifiableList($T.<$T>asList(\n",
        CLASS_COLLECTIONS, CLASS_ARRAYS, CLASS_DEEP_LINK_ENTRY)
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
    Root urisTrie = new Root();
    Set<String> pathVariableKeys = new HashSet<>();
    for (int i = 0; i < totalElements; i++) {
      DeepLinkAnnotatedElement element = elements.get(i);
      String type = "DeepLinkEntry.Type." + element.getAnnotationType().toString();
      ClassName activity = ClassName.get(element.getAnnotatedElement());
      Object method = element.getMethod();
      String uri = element.getUri();
      DeepLinkUri deeplinkUri = DeepLinkUri.parse(uri);

      try {
        urisTrie.addToTrie(i, deeplinkUri, element.getAnnotatedElement().toString(),
          element.getMethod());
      } catch (IllegalArgumentException e) {
        error(element.getAnnotatedElement(), e.getMessage());
      }

      //Keep track of pathVariables added in a module so that we can check at runtime to ensure
      //that all pathVariables have a corresponding entry provided to BaseDeepLinkDelegate.
      for (String pathSegment : deeplinkUri.pathSegments()) {
        if (isConfigurablePathSegment(pathSegment)) {
          pathVariableKeys.add(pathSegment.substring(configurablePathSegmentPrefix.length(),
            pathSegment.length() - configurablePathSegmentSuffix.length()));
        }
      }
      deeplinks.add("new DeepLinkEntry($S, $L, $T.class, $S)$L\n",
        uri, type, activity, method, (i < totalElements - 1) ? "," : "");
    }

    TypeSpec.Builder deepLinkRegistryBuilder = TypeSpec.classBuilder(className
      + REGISTRY_CLASS_SUFFIX).addModifiers(Modifier.PUBLIC, Modifier.FINAL)
      .superclass(ClassName.get(BaseRegistry.class));

    StringBuilder stringMethodNames = getStringMethodNames(urisTrie, deepLinkRegistryBuilder);

    MethodSpec constructor = MethodSpec.constructorBuilder()
      .addModifiers(Modifier.PUBLIC)
      .addCode(deeplinks.unindent().add(")), $T.readMatchIndexFromStrings( new String[] {"
        + stringMethodNames + "})", CLASS_UTILS).build())
      .addCode(generatePathVariableKeysBlock(pathVariableKeys))
      .build();

    // For debugging it is nice to have a file version of the index, just comment this in to get
    // on in the classpath
//    FileObject indexResource = filer.createResource(StandardLocation.CLASS_OUTPUT, "",
//    MatchIndex.getMatchIdxFileName(className));
//    urisTrie.writeToOutoutStream(indexResource.openOutputStream());

    deepLinkRegistryBuilder.addMethod(constructor);
    for (Element originatingElement : originatingElements) {
      deepLinkRegistryBuilder.addOriginatingElement(originatingElement);
    }

    TypeSpec deepLinkRegistry = deepLinkRegistryBuilder.build();

    JavaFile.builder(packageName, deepLinkRegistry)
      .build()
      .writeTo(filer);
  }

  private CodeBlock generatePathVariableKeysBlock(Set<String> pathVariableKeys) {
    CodeBlock.Builder pathVariableKeysBuilder = CodeBlock.builder();
    pathVariableKeysBuilder.add(",\n" + "new $T[]{",
      ClassName.get(String.class));
    String[] pathVariableKeysArray = pathVariableKeys.toArray(new String[0]);
    for (int i = 0; i < pathVariableKeysArray.length; i++) {
      pathVariableKeysBuilder.add("$S", pathVariableKeysArray[i]);
      if (i < pathVariableKeysArray.length - 1) {
        pathVariableKeysBuilder.add(", ");
      }
    }
    pathVariableKeysBuilder.add("});\n");
    return pathVariableKeysBuilder.build();
  }

  /**
   * Add methods containing the Strings to store the match index to the deepLinkRegistryBuilder and
   * return a string which contains the calls to those methods.
   * <p>
   * e.g. "method1(), method2()" etc.
   *
   * @param urisTrie                The {@link UrlTreeKt} containing all Urls that can be matched.
   * @param deepLinkRegistryBuilder The builder used to add the methods
   * @return
   */
  @NotNull
  private StringBuilder getStringMethodNames(Root urisTrie,
                                             TypeSpec.Builder deepLinkRegistryBuilder) {
    int i = 0;
    StringBuilder stringMethodNames = new StringBuilder();
    for (String string : urisTrie.getStrings()) {
      String methodName = "matchIndex" + i;
      stringMethodNames.append(methodName).append("(), ");
      deepLinkRegistryBuilder.addMethod(MethodSpec.methodBuilder(methodName)
        .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
        .returns(String.class)
        .addCode(CodeBlock.builder().add("return $S;", string).build()).build());
      i++;
    }
    return stringMethodNames;
  }

  private static String moduleNameToRegistryName(TypeElement typeElement) {
    return typeElement.getSimpleName().toString() + REGISTRY_CLASS_SUFFIX;
  }

  private static ClassName moduleElementToRegistryClassName(TypeElement element) {
    return ClassName.get(getPackage(element).getQualifiedName().toString(),
      element.getSimpleName().toString() + REGISTRY_CLASS_SUFFIX);
  }

  private static PackageElement getPackage(Element type) {
    while (type.getKind() != ElementKind.PACKAGE) {
      type = type.getEnclosingElement();
    }
    return (PackageElement) type;
  }

  private void generateDeepLinkDelegate(String packageName,
                                        List<TypeElement> registryClasses,
                                        Element originatingElement)
    throws IOException {

    CodeBlock.Builder moduleRegistriesArgument = CodeBlock.builder();
    int totalElements = registryClasses.size();
    for (int i = 0; i < totalElements; i++) {
      moduleRegistriesArgument.add("$L$L",
        decapitalize(moduleNameToRegistryName(registryClasses.get(i))),
        i < totalElements - 1 ? ",\n" : "");
    }

    CodeBlock registriesInitializerBuilder = CodeBlock.builder()
      .add("super($T.asList(\n", ClassName.get(Arrays.class))
      .indent()
      .add(moduleRegistriesArgument.build())
      .add("\n").unindent().add("));\n")
      .build();

    CodeBlock registriesInitializerBuilderWithPathVariables = CodeBlock.builder()
      .add("super($T.asList(\n", ClassName.get(Arrays.class))
      .indent()
      .add(moduleRegistriesArgument.build())
      .add("),\nconfigurablePathSegmentReplacements").unindent().add("\n);\n")
      .build();

    MethodSpec constructor = MethodSpec.constructorBuilder()
      .addModifiers(Modifier.PUBLIC)
      .addParameters(FluentIterable.from(registryClasses).transform(
        new Function<TypeElement, ParameterSpec>() {
          @Override
          public ParameterSpec apply(TypeElement typeElement) {
            return ParameterSpec.builder(moduleElementToRegistryClassName(typeElement),
              decapitalize(moduleNameToRegistryName(typeElement))).build();
          }
        }).toList())
      .addCode(registriesInitializerBuilder)
      .build();

    ParameterSpec configurablePathSegmentReplacementsParam = ParameterSpec.builder(
      ParameterizedTypeName.get(Map.class, String.class, String.class),
      "configurablePathSegmentReplacements")
      .build();

    MethodSpec constructorWithPathVariables = MethodSpec.constructorBuilder()
      .addModifiers(Modifier.PUBLIC)
      .addParameters(
        registryClasses.stream().map(typeElement ->
          ParameterSpec.builder(moduleElementToRegistryClassName(typeElement),
            decapitalize(moduleNameToRegistryName(typeElement)))
            .build())
          .collect(Collectors.toList())
      )
      .addParameter(configurablePathSegmentReplacementsParam)
      .addCode(registriesInitializerBuilderWithPathVariables)
      .build();

    TypeSpec deepLinkDelegate = TypeSpec.classBuilder("DeepLinkDelegate")
      .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
      .superclass(CLASS_BASE_DEEP_LINK_DELEGATE)
      .addMethod(constructor)
      .addMethod(constructorWithPathVariables)
      .addOriginatingElement(originatingElement)
      .build();

    JavaFile.builder(packageName, deepLinkDelegate)
      .build()
      .writeTo(filer);
  }

  private static final class IncrementalMetadata {
    private String[] customAnnotations;

    private IncrementalMetadata(String[] customAnnotations) {
      this.customAnnotations = customAnnotations;
    }
  }
}
