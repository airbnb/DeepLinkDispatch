package com.airbnb.deeplinkdispatch.sample;

import android.app.Application;
import android.util.Log;

import com.airbnb.deeplinkdispatch.DeepLinkError;
import com.airbnb.deeplinkdispatch.OnDeepLinkListener;

public class SampleApplication extends Application implements OnDeepLinkListener {

  private static final String TAG = "DeepLinkDispatch";

  @Override
  public void onDeepLinkSuccess(String uri) {
    Log.i(TAG, "Successful deep link: " + uri.toString());
  }

  @Override
  public void onDeepLinkError(DeepLinkError error) {
    Log.e(TAG, "Deep Link Error: " + error.getErrorMessage());
  }
}
