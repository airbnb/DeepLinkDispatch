package com.airbnb.deeplinkdispatch

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

@kotlin.ExperimentalUnsignedTypes
class MatchIndexTests {

    private val entryWithAllowedValues = DeepLinkEntry("http{scheme(|s)}://www.example.{tld(de|fr)}/somePath1/differentPath2", MatchIndexTests::class.java.name, "someMethod10")
    private val entryWithAllowedValueOnlyOneElement = DeepLinkEntry("http{scheme(s)}://www.justtesting.com/somePath", MatchIndexTests::class.java.name, "someMethod10")
    private val entryWithEmptyAllowedValue = DeepLinkEntry("http{scheme()}://www.anothertest.com/somePath", MatchIndexTests::class.java.name, "someMethod10")

    private val allowedValuesPlaceholderNames = setOf("scheme", "tld")

    private val testEntries: List<DeepLinkEntry> = listOf(
            DeepLinkEntry("http://www.example.com/somePath1/differentPath2", MatchIndexTests::class.java.name, "someMethod1"),
            DeepLinkEntry("https://www.example.com/path1/path2", MatchIndexTests::class.java.name, "someMethod2"),
            DeepLinkEntry("dld://dldPath1/dldPath2", MatchIndexTests::class.java.name, "someMethod3"),
            DeepLinkEntry("http://example.de/", MatchIndexTests::class.java.name, "someMethod4"),
            DeepLinkEntry("http://example.com/path1", MatchIndexTests::class.java.name, "someMethod7"),
            DeepLinkEntry("http://example.com/somethingElse", MatchIndexTests::class.java.name, "someMethod9"),
            DeepLinkEntry("http://example.com/path1/pathElement2/path3", MatchIndexTests::class.java.name, "someMethod5"),
            DeepLinkEntry("http://example.com/path1/someOtherPathElement2", MatchIndexTests::class.java.name, "someMethod6"),
            DeepLinkEntry("http://example.com/", MatchIndexTests::class.java.name, "someMethod8"),
            entryWithAllowedValues,
            entryWithAllowedValueOnlyOneElement
    ).sortedBy { it.uriTemplate }

    private fun testRegistry(entries: List<DeepLinkEntry>): TestRegistry {
        val root = Root()
        entries.forEach {
            root.addToTrie(it.uriTemplate, it.className, it.method)
        }
        return TestRegistry(root.toUByteArray().toByteArray())
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
        val matchEntry1 = testRegistry(testEntries).idxMatch(DeepLinkUri.parse("https://www.example.co.uk/somePath1/differentPath2"), emptyMap())
        assertThat(matchEntry1).isNull()
        val matchEntry2 = testRegistry(testEntries).idxMatch(DeepLinkUri.parse("httpx://www.example.de/somePath1/differentPath2"), emptyMap())
        assertThat(matchEntry2).isNull()
    }

    class TestRegistry(val matchArray: ByteArray) : BaseRegistry(matchArray, emptyArray())
}
