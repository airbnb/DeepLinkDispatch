package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.MatchIndex

/**
 * Used to encapsulate the scheme host and path of a DeepLinkUri into a type.
 *
 * All operations are happening on the UI thread by definition so it is ok to make the lazies
 * not thread safe for speed improvement.
 */
class SchemeHostAndPath(val uri: DeepLinkUri) {

    val matchList: List<UrlElement>

    init {
        matchList = listOf(UrlElement(MatchIndex.TYPE_ROOT, MatchIndex.ROOT_VALUE.toByteArray()),
                UrlElement(MatchIndex.TYPE_SCHEME, uri.scheme().toByteArray()),
                UrlElement(MatchIndex.TYPE_HOST, uri.encodedHost().toByteArray())) +
                uri.encodedPathSegments().map { pathElement ->
                    UrlElement(MatchIndex.TYPE_PATH_SEGMENT, pathElement.toByteArray())
                }
    }
}

class UrlElement(val type: Byte, val value: ByteArray) {

    /**
     * This is for debugging, it's actually not called at runtime
     */
    override fun toString(): String {
        return "Type: ${typeToString()}, Value: ${String(value)}"
    }

    private fun typeToString(): String {
        return when (type) {
            MatchIndex.TYPE_ROOT -> "root"
            MatchIndex.TYPE_SCHEME -> "scheme"
            MatchIndex.TYPE_HOST -> "host"
            MatchIndex.TYPE_PATH_SEGMENT -> "path_segment"
            else -> "unknown"
        }
    }
}
