package com.airbnb.deeplinkdispatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseDeepLinkDelegateTest {

  @Test
  public void testDispatchNullActivity() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");
    TestDeepLinkDelegate testDelegate = getOneRegistryTestDelegate(Arrays.asList(new DeepLinkEntry[]{entry}), null);
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
    TestDeepLinkDelegate testDelegate = getOneRegistryTestDelegate(Arrays.asList(new DeepLinkEntry[]{entry}), null);
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
    TestDeepLinkDelegate testDelegate = getOneRegistryTestDelegate(Arrays.asList(new DeepLinkEntry[]{entry}), null);

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
    TestDeepLinkDelegate testDelegate = getOneRegistryTestDelegate(Arrays.asList(new DeepLinkEntry[]{entry}), null);

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
    TestDeepLinkDelegate testDelegate = getOneRegistryTestDelegate(Arrays.asList(new DeepLinkEntry[]{entry}), null);

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
    TestDeepLinkDelegate testDelegate = getOneRegistryTestDelegate(Arrays.asList(new DeepLinkEntry[]{entry}), null);

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
    TestDeepLinkDelegate testDelegate = getOneRegistryTestDelegate(Arrays.asList(new DeepLinkEntry[]{entry}), null);

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

  @Test
  public void testErrorHanderWithDuplicateMartch() {
    String deeplinkUrl = "airbnb://foo/{bar}";
    DeepLinkEntry entry = deepLinkEntry(deeplinkUrl);

    Uri uri = mock(Uri.class);
    when(uri.toString())
      .thenReturn(deeplinkUrl);
    Intent intent = mock(Intent.class);
    when(intent.getData())
      .thenReturn(uri);
    Context appContext = mock(Context.class);
    Activity activity = mock(Activity.class);
    when(activity.getIntent())
      .thenReturn(intent);
    when(activity.getApplicationContext())
      .thenReturn(appContext);

    TestErrorHandler errorHandler = new TestErrorHandler();
    TestDeepLinkDelegate testDelegate = getTwoRegistriesTestDelegate(Arrays.asList(new DeepLinkEntry[]{entry}), Arrays.asList(new DeepLinkEntry[]{entry}), errorHandler);
    DeepLinkResult result = testDelegate.dispatchFrom(activity, intent);

    assertThat(errorHandler.duplicatedMatchCalled()).isTrue();
    assertThat(errorHandler.getDuplicatedMatches()).isNotNull();
    assertThat(errorHandler.getDuplicatedMatches().size()).isEqualTo(2);
    assertThat(errorHandler.getDuplicatedMatches().get(0)).isEqualTo(entry);
    assertThat(errorHandler.getDuplicatedMatches().get(1)).isEqualTo(entry);

    assertThat(result.getDeepLinkEntry().equals(entry));
  }

  @Test
  public void testErrorHandlerNotGettingCalled() {
    String deeplinkUrl1 = "airbnb://foo/{bar}";
    String deeplinkUrl2 = "airbnb://bar/{foo}";
    DeepLinkEntry entry1 = deepLinkEntry(deeplinkUrl1);
    DeepLinkEntry entry2 = deepLinkEntry(deeplinkUrl2);

    Uri uri = mock(Uri.class);
    when(uri.toString())
      .thenReturn(deeplinkUrl2);
    Intent intent = mock(Intent.class);
    when(intent.getData())
      .thenReturn(uri);
    Context appContext = mock(Context.class);
    Activity activity = mock(Activity.class);
    when(activity.getIntent())
      .thenReturn(intent);
    when(activity.getApplicationContext())
      .thenReturn(appContext);

    TestErrorHandler errorHandler = new TestErrorHandler();
    TestDeepLinkDelegate testDelegate = getTwoRegistriesTestDelegate(Arrays.asList(new DeepLinkEntry[]{entry1}), Arrays.asList(new DeepLinkEntry[]{entry2}), errorHandler);
    DeepLinkResult result = testDelegate.dispatchFrom(activity, intent);

    assertThat(errorHandler.duplicatedMatchCalled()).isFalse();
    assertThat(result.getDeepLinkEntry().equals(entry2));
  }

  class TestErrorHandler implements ErrorHandler {

    List<DeepLinkEntry> duplicatedMatches = null;
    boolean duplicateMatchCalled = false;

    public List<DeepLinkEntry> getDuplicatedMatches() {
      return duplicatedMatches;
    }

    public boolean duplicatedMatchCalled() {
      return duplicateMatchCalled;
    }

    @Override
    public void duplicateMatch(@NotNull List<DeepLinkEntry> duplicatedMatches) {
      this.duplicatedMatches = duplicatedMatches;
      duplicateMatchCalled = true;
    }
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
    private static byte[] getSearchIndex(List<DeepLinkEntry> deepLinkEntries) {
      Root trieRoot = new Root();
      for (int i = 0; i < deepLinkEntries.size(); i++) {
        trieRoot.addToTrie(i, DeepLinkUri.parse(deepLinkEntries.get(i).getUriTemplate()), deepLinkEntries.get(i).getActivityClass().toString(), deepLinkEntries.get(i).getMethod());
      }
      return trieRoot.toUByteArray();
    }
  }

  private static TestDeepLinkDelegate getTwoRegistriesTestDelegate(List<DeepLinkEntry> entriesFirstRegistry, List<DeepLinkEntry> entriesSecondRegistry, ErrorHandler errorHandler) {
    return new TestDeepLinkDelegate(Arrays.asList(getTestRegistry(entriesFirstRegistry), getTestRegistry(entriesSecondRegistry)), errorHandler);
  }

  private static TestDeepLinkDelegate getOneRegistryTestDelegate(List<DeepLinkEntry> entries, ErrorHandler errorHandler) {
    return new TestDeepLinkDelegate(Arrays.asList(getTestRegistry(entries)), errorHandler);
  }

  private static class TestDeepLinkDelegate extends BaseDeepLinkDelegate {

    public TestDeepLinkDelegate(List<? extends BaseRegistry> registries, ErrorHandler errorHandler) {
      super(registries, errorHandler);
    }
  }
}
