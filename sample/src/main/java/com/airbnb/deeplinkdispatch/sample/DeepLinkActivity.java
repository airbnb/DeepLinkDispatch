package com.airbnb.deeplinkdispatch.sample;

import android.app.Activity;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.airbnb.deeplinkdispatch.sample.benchmarkable.BenchmarkDeepLinkModule;
import com.airbnb.deeplinkdispatch.sample.benchmarkable.BenchmarkDeepLinkModuleRegistry;
import com.airbnb.deeplinkdispatch.sample.kaptlibrary.KaptLibraryDeepLinkModule;
import com.airbnb.deeplinkdispatch.sample.kaptlibrary.KaptLibraryDeepLinkModuleRegistry;
import com.airbnb.deeplinkdispatch.sample.ksplibrary.KspLibraryDeepLinkModule;
import com.airbnb.deeplinkdispatch.sample.ksplibrary.KspLibraryDeepLinkModuleRegistry;
import com.airbnb.deeplinkdispatch.sample.library.LibraryDeepLinkModule;
import com.airbnb.deeplinkdispatch.sample.library.LibraryDeepLinkModuleRegistry;

import java.util.HashMap;
import java.util.Map;

@DeepLinkHandler({SampleModule.class, LibraryDeepLinkModule.class, BenchmarkDeepLinkModule.class, KaptLibraryDeepLinkModule.class, KspLibraryDeepLinkModule.class})
public class DeepLinkActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //    Debug.startMethodTracing("deeplink.trace",90000000);
    Map configurablePlaceholdersMap = new HashMap();
    configurablePlaceholdersMap.put("configPathOne", "somePathThree");
    configurablePlaceholdersMap.put("configurable-path-segment-one", "");
    configurablePlaceholdersMap.put("configurable-path-segment", "");
    configurablePlaceholdersMap.put("configurable-path-segment-two", "");
    configurablePlaceholdersMap.put("configPathOne", "somePathOne");
    DeepLinkDelegate deepLinkDelegate = new DeepLinkDelegate(
            new SampleModuleRegistry(),
            new LibraryDeepLinkModuleRegistry(),
            new BenchmarkDeepLinkModuleRegistry(),
            new KaptLibraryDeepLinkModuleRegistry(),
            new KspLibraryDeepLinkModuleRegistry(),
            configurablePlaceholdersMap
    );
    deepLinkDelegate.dispatchFrom(this);
    //    Debug.stopMethodTracing();
    finish();
  }
}
