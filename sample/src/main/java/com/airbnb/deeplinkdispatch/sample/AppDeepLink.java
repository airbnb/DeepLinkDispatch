package com.airbnb.deeplinkdispatch.sample;

import com.airbnb.deeplinkdispatch.DeepLinkSpec;

@DeepLinkSpec(
    prefix = { "app://airbnb" },
        activityClassFqn = "com.airbnb.deeplinkdispatch.sample.DeepLinkActivity"
)
public @interface AppDeepLink {
  String[] value();
}

