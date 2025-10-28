package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.MetadataMasks.COMPONENT_TYPE_HOST_MASK
import com.airbnb.deeplinkdispatch.MetadataMasks.COMPONENT_TYPE_PATH_SEGMENT_MASK
import com.airbnb.deeplinkdispatch.MetadataMasks.COMPONENT_TYPE_ROOT_MASK
import com.airbnb.deeplinkdispatch.MetadataMasks.COMPONENT_TYPE_SCHEME_MASK
import com.airbnb.deeplinkdispatch.NodeMetadata.Companion.isComponentTypeHost
import com.airbnb.deeplinkdispatch.NodeMetadata.Companion.isComponentTypePathSegment
import com.airbnb.deeplinkdispatch.NodeMetadata.Companion.isComponentTypeRoot
import com.airbnb.deeplinkdispatch.NodeMetadata.Companion.isComponentTypeScheme
import com.airbnb.deeplinkdispatch.base.MatchIndex

/**
 * Used to categorize a DeepLinkUri's components into the types: scheme, host, and path.
 *
 * All operations are happening on the UI thread by definition so it is ok to make the lazies
 * not thread safe for speed improvement.
 */
class SchemeHostAndPath(
    val uri: DeepLinkUri,
) {
    val matchList: List<UrlElement> =
        listOf(
            UrlElement(COMPONENT_TYPE_ROOT_MASK, MatchIndex.ROOT_VALUE.toByteArray()),
            UrlElement(COMPONENT_TYPE_SCHEME_MASK, uri.scheme().toByteArray()),
            UrlElement(COMPONENT_TYPE_HOST_MASK, uri.encodedHost().toByteArray()),
        ) +
            uri.encodedPathSegments().map { pathSegment ->
                UrlElement(COMPONENT_TYPE_PATH_SEGMENT_MASK, pathSegment.toByteArray())
            }
}

class UrlElement(
    val typeFlag: Byte,
    val value: ByteArray,
) {
    /**
     * This is for debugging, it's actually not called at runtime
     */
    override fun toString(): String = "Type: ${typeToString()}, Value: ${String(value)}"

    private fun typeToString(): String =
        when {
            isComponentTypeRoot(typeFlag) -> "root"
            isComponentTypeScheme(typeFlag) -> "scheme"
            isComponentTypeHost(typeFlag) -> "host"
            isComponentTypePathSegment(typeFlag) -> "path_segment"
            else -> "unknown"
        }
}
