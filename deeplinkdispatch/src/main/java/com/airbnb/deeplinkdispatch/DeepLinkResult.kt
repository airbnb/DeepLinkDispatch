package com.airbnb.deeplinkdispatch

import android.content.Intent
import androidx.core.app.TaskStackBuilder
import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler

data class DeepLinkResult(
    /**
     * @return whether or not the dispatch was a success.
     */
    val isSuccessful: Boolean,
    val uriString: String? = null,
    val error: String = "",
    val errorThrowable: Throwable? = null,
    val deepLinkMatchResult: DeepLinkMatchResult? = null,
    val methodResult: DeepLinkMethodResult = DeepLinkMethodResult(null, null),
    val parameters: Map<String, String> = emptyMap(),
    val deepLinkHandlerResult: DeepLinkHandlerResult<Any>? = null,
) {
    /**
     * This exists so that calls from Kotlin code [error()] are maintained across major version 4.
     * This will be removed in DLD 5.x.x
     */
    fun error(): String? = error

    override fun toString(): String =
        (
            "DeepLinkResult{" +
                "successful=" + isSuccessful +
                ", uriString=" + uriString +
                ", error='" + error + '\''.toString() +
                '}'.toString()
        )
}

/**
 * Can be used to return from any deeplink annotated method. Whatever entry is null will be used.
 * If both are not null we will use the taskStackBuilder.
 */
data class DeepLinkMethodResult(
    val intent: Intent? = null,
    val taskStackBuilder: TaskStackBuilder? = null,
)

data class DeepLinkHandlerResult<T>(
    val deepLinkHandler: DeepLinkHandler<T>,
    val deepLinkHandlerArgs: T,
)
