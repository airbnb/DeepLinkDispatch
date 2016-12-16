package com.airbnb.deeplinkdispatch.sample;

import android.app.Activity;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.airbnb.deeplinkdispatch.sample.library.LibraryDeepLinkModule;
import com.airbnb.deeplinkdispatch.sample.library.LibraryDeepLinkModuleLoader;

@DeepLinkHandler({ SampleModule.class, LibraryDeepLinkModule.class })
public class DeepLinkActivity extends Activity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    DeepLinkDelegate deepLinkDelegate = new DeepLinkDelegate(
        new SampleModuleLoader(), new LibraryDeepLinkModuleLoader());
    deepLinkDelegate.dispatchFrom(this);
    finish();
  }
}
