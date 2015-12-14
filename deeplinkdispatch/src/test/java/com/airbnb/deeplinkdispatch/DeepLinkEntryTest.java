package com.airbnb.deeplinkdispatch;

import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class DeepLinkEntryTest {
  @Test public void testSingleParam() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");

    Optional<DeepLinkMatch> matches = entry.matches("airbnb://foo/myData");
    assertThat(matches.isPresent()).isTrue();
    assertThat(matches.get().getEntry()).isEqualTo(entry);
    Map<String, String> parameters = matches.get().getParameters();
    assertThat(parameters.get("bar")).isEqualTo("myData");
  }

  @Test public void testTwoParams() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://test/{param1}/{param2}");

    Optional<DeepLinkMatch> matches = entry.matches("airbnb://test/12345/alice");
    assertThat(matches.isPresent()).isTrue();
    assertThat(matches.get().getEntry()).isEqualTo(entry);
    Map<String, String> parameters = matches.get().getParameters();
    assertThat(parameters.get("param1")).isEqualTo("12345");
    assertThat(parameters.get("param2")).isEqualTo("alice");
  }

  @Test public void testParamWithSpecialCharacters() throws Exception {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");

    Optional<DeepLinkMatch> matches = entry.matches("airbnb://foo/hyphens-and_underscores123");
    assertThat(matches.isPresent()).isTrue();
    assertThat(matches.get().getEntry()).isEqualTo(entry);
    Map<String, String> parameters = matches.get().getParameters();
    assertThat(parameters.get("bar")).isEqualTo("hyphens-and_underscores123");
  }

  @Test public void testParamWithTildeAndDollarSign() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://test/{param1}");

    Optional<DeepLinkMatch> matches = entry.matches("airbnb://test/tilde~dollar$ign");
    assertThat(matches.isPresent()).isTrue();
    assertThat(matches.get().getEntry()).isEqualTo(entry);
    Map<String, String> parameters = matches.get().getParameters();
    assertThat(parameters.get("param1")).isEqualTo("tilde~dollar$ign");
  }

  @Test public void testParamWithDotAndComma() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://test/{param1}");

    Optional<DeepLinkMatch> matches = entry.matches("airbnb://test/N1.55,22.11");
    assertThat(matches.isPresent()).isTrue();
    assertThat(matches.get().getEntry()).isEqualTo(entry);
    Map<String, String> parameters = matches.get().getParameters();
    assertThat(parameters.get("param1")).isEqualTo("N1.55,22.11");
  }

  @Test public void testParamWithSlash() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://test/{param1}");

    Optional<DeepLinkMatch> matches = entry.matches("airbnb://test/123/foo");
    assertThat(matches.isPresent()).isFalse();
  }

  @Test public void testNoMatchesFound() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");

    Optional<DeepLinkMatch> matches = entry.matches("airbnb://test.com");
    assertThat(matches.isPresent()).isFalse();
  }

  @Test public void testWithQueryParam() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://something");

    Optional<DeepLinkMatch> matches = entry.matches("airbnb://something?foo=bar");
    assertThat(matches.isPresent()).isTrue();
    assertThat(matches.get().getEntry()).isEqualTo(entry);
    Map<String, String> parameters = matches.get().getParameters();
    assertThat(parameters.isEmpty()).isTrue();
  }

  @Test public void noMatches() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://something.com/some-path");

    Optional<DeepLinkMatch> matches = entry.matches("airbnb://something.com/something-else");
    assertThat(matches.isPresent()).isFalse();
  }

  @Test public void pathParamAndQueryString() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");

    Optional<DeepLinkMatch> matches = entry.matches("airbnb://foo/baz?kit=kat");
    assertThat(matches.isPresent()).isTrue();
    assertThat(matches.get().getEntry()).isEqualTo(entry);
    Map<String, String> parameters = matches.get().getParameters();
    assertThat(parameters.get("bar")).isEqualTo("baz");
  }

  @Test public void urlWithSpaces() {
    DeepLinkEntry entry = deepLinkEntry("http://example.com/{query}");

    Optional<DeepLinkMatch> matches = entry.matches("http://example.com/search%20paris");
    Map<String, String> parameters = matches.get().getParameters();
    assertThat(parameters.get("query")).isEqualTo("search%20paris");
  }

  @Test public void noMatchesDifferentScheme() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://something");

    Optional<DeepLinkMatch> matches = entry.matches("http://something");
    assertThat(matches.isPresent()).isFalse();
  }

  @Test public void invalidUrl() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://something");
    Optional<DeepLinkMatch> matches = entry.matches("airbnb://");
    assertThat(matches.isPresent()).isFalse();
  }

  @Test public void pathWithQuotes() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://s/{query}");
    assertThat(entry.matches("airbnb://s/Sant'Eufemia-a-Maiella--Italia").isPresent()).isTrue();
  }

  @Test public void schemeWithNumbers() {
    DeepLinkEntry entry = deepLinkEntry("jackson5://example.com");
    Optional<DeepLinkMatch> matches = entry.matches("jackson5://example.com");
    assertThat(matches.isPresent()).isTrue();
  }

  @Test public void multiplePathParams() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://{foo}/{bar}");

    Optional<DeepLinkMatch> matches = entry.matches("airbnb://baz/qux");
    assertThat(matches.isPresent()).isTrue();
    assertThat(matches.get().getEntry()).isEqualTo(entry);
    Map<String, String> parameters = matches.get().getParameters();
    assertThat(parameters)
      .hasSize(2)
      .contains(entry("foo", "baz"), entry("bar", "qux"));
  }

  private static DeepLinkEntry deepLinkEntry(String uri) {
    return new DeepLinkEntry(uri, DeepLinkEntry.Type.CLASS, String.class, null);
  }
}
