package com.airbnb.deeplinkdispatch.sample;

import com.airbnb.deeplinkdispatch.DeepLinkSpec;

@DeepLinkSpec(prefix = { "app://airbnb" })
public @interface AppDeepLink {
  String[] value();
}

