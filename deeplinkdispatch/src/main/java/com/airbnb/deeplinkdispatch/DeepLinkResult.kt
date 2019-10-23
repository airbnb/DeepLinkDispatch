package com.airbnb.deeplinkdispatch

import android.content.Intent
import androidx.core.app.TaskStackBuilder

data class DeepLinkResult(
        /**
         * @return whether or not the dispatch was a success.
         */
        val isSuccessful: Boolean,
        val uriString: String?,
        val error: String?,
        /**
         * The intent for the Deep Link's destination. eg. `com.airbnb.deeplinkdispatch.sample.DeepLinkActivity`
         */
        /**
         * @return the Intent for the DeepLink's destination. `com.airbnb.deeplinkdispatch.sample.DeepLinkActivity`
         */
        val intent: Intent?,
        val taskStackBuilder: TaskStackBuilder?, val deepLinkEntry: DeepLinkEntry?
) {
    override fun toString(): String {
        return ("DeepLinkResult{"
                + "successful=" + isSuccessful
                + ", uriString=" + uriString
                + ", error='" + error + '\''.toString()
                + '}'.toString())
    }
}
