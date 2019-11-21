package com.airbnb.deeplinkdispatch

/**
 * Used to encapsulate the scheme host and path of a DeepLinkUri into a type.
 */
class SchemeHostAndPath(val uri: DeepLinkUri) {
    val schemeHostAndPath by lazy {
        uri.scheme() + "://" + uri.encodedHost() + uri.encodedPath()
    }
}