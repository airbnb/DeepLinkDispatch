package com.airbnb.deeplinkdispatch.sample.kaptlibrary

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.deeplinkdispatch.DeepLink

@DeepLink("http://example.com/kapt-library")
class KaptLibraryActivity : AppCompatActivity() {
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            Toast.makeText(
                this, "Got deep link " + intent.getStringExtra(DeepLink.URI),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        private val TAG = KaptLibraryActivity::class.java.simpleName
    }
}