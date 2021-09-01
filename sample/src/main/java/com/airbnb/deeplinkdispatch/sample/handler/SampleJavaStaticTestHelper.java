package com.airbnb.deeplinkdispatch.sample.handler;

import android.util.Log;

public class SampleJavaStaticTestHelper {

  public static void invokedHandler(TestJavaDeepLinkHandlerDeepLinkArgs parameters) {
    Log.d("SampleJavaDeeplinkHand", "Received handler call with " + parameters.toString());
  }

}
