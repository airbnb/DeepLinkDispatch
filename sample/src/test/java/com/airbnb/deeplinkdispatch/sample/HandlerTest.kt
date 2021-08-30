package com.airbnb.deeplinkdispatch.sample

import android.content.Intent
import android.net.Uri
import com.airbnb.deeplinkdispatch.sample.handler.TestDeepLinkHandler
import com.airbnb.deeplinkdispatch.sample.handler.TestDeepLinkHandlerDeepLinkArgs
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [21], manifest = "../sample/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner::class)
class HandlerTest {
    @Test
    fun testIntent() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://airbnb.com/pathSegment/1/pathDatas2/5.1/true?queryParam=17")
        )
            .putExtra("TEST_EXTRA", "FOO")
        val deepLinkActivity = Robolectric.buildActivity(
            DeepLinkActivity::class.java, intent
        )
            .create().get()

        mockkObject(TestDeepLinkHandler)
        val expectedDeepLinkParams = TestDeepLinkHandlerDeepLinkArgs(1, "pathData2", 5f, true, null, 17)
        every { TestDeepLinkHandler.handleDeepLink(expectedDeepLinkParams)  } just Runs
//        verify(exactly = 1) { TestDeepLinkHandler.handleDeepLink(expectedDeepLinkParams) }
    }

    @Test
    fun testIntentWrongType() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://airbnb.com/pathSegment/notAnInt/pathDatas2/5.1/true?queryParam=17")
        )
            .putExtra("TEST_EXTRA", "FOO")
        val deepLinkActivity = Robolectric.buildActivity(
            DeepLinkActivity::class.java, intent
        )
            .create().get()

        mockkObject(TestDeepLinkHandler)
        val expectedDeepLinkParams = TestDeepLinkHandlerDeepLinkArgs(1, "pathData2", 5f, true, null, 17)
        every { TestDeepLinkHandler.handleDeepLink(expectedDeepLinkParams)  } just Runs
//        verify(exactly = 1) { TestDeepLinkHandler.handleDeepLink(expectedDeepLinkParams) }
    }

    @Test
    fun testIntentFloatWithInteger() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://airbnb.com/pathSegment/notAnInt/pathDatas2/5/true?queryParam=17")
        )
            .putExtra("TEST_EXTRA", "FOO")
        val deepLinkActivity = Robolectric.buildActivity(
            DeepLinkActivity::class.java, intent
        )
            .create().get()

        mockkObject(TestDeepLinkHandler)
        val expectedDeepLinkParams = TestDeepLinkHandlerDeepLinkArgs(1, "pathData2", 5f, true, null, 17)
        every { TestDeepLinkHandler.handleDeepLink(expectedDeepLinkParams)  } just Runs
//        verify(exactly = 1) { TestDeepLinkHandler.handleDeepLink(expectedDeepLinkParams) }
    }
}

