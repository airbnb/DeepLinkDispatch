package com.airbnb.deeplinkdispatch;

import javax.lang.model.element.Element;

class DeepLinkProcessorException extends RuntimeException {
  private final Element element;

  DeepLinkProcessorException(String message) {
    this(message, null);
  }

  DeepLinkProcessorException(String message, Element element) {
    super(message);
    this.element = element;
  }

  Element getElement() {
    return element;
  }
}
