package com.airbnb.deeplinkdispatch.sample

import android.content.Intent
import android.net.Uri
import com.airbnb.deeplinkdispatch.sample.handler.TestDeepLinkHandler
import com.airbnb.deeplinkdispatch.sample.handler.TestDeepLinkHandlerDeepLinkArgs
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [21], manifest = "../sample/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner::class)
class DeepLinkHandlerTest {

    @Test
    fun testCorrectHandlerCall() {
        mockkObject(TestDeepLinkHandler)
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://airbnb.com/pathSegment/1/pathData2/5.1/true?queryParam=17")
        )
        Robolectric.buildActivity(DeepLinkActivity::class.java, intent).create().get()

        val expectedDeepLinkParams =
            TestDeepLinkHandlerDeepLinkArgs(1, "pathData2", 5.1f, true, null, 17)
        verify(exactly = 1) { TestDeepLinkHandler.handleDeepLink(expectedDeepLinkParams) }
    }

    @Test
    fun testHandlerCallWrongTypePath() {
        mockkObject(TestDeepLinkHandler)
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://airbnb.com/pathSegment/notAnInt/pathData2/5.1/true?queryParam=17")
        )
        Robolectric.buildActivity(DeepLinkActivity::class.java, intent).create().get()

        val expectedDeepLinkParams =
            TestDeepLinkHandlerDeepLinkArgs(0, "pathData2", 5.1f, true, null, 17)
        verify(exactly = 1) { TestDeepLinkHandler.handleDeepLink(expectedDeepLinkParams) }
    }

    @Test
    fun testHandlerCallWrongTypeQuery() {
        mockkObject(TestDeepLinkHandler)
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://airbnb.com/pathSegment/1/pathData2/5.1/true?queryParam=notAnInt")
        )
        Robolectric.buildActivity(DeepLinkActivity::class.java, intent).create().get()

        val expectedDeepLinkParams =
            TestDeepLinkHandlerDeepLinkArgs(1, "pathData2", 5.1f, true, null, null)
        verify(exactly = 1) { TestDeepLinkHandler.handleDeepLink(expectedDeepLinkParams) }
    }

    @Test
    fun testHandlerCallFloatArgumentIntegerValueInUrl() {
        mockkObject(TestDeepLinkHandler)
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://airbnb.com/pathSegment/1/pathData2/5/true?queryParam=17")
        )
        Robolectric.buildActivity(DeepLinkActivity::class.java, intent).create().get()

        val expectedDeepLinkParams =
            TestDeepLinkHandlerDeepLinkArgs(1, "pathData2", 5f, true, null, 17)
        verify(exactly = 1) { TestDeepLinkHandler.handleDeepLink(expectedDeepLinkParams) }
    }
}

