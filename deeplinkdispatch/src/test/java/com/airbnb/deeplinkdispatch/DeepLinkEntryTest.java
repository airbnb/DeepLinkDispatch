package com.airbnb.deeplinkdispatch;

import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DeepLinkEntryTest {
  @Test public void testSingleParam() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");

    assertThat(entry.getParameters("airbnb://foo/myData").get("bar")).isEqualTo("myData");
  }

  @Test public void testTwoParams() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://test/{param1}/{param2}");

    Map<String, String> parameters = entry.getParameters("airbnb://test/12345/alice");
    assertThat(parameters.get("param1")).isEqualTo("12345");
    assertThat(parameters.get("param2")).isEqualTo("alice");
  }

  @Test public void testParamWithSpecialCharacters() throws Exception {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");

    Map<String, String> parameters = entry.getParameters("airbnb://foo/hyphens-and_underscores123");
    assertThat(parameters.get("bar")).isEqualTo("hyphens-and_underscores123");
  }

  @Test public void testParamWithTildeAndDollarSign() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://test/{param1}");

    Map<String, String> parameters = entry.getParameters("airbnb://test/tilde~dollar$ign");
    assertThat(parameters.get("param1")).isEqualTo("tilde~dollar$ign");
  }

  @Test public void testParamWithDotAndComma() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://test/{param1}");

    Map<String, String> parameters = entry.getParameters("airbnb://test/N1.55,22.11");
    assertThat(parameters.get("param1")).isEqualTo("N1.55,22.11");
  }

  @Test public void testNoMatchesFound() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");

    assertThat(entry.matches("airbnb://test.com")).isFalse();
    assertThat(entry.getParameters("airbnb://test.com").isEmpty()).isTrue();
  }

  @Test public void testWithQueryParam() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://something");

    assertThat(entry.matches("airbnb://something?foo=bar")).isTrue();
    assertThat(entry.getParameters("airbnb://something?foo=bar").isEmpty()).isTrue();
  }

  @Test public void noMatches() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://something.com/some-path");

    assertThat(entry.matches("airbnb://something.com/something-else")).isFalse();
  }

  @Test public void pathParamAndQueryString() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");

    Map<String, String> parameters = entry.getParameters("airbnb://foo/baz?kit=kat");
    assertThat(parameters.get("bar")).isEqualTo("baz");
  }

  @Test public void urlWithSpaces() {
    DeepLinkEntry entry = deepLinkEntry("http://example.com/{query}");

    Map<String, String> parameters = entry.getParameters("http://example.com/search%20paris");
    assertThat(parameters.get("query")).isEqualTo("search%20paris");
  }

  @Test public void noMatchesDifferentScheme() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://something");

    assertThat(entry.matches("http://something")).isFalse();
    assertThat(entry.getParameters("http://something").isEmpty()).isTrue();
  }

  private static DeepLinkEntry deepLinkEntry(String uri) {
    return new DeepLinkEntry(uri, DeepLinkEntry.Type.CLASS, String.class, null);
  }
}
