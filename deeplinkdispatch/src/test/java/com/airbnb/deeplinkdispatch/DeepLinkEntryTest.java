package com.airbnb.deeplinkdispatch;

import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DeepLinkEntryTest {
  @Test public void testSingleParam() {
    DeepLinkEntry entry = new DeepLinkEntry(
        "foo/{bar}", DeepLinkEntry.Type.CLASS, String.class, null);

    Map<String, String> parameters = entry.getParameters("foo/myData");

    assertThat(parameters.get("bar")).isEqualTo("myData");
  }

  @Test public void testTwoParams() {
    DeepLinkEntry entry = new DeepLinkEntry(
        "test/{param1}/{param2}", DeepLinkEntry.Type.CLASS, String.class, null);

    Map<String, String> parameters = entry.getParameters("test/12345/alice");
    assertThat(parameters.get("param1")).isEqualTo("12345");
    assertThat(parameters.get("param2")).isEqualTo("alice");
  }

  @Test public void testParamWithSpecialCharacters() throws Exception {
    DeepLinkEntry entry = new DeepLinkEntry(
        "foo/{bar}", DeepLinkEntry.Type.CLASS, String.class, null);

    Map<String, String> parameters = entry.getParameters("foo/hyphens-and_underscores123");
    assertThat(parameters.get("bar")).isEqualTo("hyphens-and_underscores123");
  }
}
