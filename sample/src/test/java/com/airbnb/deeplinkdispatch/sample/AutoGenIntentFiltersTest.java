package com.airbnb.deeplinkdispatch.sample;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.robolectric.Shadows.shadowOf;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.DeepLinkDispatch;
import com.airbnb.deeplinkdispatch.sample.benchmarkable.BenchmarkDeepLinkModuleRegistry;
import com.airbnb.deeplinkdispatch.sample.kaptlibrary.KaptLibraryDeepLinkModuleRegistry;
import com.airbnb.deeplinkdispatch.sample.ksplibrary.KspLibraryDeepLinkModuleRegistry;
import com.airbnb.deeplinkdispatch.sample.library.LibraryActivity;
import com.airbnb.deeplinkdispatch.sample.library.LibraryDeepLinkModuleRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowApplication;

import java.util.HashMap;
import java.util.Map;

@Config(sdk = 21, manifest = "../sample/src/main/AndroidManifest.xml", shadows = {ShadowTaskStackBuilder.class})
@RunWith(RobolectricTestRunner.class)
public class AutoGenIntentFiltersTest {

  @Test
  public void testIntentViaInnerClassMethodResult() {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://example.com/deepLink_second/12345/sample_name"));
    DeepLinkActivity deepLinkActivity = Robolectric.buildActivity(DeepLinkActivity.class, intent)
      .create().get();
    ShadowActivity shadowActivity = shadowOf(deepLinkActivity);
    Intent launchedIntent = shadowActivity.peekNextStartedActivityForResult().intent;
    assertThat(launchedIntent.getComponent(),
      equalTo(new ComponentName(deepLinkActivity, SecondActivity.class)));

    assertThat(launchedIntent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false), equalTo(true));
    assertThat(launchedIntent.getAction(), equalTo(Intent.ACTION_VIEW));
    assertThat(launchedIntent.getStringExtra("name"), equalTo("sample_name"));
    assertThat(launchedIntent.getStringExtra("id"), equalTo("12345"));

  }



}
