package com.airbnb.deeplinkdispatch.internal;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.internal.DeepLinkEntry.Type;

import javax.lang.model.element.Element;

public class DeepLinkAnnotatedElement {
  private String uri;
  private Type annotationType;
  private String activity;
  private String method;

  public DeepLinkAnnotatedElement(Element element, Type type) {
    DeepLink annotation = element.getAnnotation(DeepLink.class);

    uri = annotation.uri();
    activity = element.getEnclosingElement().getSimpleName().toString();
    annotationType = type;

    if (type == Type.METHOD) {
      activity = element.getEnclosingElement().toString();
      method = element.getSimpleName().toString();
    } else {
      activity = element.toString();
      method = null;
    }
  }

  public String getUri() {
    return uri;
  }

  public Type getAnnotationType() {
    return annotationType;
  }

  public String getActivity() {
    return activity;
  }

  public String getMethod() {
    return method;
  }
}
