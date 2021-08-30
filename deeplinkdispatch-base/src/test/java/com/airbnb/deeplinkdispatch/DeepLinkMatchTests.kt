package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.MatchIndex
import junit.framework.TestCase.*
import org.junit.Test

private const val ONE_PARAM_SCHEMA = "scheme://host/one/{param}/three"
private const val METHOD_NAME = "methodName"

@kotlin.ExperimentalUnsignedTypes
class DeepLinkMatchTests {

    @Test
    fun testMatchArraySerializationDeserializationNoMethod() {
        val matchByteArray = matchByteArray(UriMatch(MatchType.Activity, ONE_PARAM_SCHEMA, this.javaClass.name, null))
        val entryFromArray = MatchIndex(matchByteArray.toByteArray()).getMatchResultFromIndex(
            matchByteArray.size,
            0,
            DeepLinkUri.parse("schema://none"),
            emptyMap()
        )
        assertNotNull(entryFromArray)
        entryFromArray?.let {
            assertTrue(it.deeplinkEntry is DeepLinkEntry.ActivityDeeplinkEntry)
            assertEquals(ONE_PARAM_SCHEMA, it.deeplinkEntry.uriTemplate)
            assertEquals(this.javaClass, it.deeplinkEntry.clazz)
        }
    }

    @Test
    fun testMatchArraySerializationDeserialization() {
        val matchByteArray = matchByteArray(UriMatch(MatchType.Method, ONE_PARAM_SCHEMA, this.javaClass.name, METHOD_NAME))
        val entryFromArray = MatchIndex(matchByteArray.toByteArray()).getMatchResultFromIndex(
            matchByteArray.size,
            0,
            DeepLinkUri.parse("schema://none"),
            emptyMap()
        )
        assertNotNull(entryFromArray)
        entryFromArray?.let {
            assertTrue(it.deeplinkEntry is DeepLinkEntry.MethodDeeplinkEntry)
            val methodEntry = it.deeplinkEntry as DeepLinkEntry.MethodDeeplinkEntry
            assertEquals(ONE_PARAM_SCHEMA, methodEntry.uriTemplate)
            assertEquals(this.javaClass, methodEntry.clazz)
            assertEquals(METHOD_NAME, methodEntry.method)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun testMatchArraySerializationDeserializationNonExistantClass() {
        val matchByteArray = matchByteArray(UriMatch(MatchType.Handler, ONE_PARAM_SCHEMA, "someNonexistantClass", null))
        val entryFromArray = MatchIndex(matchByteArray.toByteArray()).getMatchResultFromIndex(
            matchByteArray.size,
            0,
            DeepLinkUri.parse("schema://none"),
            emptyMap()
        )
        assertNotNull(entryFromArray)
        entryFromArray!!.let {
            assertTrue(it.deeplinkEntry is DeepLinkEntry.HandlerDeepLinkEntry)
            assertEquals(ONE_PARAM_SCHEMA, it.deeplinkEntry.uriTemplate)
            it.deeplinkEntry.clazz
        }
    }

    @Test fun testMatchArraySerializationDeserializationNoMatch(){
        val entryFromArray = MatchIndex(ByteArray(0)).getMatchResultFromIndex(
            0,
            0,
            DeepLinkUri.parse("schema://none"),
            emptyMap()
        )
        assertNull(entryFromArray)
    }
}