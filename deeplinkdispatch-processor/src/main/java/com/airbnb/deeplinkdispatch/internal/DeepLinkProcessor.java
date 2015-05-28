package com.airbnb.deeplinkdispatch.internal;

import com.google.auto.service.AutoService;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.DeepLinkEntry;
import com.airbnb.deeplinkdispatch.DeepLinkRegistry;
import com.airbnb.deeplinkdispatch.DeepLinks;
import com.airbnb.deeplinkdispatch.Loader;
import com.airbnb.deeplinkdispatch.javawriter.JavaWriter;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
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
    JavaFileObject jfo = filer.createSourceFile("DeepLinkActivity");

    Writer writer = jfo.openWriter();
    JavaWriter jw = new JavaWriter(writer);
    jw.emitPackage("com.airbnb.deeplinkdispatch");

    List<String> imports = Arrays.asList("android.app.Activity",
        "android.content.Context",
        "android.content.Intent",
        "android.net.Uri",
        "android.os.Bundle",
        "android.util.Log",
        "android.util.Log",
        "java.lang.reflect.InvocationTargetException",
        "java.lang.reflect.Method",
        "java.util.Map");
    jw.emitImports(imports);
    jw.emitEmptyLine();

    jw.beginType("DeepLinkActivity", "class", EnumSet.of(Modifier.PUBLIC), "Activity");
    jw.emitEmptyLine();

    jw.emitField("String",
        "TAG",
        EnumSet.of(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL),
        "DeepLinkActivity.class.getSimpleName()");
    jw.emitEmptyLine();

    jw.emitAnnotation(Override.class);
    jw.beginMethod("void", "onCreate", EnumSet.of(Modifier.PROTECTED), "Bundle",
        "savedInstanceState");

    jw.emitStatement("super.onCreate(savedInstanceState)");
    jw.emitEmptyLine();

    jw.emitStatement("Loader loader = new com.airbnb.deeplinkdispatch.DeepLinkLoader()");
    jw.emitStatement("DeepLinkRegistry registry = new DeepLinkRegistry(loader)");
    jw.emitStatement("Uri uri = getIntent().getData()");
    jw.emitStatement("String hostPath = uri.getHost() + uri.getPath()");
    jw.emitStatement("DeepLinkEntry entry = registry.parseUri(hostPath)");
    jw.emitEmptyLine();

    jw.beginControlFlow("if (entry != null)");
    jw.emitStatement("Map<String, String> parameterMap = entry.getParameters(hostPath)");
    jw.emitEmptyLine();

    jw.beginControlFlow("for (String queryParameter : uri.getQueryParameterNames())");
    jw.beginControlFlow("if (parameterMap.containsKey(queryParameter))");
    jw.emitStatement(
        "Log.w(TAG, \"Duplicate parameter name in path and query param: \" + queryParameter)");
    jw.endControlFlow();
    jw.emitEmptyLine();

    jw.emitStatement("parameterMap.put(queryParameter, uri.getQueryParameter(queryParameter))");
    jw.endControlFlow();
    jw.emitEmptyLine();

    jw.beginControlFlow("try");
    jw.emitStatement("Class<?> c = Class.forName(entry.getActivity())");
    jw.emitEmptyLine();

    jw.emitStatement("Intent intent");
    jw.beginControlFlow("if (entry.getType() == DeepLinkEntry.Type.CLASS)");
    jw.emitStatement("intent = new Intent(this, c)");
    jw.nextControlFlow("else");
    jw.emitStatement("Method method = c.getMethod(entry.getMethod(), Context.class)");
    jw.emitStatement("intent = (Intent) method.invoke(c, this)");
    jw.endControlFlow();
    jw.emitEmptyLine();

    jw.beginControlFlow("if (intent.getAction() == null)");
    jw.emitStatement("intent.setAction(getIntent().getAction())");
    jw.endControlFlow();
    jw.emitEmptyLine();

    jw.beginControlFlow("if (intent.getData() == null)");
    jw.emitStatement("intent.setData(getIntent().getData())");
    jw.endControlFlow();
    jw.emitEmptyLine();

    jw.emitStatement("Bundle parameters");
    jw.beginControlFlow("if (getIntent().getExtras() != null)");
    jw.emitStatement("parameters = new Bundle(getIntent().getExtras())");
    jw.nextControlFlow("else");
    jw.emitStatement("parameters = new Bundle()");
    jw.endControlFlow();
    jw.emitEmptyLine();

    jw.beginControlFlow("for (Map.Entry<String, String> parameterEntry : parameterMap.entrySet())");
    jw.emitStatement("parameters.putString(parameterEntry.getKey(), parameterEntry.getValue())");
    jw.endControlFlow();
    jw.emitStatement("intent.putExtras(parameters)");
    jw.emitStatement("intent.putExtra(DeepLink.IS_DEEP_LINK, true)");
    jw.emitEmptyLine();

    jw.emitStatement("startActivity(intent)");
    jw.emitStatement("notifyListener(false, uri, null)");
    jw.nextControlFlow("catch (ClassNotFoundException exception)");
    jw.emitStatement("notifyListener(true, uri, \"Deep link to non-existent class: \" + entry.getActivity())");
    jw.nextControlFlow("catch (NoSuchMethodException exception)");
    jw.emitStatement("notifyListener(true, uri, \"Deep link to non-existent method: \" + entry.getMethod())");
    jw.nextControlFlow("catch (IllegalAccessException exception)");
    jw.emitStatement("notifyListener(true, uri, \"Could not deep link to method: \" + entry.getMethod())");
    jw.nextControlFlow("catch(InvocationTargetException  exception)");
    jw.emitStatement("notifyListener(true, uri, \"Could not deep link to method: \" + entry.getMethod())");
    jw.nextControlFlow("finally");
    jw.emitStatement("finish()");
    jw.endControlFlow();

    jw.nextControlFlow("else");
    jw.emitStatement(
        "notifyListener(true, uri, \"No registered entity to handle deep link: \" + uri.toString())");
    jw.emitStatement("finish()");
    jw.endControlFlow();

    jw.endMethod();

    jw.beginMethod("void", "notifyListener", EnumSet.of(Modifier.PRIVATE), "boolean",
                   "isError", "Uri", "uri", "String", "errorMessage");
    jw.beginControlFlow("if (getApplication() instanceof DeepLinkCallback)");
    jw.emitStatement("DeepLinkCallback listener = (DeepLinkCallback) getApplication()");
    jw.beginControlFlow("if (!isError)");
    jw.emitStatement("listener.onSuccess(uri.toString())");
    jw.nextControlFlow("else");
    jw.emitStatement("listener.onError(new DeepLinkError(uri.toString(), errorMessage))");
    jw.endControlFlow();
    jw.endControlFlow();
    jw.endMethod();

    jw.endType();

    jw.close();
  }
}
