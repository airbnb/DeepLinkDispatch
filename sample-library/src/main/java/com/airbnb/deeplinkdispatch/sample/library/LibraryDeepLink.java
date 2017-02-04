package com.airbnb.deeplinkdispatch.sample.library;

import com.airbnb.deeplinkdispatch.DeepLinkSpec;

@DeepLinkSpec(prefix = { "library://dld" })
public @interface LibraryDeepLink {
    String[] value();
}
