package com.airbnb.deeplinkdispatch;

public class DeepLinkError {
  public final String uri;
  public final String errorMessage;

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
