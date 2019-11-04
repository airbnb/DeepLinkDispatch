package com.airbnb.deeplinkdispatch.sample

import android.content.Context
import android.content.Intent

object UserDeepLinkIntents {
    @WebLink("users/create/{user_id}")
    @JvmStatic
    fun forWeblink(context: Context): Intent {
        return Intent(context, SecondActivity::class.java)
    }
}
