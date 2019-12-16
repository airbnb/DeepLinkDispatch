package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.MatchIndex
import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase.typeToString

/**
 * Used to encapsulate the scheme host and path of a DeepLinkUri into a type.
 *
 * All operations are happening on the UI thread by definition so it is ok to make the lazies
 * not thread safe for speed improvement.
 */
class SchemeHostAndPath(val uri: DeepLinkUri) {
    val schemeHostAndPath: String by lazy(LazyThreadSafetyMode.NONE) {
        uri.scheme() + "://" + uri.encodedHost() + uri.encodedPath()
    }

    val matchList: List<UrlElement> by lazy(LazyThreadSafetyMode.NONE) {
        val list = mutableListOf(UrlElement(MatchIndex.TYPE_ROOT, MatchIndex.ROOT_VALUE.toByteArray()),
                UrlElement(MatchIndex.TYPE_SCHEME, uri.scheme().toByteArray()),
                UrlElement(MatchIndex.TYPE_HOST, uri.encodedHost().toByteArray()))
        uri.encodedPathSegments().forEach { pathElement ->
            list.add(UrlElement(MatchIndex.TYPE_PATH_SEGMENT, pathElement.toByteArray()))
        }
        list
    }
}

class UrlElement(val type: Byte, val value: ByteArray) {

    override fun toString(): String {
        return "Type: ${typeToString()}, Value: ${String(value)}"
    }

    private fun typeToString(): String {
        return when(type){
            MatchIndex.TYPE_ROOT -> "root"
            MatchIndex.TYPE_SCHEME -> "scheme"
            MatchIndex.TYPE_HOST    -> "host"
            MatchIndex.TYPE_PATH_SEGMENT -> "path_segment"
            else -> "unknown"
        }
    }
}
