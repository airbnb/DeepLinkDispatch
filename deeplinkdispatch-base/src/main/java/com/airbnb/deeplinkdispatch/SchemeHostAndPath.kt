package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.MetadataMasks.ComponentTypeHostMask
import com.airbnb.deeplinkdispatch.MetadataMasks.ComponentTypePathSegmentMask
import com.airbnb.deeplinkdispatch.MetadataMasks.ComponentTypeRootMask
import com.airbnb.deeplinkdispatch.MetadataMasks.ComponentTypeSchemeMask
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
class SchemeHostAndPath(val uri: DeepLinkUri) {

    val matchList: List<UrlElement> = listOf(
        UrlElement(ComponentTypeRootMask, MatchIndex.ROOT_VALUE.toByteArray()),
        UrlElement(ComponentTypeSchemeMask, uri.scheme().toByteArray()),
        UrlElement(ComponentTypeHostMask, uri.encodedHost().toByteArray())
    ) + uri.encodedPathSegments().map { pathSegment ->
        UrlElement(ComponentTypePathSegmentMask, pathSegment.toByteArray())
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
