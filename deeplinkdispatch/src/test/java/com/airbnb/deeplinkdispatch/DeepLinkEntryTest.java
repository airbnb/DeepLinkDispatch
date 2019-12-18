package com.airbnb.deeplinkdispatch;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class DeepLinkEntryTest {
  @Test public void testSingleParam() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");
    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    DeepLinkEntry match = testLoader.idxMatch(DeepLinkUri.parse("airbnb://foo/myData"));
    assertThat(match.getParameters(DeepLinkUri.parse("airbnb://foo/myData")).get("bar")).isEqualTo("myData");
  }

  @Test public void testTwoParams() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://test/{param1}/{param2}");
    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    DeepLinkEntry match = testLoader.idxMatch(DeepLinkUri.parse("airbnb://test/12345/alice"));

    Map<String, String> parameters = match.getParameters(DeepLinkUri.parse("airbnb://test/12345/alice"));
    assertThat(parameters.get("param1")).isEqualTo("12345");
    assertThat(parameters.get("param2")).isEqualTo("alice");
  }

  @Test public void testParamWithSpecialCharacters() throws Exception {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");
    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    DeepLinkEntry match = testLoader.idxMatch(DeepLinkUri.parse("airbnb://foo/hyphens-and_underscores123"));

    Map<String, String> parameters = match.getParameters(DeepLinkUri.parse("airbnb://foo/hyphens-and_underscores123"));
    assertThat(parameters.get("bar")).isEqualTo("hyphens-and_underscores123");
  }

  @Test public void testParamWithTildeAndDollarSign() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://test/{param1}");
    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    DeepLinkEntry match = testLoader.idxMatch(DeepLinkUri.parse("airbnb://test/tilde~dollar$ign"));

    Map<String, String> parameters = match.getParameters(DeepLinkUri.parse("airbnb://test/tilde~dollar$ign"));
    assertThat(parameters.get("param1")).isEqualTo("tilde~dollar$ign");
  }

  @Test public void testParamWithDotAndComma() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://test/{param1}");
    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    DeepLinkEntry match = testLoader.idxMatch(DeepLinkUri.parse("airbnb://test/N1.55,22.11"));

    Map<String, String> parameters = match.getParameters(DeepLinkUri.parse("airbnb://test/N1.55,22.11"));
    assertThat(parameters.get("param1")).isEqualTo("N1.55,22.11");
  }

  @Test public void testParamForAtSign() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://test/{param1}");
    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    DeepLinkEntry match = testLoader.idxMatch(DeepLinkUri.parse("airbnb://test/somename@gmail.com"));

    Map<String, String> parameters = match.getParameters(DeepLinkUri.parse("airbnb://test/somename@gmail.com"));
    assertThat(parameters.get("param1")).isEqualTo("somename@gmail.com");
  }

  @Test public void testParamForColon() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://test/{param1}");
    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    DeepLinkEntry match = testLoader.idxMatch(DeepLinkUri.parse("airbnb://test/a1:b2:c3"));

    Map<String, String> parameters = match.getParameters(DeepLinkUri.parse("airbnb://test/a1:b2:c3"));
    assertThat(parameters.get("param1")).isEqualTo("a1:b2:c3");
  }

  @Test public void testParamWithSlash() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://test/{param1}/foo");
    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    DeepLinkEntry match = testLoader.idxMatch(DeepLinkUri.parse("airbnb://test/123/foo"));

    Map<String, String> parameters = match.getParameters(DeepLinkUri.parse("airbnb://test/123/foo"));
    assertThat(parameters.get("param1")).isEqualTo("123");
  }

  @Test public void testNoMatchesFound() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");
    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    assertThat(testLoader.idxMatch(DeepLinkUri.parse("airbnb://test.com"))).isNull();
    assertThat(entry.getParameters(DeepLinkUri.parse("airbnb://test.com")).isEmpty()).isTrue();
  }

  @Test
  public void testEmptyParametersDontMatch() throws Exception {
    DeepLinkEntry entry = deepLinkEntry("dld://foo/{id}/bar");

    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    DeepLinkEntry match = testLoader.idxMatch(DeepLinkUri.parse("dld://foo//bar"));

    assertThat(match).isNull();
  }

  @Test
  public void testEmptyParametersNameDontMatch() throws Exception {
    DeepLinkEntry entry = deepLinkEntry("dld://foo/{}/bar");

    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    DeepLinkEntry match = testLoader.idxMatch(DeepLinkUri.parse("dld://foo/bla/bar"));

    assertThat(match).isNull();
  }

  @Test
  public void testEmptyPathPresentParams() throws Exception {
    DeepLinkEntry entry = deepLinkEntry("dld://foo/{id}");
    DeepLinkEntry entryNoParam = deepLinkEntry("dld://foo");

    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    DeepLinkEntry match = testLoader.idxMatch(DeepLinkUri.parse("dld://foo"));

    TestDeepLinkLoader testLoaderNoParm = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entryNoParam}));
    DeepLinkEntry matchNoParam = testLoaderNoParm.idxMatch(DeepLinkUri.parse("dld://foo"));

    assertThat(match).isNull();
    assertThat(matchNoParam).isEqualTo(entryNoParam);
  }

  @Test public void testWithQueryParam() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://something");

