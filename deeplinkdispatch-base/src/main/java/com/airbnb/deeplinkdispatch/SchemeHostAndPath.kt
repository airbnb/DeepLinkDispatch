package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.NodeMetadata.*
import com.airbnb.deeplinkdispatch.base.MatchIndex
import com.airbnb.deeplinkdispatch.NodeMetadataConverters.isComponentTypeHost
import com.airbnb.deeplinkdispatch.NodeMetadataConverters.isComponentTypePathSegment
import com.airbnb.deeplinkdispatch.NodeMetadataConverters.isComponentTypeRoot
import com.airbnb.deeplinkdispatch.NodeMetadataConverters.isComponentTypeScheme

/**
 * Used to categorize a DeepLinkUri's components into the types: scheme, host, and path.
 *
 * All operations are happening on the UI thread by definition so it is ok to make the lazies
 * not thread safe for speed improvement.
 */
class SchemeHostAndPath(val uri: DeepLinkUri) {

    val matchList: List<UrlElement> = listOf(UrlElement(IsComponentTypeRoot.flag.toByte(), MatchIndex.ROOT_VALUE.toByteArray()),
            UrlElement(IsComponentTypeScheme.flag.toByte(), uri.scheme().toByteArray()),
            UrlElement(IsComponentTypeHost.flag.toByte(), uri.encodedHost().toByteArray())) +
            uri.encodedPathSegments().map { pathElement ->
                UrlElement(IsComponentTypePathSegment.flag.toByte(), pathElement.toByteArray())
            }
}

class UrlElement(val typeFlag: Byte, val value: ByteArray) {

    /**
     * This is for debugging, it's actually not called at runtime
     */
    override fun toString(): String {
        return "Type: ${typeToString()}, Value: ${String(value)}"
    }

    private fun typeToString(): String = when {
        isComponentTypeRoot(typeFlag) -> "root"
        isComponentTypeScheme(typeFlag) -> "scheme"
        isComponentTypeHost(typeFlag) -> "host"
        isComponentTypePathSegment(typeFlag) -> "path_segment"
        else -> "unknown"
    }
}
