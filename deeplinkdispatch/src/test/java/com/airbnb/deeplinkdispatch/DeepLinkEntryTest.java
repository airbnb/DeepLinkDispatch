package com.airbnb.deeplinkdispatch;

import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

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

  @Test public void testParamForAtSign() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://test/{param1}");

    Map<String, String> parameters = entry.getParameters("airbnb://test/somename@gmail.com");
    assertThat(parameters.get("param1")).isEqualTo("somename@gmail.com");
  }

  @Test public void testParamForColon() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://test/{param1}");

    Map<String, String> parameters = entry.getParameters("airbnb://test/a1:b2:c3");
    assertThat(parameters.get("param1")).isEqualTo("a1:b2:c3");
  }

  @Test public void testParamWithSlash() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://test/{param1}");

    Map<String, String> parameters = entry.getParameters("airbnb://test/123/foo");
    assertThat(parameters).isEmpty();
  }

  @Test public void testNoMatchesFound() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");

    assertThat(entry.matches("airbnb://test.com")).isFalse();
    assertThat(entry.getParameters("airbnb://test.com").isEmpty()).isTrue();
  }

  @Test
  public void testEmptyParametersDontMatch() throws Exception {
    DeepLinkEntry entry = deepLinkEntry("dld://foo/{id}/bar");

    assertThat(entry.matches("dld://foo//bar")).isFalse();
  }

  @Test
  public void testEmptyPathPresentParams() throws Exception {
    DeepLinkEntry entry = deepLinkEntry("dld://foo/{id}");
    DeepLinkEntry entryNoParam = deepLinkEntry("dld://foo");

    assertThat(entry.matches("dld://foo")).isFalse();
    assertThat(entryNoParam.matches("dld://foo")).isTrue();
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

  @Test public void invalidUrl() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://something");
    assertThat(entry.matches("airbnb://")).isFalse();
  }

  @Test public void pathWithQuotes() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://s/{query}");
    assertThat(entry.matches("airbnb://s/Sant'Eufemia-a-Maiella--Italia")).isTrue();
  }

  @Test public void schemeWithNumbers() {
    DeepLinkEntry entry = deepLinkEntry("jackson5://example.com");
    assertThat(entry.matches("jackson5://example.com")).isTrue();
  }

  @Test public void multiplePathParams() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://{foo}/{bar}");

    Map<String, String> parameters = entry.getParameters("airbnb://baz/qux");
    assertThat(parameters)
      .hasSize(2)
      .contains(entry("foo", "baz"), entry("bar", "qux"));
  }

  @Test public void templateWithoutParameters() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://something");
    assertThat("airbnb://something".equals(entry.getUriTemplate())).isTrue();
  }

  @Test public void templateWithParameters() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://test/{param1}/{param2}");
    assertThat("airbnb://test/{param1}/{param2}".equals(entry.getUriTemplate())).isTrue();
  }

  private static DeepLinkEntry deepLinkEntry(String uri) {
    return new DeepLinkEntry(uri, DeepLinkEntry.Type.CLASS, String.class, null);
  }
}
