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
     * These lambdas are called if a value cannot be converted to the desired type. The default
     * behavior is to make these values 0/null respectively but the default behavior can be overridden.
     */
    open val typeConversionErrorNullable: (String) -> Int? = { value: String -> null }
    open val typeConversionErrorNonNullable: (String) -> Int = { value: String -> 0 }
}
