package com.airbnb.deeplinkdispatch;

public interface Parser {
  DeepLinkEntry parseUri(String uri);
}
