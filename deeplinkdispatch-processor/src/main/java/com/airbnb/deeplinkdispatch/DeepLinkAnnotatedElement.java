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

import java.net.MalformedURLException;

import javax.lang.model.element.Element;

final class DeepLinkAnnotatedElement {

  private final String uri;
  private final DeepLinkEntry.Type annotationType;
  private final String activity;
  private final String method;

  public DeepLinkAnnotatedElement(String annotation, Element element, DeepLinkEntry.Type type)
      throws MalformedURLException {
    DeepLinkUri url = DeepLinkUri.parse(annotation);
    if (url == null) {
      throw new MalformedURLException("Malformed Uri " + annotation);
    }
    uri = annotation;
    annotationType = type;

    if (type == DeepLinkEntry.Type.METHOD) {
      activity = element.getEnclosingElement().toString();
      method = element.getSimpleName().toString();
    } else {
      activity = element.toString();
      method = null;
    }
  }

  String getUri() {
    return uri;
  }

  DeepLinkEntry.Type getAnnotationType() {
    return annotationType;
  }

  String getActivity() {
    return activity;
  }

  String getMethod() {
    return method;
  }
}
