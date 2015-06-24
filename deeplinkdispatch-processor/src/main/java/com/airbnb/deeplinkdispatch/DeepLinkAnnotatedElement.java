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

import javax.lang.model.element.Element;

final class DeepLinkAnnotatedElement {

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
