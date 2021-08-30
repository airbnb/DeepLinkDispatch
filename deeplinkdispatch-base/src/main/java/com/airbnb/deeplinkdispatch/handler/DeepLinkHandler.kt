package com.airbnb.deeplinkdispatch.handler

const val DEEP_LINK_HANDLER_METHOD_NAME = "handleDeepLink"

abstract class DeepLinkHandler<T> {
     abstract fun handleDeepLink(parameters: T)
}