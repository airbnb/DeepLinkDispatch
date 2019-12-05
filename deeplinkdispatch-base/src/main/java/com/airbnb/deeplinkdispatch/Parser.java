package com.airbnb.deeplinkdispatch;

import com.airbnb.deeplinkdispatch.base.Match;
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
   * Use the binary matchIndex to find a martch for the given {@link DeepLinkUri}.
   *
   * @param deepLinkUri The {@link DeepLinkUri} the match should be retrieved for.
   * @return Either a {@link DeepLinkEntry} object if a match was found or null if no match was found.
   */
  public DeepLinkEntry idxMatch(DeepLinkUri deepLinkUri) {
    Match elementMatch = matchIndex.matchUri(new SchemeHostAndPath(deepLinkUri).getMatchList(), 0, 0, matchIndex.length());
    if (elementMatch != null) {
      DeepLinkEntry matchedDeeplinkEntry = registry.get(elementMatch.getMatchIndex());
      matchedDeeplinkEntry.setParameters(deepLinkUri, elementMatch.getPlaceholders());
      return matchedDeeplinkEntry;
    } else {
      return null;
    }
  }

}
