package com.airbnb.deeplinkdispatch;

import java.util.Map;

class DeepLinkMatch {
  private final DeepLinkEntry entry;
  private final Map<String, String> parameters;
  private final DeepLinkUri deepLinkUri;

  DeepLinkMatch(DeepLinkEntry entry, Map<String, String> parameters, DeepLinkUri deepLinkUri) {
    this.entry = entry;
    this.parameters = parameters;
    this.deepLinkUri = deepLinkUri;
  }

  DeepLinkEntry getEntry() {
    return entry;
  }

  Map<String, String> getParameters() {
    return parameters;
  }

  DeepLinkUri getUri() {
    return deepLinkUri;
  }
}
