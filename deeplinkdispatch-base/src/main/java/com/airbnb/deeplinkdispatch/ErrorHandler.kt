package com.airbnb.deeplinkdispatch

interface ErrorHandler {
    fun duplicateMatch(uriString: String, duplicatedMatches: List<DeepLinkMatchResult>)
}
