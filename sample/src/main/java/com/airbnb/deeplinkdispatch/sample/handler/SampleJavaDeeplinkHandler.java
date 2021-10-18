package com.airbnb.deeplinkdispatch.sample.handler;

import android.content.Context;

import androidx.annotation.NonNull;

import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler;
import com.airbnb.deeplinkdispatch.sample.WebDeepLink;

@WebDeepLink({"/java/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}/{path_segment_variable_4}?show_taxes={query_param_1}&queryParam={query_param_2}"})
public class SampleJavaDeeplinkHandler implements DeepLinkHandler<TestJavaDeepLinkHandlerDeepLinkArgs> {
  @Override
  public void handleDeepLink(@NonNull Context context, TestJavaDeepLinkHandlerDeepLinkArgs parameters) {
    SampleJavaStaticTestHelper.invokedHandler(context, parameters);
    /**
     * From here any internal/3rd party navigation framework can be called the provided args.
     */
  }
}
