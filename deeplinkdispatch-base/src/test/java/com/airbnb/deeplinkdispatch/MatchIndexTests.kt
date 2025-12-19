package com.airbnb.deeplinkdispatch

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

@kotlin.ExperimentalUnsignedTypes
class MatchIndexTests {
    private val entryWithAllowedValues =
        DeepLinkEntry.MethodDeeplinkEntry(
            "http{scheme(|s)}://www.example.{tld(de|fr)}/somePath1/differentPath2",
            MatchIndexTests::class.java.name,
            "someMethod10",
        )
    private val entryWithAllowedValueOnlyOneElement =
        DeepLinkEntry.MethodDeeplinkEntry(
            "http{scheme(s)}://www.justtesting.com/somePath",
            MatchIndexTests::class.java.name,
            "someMethod10",
        )
    private val entryWithEmptyAllowedValue =
        DeepLinkEntry.MethodDeeplinkEntry(
            "http{scheme()}://www.anothertest.com/somePath",
            MatchIndexTests::class.java.name,
            "someMethod10",
        )
    private val entryWithAllowedValueAndLongerValueThan =
        DeepLinkEntry.MethodDeeplinkEntry(
            "scheme://{some_value(allowed|values)}somethinglonger/one/{param}/three",
            MatchIndexTests::class.java.name,
            "someMethod10",
        )

    private val allowedValuesPlaceholderNames = setOf("scheme", "tld")

    private val testEntries: List<DeepLinkEntry> =
        listOf(
            DeepLinkEntry.MethodDeeplinkEntry(
                "http://www.example.com/somePath1/differentPath2",
                MatchIndexTests::class.java.name,
                "someMethod1",
            ),
            DeepLinkEntry.MethodDeeplinkEntry("https://www.example.com/path1/path2", MatchIndexTests::class.java.name, "someMethod2"),
            DeepLinkEntry.MethodDeeplinkEntry("dld://dldPath1/dldPath2", MatchIndexTests::class.java.name, "someMethod3"),
            DeepLinkEntry.MethodDeeplinkEntry("http://example.de/", MatchIndexTests::class.java.name, "someMethod4"),
            DeepLinkEntry.MethodDeeplinkEntry("http://example.com/path1", MatchIndexTests::class.java.name, "someMethod7"),
            DeepLinkEntry.MethodDeeplinkEntry("http://example.com/somethingElse", MatchIndexTests::class.java.name, "someMethod9"),
            DeepLinkEntry.MethodDeeplinkEntry(
                "http://example.com/path1/pathElement2/path3",
                MatchIndexTests::class.java.name,
                "someMethod5",
            ),
            DeepLinkEntry.MethodDeeplinkEntry(
                "http://example.com/path1/someOtherPathElement2",
                MatchIndexTests::class.java.name,
                "someMethod6",
            ),
            DeepLinkEntry.MethodDeeplinkEntry("http://example.com/", MatchIndexTests::class.java.name, "someMethod8"),
            entryWithAllowedValues,
            entryWithAllowedValueOnlyOneElement,
            entryWithAllowedValueAndLongerValueThan,
        ).sortedBy { it.uriTemplate }

    @Test
    fun testMatchWithPlaceholderThatEndsInSegmentWithLongerMatch() {
        // This is testing a condition where an URL segment (in this case the host) has a placeholder with allowed values
        // in the template and is matching (from the back) to what is in the to be matched URL, where the entry in the template
        // is matching but longer (and thus not matching) before the placeholder starts (from the back).
        // In this case the host is it like this:
        // template host: {some_value(allowed|values)}somethinglonger
        // to match host:                                      longer
        val deepLinkUri = DeepLinkUri.parse("scheme://longer/one/param/three")
        val matchEntry = testRegistry(testEntries).idxMatch(deepLinkUri, emptyMap())
        assertThat(matchEntry).isNull()
    }

    @Test
    fun testGetAllEntries() {
        val allEntries = testRegistry(testEntries).getAllEntries().sortedBy { it.uriTemplate }
        assertThat(allEntries).isEqualTo(testEntries)
    }

    @Test
    fun testMatchWithAllowedValuesInPlaceholders() {
        val deepLinkUri = DeepLinkUri.parse("https://www.example.de/somePath1/differentPath2")
        val matchEntry = testRegistry(testEntries).idxMatch(deepLinkUri, emptyMap())
        assertThat(matchEntry).isNotNull
        assertThat(matchEntry?.deeplinkEntry).isEqualTo(entryWithAllowedValues)
        assertThat(matchEntry?.parameterMap?.get(deepLinkUri)?.keys).isEqualTo(allowedValuesPlaceholderNames)
    }

    @Test
    fun testMatchWithAllowedValuesInPlaceholdersMatch() {
        val matchEntry = testRegistry(testEntries).idxMatch(DeepLinkUri.parse("http://www.example.de/somePath1/differentPath2"), emptyMap())
        assertThat(matchEntry).isNotNull
        assertThat(matchEntry?.deeplinkEntry).isEqualTo(entryWithAllowedValues)
    }

