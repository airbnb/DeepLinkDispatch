package com.airbnb.deeplinkdispatch

abstract class ErrorHandler {
    open fun duplicateMatch(uriString: String, duplicatedMatches: List<DeepLinkMatchResult>) {}

    /**
     * Called if unable to determine the type of an args class for a DeepLinkHandler.
     * If this is the case we are just assuming no arguments and do not fail this might
     * but this should not happen.
     * This will give youyr pooportunity to log these cases.
     */
    open fun unableToDetermineHandlerArgsType(uriTemplate: String, className: String) {}
}
