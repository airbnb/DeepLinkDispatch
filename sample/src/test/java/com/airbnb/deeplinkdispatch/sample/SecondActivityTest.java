package com.airbnb.deeplinkdispatch.sample;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;

import com.airbnb.deeplinkdispatch.DeepLink;

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
public class SecondActivityTest {
  @Test public void testIntent() {
    Intent intent = new Intent(Intent.ACTION_VIEW,
        Uri.parse("http://example.com/deepLink/123/myname"));
    DeepLinkActivity deepLinkActivity = Robolectric.buildActivity(DeepLinkActivity.class, intent)
        .create().get();
    ShadowActivity shadowActivity = shadowOf(deepLinkActivity);

    Intent launchedIntent = shadowActivity.peekNextStartedActivityForResult().intent;
    assertThat(launchedIntent.getComponent(),
        equalTo(new ComponentName(deepLinkActivity, SecondActivity.class)));

    assertThat(launchedIntent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false), equalTo(true));
    assertThat(launchedIntent.getStringExtra(DeepLink.URI),
        equalTo("http://example.com/deepLink/123/myname"));
  }
}
