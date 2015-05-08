package com.airbnb.deeplinkdispatch.internal;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.DeepLinkEntry;

import javax.lang.model.element.Element;

public class DeepLinkAnnotatedElement {
  private String host;
  private String path;
  private DeepLinkEntry.Type annotationType;
  private String activity;
  private String method;

  public DeepLinkAnnotatedElement(Element element, DeepLinkEntry.Type type) {
    DeepLink annotation = element.getAnnotation(DeepLink.class);

    host = annotation.host();
    path = annotation.path();

    activity = element.getEnclosingElement().getSimpleName().toString();
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
