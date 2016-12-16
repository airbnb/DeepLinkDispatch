package com.airbnb.deeplinkdispatch.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;

public class DeepLinkReceiver extends BroadcastReceiver {
  private static final String TAG = DeepLinkReceiver.class.getSimpleName();

  @Override
  public void onReceive(Context context, Intent intent) {
    String deepLinkUri = intent.getStringExtra(DeepLinkHandler.EXTRA_URI);

    if (intent.getBooleanExtra(DeepLinkHandler.EXTRA_SUCCESSFUL, false)) {
      Log.i(TAG, "Success deep linking: " + deepLinkUri);
    } else {
      String errorMessage = intent.getStringExtra(DeepLinkHandler.EXTRA_ERROR_MESSAGE);
      Log.e(TAG, "Error deep linking: " + deepLinkUri + " with error message +" + errorMessage);
    }
  }
}
