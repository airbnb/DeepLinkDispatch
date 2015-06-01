package com.airbnb.deeplinkdispatch.sample;

import android.app.Application;
import android.util.Log;

import com.airbnb.deeplinkdispatch.DeepLinkCallback;
import com.airbnb.deeplinkdispatch.DeepLinkError;

public class SampleApplication extends Application implements DeepLinkCallback {

  private static final String TAG = "DeepLinkDispatch";

  @Override
  public void onSuccess(String uri) {
    Log.i(TAG, "Successful deep link: " + uri.toString());
  }

  @Override
  public void onError(DeepLinkError error) {
    Log.e(TAG, "Deep Link Error: " + error.getErrorMessage());
  }
}