    @Test
    fun testMatchWithAllowedValuesInPlaceholdersOnlyOneElementMatch() {
        val matchEntry = testRegistry(testEntries).idxMatch(DeepLinkUri.parse("https://www.justtesting.com/somePath"), emptyMap())
        assertThat(matchEntry).isNotNull
        assertThat(matchEntry?.deeplinkEntry).isEqualTo(entryWithAllowedValueOnlyOneElement)
        val matchEntry1 = testRegistry(testEntries).idxMatch(DeepLinkUri.parse("http://www.justtesting.com/somePath"), emptyMap())
        assertThat(matchEntry1).isNull()
    }

    @Test
    fun testMatchWithEmptyAllowedValuesInElementMatch() {
        val matchEntry = testRegistry(testEntries).idxMatch(DeepLinkUri.parse("http://www.anothertest.com/somePath"), emptyMap())
        assertThat(matchEntry).isNull()
        val matchEntry1 = testRegistry(testEntries).idxMatch(DeepLinkUri.parse("https://www.anothertest.com/somePath"), emptyMap())
        assertThat(matchEntry1).isNull()
    }

    @Test
    fun testNoMatchWithAllowedValuesInPlaceholders() {
        val matchEntry1 =
            testRegistry(
                testEntries,
            ).idxMatch(DeepLinkUri.parse("https://www.example.co.uk/somePath1/differentPath2"), emptyMap())
        assertThat(matchEntry1).isNull()
        val matchEntry2 =
            testRegistry(
                testEntries,
            ).idxMatch(DeepLinkUri.parse("httpx://www.example.de/somePath1/differentPath2"), emptyMap())
        assertThat(matchEntry2).isNull()
    }

    @Test
    fun testWithLongerThan256CharUrlSegment() {
        val testRegistry =
            testRegistry(
                listOf(
                    DeepLinkEntry.MethodDeeplinkEntry(
                        uriTemplate =
                            "http{scheme_suffix(|s)}://testing.{url_domain_suffix(com|just|a|very|long|list|of|words|that|are|" +
                                "technically|not|domains|so|that|we|make|it|over|256|characters|to|test|how|this|deals|with|longer|urls|" +
                                "that|would|otherwise|cause|an|array|index|out|of|bounds|exception|within|the|match|index|itself)}/",
                        className = Object::class.java.name,
                        method = "someMethod1",
                    ),
                ),
            )
        assertThat(testRegistry.supports(DeepLinkUri.parse("https://testing.com/"))).isTrue()
    }

    // Tests for configurable path segment with leading slash

    private val entryWithConfigurablePathSegment =
        DeepLinkEntry.MethodDeeplinkEntry(
            "https://example.com/<configurable-segment>/resource",
            MatchIndexTests::class.java.name,
            "configurableMethod1",
        )

    private val entryWithConfigurablePathSegmentAtEnd =
        DeepLinkEntry.MethodDeeplinkEntry(
            "https://example.com/path/<configurable-segment>",
            MatchIndexTests::class.java.name,
            "configurableMethod2",
        )

    private val entryWithTwoConfigurablePathSegments =
        DeepLinkEntry.MethodDeeplinkEntry(
            "https://example.com/<segment-one>/<segment-two>/end",
            MatchIndexTests::class.java.name,
            "configurableMethod3",
        )

    private fun pathSegmentReplacements(vararg pairs: Pair<String, String>): Map<ByteArray, ByteArray> =
        pairs.associate { it.first.toByteArray() to it.second.toByteArray() }

    @Test
    fun testConfigurablePathSegmentWithLeadingSlashMatches() {
        val registry = testRegistry(listOf(entryWithConfigurablePathSegment))
        val replacements = pathSegmentReplacements("configurable-segment" to "/foo")
        val matchEntry = registry.idxMatch(DeepLinkUri.parse("https://example.com/foo/resource"), replacements)
        assertThat(matchEntry).isNotNull
        assertThat(matchEntry?.deeplinkEntry).isEqualTo(entryWithConfigurablePathSegment)
    }

    @Test
    fun testConfigurablePathSegmentWithLeadingSlashNoMatchWhenValueDiffers() {
        val registry = testRegistry(listOf(entryWithConfigurablePathSegment))
        val replacements = pathSegmentReplacements("configurable-segment" to "/foo")
        val matchEntry = registry.idxMatch(DeepLinkUri.parse("https://example.com/bar/resource"), replacements)
        assertThat(matchEntry).isNull()
    }

    @Test
    fun testConfigurablePathSegmentEmptyReplacementRemovesSegment() {
        val registry = testRegistry(listOf(entryWithConfigurablePathSegment))
        val replacements = pathSegmentReplacements("configurable-segment" to "")
        val matchEntry = registry.idxMatch(DeepLinkUri.parse("https://example.com/resource"), replacements)
        assertThat(matchEntry).isNotNull
        assertThat(matchEntry?.deeplinkEntry).isEqualTo(entryWithConfigurablePathSegment)
    }

