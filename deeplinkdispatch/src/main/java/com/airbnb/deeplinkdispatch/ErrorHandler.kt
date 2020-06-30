package com.airbnb.deeplinkdispatch

interface ErrorHandler {
    fun duplicateMatch(duplicatedMatches: List<DeepLinkEntry>)
}