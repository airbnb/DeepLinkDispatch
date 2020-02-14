package com.example;

import com.airbnb.deeplinkdispatch.BaseDeepLinkDelegate;
import java.lang.String;
import java.util.Arrays;
import java.util.Map;

public final class DeepLinkDelegate extends BaseDeepLinkDelegate {
  public DeepLinkDelegate(SampleModuleRegistry sampleModuleRegistry) {
    super(Arrays.asList(
      sampleModuleRegistry
    ));
  }

  public DeepLinkDelegate(SampleModuleRegistry sampleModuleRegistry,
                          Map<String, String> configurablePathSegmentReplacements) {
    super(Arrays.asList(
      sampleModuleRegistry),
      configurablePathSegmentReplacements
    );
  }
}