    @Test
    fun testConfigurablePathSegmentAtEndWithLeadingSlashMatches() {
        val registry = testRegistry(listOf(entryWithConfigurablePathSegmentAtEnd))
        val replacements = pathSegmentReplacements("configurable-segment" to "/final")
        val matchEntry = registry.idxMatch(DeepLinkUri.parse("https://example.com/path/final"), replacements)
        assertThat(matchEntry).isNotNull
        assertThat(matchEntry?.deeplinkEntry).isEqualTo(entryWithConfigurablePathSegmentAtEnd)
    }

    @Test
    fun testConfigurablePathSegmentAtEndEmptyReplacementMatches() {
        // When the configurable segment at the end is empty, it should match
        // when the inbound URI ends at the preceding path segment
        val registry = testRegistry(listOf(entryWithConfigurablePathSegmentAtEnd))
        val replacements = pathSegmentReplacements("configurable-segment" to "")
        val matchEntry = registry.idxMatch(DeepLinkUri.parse("https://example.com/path"), replacements)
        assertThat(matchEntry).isNotNull
        assertThat(matchEntry?.deeplinkEntry).isEqualTo(entryWithConfigurablePathSegmentAtEnd)
    }

    @Test
    fun testTwoConfigurablePathSegmentsWithLeadingSlashMatch() {
        val registry = testRegistry(listOf(entryWithTwoConfigurablePathSegments))
        val replacements =
            pathSegmentReplacements(
                "segment-one" to "/first",
                "segment-two" to "/second",
            )
        val matchEntry = registry.idxMatch(DeepLinkUri.parse("https://example.com/first/second/end"), replacements)
        assertThat(matchEntry).isNotNull
        assertThat(matchEntry?.deeplinkEntry).isEqualTo(entryWithTwoConfigurablePathSegments)
    }

    @Test
    fun testTwoConfigurablePathSegmentsOneEmptyMatches() {
        val registry = testRegistry(listOf(entryWithTwoConfigurablePathSegments))
        val replacements =
            pathSegmentReplacements(
                "segment-one" to "/first",
                "segment-two" to "",
            )
        val matchEntry = registry.idxMatch(DeepLinkUri.parse("https://example.com/first/end"), replacements)
        assertThat(matchEntry).isNotNull
        assertThat(matchEntry?.deeplinkEntry).isEqualTo(entryWithTwoConfigurablePathSegments)
    }

    @Test
    fun testTwoConfigurablePathSegmentsBothEmptyMatches() {
        val registry = testRegistry(listOf(entryWithTwoConfigurablePathSegments))
        val replacements =
            pathSegmentReplacements(
                "segment-one" to "",
                "segment-two" to "",
            )
        val matchEntry = registry.idxMatch(DeepLinkUri.parse("https://example.com/end"), replacements)
        assertThat(matchEntry).isNotNull
        assertThat(matchEntry?.deeplinkEntry).isEqualTo(entryWithTwoConfigurablePathSegments)
    }

    @Test
    fun testConfigurablePathSegmentWithMultiplePathPartsInReplacementNoMatch() {
        // Multi-part replacements don't work as the matching is done per-segment
        // The replacement value "/foo/bar" would try to match a single path segment,
        // which doesn't work with multiple path parts in the inbound URI
        val registry = testRegistry(listOf(entryWithConfigurablePathSegment))
        val replacements = pathSegmentReplacements("configurable-segment" to "/foo/bar")
        val matchEntry = registry.idxMatch(DeepLinkUri.parse("https://example.com/foo/bar/resource"), replacements)
        // This should NOT match because the matching is per path segment
        assertThat(matchEntry).isNull()
    }

    @Test
    fun testConfigurablePathSegmentLeadingSlashStrippedDuringComparison() {
        // This test verifies that the leading slash in the replacement value is properly
        // stripped when comparing with the inbound path segment
        val registry = testRegistry(listOf(entryWithConfigurablePathSegment))
        // Replacement value "/mypath" should match inbound path segment "mypath"
        val replacements = pathSegmentReplacements("configurable-segment" to "/mypath")
        val matchEntry = registry.idxMatch(DeepLinkUri.parse("https://example.com/mypath/resource"), replacements)
        assertThat(matchEntry).isNotNull
    }

    @Test
    fun testConfigurablePathSegmentNoMatchWithoutCorrectReplacement() {
        val registry = testRegistry(listOf(entryWithConfigurablePathSegment))
        val replacements = pathSegmentReplacements("wrong-key" to "/foo")
        val matchEntry = registry.idxMatch(DeepLinkUri.parse("https://example.com/foo/resource"), replacements)
        assertThat(matchEntry).isNull()
    }
}
