package com.airbnb.deeplinkdispatch;

import com.airbnb.deeplinkdispatch.base.MatchIndex;

import java.util.Collections;
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

  /**
   * A binary match index, created by the annotation processor.
   * In a wrapper to make handling it easier
   */
  private final MatchIndex matchIndex;

  public Parser(List<DeepLinkEntry> registry, byte[] matchIndexArray) {
    this.registry = registry;
    this.matchIndex = new MatchIndex(matchIndexArray);
  }

  /**
   * Notice that {@link com.airbnb.deeplinkdispatch.Parser#registry} is immutable.
   *
   * @return {@code Collections.unmodifiableList<DeepLinkEntry>} {@link Parser#registry}
   */
  public List<DeepLinkEntry> getRegisteredDeepLinks() {
    return registry;
  }

  /**
   * Iterate over {@link #registry} in a given Parser to see if there is a matching
   * {@link DeepLinkEntry#getUriTemplate()}.
   * @param schemeHostAndPath the SchemeHostAndPath of the Deep Link URI that we are trying to match.
   * @return A DeepLinkEntry if one can be found that matches the param uri, otherwise, null.
   */
  public DeepLinkEntry parseUri(SchemeHostAndPath schemeHostAndPath) {
    for (DeepLinkEntry entry : registry) {
      if (entry.matches(schemeHostAndPath)) {
        return entry;
      }
    }
    return null;

  }

  /**
   * Byte array format is:
   * 0                                                    type
   * 1                                                    value length
   * 2..5                                                 children length
   * 6..8                                                 match id
   * 8..(8+value length)                                  value
   * (8+value length)..((8+value length)+children length) children
   */
  public DeepLinkEntry idxMatch(DeepLinkUri deepLinkUri) {
    // The first elment is the root, start with the children
    int elementMatch = matchIndex.matchUri(new SchemeHostAndPath(deepLinkUri).getMatchList(), 0, 0, matchIndex.length());
    return elementMatch != -1 ? registry.get(elementMatch) : null;
  }

}
