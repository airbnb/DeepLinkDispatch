package com.airbnb.deeplinkdispatch.sample;

import com.airbnb.deeplinkdispatch.DeepLinkSpec;

@DeepLinkSpec(prefix = { "http://airbnb.com", "https://airbnb.com" })
public @interface WebDeepLink {
  String[] value();
}
