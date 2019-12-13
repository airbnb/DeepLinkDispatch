package com.airbnb.deeplinkdispatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.deeplinkdispatch.base.MatchIndex;

import java.util.Collections;
import java.util.List;

/**
 * BaseLoader classes are used for keeping a registry of deep links and dispatching them
 * from Intents.
 */
public abstract class BaseLoader {

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

  public BaseLoader(List<DeepLinkEntry> registry, byte[] matchIndexArray) {
    this.registry = registry;
    this.matchIndex = new MatchIndex(matchIndexArray);
  }

  /**
   * Notice that {@link BaseLoader#registry} is immutable.
   *
   * @return {@code Collections.unmodifiableList<DeepLinkEntry>} {@link BaseLoader#registry}
   */
  public List<DeepLinkEntry> getRegisteredDeepLinks() {
    return registry;
  }

  /**
   * Use a binary match index to match the given URL. Detauls can be find in {@link MatchIndex}.
   *
   * @param deepLinkUri The input {@link DeepLinkUri} to match
   * @return A {@link DeepLinkEntry} if a match was found or null if not.
   */
  public @Nullable
  DeepLinkEntry idxMatch(@NonNull DeepLinkUri deepLinkUri) {
    if (deepLinkUri == null) {
      return null;
    }
    // The first elment is the root, start with the children
    MatchIndex.Match match = matchIndex.matchUri(new SchemeHostAndPath(deepLinkUri).getMatchList(),
        null, 0, 0, matchIndex.length());
    if (match != null) {
      DeepLinkEntry matchedEntry = registry.get(match.getMatchIndex());
      matchedEntry.setParameters(deepLinkUri, match.getParameterMap());
      return matchedEntry;
    }
    return null;
  }

}
