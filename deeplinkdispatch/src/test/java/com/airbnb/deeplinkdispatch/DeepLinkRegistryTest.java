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
    registry.registerDeepLink("http://test/{param1}", DeepLinkEntry.Type.CLASS, String.class, null);

    assertThat(registry.parseUri("http://test/12345")).isNotNull();
  }

  @Test public void testTwoParameters() {
    registry.registerDeepLink(
        "http://test/{param1}/{param2}", DeepLinkEntry.Type.CLASS, String.class, null);

    assertThat(registry.parseUri("http://test/12345/alice")).isNotNull();
  }

  @Test public void testEntryNotFound() {
    registry.registerDeepLink(
        "http://test/{param1}/{param2}", DeepLinkEntry.Type.CLASS, String.class, null);

    assertThat(registry.parseUri("http://blah/232")).isNull();
  }
}
