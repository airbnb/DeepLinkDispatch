package com.airbnb.deeplinkdispatch;

public interface OnDeepLinkListener {
  void onDeepLinkSuccess(String uri);

  void onDeepLinkError(DeepLinkError error);
}
