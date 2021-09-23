package com.airbnb.deeplinkdispatch.sample.handler;

import android.content.Context;
import android.util.Log;

public class SampleJavaStaticTestHelper {

  public static void invokedHandler(Context context, TestJavaDeepLinkHandlerDeepLinkArgs parameters) {
    Log.d("SampleJavaDeeplinkHand", "Received handler call with " + parameters.toString());
  }

}
