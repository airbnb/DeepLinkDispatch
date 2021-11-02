package com.airbnb.deeplinkdispatch

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

@kotlin.ExperimentalUnsignedTypes
class MatchIndexTests {

    private val entryWithAllowedValues = DeepLinkEntry.MethodDeeplinkEntry("http{scheme(|s)}://www.example.{tld(de|fr)}/somePath1/differentPath2", MatchIndexTests::class.java.name, "someMethod10")
    private val entryWithAllowedValueOnlyOneElement = DeepLinkEntry.MethodDeeplinkEntry("http{scheme(s)}://www.justtesting.com/somePath", MatchIndexTests::class.java.name, "someMethod10")
    private val entryWithEmptyAllowedValue = DeepLinkEntry.MethodDeeplinkEntry("http{scheme()}://www.anothertest.com/somePath", MatchIndexTests::class.java.name, "someMethod10")

    private val allowedValuesPlaceholderNames = setOf("scheme", "tld")

    private val testEntries: List<DeepLinkEntry> = listOf(
        DeepLinkEntry.MethodDeeplinkEntry("http://www.example.com/somePath1/differentPath2", MatchIndexTests::class.java.name, "someMethod1"),
        DeepLinkEntry.MethodDeeplinkEntry("https://www.example.com/path1/path2", MatchIndexTests::class.java.name, "someMethod2"),
        DeepLinkEntry.MethodDeeplinkEntry("dld://dldPath1/dldPath2", MatchIndexTests::class.java.name, "someMethod3"),
        DeepLinkEntry.MethodDeeplinkEntry("http://example.de/", MatchIndexTests::class.java.name, "someMethod4"),
        DeepLinkEntry.MethodDeeplinkEntry("http://example.com/path1", MatchIndexTests::class.java.name, "someMethod7"),
        DeepLinkEntry.MethodDeeplinkEntry("http://example.com/somethingElse", MatchIndexTests::class.java.name, "someMethod9"),
        DeepLinkEntry.MethodDeeplinkEntry("http://example.com/path1/pathElement2/path3", MatchIndexTests::class.java.name, "someMethod5"),
        DeepLinkEntry.MethodDeeplinkEntry("http://example.com/path1/someOtherPathElement2", MatchIndexTests::class.java.name, "someMethod6"),
        DeepLinkEntry.MethodDeeplinkEntry("http://example.com/", MatchIndexTests::class.java.name, "someMethod8"),
        entryWithAllowedValues,
        entryWithAllowedValueOnlyOneElement
    ).sortedBy { it.uriTemplate }

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
        val matchEntry1 = testRegistry(testEntries).idxMatch(DeepLinkUri.parse("https://www.example.co.uk/somePath1/differentPath2"), emptyMap())
        assertThat(matchEntry1).isNull()
        val matchEntry2 = testRegistry(testEntries).idxMatch(DeepLinkUri.parse("httpx://www.example.de/somePath1/differentPath2"), emptyMap())
        assertThat(matchEntry2).isNull()
    }

    @Test
    fun testWithLongerThan256CharUrlSegment() {
        val testRegistry = testRegistry(
            listOf(
                DeepLinkEntry.MethodDeeplinkEntry(
                    uriTemplate = "http{scheme_suffix(|s)}://testing.{url_domain_suffix(com|just|a|very|long|list|of|words|that|are|technically|not|domains|so|that|we|make|it|over|256|characters|to|test|how|this|deals|with|longer|urls|that|would|otherwise|cause|an|array|index|out|of|bounds|exception|within|the|match|index|itself)}/",
                    className = Object::class.java.name,
                    method = "someMethod1"
                ),
            )
        )
        assertThat(testRegistry.supports(DeepLinkUri.parse("https://testing.com/"))).isTrue()
    }
}
