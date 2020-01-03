package com.airbnb.deeplinkdispatch.sample;

import android.app.Activity;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.airbnb.deeplinkdispatch.sample.benchmarkable.BenchmarkDeepLinkModule;
import com.airbnb.deeplinkdispatch.sample.benchmarkable.BenchmarkDeepLinkModuleRegistry;
import com.airbnb.deeplinkdispatch.sample.library.LibraryDeepLinkModule;
import com.airbnb.deeplinkdispatch.sample.library.LibraryDeepLinkModuleRegistry;

@DeepLinkHandler({SampleModule.class, LibraryDeepLinkModule.class, BenchmarkDeepLinkModule.class})
public class DeepLinkActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//    Debug.startMethodTracing("deeplink.trace",90000000);
        DeepLinkDelegate deepLinkDelegate = new DeepLinkDelegate(
                new SampleModuleRegistry(), new LibraryDeepLinkModuleRegistry(), new BenchmarkDeepLinkModuleRegistry());

        deepLinkDelegate.dispatchFrom(this);
//    Debug.stopMethodTracing();

        finish();
    }
}
