package com.airbnb.deeplinkdispatch.sample;

import android.app.Activity;
import android.os.Bundle;

import android.os.Debug;
import android.os.SystemClock;
import android.util.Log;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.airbnb.deeplinkdispatch.sample.library.LibraryDeepLinkModule;
import com.airbnb.deeplinkdispatch.sample.library.LibraryDeepLinkModuleRegistry;

@DeepLinkHandler({SampleModule.class, LibraryDeepLinkModule.class})
public class DeepLinkActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    Debug.startMethodTracing("deeplink.trace",90000000);
        LibraryDeepLinkModuleRegistry libraryDeepLinkModuleRegistry = new LibraryDeepLinkModuleRegistry();
        DeepLinkDelegate deepLinkDelegate = new DeepLinkDelegate(
                new SampleModuleRegistry(), libraryDeepLinkModuleRegistry);

        deepLinkDelegate.dispatchFrom(this);
    Debug.stopMethodTracing();

        finish();
    }
}
