package com.airbnb.deeplinkdispatch;

import java.util.List;

/**
 * Parser classes are used for keeping a registry of deep links and dispatching them from Intents.
 */
public abstract class Parser {

  public Parser(List<DeepLinkEntry> registry) {
    this.registry = registry;
  }

  /**
   * The List of DeepLinkEntries collected from a Module. Annotation Processing-generated Children
   * of this class will pass the collected {@code List<DeepLinkEntry>} in the constructor's
   * {@code super()}.
   */
  private final List<DeepLinkEntry> registry;

  public List<DeepLinkEntry> getRegistry() {
    return registry;
  }

  public DeepLinkEntry parseUri(String uri) {
    for (DeepLinkEntry entry : registry) {
      if (entry.matches(uri)) {
        return entry;
      }
    }
    return null;

  }

}
