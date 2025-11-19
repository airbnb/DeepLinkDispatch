package com.airbnb.deeplinkdispatch.sample.ksplibrary

import android.content.Context
import android.content.Intent
import com.airbnb.deeplinkdispatch.DeepLink

object KspLibraryDeeplinks {
    @DeepLink(
        value = ["https://method-example.com"],
        activityClassFqn = "com.airbnb.deeplinkdispatch.sample.ksplibrary.KspLibraryActivity",
        actions = ["android.intent.action.VIEW", "android.intent.action.SEND"],
        categories = ["android.intent.category.DEFAULT", "android.intent.category.BROWSABLE", "android.intent.category.INFO"],
        intentFilterAttributes = ["android:label=\"@string/intent_filter_label\""],
    )
    @JvmStatic
    fun intentForDeepLinkMethod(context: Context?): Intent = Intent(context, KspLibraryActivity::class.java)
}
