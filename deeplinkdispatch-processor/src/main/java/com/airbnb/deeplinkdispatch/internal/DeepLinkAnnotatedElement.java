package com.airbnb.deeplinkdispatch.internal;

import com.airbnb.deeplinkdispatch.DeepLinkEntry;

import javax.lang.model.element.Element;

public class DeepLinkAnnotatedElement {

  private final String host;
  private final String path;
  private final DeepLinkEntry.Type annotationType;
  private final String activity;
  private final String method;

  public DeepLinkAnnotatedElement(String annotation, Element element, DeepLinkEntry.Type type) {
    int firstSlash = annotation.indexOf('/');
    if (firstSlash != -1) {
      host = annotation.substring(0, firstSlash);
      path = annotation.substring(firstSlash + 1, annotation.length());
    } else {
      host = annotation;
      path = "";
    }
    annotationType = type;

    if (type == DeepLinkEntry.Type.METHOD) {
      activity = element.getEnclosingElement().toString();
      method = element.getSimpleName().toString();
    } else {
      activity = element.toString();
      method = null;
    }
  }

  public String getHost() {
    return host;
  }

  public String getPath() {
    return path;
  }

  public DeepLinkEntry.Type getAnnotationType() {
    return annotationType;
  }

  public String getActivity() {
    return activity;
  }

  public String getMethod() {
    return method;
  }
}
