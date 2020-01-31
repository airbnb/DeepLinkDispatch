package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.MatchIndex

/**
 * Used to categorize a DeepLinkUri's components into the types: scheme, host, and path.
 *
 * All operations are happening on the UI thread by definition so it is ok to make the lazies
 * not thread safe for speed improvement.
 */
class SchemeHostAndPath(val uri: DeepLinkUri) {

    val matchList: List<UrlElement> = listOf(UrlElement(MatchIndex.COMPONENT_ROOT, MatchIndex.ROOT_VALUE.toByteArray()),
            UrlElement(MatchIndex.COMPONENT_SCHEME, uri.scheme().toByteArray()),
            UrlElement(MatchIndex.COMPONENT_HOST, uri.encodedHost().toByteArray())) +
            uri.encodedPathSegments().map { pathElement ->
                UrlElement(MatchIndex.COMPONENT_PATH_SEGMENT, pathElement.toByteArray())
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
            MatchIndex.COMPONENT_ROOT -> "root"
            MatchIndex.COMPONENT_SCHEME -> "scheme"
            MatchIndex.COMPONENT_HOST -> "host"
            MatchIndex.COMPONENT_PATH_SEGMENT -> "path_segment"
            else -> "unknown"
        }
    }
}
