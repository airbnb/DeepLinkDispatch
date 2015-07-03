package com.airbnb.deeplinkdispatch.sample;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.DeepLinkActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.robolectric.Shadows.shadowOf;

@Config(sdk = 21, manifest = "../sample/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
  @Test public void testIntent() {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("airbnb://example.com/deepLink"));
    DeepLinkActivity deepLinkActivity = Robolectric.buildActivity(DeepLinkActivity.class)
        .withIntent(intent).create().get();
    ShadowActivity shadowActivity = shadowOf(deepLinkActivity);

    Intent launchedIntent = shadowActivity.peekNextStartedActivityForResult().intent;
    assertThat(launchedIntent.getComponent(),
        equalTo(new ComponentName(deepLinkActivity, MainActivity.class)));

    assertThat(launchedIntent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false), equalTo(true));
  }

  @Test public void testMethodAnnotationWithParams() {
    Intent intent = new Intent(Intent.ACTION_VIEW,
        Uri.parse("airbnb://host/somePath/1234321"));
    DeepLinkActivity deepLinkActivity = Robolectric.buildActivity(DeepLinkActivity.class)
        .withIntent(intent).create().get();
    ShadowActivity shadowActivity = shadowOf(deepLinkActivity);

    Intent launchedIntent = shadowActivity.peekNextStartedActivityForResult().intent;
    assertThat(launchedIntent.getComponent(),
        equalTo(new ComponentName(deepLinkActivity, MainActivity.class)));

    assertThat(launchedIntent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false), equalTo(true));
    assertThat(launchedIntent.getStringExtra("arbitraryNumber"), equalTo("1234321"));
    assertThat(launchedIntent.getAction(), equalTo("deep_link_complex"));
  }

  @Test public void testQueryParams() {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("airbnb://classDeepLink?foo=bar"));
    DeepLinkActivity deepLinkActivity = Robolectric.buildActivity(DeepLinkActivity.class)
        .withIntent(intent).create().get();
    ShadowActivity shadowActivity = shadowOf(deepLinkActivity);

    Intent launchedIntent = shadowActivity.peekNextStartedActivityForResult().intent;
    assertThat(launchedIntent.getComponent(),
        equalTo(new ComponentName(deepLinkActivity, MainActivity.class)));

    assertThat(launchedIntent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false), equalTo(true));
    assertThat(launchedIntent.getStringExtra("foo"), equalTo("bar"));
  }
}