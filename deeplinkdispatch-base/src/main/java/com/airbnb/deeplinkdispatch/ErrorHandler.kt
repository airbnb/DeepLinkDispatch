package com.airbnb.deeplinkdispatch

interface ErrorHandler {
    fun duplicateMatch(uriString: String, duplicatedMatches: List<DeepLinkMatchResult>)

    fun activityClassNotFound(uriString: String, className: String) {
        // Empty default implementation
    }
}