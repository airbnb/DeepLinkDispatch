package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.MatchIndex
import org.junit.Assert.*
import org.junit.Test
import java.lang.IllegalStateException

private const val ONE_PARAM_SCHEMA = "scheme://host/one/{param}/three"
private const val METHOD_NAME = "methodName"

@kotlin.ExperimentalUnsignedTypes
class DeepLinkMatchTests {

    @Test
    fun testMatchArraySerializationDeserializationNoMethod() {
        val matchByteArray = matchByteArray(UriMatch(ONE_PARAM_SCHEMA, this.javaClass.name, null))
        val entryFromArray = MatchIndex.getDeeplinkEntryFromArray(matchByteArray.toByteArray(), matchByteArray.size, 0)
        assertNotNull(entryFromArray)
        entryFromArray?.let {
            assertEquals(ONE_PARAM_SCHEMA.toString(), it.uriTemplate)
            assertEquals(this.javaClass, it.activityClass)
            assertNull(it.method)
        }
    }

    @Test
    fun testMatchArraySerializationDeserialization() {
        val matchByteArray = matchByteArray(UriMatch(ONE_PARAM_SCHEMA, this.javaClass.name, METHOD_NAME))
        val entryFromArray = MatchIndex.getDeeplinkEntryFromArray(matchByteArray.toByteArray(), matchByteArray.size, 0)
        assertNotNull(entryFromArray)
        entryFromArray?.let {
            assertEquals(ONE_PARAM_SCHEMA, it.uriTemplate)
            assertEquals(this.javaClass, it.activityClass)
            assertEquals(METHOD_NAME, it.method)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun testMatchArraySerializationDeserializationNonExistantClass() {
        val matchByteArray = matchByteArray(UriMatch(ONE_PARAM_SCHEMA, "soneNonexistantClass", null))
        MatchIndex.getDeeplinkEntryFromArray(matchByteArray.toByteArray(), matchByteArray.size, 0)
    }

    @Test fun testMatchArraySerializationDeserializationNoMatch(){
        val entryFromArray = MatchIndex.getDeeplinkEntryFromArray(ByteArray(0), 0, 0)
        assertNull(entryFromArray)
    }
}