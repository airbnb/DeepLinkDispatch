package com.airbnb.deeplinkdispatch;

import android.app.Activity;
import android.content.Intent;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseDeepLinkDelegateTest {

  @Test
  public void testDispatchNullActivity() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");
    TestDeepLinkDelegate testDelegate = getTestDelegate(Arrays.asList(new DeepLinkEntry[]{entry}));
    String message = null;
    try {
      testDelegate.dispatchFrom(null);
    } catch (NullPointerException e) {
      message = e.getMessage();
    }

    assertThat(message).isEqualTo("activity == null");
  }

  @Test
  public void testDispatchNullActivityNullIntent() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");
    TestDeepLinkDelegate testDelegate = getTestDelegate(Arrays.asList(new DeepLinkEntry[]{entry}));
    String message = null;
    try {
      testDelegate.dispatchFrom(null, null);
    } catch (NullPointerException e) {
      message = e.getMessage();
    }

    assertThat(message).isEqualTo("activity == null");
  }

  @Test
  public void testDispatchNullIntent() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");
    TestDeepLinkDelegate testDelegate = getTestDelegate(Arrays.asList(new DeepLinkEntry[]{entry}));

    Activity activity = mock(Activity.class);
    when(activity.getIntent())
      .thenReturn(null);

    String message = null;
    try {
      testDelegate.dispatchFrom(activity);
    } catch (NullPointerException e) {
      message = e.getMessage();
    }

    assertThat(message).isEqualTo("sourceIntent == null");
  }

  @Test
  public void testDispatchNonNullActivityNullIntent() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");
    TestDeepLinkDelegate testDelegate = getTestDelegate(Arrays.asList(new DeepLinkEntry[]{entry}));

    Activity activity = mock(Activity.class);
    when(activity.getIntent())
      .thenReturn(null);

    String message = null;
    try {
      testDelegate.dispatchFrom(activity, null);
    } catch (NullPointerException e) {
      message = e.getMessage();
    }

    assertThat(message).isEqualTo("sourceIntent == null");
  }

  @Test
  public void testCreateResultAllNull() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");
    TestDeepLinkDelegate testDelegate = getTestDelegate(Arrays.asList(new DeepLinkEntry[]{entry}));

    String message = null;
    try {
      testDelegate.createResult(null, null, null);
    } catch (NullPointerException e) {
      message = e.getMessage();
    }

    assertThat(message).isEqualTo("activity == null");
  }

  @Test
  public void testCreateResultNullIntent() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");
    TestDeepLinkDelegate testDelegate = getTestDelegate(Arrays.asList(new DeepLinkEntry[]{entry}));

    Activity activity = mock(Activity.class);
    when(activity.getIntent())
      .thenReturn(null);

    String message = null;
    try {
      testDelegate.createResult(activity, null, null);
    } catch (NullPointerException e) {
      message = e.getMessage();
    }

    assertThat(message).isEqualTo("sourceIntent == null");
  }

  @Test
  public void testCreateResultAllNullData() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");
    TestDeepLinkDelegate testDelegate = getTestDelegate(Arrays.asList(new DeepLinkEntry[]{entry}));

    Intent intent = mock(Intent.class);
    when(intent.getData())
      .thenReturn(null);
    Activity activity = mock(Activity.class);
    when(activity.getIntent())
      .thenReturn(intent);

    DeepLinkResult result = testDelegate.createResult(activity, intent, null);

    assertThat(result).isEqualTo(new DeepLinkResult(
      false, null, "No Uri in given activity's intent.", null, null, null));
  }

  private static DeepLinkEntry deepLinkEntry(String uri) {
    return new DeepLinkEntry(uri, DeepLinkEntry.Type.CLASS, String.class, null);
  }

  /**
   * Helper method to get a class extending {@link BaseRegistry} acting as the delegate
   * for the
   *
   * @param deepLinkEntries
   * @return
   */
  private static TestDeepLinkRegistry getTestRegistry(List<DeepLinkEntry> deepLinkEntries) {
    return new TestDeepLinkRegistry(deepLinkEntries);
  }

  private static class TestDeepLinkRegistry extends BaseRegistry {
    public TestDeepLinkRegistry(List<DeepLinkEntry> registry) {
      super(registry, getSearchIndex(registry), new String[]{});
    }

    @NotNull
    private static byte[] getSearchIndex(List<DeepLinkEntry> registry) {
      Root trieRoot = new Root();
      for (int i = 0; i < registry.size(); i++) {
        trieRoot.addToTrie(i, DeepLinkUri.parse(registry.get(i).getUriTemplate()), registry.get(i).getActivityClass().toString(), registry.get(i).getMethod());
      }
      return trieRoot.toUByteArray();
    }
  }

  private static TestDeepLinkDelegate getTestDelegate(List<DeepLinkEntry> entries) {
    return new TestDeepLinkDelegate(Arrays.asList(getTestRegistry(entries)));
  }

  private static class TestDeepLinkDelegate extends BaseDeepLinkDelegate {

    public TestDeepLinkDelegate(List<? extends BaseRegistry> registries) {
      super(registries);
    }
  }

}
