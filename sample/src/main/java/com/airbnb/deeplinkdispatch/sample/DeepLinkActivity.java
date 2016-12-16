package com.airbnb.deeplinkdispatch.sample;

import android.app.Activity;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;

@DeepLinkHandler({ SampleModule.class })
public class DeepLinkActivity extends Activity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    DeepLinkDelegate deepLinkDelegate = new DeepLinkDelegate(new SampleModuleLoader());
    deepLinkDelegate.dispatchFrom(this);
    finish();
  }
}
