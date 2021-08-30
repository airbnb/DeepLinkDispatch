package com.airbnb.deeplinkdispatch

import android.content.Intent
import androidx.core.app.TaskStackBuilder

data class DeepLinkResult(
    /**
     * @return whether or not the dispatch was a success.
     */
    val isSuccessful: Boolean,
    val uriString: String?,
    val error: String,
    val deepLinkMatchResult: DeepLinkMatchResult?,
    val methodResult: DeepLinkMethodResult,
    val parameters: Map<String, String> = emptyMap()
) {
    /**
     * This exists so that calls from Kotlin code [error()] are maintained across major version 4.
     * This will be removed in DLD 5.x.x
     */
    fun error(): String? = error

    override fun toString(): String {
        return ("DeepLinkResult{"
                + "successful=" + isSuccessful
                + ", uriString=" + uriString
                + ", error='" + error + '\''.toString()
                + '}'.toString())
    }
}

/**
 * Can be used to return from any deeplink annotated method. Whatever entry is null will be used.
 * If both are not null we will use the taskStackBuilder.
 */
data class DeepLinkMethodResult(
    val intent: Intent? = null,
    val taskStackBuilder: TaskStackBuilder? = null
)