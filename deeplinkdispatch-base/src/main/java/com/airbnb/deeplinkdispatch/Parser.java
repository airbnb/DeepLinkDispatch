package com.airbnb.deeplinkdispatch;

import java.util.List;

/**
 * Parser classes are used for keeping a registry of deep links and dispatching them from Intents.
 */
public abstract class Parser {

  /**
   * The List of DeepLinkEntries collected from a Module. Annotation Processing-generated Children
   * of this class will pass the collected {@code List<DeepLinkEntry>} in the constructor's
   * {@code super()}. The generated implementation will be a {@link
   * Collections#unmodifiableList(java.util.List)}.
   */
  private final List<DeepLinkEntry> registry;

  public Parser(List<DeepLinkEntry> registry) {
    this.registry = registry;
  }

  /**
   * Notice that {@link com.airbnb.deeplinkdispatch.Parser#registry} is immutable.
   *
   * @return {@code Collections.unmodifiableList<DeepLinkEntry>} {@link Parser#registry}
   */
  public List<DeepLinkEntry> getRegisteredDeepLinks() {
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