//    assertThat(entry.matches("airbnb://something?foo=bar")).isTrue();
    assertThat(entry.getParameters(DeepLinkUri.parse("airbnb://something?foo=bar")).isEmpty()).isTrue();
  }

  @Test public void noMatches() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://something.com/some-path");

    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    DeepLinkEntry match = testLoader.idxMatch(DeepLinkUri.parse("airbnb://something.com/something-else"));

    assertThat(match).isNull();
  }

  @Test public void pathParamAndQueryString() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://foo/{bar}");

    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    DeepLinkEntry match = testLoader.idxMatch(DeepLinkUri.parse("airbnb://foo/baz?kit=kat"));

    Map<String, String> parameters = match.getParameters(DeepLinkUri.parse("airbnb://foo/baz?kit=kat"));
    assertThat(parameters.get("bar")).isEqualTo("baz");
  }

  @Test public void urlWithSpaces() {
    DeepLinkEntry entry = deepLinkEntry("http://example.com/{query}");

    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    DeepLinkEntry match = testLoader.idxMatch(DeepLinkUri.parse("http://example.com/search%20paris"));

    Map<String, String> parameters = match.getParameters(DeepLinkUri.parse("http://example.com/search%20paris"));
    assertThat(parameters.get("query")).isEqualTo("search%20paris");
  }

  @Test public void noMatchesDifferentScheme() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://something");

    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    DeepLinkEntry match = testLoader.idxMatch(DeepLinkUri.parse("http://something"));

    assertThat(match).isNull();
  }

  @Test public void invalidUrl() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://something");

    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    DeepLinkEntry match = testLoader.idxMatch(DeepLinkUri.parse("airbnb://"));

    assertThat(match).isNull();
  }

  @Test public void pathWithQuotes() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://s/{query}");

    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    DeepLinkEntry match = testLoader.idxMatch(DeepLinkUri.parse("airbnb://s/Sant'Eufemia-a-Maiella--Italia"));

    assertThat(match).isEqualTo(entry);
  }

  @Test public void schemeWithNumbers() {
    DeepLinkEntry entry = deepLinkEntry("jackson5://example.com");

    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    DeepLinkEntry match = testLoader.idxMatch(DeepLinkUri.parse("jackson5://example.com"));

    assertThat(match).isEqualTo(entry);
  }

  @Test public void multiplePathParams() {
    DeepLinkEntry entry = deepLinkEntry("airbnb://{foo}/{bar}");

    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entry}));
    DeepLinkEntry match = testLoader.idxMatch(DeepLinkUri.parse("airbnb://baz/qux"));

    Map<String, String> parameters = match.getParameters(DeepLinkUri.parse("airbnb://baz/qux"));
    assertThat(parameters)
      .hasSize(2)
      .contains(entry("foo", "baz"), entry("bar", "qux"));
  }

  @Test public void placeholderOverlapBetweenMatchAndNonMach() {
    DeepLinkEntry entryMatch = deepLinkEntry("airbnb://{foo}/{bar}/match");
    DeepLinkEntry entryNoMatch = deepLinkEntry("airbnb://{hey}/{ho}/noMatch");

    TestDeepLinkLoader testLoader = getTestDelgate(Arrays.asList(new DeepLinkEntry[] {entryNoMatch,entryMatch}));
    DeepLinkUri matchUri = DeepLinkUri.parse("airbnb://baz/qux/match");
    DeepLinkEntry match = testLoader.idxMatch(matchUri);

    Map<String, String> parameters = match.getParameters(matchUri);
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

  /**
   * Helper method to get a class extending {@link BaseDeepLinkDelegate} acting as the delegate
   * for the
   * @param deepLinkEntries
   * @return
   */
  private static TestDeepLinkLoader getTestDelgate(List<DeepLinkEntry> deepLinkEntries){
    return new TestDeepLinkLoader(deepLinkEntries);
  }

  private static class TestDeepLinkLoader extends BaseRegistry {
    public TestDeepLinkLoader(List<DeepLinkEntry> registry) {
      super(registry, getSearchIndex(registry));
    }

    @NotNull
    private static byte[] getSearchIndex(List<DeepLinkEntry> registry) {
      Root trieRoot = new Root();
      for (int i = 0; i < registry.size(); i++) {
        trieRoot.addToTrie(i, DeepLinkUri.parse(registry.get(i).getUriTemplate()),registry.get(i).getActivityClass().toString(), registry.get(i).getMethod());
      }
      return trieRoot.toUByteArray();
    }
  }

}
