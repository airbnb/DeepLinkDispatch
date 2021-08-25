package com.airbnb.deeplinkdispatch.handler

abstract class DeepLinkHandler<T> {

     abstract fun handleDeepLink(parameters: T)

}