package com.airbnb.deeplinkdispatch;

/**
 * Parser classes are used for keeping a registry of deep links and dispatching them from Intents.
 */
public interface Parser {
  DeepLinkEntry parseUri(String uri);
}
