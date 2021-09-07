package com.airbnb.deeplinkdispatch

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.entry
import org.junit.Test

@kotlin.ExperimentalUnsignedTypes
class DeepLinkEntryTest {
    @Test
    fun testSingleParam() {
        val entry = activityDeepLinkEntry("airbnb://foo/{bar}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://foo/myData"))
        assertThat(match!!.getParameters(DeepLinkUri.parse("airbnb://foo/myData"))["bar"]).isEqualTo("myData")
    }

    @Test
    fun testSinglePartialParam() {
        val entry = activityDeepLinkEntry("airbnb://foo/pre{bar}post")
        val testRegistry = getTestRegistry(listOf(entry))

        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://foo/premyDatapost"))
        assertThat(match).isNotNull
        assertThat(match!!.getParameters(DeepLinkUri.parse("airbnb://foo/premyDatapost"))["bar"]).isEqualTo("myData")

        val emptyPlaceholderMatch = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://foo/prepost"))
        assertThat(emptyPlaceholderMatch).isNotNull
        assertThat(emptyPlaceholderMatch!!.getParameters(DeepLinkUri.parse("airbnb://foo/prepost"))["bar"]).isEqualTo("")

        val noMatch = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://foo/pre1"))
        assertThat(noMatch).isNull()

        val noMatchJustPre = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://foo/pre"))
        assertThat(noMatchJustPre).isNull()
    }

    @Test
    fun testSinglePartialParamLast() {
        val entry = activityDeepLinkEntry("airbnb://foo/pre{bar}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://foo/premyData"))
        assertThat(match!!.getParameters(DeepLinkUri.parse("airbnb://foo/premyData"))["bar"]).isEqualTo("myData")
    }

    @Test
    fun testSinglePartialParamLastEmpty() {
        val entry = activityDeepLinkEntry("airbnb://foo/pre{bar}")
        val testRegistry = getTestRegistry(listOf(entry))
        val noMatchLastCharDifferent = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://foo/pro"))
        assertThat(noMatchLastCharDifferent).isNull()

        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://foo/pre"))
        assertThat(match!!.getParameters(DeepLinkUri.parse("airbnb://foo/pre"))["bar"]).isEqualTo("")
    }

    @Test
    fun testTwoParams() {
        val entry = activityDeepLinkEntry("airbnb://test/{param1}/{param2}")
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
        val entryOneParams = activityDeepLinkEntry("airbnb://test/path/{param3}")
        val entryTwoParams = activityDeepLinkEntry("airbnb://test/path/{param2}/{param1}")
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
        val entry = activityDeepLinkEntry("airbnb://foo/{bar}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://foo/hyphens-and_underscores123"))
        val parameters = match!!.getParameters(DeepLinkUri.parse("airbnb://foo/hyphens-and_underscores123"))
        assertThat(parameters["bar"]).isEqualTo("hyphens-and_underscores123")
    }

    @Test
    fun testParamWithTildeAndDollarSign() {
        val entry = activityDeepLinkEntry("airbnb://test/{param1}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://test/tilde~dollar\$ign"))
        val parameters = match!!.getParameters(DeepLinkUri.parse("airbnb://test/tilde~dollar\$ign"))
        assertThat(parameters["param1"]).isEqualTo("tilde~dollar\$ign")
    }

    @Test
    fun testParamWithDotAndComma() {
        val entry = activityDeepLinkEntry("airbnb://test/{param1}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://test/N1.55,22.11"))
        val parameters = match!!.getParameters(DeepLinkUri.parse("airbnb://test/N1.55,22.11"))
        assertThat(parameters["param1"]).isEqualTo("N1.55,22.11")
    }

    @Test
    fun testParamForAtSign() {
        val entry = activityDeepLinkEntry("airbnb://test/{param1}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://test/somename@gmail.com"))
        val parameters = match!!.getParameters(DeepLinkUri.parse("airbnb://test/somename@gmail.com"))
        assertThat(parameters["param1"]).isEqualTo("somename@gmail.com")
    }

    @Test
    fun testParamForColon() {
        val entry = activityDeepLinkEntry("airbnb://test/{param1}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://test/a1:b2:c3"))
        val parameters = match!!.getParameters(DeepLinkUri.parse("airbnb://test/a1:b2:c3"))
        assertThat(parameters["param1"]).isEqualTo("a1:b2:c3")
    }

    @Test
    fun testParamWithSlash() {
        val entry = activityDeepLinkEntry("airbnb://test/{param1}/foo")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://test/123/foo"))
        val parameters = match!!.getParameters(DeepLinkUri.parse("airbnb://test/123/foo"))
        assertThat(parameters["param1"]).isEqualTo("123")
    }

    @Test
    fun testNoMatchesFound() {
        val entry = activityDeepLinkEntry("airbnb://foo/{bar}")
        val testRegistry = getTestRegistry(listOf(entry))
        assertThat(testRegistry.idxMatch(DeepLinkUri.parse("airbnb://test.com"))).isNull()
    }

    @Test
    @Throws(Exception::class)
    fun testEmptyParametersDontMatch() {
        val entry = activityDeepLinkEntry("dld://foo/{id}/bar")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("dld://foo//bar"))
        assertThat(match).isNull()
    }

    @Test
    @Throws(Exception::class)
    fun testEmptyParametersNameDontMatch() {
        val entry = activityDeepLinkEntry("dld://foo/{}/bar")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("dld://foo/bla/bar"))
        assertThat(match).isNull()
    }

    @Test
    @Throws(Exception::class)
    fun testEmptyPathPresentParams() {
        val urlTemplate = "dld://foo/{id}"
        val url = "dld://foo"
        val entry = activityDeepLinkEntry(urlTemplate)
        val entryNoParam = activityDeepLinkEntry(url)
        val entryMatchNoParam = DeepLinkMatchResult(entryNoParam, mapOf(DeepLinkUri.parse(url) to emptyMap()))
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse(url))
        val testRegistryNoParam = getTestRegistry(listOf(entryNoParam))
        val matchNoParam = testRegistryNoParam.idxMatch(DeepLinkUri.parse(url))
        assertThat(match).isNull()
        assertThat(matchNoParam).isEqualTo(entryMatchNoParam)
    }

    @Test
    fun testWithQueryParam() {
        val entry = activityDeepLinkEntry("airbnb://something")
        val testRegistry = getTestRegistry(listOf(entry))
        val matchResult = DeepLinkMatchResult(entry, mapOf(DeepLinkUri.parse("airbnb://something?foo=bar") to emptyMap()))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://something?foo=bar"))
        assertThat(match).isNotNull
        assertThat(match).isEqualTo(matchResult)
    }

    @Test
    fun noMatches() {
        val entry = activityDeepLinkEntry("airbnb://something.com/some-path")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://something.com/something-else"))
        assertThat(match).isNull()
    }

    @Test
    fun pathParamAndQueryString() {
        val entry = activityDeepLinkEntry("airbnb://foo/{bar}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://foo/baz?kit=kat"))
        val parameters = match!!.getParameters(DeepLinkUri.parse("airbnb://foo/baz?kit=kat"))
        assertThat(parameters["bar"]).isEqualTo("baz")
    }

    @Test
    fun urlWithSpaces() {
        val entry = activityDeepLinkEntry("http://example.com/{query}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("http://example.com/search%20paris"))
        val parameters = match!!.getParameters(DeepLinkUri.parse("http://example.com/search%20paris"))
        assertThat(parameters["query"]).isEqualTo("search%20paris")
    }

    @Test
    fun noMatchesDifferentScheme() {
        val entry = activityDeepLinkEntry("airbnb://something")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("http://something"))
        assertThat(match).isNull()
    }

    @Test
    fun testNoMatchForPartialOfRealMatch() {
        val entry = activityDeepLinkEntry("airbnb://host/something/something")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://host/something"))
        assertThat(match).isNull()
    }

    @Test
    fun invalidUrl() {
        val entry = activityDeepLinkEntry("airbnb://something")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://"))
        assertThat(match).isNull()
    }

    @Test
    fun pathWithQuotes() {
        val matchTemplate = "airbnb://s/{query}"
        val matchUrl = "airbnb://s/Sant'Eufemia-a-Maiella--Italia"
        val entry = activityDeepLinkEntry(matchTemplate)
        val parametersMap = mapOf(DeepLinkUri.parse(matchUrl) to mapOf("query" to "Sant'Eufemia-a-Maiella--Italia"))
        val entryMatch = DeepLinkMatchResult(entry, parametersMap)
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse(matchUrl))
        assertThat(match).isEqualTo(entryMatch)
    }

    @Test
    fun schemeWithNumbers() {
        val deeplinkUrl = "jackson5://example.com"
        val entry = activityDeepLinkEntry(deeplinkUrl)
        val entryMatch = DeepLinkMatchResult(entry, mapOf(DeepLinkUri.parse(deeplinkUrl) to emptyMap()))
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse(deeplinkUrl))
        assertThat(match).isEqualTo(entryMatch)
    }

    @Test
    fun multiplePathParams() {
        val entry = activityDeepLinkEntry("airbnb://{foo}/{bar}")
        val testRegistry = getTestRegistry(listOf(entry))
        val match = testRegistry.idxMatch(DeepLinkUri.parse("airbnb://baz/qux"))
        val parameters = match!!.getParameters(DeepLinkUri.parse("airbnb://baz/qux"))
        assertThat(parameters)
            .hasSize(2)
            .contains(entry("foo", "baz"), entry("bar", "qux"))
    }

    @Test
    fun placeholderOverlapBetweenMatchAndNonMach() {
        val entryMatch = activityDeepLinkEntry("airbnb://{foo}/{bar}/match")
        val entryNoMatch = activityDeepLinkEntry("airbnb://{hey}/{ho}/noMatch")
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
        val entry = activityDeepLinkEntry("airbnb://something")
        assertThat("airbnb://something" == entry.uriTemplate).isTrue
    }

    @Test
    fun templateWithParameters() {
        val entry = activityDeepLinkEntry("airbnb://test/{param1}/{param2}")
        assertThat("airbnb://test/{param1}/{param2}" == entry.uriTemplate).isTrue
    }

    @Test
    fun testSchemaHostParam() {
        val entryWihtPlaceholderSchemeHost = activityDeepLinkEntry("http{scheme}://{host}airbnb.com/test")
        val testRegistry = getTestRegistry(listOf(entryWihtPlaceholderSchemeHost))

        testParametrizedUrl(testRegistry, "http://en.airbnb.com/test", mapOf("scheme" to "", "host" to "en."))
        testParametrizedUrl(testRegistry, "http://www.airbnb.com/test", mapOf("scheme" to "", "host" to "www."))
        testParametrizedUrl(testRegistry, "http://airbnb.com/test", mapOf("scheme" to "", "host" to ""))

        testParametrizedUrl(testRegistry, "https://en.airbnb.com/test", mapOf("scheme" to "s", "host" to "en.", "scheme" to "s"))
        testParametrizedUrl(testRegistry, "https://www.airbnb.com/test", mapOf("scheme" to "s", "host" to "www.", "scheme" to "s"))
        testParametrizedUrl(testRegistry, "https://airbnb.com/test", mapOf("scheme" to "s", "host" to "", "scheme" to "s"))

        val matchDeTld = testRegistry.idxMatch(DeepLinkUri.parse("http://www.airbnb.de/test"))
        assertThat(matchDeTld).isNull()
        val matchHttpsDeTld = testRegistry.idxMatch(DeepLinkUri.parse("https://www.airbnb.de/test"))
        assertThat(matchHttpsDeTld).isNull()
    }

    @Test
    fun testSupportsWithNonExistantClass() {
        class NotInAppClassPath {}
        val deeplinkEntryWithNonExistentClass = activityDeepLinkEntry("http://test.com/", className = "notExisting" )
        val testRegistry = getTestRegistry(listOf(deeplinkEntryWithNonExistentClass))
        assertThat(testRegistry.supports(DeepLinkUri.parse("http://test.com/"))).isTrue
        assertThat(testRegistry.supports(DeepLinkUri.parse("http://false.com/"))).isFalse
    }

    @Test
    fun testIdxMatchWithNonExistantClass() {
        val deeplinkEntryWithNonExistentClass = activityDeepLinkEntry("http://test.com/", className = "notExisting" )
        val testRegistry = getTestRegistry(listOf(deeplinkEntryWithNonExistentClass))
        assertThat(testRegistry.idxMatch(DeepLinkUri.parse("http://test.com/"))).isNotNull
    }

    private fun testParametrizedUrl(
        testRegistry: TestDeepLinkRegistry,
        urlString: String,
        parameterMap: Map<String, String>
    ) {
        val url = DeepLinkUri.parse(urlString)
        val matchEnHost = testRegistry.idxMatch(url)
        assertThat(matchEnHost).isNotNull
        assertThat(matchEnHost?.parameterMap).isNotNull
        assertThat(matchEnHost!!.parameterMap[url]).isEqualTo(parameterMap)
    }

    private class TestDeepLinkRegistry(registry: List<DeepLinkEntry>) : BaseRegistry(getSearchIndex(registry), arrayOf()) {

        companion object {
            private fun getSearchIndex(registry: List<DeepLinkEntry>): ByteArray {
                val trieRoot = Root()
                for (entry in registry) {
                    trieRoot.addToTrie(entry)
                }
                return trieRoot.toUByteArray().toByteArray()
            }
        }
    }

    companion object {
        private fun activityDeepLinkEntry(uriTemplate: String, className: String = "java.lang.String"): DeepLinkEntry {
            return DeepLinkEntry.ActivityDeeplinkEntry(uriTemplate, className)
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
