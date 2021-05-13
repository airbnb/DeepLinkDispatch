package com.airbnb.deeplinkdispatch

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.entry
import org.junit.Test

@kotlin.ExperimentalUnsignedTypes
class DeepLinkEntryTest {
    @Test
    fun testSingleParam() {
        val entry = deepLinkEntry("airbnb://foo/{bar}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://foo/myData"))
        assertThat(match!!.getParameters(DeepLinkUri.parse("airbnb://foo/myData"))["bar"]).isEqualTo("myData")
    }

    @Test
    fun testTwoParams() {
        val entry = deepLinkEntry("airbnb://test/{param1}/{param2}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://test/12345/alice"))
        val parameters = match!!.getParameters(DeepLinkUri.parse("airbnb://test/12345/alice"))
        assertThat(parameters["param1"]).isEqualTo("12345")
        assertThat(parameters["param2"]).isEqualTo("alice")
    }

    /**
     * This test makes sure that both version of the parametrized strings are matched correctly.
     * As match groups are sorted alphabetically in the index we need to make sure that the
     * shorter one is in the index behind the longer one (as otherwise the test would match
     * correctly anyway)
     */
    @Test
    fun testOneAndTwoParamsSubPath() {
        val entryOneParams = deepLinkEntry("airbnb://test/path/{param3}")
        val entryTwoParams = deepLinkEntry("airbnb://test/path/{param2}/{param1}")
        val testRegistry = getTestRegistry(listOf(entryTwoParams, entryOneParams))
        val url2Params = "airbnb://test/path/bob/alice"
        val matchTwo = testRegistry.idxMatch(DeepLinkUri.parse(url2Params))
        assertThat(matchTwo).isNotNull
        val parameters2 = matchTwo!!.getParameters(DeepLinkUri.parse(url2Params))
        assertThat(parameters2["param2"]).isEqualTo("bob")
        assertThat(parameters2["param1"]).isEqualTo("alice")
        assertThat(parameters2.size).isEqualTo(2)
        val url1Param = "airbnb://test/path/eve"
        val matchOne = testRegistry.idxMatch(DeepLinkUri.parse(url1Param))
        val parameters1 = matchOne!!.getParameters(DeepLinkUri.parse(url1Param))
        assertThat(matchOne).isNotNull
        assertThat(parameters1["param3"]).isEqualTo("eve")
        assertThat(parameters1.size).isEqualTo(1)
    }

    @Test
    @Throws(Exception::class)
    fun testParamWithSpecialCharacters() {
        val entry = deepLinkEntry("airbnb://foo/{bar}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://foo/hyphens-and_underscores123"))
        val parameters = match!!.getParameters(DeepLinkUri.parse("airbnb://foo/hyphens-and_underscores123"))
        assertThat(parameters["bar"]).isEqualTo("hyphens-and_underscores123")
    }

    @Test
    fun testParamWithTildeAndDollarSign() {
        val entry = deepLinkEntry("airbnb://test/{param1}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://test/tilde~dollar\$ign"))
        val parameters = match!!.getParameters(DeepLinkUri.parse("airbnb://test/tilde~dollar\$ign"))
        assertThat(parameters["param1"]).isEqualTo("tilde~dollar\$ign")
    }

    @Test
    fun testParamWithDotAndComma() {
        val entry = deepLinkEntry("airbnb://test/{param1}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://test/N1.55,22.11"))
        val parameters = match!!.getParameters(DeepLinkUri.parse("airbnb://test/N1.55,22.11"))
        assertThat(parameters["param1"]).isEqualTo("N1.55,22.11")
    }

    @Test
    fun testParamForAtSign() {
        val entry = deepLinkEntry("airbnb://test/{param1}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://test/somename@gmail.com"))
        val parameters = match!!.getParameters(DeepLinkUri.parse("airbnb://test/somename@gmail.com"))
        assertThat(parameters["param1"]).isEqualTo("somename@gmail.com")
    }

    @Test
    fun testParamForColon() {
        val entry = deepLinkEntry("airbnb://test/{param1}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://test/a1:b2:c3"))
        val parameters = match!!.getParameters(DeepLinkUri.parse("airbnb://test/a1:b2:c3"))
        assertThat(parameters["param1"]).isEqualTo("a1:b2:c3")
    }

    @Test
    fun testParamWithSlash() {
        val entry = deepLinkEntry("airbnb://test/{param1}/foo")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://test/123/foo"))
        val parameters = match!!.getParameters(DeepLinkUri.parse("airbnb://test/123/foo"))
        assertThat(parameters["param1"]).isEqualTo("123")
    }

    @Test
    fun testNoMatchesFound() {
        val entry = deepLinkEntry("airbnb://foo/{bar}")
        val testRegistry = getTestRegistry(listOf(entry))
        assertThat(testRegistry.idxMatch(DeepLinkUri.parse("airbnb://test.com"))).isNull()
        assertThat(entry.getParameters(DeepLinkUri.parse("airbnb://test.com")).isEmpty()).isTrue
    }

    @Test
    @Throws(Exception::class)
    fun testEmptyParametersDontMatch() {
        val entry = deepLinkEntry("dld://foo/{id}/bar")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("dld://foo//bar"))
        assertThat(match).isNull()
    }

    @Test
    @Throws(Exception::class)
    fun testEmptyParametersNameDontMatch() {
        val entry = deepLinkEntry("dld://foo/{}/bar")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("dld://foo/bla/bar"))
        assertThat(match).isNull()
    }

    @Test
    @Throws(Exception::class)
    fun testEmptyPathPresentParams() {
        val urlTemplate = "dld://foo/{id}"
        val url = "dld://foo"
        val entry = deepLinkEntry(urlTemplate)
        val entryNoParam = deepLinkEntry(url)
        entryNoParam.setParameters(DeepLinkUri.parse(url), emptyMap())
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse(url))
        val testRegistryNoParam = getTestRegistry(listOf(entryNoParam))
        val matchNoParam = testRegistryNoParam.idxMatch(DeepLinkUri.parse(url))
        assertThat(match).isNull()
        assertThat(matchNoParam).isEqualTo(entryNoParam)
    }

    @Test
    fun testWithQueryParam() {
        val entry = deepLinkEntry("airbnb://something")

//    assertThat(entry.matches("airbnb://something?foo=bar")).isTrue();
        assertThat(entry.getParameters(DeepLinkUri.parse("airbnb://something?foo=bar")).isEmpty()).isTrue
    }

    @Test
    fun noMatches() {
        val entry = deepLinkEntry("airbnb://something.com/some-path")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://something.com/something-else"))
        assertThat(match).isNull()
    }

    @Test
    fun pathParamAndQueryString() {
        val entry = deepLinkEntry("airbnb://foo/{bar}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://foo/baz?kit=kat"))
        val parameters = match!!.getParameters(DeepLinkUri.parse("airbnb://foo/baz?kit=kat"))
        assertThat(parameters["bar"]).isEqualTo("baz")
    }

    @Test
    fun urlWithSpaces() {
        val entry = deepLinkEntry("http://example.com/{query}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("http://example.com/search%20paris"))
        val parameters = match!!.getParameters(DeepLinkUri.parse("http://example.com/search%20paris"))
        assertThat(parameters["query"]).isEqualTo("search%20paris")
    }

    @Test
    fun noMatchesDifferentScheme() {
        val entry = deepLinkEntry("airbnb://something")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("http://something"))
        assertThat(match).isNull()
    }

    @Test
    fun testNoMatchForPartialOfRealMatch() {
        val entry = deepLinkEntry("airbnb://host/something/something")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://host/something"))
        assertThat(match).isNull()
    }

    @Test
    fun invalidUrl() {
        val entry = deepLinkEntry("airbnb://something")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://"))
        assertThat(match).isNull()
    }

    @Test
    fun pathWithQuotes() {
        val matchTemplate = "airbnb://s/{query}"
        val matchUrl = "airbnb://s/Sant'Eufemia-a-Maiella--Italia"
        val entry = deepLinkEntry(matchTemplate)
        val parametersMap = mapOf("query" to "Sant'Eufemia-a-Maiella--Italia")
        entry.setParameters(DeepLinkUri.parse(matchUrl), parametersMap)
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse(matchUrl))
        assertThat(match).isEqualTo(entry)
    }

    @Test
    fun schemeWithNumbers() {
        val deeplinkUrl = "jackson5://example.com"
        val entry = deepLinkEntry(deeplinkUrl)
        entry.setParameters(DeepLinkUri.parse(deeplinkUrl), emptyMap())
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse(deeplinkUrl))
        assertThat(match).isEqualTo(entry)
    }

    @Test
    fun multiplePathParams() {
        val entry = deepLinkEntry("airbnb://{foo}/{bar}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://baz/qux"))
        val parameters = match!!.getParameters(DeepLinkUri.parse("airbnb://baz/qux"))
        assertThat(parameters)
                .hasSize(2)
                .contains(entry("foo", "baz"), entry("bar", "qux"))
    }

    @Test
    fun placeholderOverlapBetweenMatchAndNonMach() {
        val entryMatch = deepLinkEntry("airbnb://{foo}/{bar}/match")
        val entryNoMatch = deepLinkEntry("airbnb://{hey}/{ho}/noMatch")
        val testRegistry = getTestRegistry(listOf(entryNoMatch, entryMatch))
        val matchUri = DeepLinkUri.parse("airbnb://baz/qux/match")
        val match = testRegistry.idxMatch(matchUri)
        val parameters = match!!.getParameters(matchUri)
        assertThat(parameters)
                .hasSize(2)
                .contains(entry("foo", "baz"), entry("bar", "qux"))
    }

    @Test
    fun templateWithoutParameters() {
        val entry = deepLinkEntry("airbnb://something")
        assertThat("airbnb://something" == entry.uriTemplate).isTrue
    }

    @Test
    fun templateWithParameters() {
        val entry = deepLinkEntry("airbnb://test/{param1}/{param2}")
        assertThat("airbnb://test/{param1}/{param2}" == entry.uriTemplate).isTrue
    }

    private class TestDeepLinkRegistry(registry: List<DeepLinkEntry>) : BaseRegistry(getSearchIndex(registry), arrayOf()) {
        companion object {
            private fun getSearchIndex(registry: List<DeepLinkEntry>): ByteArray {
                val trieRoot = Root()
                for (entry in registry) {
                    trieRoot.addToTrie(entry.uriTemplate, entry.activityClass.canonicalName!!, entry.method)
                }
                return trieRoot.toUByteArray().toByteArray()
            }
        }
    }

    companion object {
        private fun deepLinkEntry(uri: String): DeepLinkEntry {
            return DeepLinkEntry(uri, String::class.java, null)
        }

        /**
         * Helper method to get a class extending [BaseRegistry] acting as the delegate
         * for the
         * @param deepLinkEntries
         * @return
         */
        private fun getTestRegistry(deepLinkEntries: List<DeepLinkEntry>): TestDeepLinkRegistry {
            return TestDeepLinkRegistry(deepLinkEntries)
        }
    }
}