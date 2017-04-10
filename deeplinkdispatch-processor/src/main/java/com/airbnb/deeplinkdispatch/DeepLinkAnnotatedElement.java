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

import javax.annotation.Nullable;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

final class DeepLinkAnnotatedElement {
  private final String uri;
  private final DeepLinkEntry.Type annotationType;
  private final TypeElement annotatedElement;
  private final String method;
  private final Element element;
  private final String[] prefixes;

  DeepLinkAnnotatedElement(String annotation, Element element, DeepLinkEntry.Type type,
                           @Nullable String[] prefixes)
          throws MalformedURLException {
    this.prefixes = prefixes;

    validateAnnotation(annotation);

    uri = annotation;
    annotationType = type;

    if (type == DeepLinkEntry.Type.METHOD) {
      annotatedElement = (TypeElement) element.getEnclosingElement();
      method = element.getSimpleName().toString();
    } else {
      annotatedElement = (TypeElement) element;
      method = null;
    }
    this.element = element;
  }

  private void validateAnnotation(String annotation) throws MalformedURLException {
    DeepLinkUri url;

    if (prefixes == null) {
      url = DeepLinkUri.parse(annotation);
      if (url == null) {
        throw new MalformedURLException("Malformed Uri " + annotation);
      }
    } else {
      for (String prefix : prefixes) {
        String fullAnnotation = prefix + annotation;
        url = DeepLinkUri.parse(fullAnnotation);
        if (url == null) {
          throw new MalformedURLException("Malformed Uri " + fullAnnotation);
        }
      }
    }
  }

  String getUri() {
    return uri;
  }

  DeepLinkEntry.Type getAnnotationType() {
    return annotationType;
  }

  TypeElement getAnnotatedElement() {
    return annotatedElement;
  }

  String getMethod() {
    return method;
  }

  Element getElement() {
    return element;
  }

  String[] getPrefixes() {
    return prefixes;
  }
}
