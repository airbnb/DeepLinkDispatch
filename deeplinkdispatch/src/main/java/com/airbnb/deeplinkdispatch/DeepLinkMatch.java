package com.airbnb.deeplinkdispatch;

import java.util.Map;

public class DeepLinkMatch {
  private final DeepLinkEntry entry;
  private final Map<String, String> parameters;
  private final DeepLinkUri deepLinkUri;

  public DeepLinkMatch(DeepLinkEntry entry, Map<String, String> parameters, DeepLinkUri deepLinkUri) {
    this.entry = entry;
    this.parameters = parameters;
    this.deepLinkUri = deepLinkUri;
  }

  public DeepLinkEntry getEntry() {
    return entry;
  }

  public Map<String, String> getParameters() {
    return parameters;
  }

  public DeepLinkUri getUri() {
    return deepLinkUri;
  }
}
