package com.airbnb.deeplinkdispatch.handler

const val DEEP_LINK_HANDLER_METHOD_NAME = "handleDeepLink"

/**
 * You can extend this class in your own project and annotated it with an @DeepLink annotation.
 * This will lead to calling handleDeepLink function in your handler with an arguments object
 * of type T (that you will define in your project) containing the parsed arguments (path placeholder
 * and query parameters).
 */
abstract class DeepLinkHandler<T> {
     abstract fun handleDeepLink(parameters: T)

     /**
      * This can be set to true in your extension of the DeepLinkHandler. If it is set to true
      * any type conversion error for any args value will throw a kotlin.NumberFormatException
      * instead of falling back to a 0 value, which is the default behavior.
      */
     open val throwOnTypeConversion = false
}