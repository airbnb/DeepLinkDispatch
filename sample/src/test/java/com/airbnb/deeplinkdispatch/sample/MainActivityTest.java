package com.airbnb.deeplinkdispatch.sample;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.sample.benchmarkable.BenchmarkDeepLinkModuleRegistry;
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

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.robolectric.Shadows.shadowOf;

@Config(sdk = 21, manifest = "../sample/src/main/AndroidManifest.xml", shadows = {ShadowTaskStackBuilder.class})
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
  @Test public void testIntent() {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("dld://host/somePath/1234321"))
        .putExtra("TEST_EXTRA", "FOO");
    DeepLinkActivity deepLinkActivity = Robolectric.buildActivity(DeepLinkActivity.class, intent)
        .create().get();
    ShadowActivity shadowActivity = shadowOf(deepLinkActivity);
    Intent launchedIntent = shadowActivity.peekNextStartedActivityForResult().intent;
    assertThat(launchedIntent.getComponent(),
        equalTo(new ComponentName(deepLinkActivity, MainActivity.class)));

    assertThat(launchedIntent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false), equalTo(true));
    assertThat(launchedIntent.getStringExtra("arbitraryNumber"), equalTo("1234321"));
    assertThat(launchedIntent.getStringExtra("TEST_EXTRA"), equalTo("FOO"));
    assertThat(launchedIntent.getAction(), equalTo("deep_link_complex"));
    assertThat(launchedIntent.<Uri>getParcelableExtra(DeepLink.REFERRER_URI).toString(),
        equalTo("dld://host/somePath/1234321"));
    assertThat(launchedIntent.getData(), equalTo(Uri.parse("dld://host/somePath/1234321")));
    assertThat(launchedIntent.getStringExtra(DeepLink.URI),
        equalTo("dld://host/somePath/1234321"));
  }

  @Test public void testPartialSegmentPlaceholderStart() {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://example.com/test123bar"));
    DeepLinkActivity deepLinkActivity = Robolectric.buildActivity(DeepLinkActivity.class, intent)
        .create().get();
    ShadowActivity shadowActivity = shadowOf(deepLinkActivity);
    Intent launchedIntent = shadowActivity.peekNextStartedActivityForResult().intent;
    assertThat(launchedIntent.getComponent(),
        equalTo(new ComponentName(deepLinkActivity, MainActivity.class)));

    assertThat(launchedIntent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false), equalTo(true));
    assertThat(launchedIntent.getStringExtra("arg_start"), equalTo("test123"));
    assertThat(launchedIntent.getAction(), equalTo(Intent.ACTION_VIEW));
    assertThat(launchedIntent.getData(), equalTo(Uri.parse("http://example.com/test123bar")));
    assertThat(launchedIntent.getStringExtra(DeepLink.URI),
        equalTo("http://example.com/test123bar"));
  }

  @Test public void testPartialSegmentPlaceholdeEnd() {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://example.com/footest123"));
    DeepLinkActivity deepLinkActivity = Robolectric.buildActivity(DeepLinkActivity.class, intent)
        .create().get();
    ShadowActivity shadowActivity = shadowOf(deepLinkActivity);
    Intent launchedIntent = shadowActivity.peekNextStartedActivityForResult().intent;
    assertThat(launchedIntent.getComponent(),
        equalTo(new ComponentName(deepLinkActivity, MainActivity.class)));

    assertThat(launchedIntent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false), equalTo(true));
    assertThat(launchedIntent.getStringExtra("arg_end"), equalTo("test123"));
    assertThat(launchedIntent.getAction(), equalTo(Intent.ACTION_VIEW));
    assertThat(launchedIntent.getData(), equalTo(Uri.parse("http://example.com/footest123")));
    assertThat(launchedIntent.getStringExtra(DeepLink.URI),
        equalTo("http://example.com/footest123"));
  }

  @Test public void testQueryParams() {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("dld://classDeepLink?foo=bar"));
    DeepLinkActivity deepLinkActivity = Robolectric.buildActivity(DeepLinkActivity.class, intent)
        .create().get();
    ShadowActivity shadowActivity = shadowOf(deepLinkActivity);

    Intent launchedIntent = shadowActivity.peekNextStartedActivityForResult().intent;
    assertThat(launchedIntent.getComponent(),
        equalTo(new ComponentName(deepLinkActivity, MainActivity.class)));

    assertThat(launchedIntent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false), equalTo(true));
    assertThat(launchedIntent.getStringExtra("foo"), equalTo("bar"));
    assertThat(launchedIntent.getAction(), equalTo(Intent.ACTION_VIEW));
    assertThat(launchedIntent.getStringExtra(DeepLink.URI),
        equalTo("dld://classDeepLink?foo=bar"));
  }

  @Test public void testQueryParamsWithBracket() {
    Intent intent =
        new Intent(Intent.ACTION_VIEW, Uri.parse("dld://classDeepLink?foo[max]=123"));
    DeepLinkActivity deepLinkActivity = Robolectric.buildActivity(DeepLinkActivity.class, intent)
        .create().get();
    ShadowActivity shadowActivity = shadowOf(deepLinkActivity);

    Intent launchedIntent = shadowActivity.peekNextStartedActivityForResult().intent;
    assertThat(launchedIntent.getComponent(),
        equalTo(new ComponentName(deepLinkActivity, MainActivity.class)));

    assertThat(launchedIntent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false), equalTo(true));
    assertThat(launchedIntent.getStringExtra("foo[max]"), equalTo("123"));
    assertThat(launchedIntent.getStringExtra(DeepLink.URI),
        equalTo("dld://classDeepLink?foo[max]=123"));
  }

  @Test public void testHttpScheme() {
    Intent intent = new Intent(Intent.ACTION_VIEW,
        Uri.parse("http://example.com/fooball?baz=something"));
    DeepLinkActivity deepLinkActivity = Robolectric.buildActivity(DeepLinkActivity.class, intent)
        .create().get();
    ShadowActivity shadowActivity = shadowOf(deepLinkActivity);

    Intent launchedIntent = shadowActivity.peekNextStartedActivityForResult().intent;
    assertThat(launchedIntent.getComponent(),
        equalTo(new ComponentName(deepLinkActivity, MainActivity.class)));

    assertThat(launchedIntent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false), equalTo(true));
    assertThat(launchedIntent.getStringExtra("baz"), equalTo("something"));
    assertThat(launchedIntent.getStringExtra("arg_end"), equalTo("ball"));
    assertThat(launchedIntent.getStringExtra(DeepLink.URI),
        equalTo("http://example.com/fooball?baz=something"));
  }


  @Test public void testTaskStackBuilderIntents() {
    Intent intent = new Intent(Intent.ACTION_VIEW,
        Uri.parse("http://example.com/deepLink/testid/testname/testplace"));
    DeepLinkActivity deepLinkActivity = Robolectric.buildActivity(DeepLinkActivity.class, intent)
        .create().get();
    ShadowApplication shadowApplication = shadowOf(RuntimeEnvironment.application);
    Intent launchedIntent = shadowApplication.getNextStartedActivity();
    assertNotNull(launchedIntent);
    assertThat(launchedIntent.getComponent(),
        equalTo(new ComponentName(deepLinkActivity, SecondActivity.class)));

    assertThat(launchedIntent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false), equalTo(true));
    assertThat(launchedIntent.getStringExtra("id"), equalTo("testid"));
    assertThat(launchedIntent.getStringExtra("name"), equalTo("testname"));
    assertThat(launchedIntent.getStringExtra("place"), equalTo("testplace"));
    assertThat(launchedIntent.getStringExtra(DeepLink.URI),
        equalTo("http://example.com/deepLink/testid/testname/testplace"));

    Intent parentIntent = shadowApplication.getNextStartedActivity();
    assertNotNull(parentIntent);
    assertThat(parentIntent.getComponent(),
        equalTo(new ComponentName(deepLinkActivity, MainActivity.class)));
    Intent nextNullIntent = shadowApplication.getNextStartedActivity();
    assertNull(nextNullIntent);
  }

  @Test
  public void testSupportsUri() throws Exception {
    DeepLinkDelegate deepLinkDelegate = new DeepLinkDelegate(new SampleModuleRegistry(),
        new LibraryDeepLinkModuleRegistry(), new BenchmarkDeepLinkModuleRegistry());
    assertThat(deepLinkDelegate.supportsUri("dld://classDeepLink"), equalTo(true));
    assertThat(deepLinkDelegate.supportsUri("some://weirdNonExistentUri"), equalTo(false));
  }

  @Test
  public void testPlaceholderSubstitution() {
    DeepLinkDelegate deepLinkDelegate = new DeepLinkDelegate(new SampleModuleRegistry(),
      new LibraryDeepLinkModuleRegistry(), new BenchmarkDeepLinkModuleRegistry(), "obamaOs");
    assertThat(deepLinkDelegate.supportsUri("https://www.example.com/obamaOs/bar"), equalTo(true));
  }
}
