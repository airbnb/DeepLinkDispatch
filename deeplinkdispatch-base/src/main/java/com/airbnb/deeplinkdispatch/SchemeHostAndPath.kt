package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.MatchIndex

/**
 * Used to encapsulate the scheme host and path of a DeepLinkUri into a type.
 *
 * All operations are happening on the UI thread by definition so it is ok to make the lazies
 * not thread safe for speed improvement.
 */
class SchemeHostAndPath(val uri: DeepLinkUri) {
    val schemeHostAndPath by lazy(LazyThreadSafetyMode.NONE) {
        uri.scheme() + "://" + uri.encodedHost() + uri.encodedPath()
    }

    val matchList by lazy(LazyThreadSafetyMode.NONE) {
        val list = mutableListOf(UrlElement(MatchIndex.TYPE_ROOT,MatchIndex.ROOT_VALUE.toUByteArray()),UrlElement(MatchIndex.TYPE_SCHEME, uri.scheme().toUByteArray()),UrlElement(MatchIndex.TYPE_HOST, uri.encodedHost().toUByteArray()))
        uri.encodedPathSegments().forEach({pathElement -> list.add(UrlElement(MatchIndex.TYPE_PATH_SEGMENT, pathElement.toUByteArray()))})
        list
    }
}

data class UrlElement(val type: UByte, val value: UByteArray)

fun String.toUByteArray() : UByteArray {
    return toByteArray().toUByteArray()
}
