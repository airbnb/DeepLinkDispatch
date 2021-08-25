package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.MatchIndex
import org.assertj.core.api.Assertions
import org.junit.Test

@kotlin.ExperimentalUnsignedTypes
class MatchIndexTests {

    @Test
    fun testGetAllEntries() {
        val deepLinkEntries = listOf(
            DeepLinkEntry(MatchType.Method, "http://www.example.com/somePath1/differentPath2", MatchIndexTests::class.java.name, "someMethod1"),
            DeepLinkEntry(MatchType.Method, "https://www.example.com/path1/path2", MatchIndexTests::class.java.name, "someMethod2"),
            DeepLinkEntry(MatchType.Method, "dld://dldPath1/dldPath2", MatchIndexTests::class.java.name, "someMethod3"),
            DeepLinkEntry(MatchType.Method, "http://example.de/", MatchIndexTests::class.java.name, "someMethod4"),
            DeepLinkEntry(MatchType.Method, "http://example.com/path1", MatchIndexTests::class.java.name, "someMethod7"),
            DeepLinkEntry(MatchType.Method, "http://example.com/somethingElse", MatchIndexTests::class.java.name, "someMethod9"),
            DeepLinkEntry(MatchType.Method, "http://example.com/path1/pathElement2/path3", MatchIndexTests::class.java.name, "someMethod5"),
            DeepLinkEntry(MatchType.Method, "http://example.com/path1/someOtherPathElement2", MatchIndexTests::class.java.name, "someMethod6"),
            DeepLinkEntry(MatchType.Method, "http://example.com/", MatchIndexTests::class.java.name, "someMethod8"),
        ).sortedBy { it.uriTemplate }
        val root = Root()
        deepLinkEntries.forEach{
            root.addToTrie(it.type, it.uriTemplate, it.className, it.method)
        }
        val testRegistry = TestRegistry(root.toUByteArray().toByteArray())
        val allEntries = testRegistry.getAllEntries().sortedBy { it.uriTemplate }
        Assertions.assertThat(allEntries).isEqualTo(deepLinkEntries)
    }

    class TestRegistry(val matchArray: ByteArray) : BaseRegistry(matchArray, emptyArray())

}