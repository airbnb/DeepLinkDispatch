package com.airbnb.deeplinkdispatch.sample;

import android.app.Application;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class SampleApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    IntentFilter intentFilter = new IntentFilter("com.airbnb.deeplinkdispatch.DEEPLINK_ACTION");
    LocalBroadcastManager.getInstance(this).registerReceiver(new DeepLinkReceiver(), intentFilter);
  }
}
