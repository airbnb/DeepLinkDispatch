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
        /**
         * The intent for the Deep Link's destination. eg. [com.airbnb.deeplinkdispatch.sample.DeepLinkActivity]
         */
        val intent: Intent?,
        val taskStackBuilder: TaskStackBuilder?, val deepLinkEntry: DeepLinkMatchResult?
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
