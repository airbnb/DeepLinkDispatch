package com.airbnb.deeplinkdispatch;

public class DeepLinkError {
  public String uri;
  public String errorMessage;

  public DeepLinkError(String uri, String errorMessage) {
    this.uri = uri;
    this.errorMessage = errorMessage;
  }

  public String getUri() {
    return uri;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
