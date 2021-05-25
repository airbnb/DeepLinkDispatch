package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.MatchIndex
import org.assertj.core.api.Assertions
import org.junit.Test

@kotlin.ExperimentalUnsignedTypes
class MatchIndexTests {

    @Test
    fun testGetAllEntries() {
        val deepLinkEntries = listOf(
            DeepLinkEntry("http://www.example.com/path1/path2", MatchIndexTests::class.java, "someMethod1"),
            DeepLinkEntry("https://www.example.com/path1/path2", MatchIndexTests::class.java, "someMethod2"),
            DeepLinkEntry("dld://dldPath1/dldPath2", MatchIndexTests::class.java, "someMethod3"),
            DeepLinkEntry("http://example.de/", MatchIndexTests::class.java, "someMethod4"),
            DeepLinkEntry("http://example.com/path1/path2/path3", MatchIndexTests::class.java, "someMethod5"),
            DeepLinkEntry("http://example.com/path1/path2", MatchIndexTests::class.java, "someMethod6"),
            DeepLinkEntry("http://example.com/path1", MatchIndexTests::class.java, "someMethod7"),
            DeepLinkEntry("http://example.com/", MatchIndexTests::class.java, "someMethod8"),
        ).sortedBy { it.uriTemplate }
        val root = Root()
        deepLinkEntries.forEach{
            root.addToTrie(it.uriTemplate, it.activityClass.name, it.method)
        }
        val matchIndex = MatchIndex(root.toUByteArray().toByteArray())
        val allEntries = matchIndex.getAllEntries(0, matchIndex.length()).toList().sortedBy { it.uriTemplate }
        Assertions.assertThat(allEntries).isEqualTo(deepLinkEntries)
    }
}