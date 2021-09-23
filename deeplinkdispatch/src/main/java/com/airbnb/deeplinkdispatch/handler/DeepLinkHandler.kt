package com.airbnb.deeplinkdispatch.handler

import android.content.Context

/**
 * You can implement this interface in your own project and annotated it with an @DeepLink annotation.
 * This will lead to calling handleDeepLink function in your handler with an arguments object
 * of type T (that you will define in your project) containing the parsed arguments (path placeholder
 * and query parameters). For deserilzation simple data types are supported directlty but you can
 * also add your own TypeConverters when you instantiate you DeepLinkDelegate class.
 * There you can also provide error handler overrides for the standard type converters.
 *
 * Note: This is referenced via reflection in DeepLinkProcessor as well as in the proguard-rules.pro
 * file of this lib. If you make any changes here be sure to also update those two usages!
 */
interface DeepLinkHandler<T> {
    fun handleDeepLink(context: Context, parameters: T)
}
