package com.airbnb.deeplinkdispatch.sample.typeconversion;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.airbnb.deeplinkdispatch.DeepLinkUri;
import com.airbnb.deeplinkdispatch.handler.TypeConverters;
import com.airbnb.deeplinkdispatch.sample.SampleModule;
import com.airbnb.deeplinkdispatch.sample.SampleModuleRegistry;
import com.airbnb.deeplinkdispatch.sample.benchmarkable.BenchmarkDeepLinkModule;
import com.airbnb.deeplinkdispatch.sample.benchmarkable.BenchmarkDeepLinkModuleRegistry;
import com.airbnb.deeplinkdispatch.sample.kaptlibrary.KaptLibraryDeepLinkModule;
import com.airbnb.deeplinkdispatch.sample.ksplibrary.KspLibraryDeepLinkModule;
import com.airbnb.deeplinkdispatch.sample.kaptlibrary.KaptLibraryDeepLinkModuleRegistry;
import com.airbnb.deeplinkdispatch.sample.ksplibrary.KspLibraryDeepLinkModuleRegistry;
import com.airbnb.deeplinkdispatch.sample.library.LibraryDeepLinkModule;
import com.airbnb.deeplinkdispatch.sample.library.LibraryDeepLinkModuleRegistry;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function3;

@DeepLinkHandler({SampleModule.class, LibraryDeepLinkModule.class, BenchmarkDeepLinkModule.class, KaptLibraryDeepLinkModule.class, KspLibraryDeepLinkModule.class})
public class TypeConversionErrorHandlerCustomTypeDeepLinkActivity extends Activity {

  private static final String TAG = "ErrorHandlerCustomType";

  List<String> stringList;

  private TypeConverters typeConverters;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    typeConverters = new TypeConverters();
    typeConverters.put(ComparableColorDrawable.class, value -> {
      switch (value.toLowerCase()) {
        case "red":
          return new ComparableColorDrawable(0xff0000ff);
        case "green":
          return new ComparableColorDrawable(0x00ff00ff);
        case "blue":
          return new ComparableColorDrawable(0x000ffff);
        default:
          return new ComparableColorDrawable(0xffffffff);
      }
    });
    try {
      // Not very elegant reflection way to get the right type to add to the mapper.
      typeConverters.put(TypeConversionErrorHandlerCustomTypeDeepLinkActivity.class.getDeclaredField("stringList").getGenericType(), value -> Arrays.asList(value.split(",")));
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
    Function3<DeepLinkUri, Type, ? super String, Integer> typeConversionErrorNullable = (Function3<DeepLinkUri, Type, String, Integer>) (uriTemplate, type, s) -> {
      Log.e(TAG, "Unable to convert " + s + " used in urlTemplate " + uriTemplate + " to a " + type + ". Returning null.");
      throw new NumberFormatException("For input string: \"" + s + "\"");
    };
    Function3<DeepLinkUri, Type, ? super String, Integer> typeConversionErrorNonNullable = (Function3<DeepLinkUri, Type, String, Integer>) (uriTemplate, type, s) -> {
      Log.e(TAG, "Unable to convert " + s + " used in urlTemplate " + uriTemplate + " to a " + type + ". Returning 0.");
      throw new NumberFormatException("For input string: \"" + s + "\"");
    };
    Function0<TypeConverters> typeConvertersLambda = () -> typeConverters;

    super.onCreate(savedInstanceState);
    Map configurablePlaceholdersMap = new HashMap();
    configurablePlaceholdersMap.put("configPathOne", "/somePathThree");
    configurablePlaceholdersMap.put("configurable-path-segment-one", "");
    configurablePlaceholdersMap.put("configurable-path-segment", "");
    configurablePlaceholdersMap.put("configurable-path-segment-two", "");
    configurablePlaceholdersMap.put("configPathOne", "/somePathOne");
    // KSP library modules with manifest-generation plugin require AssetManager to load binary match index
    DeepLinkDelegate deepLinkDelegate = new DeepLinkDelegate(
            new SampleModuleRegistry(),
            new LibraryDeepLinkModuleRegistry(),
            new BenchmarkDeepLinkModuleRegistry(getAssets()),
            new KaptLibraryDeepLinkModuleRegistry(),
            new KspLibraryDeepLinkModuleRegistry(getAssets()),
            configurablePlaceholdersMap,
            typeConvertersLambda,
            typeConversionErrorNullable,
            typeConversionErrorNonNullable);
    deepLinkDelegate.dispatchFrom(this);
    finish();
  }

}

