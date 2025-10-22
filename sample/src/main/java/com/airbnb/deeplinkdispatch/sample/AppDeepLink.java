package com.airbnb.deeplinkdispatch.sample;

import com.airbnb.deeplinkdispatch.DeepLinkSpec;

@DeepLinkSpec(
    prefix = { "app://airbnb" },
    activityClasFqn = "com.airbnb.deeplinkdispatch.sample.DeepLinkActivity"
)
public @interface AppDeepLink {
  String[] value();
}

