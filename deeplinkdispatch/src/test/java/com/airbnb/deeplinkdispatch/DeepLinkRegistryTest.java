package com.airbnb.deeplinkdispatch;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeepLinkRegistryTest {

  private DeepLinkRegistry registry;

  @Before public void setUp() {
    registry = new DeepLinkRegistry(null);
  }

  @Test public void testSingleParameter() {
    registry.registerDeepLink("test/{param1}", DeepLinkEntry.Type.CLASS, String.class, null);

    DeepLinkEntry entry = registry.parseUri("test/12345");

    assertThat(entry).isNotNull();
  }

  @Test public void testTwoParameters() {
    registry.registerDeepLink(
        "test/{param1}/{param2}", DeepLinkEntry.Type.CLASS, String.class, null);

    DeepLinkEntry entry = registry.parseUri("test/12345/alice");

    assertThat(entry).isNotNull();
  }

  @Test public void testEntryNotFound() {
    registry.registerDeepLink(
        "test/{param1}/{param2}", DeepLinkEntry.Type.CLASS, String.class, null);

    DeepLinkEntry entry = registry.parseUri("blah/232");

    assertThat(entry).isNull();
  }
}
