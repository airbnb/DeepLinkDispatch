package com.airbnb.deeplinkdispatch.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.airbnb.deeplinkdispatch.DeepLinkActivity;

public class DeepLinkReceiver extends BroadcastReceiver {

  private static final String TAG = DeepLinkReceiver.class.getSimpleName();

  @Override
  public void onReceive(Context context, Intent intent) {
    String deepLinkUri = intent.getStringExtra(DeepLinkActivity.EXTRA_URI);

    if (intent.getBooleanExtra(DeepLinkActivity.EXTRA_RESULT_OK, false)) {
      Log.i(TAG, "Success deep linking: " + deepLinkUri);
    } else {
      String errorMessage = intent.getStringExtra(DeepLinkActivity.EXTRA_ERROR_MESSAGE);
      Log.e(TAG, "Error deep linking: " + deepLinkUri + " with error message +" + errorMessage);
    }
  }
}
