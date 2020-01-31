package com.example;

import com.airbnb.deeplinkdispatch.BaseDeepLinkDelegate;
import java.lang.String;
import java.util.Arrays;

public final class DeepLinkDelegate extends BaseDeepLinkDelegate {
  public DeepLinkDelegate(SampleModuleRegistry sampleModuleRegistry) {
    super(Arrays.asList(
      sampleModuleRegistry
    ));
  }

  public DeepLinkDelegate(SampleModuleRegistry sampleModuleRegistry,
                          String pathVariableReplacementValue) {
    super(Arrays.asList(
      sampleModuleRegistry),
      pathVariableReplacementValue
    );
  }
}