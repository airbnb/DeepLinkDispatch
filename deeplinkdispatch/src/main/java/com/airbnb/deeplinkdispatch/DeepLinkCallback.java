package com.airbnb.deeplinkdispatch;

public interface DeepLinkCallback {
  void onSuccess(String uri);

  void onError(DeepLinkError error);
}
