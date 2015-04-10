package com.airbnb.deeplinkdispatch.internal;

import com.airbnb.deeplinkdispatch.DeepLink;

import javax.lang.model.element.Element;

public class DeepLinkAnnotatedMethod {

  private String uri;
  private String method;
  private String activity;

  public DeepLinkAnnotatedMethod(Element element) {
    DeepLink annotation = element.getAnnotation(DeepLink.class);

    uri = annotation.uri();
    method = element.getSimpleName().toString();
    activity = element.getEnclosingElement().getSimpleName().toString();
  }

  public String getUri() {
    return uri;
  }

  public String getMethod() {
    return method;
  }

  public String getActivity() {
    return activity;
  }
}
