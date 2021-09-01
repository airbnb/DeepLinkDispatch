package com.airbnb.deeplinkdispatch.sample

import android.content.Intent
import android.net.Uri
import com.airbnb.deeplinkdispatch.sample.handler.SampleJavaStaticTestHelper
import com.airbnb.deeplinkdispatch.sample.handler.SampleKotlinDeepLinkHandler
import com.airbnb.deeplinkdispatch.sample.handler.SampleThrowingKotlinDeepLinkHandler
import com.airbnb.deeplinkdispatch.sample.handler.TestJavaDeepLinkHandlerDeepLinkArgs
import com.airbnb.deeplinkdispatch.sample.handler.TestKotlinDeepLinkHandlerDeepLinkArgs
import io.mockk.mockkObject
import io.mockk.mockkStatic
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
    fun testKotlinCorrectHandlerCall() {
        mockkObject(SampleKotlinDeepLinkHandler)
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://airbnb.com/kotlin/1/pathData2/5.1/true?queryParam=17")
        )
        Robolectric.buildActivity(DeepLinkActivity::class.java, intent).create().get()

        val expectedDeepLinkParams =
            TestKotlinDeepLinkHandlerDeepLinkArgs(1, "pathData2", 5.1f, true, null, 17)
        verify(exactly = 1) { SampleKotlinDeepLinkHandler.handleDeepLink(expectedDeepLinkParams) }
    }

    @Test(expected = NumberFormatException::class)
    fun testKotlinHandlerCallWrongTypePathThrows() {
        mockkObject(SampleThrowingKotlinDeepLinkHandler)
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://airbnb.com/kotlin/throws/notALong")
        )
        Robolectric.buildActivity(DeepLinkActivity::class.java, intent).create().get()
    }

    @Test
    fun testKotlinHandlerCallWrongTypePath() {
        mockkObject(SampleKotlinDeepLinkHandler)
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://airbnb.com/kotlin/notAnInt/pathData2/5.1/true?queryParam=17")
        )
        Robolectric.buildActivity(DeepLinkActivity::class.java, intent).create().get()

        val expectedDeepLinkParams =
            TestKotlinDeepLinkHandlerDeepLinkArgs(0, "pathData2", 5.1f, true, null, 17)
        verify(exactly = 1) { SampleKotlinDeepLinkHandler.handleDeepLink(expectedDeepLinkParams) }
    }

    @Test
    fun testKotlinHandlerCallWrongTypeQuery() {
        mockkObject(SampleKotlinDeepLinkHandler)
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://airbnb.com/kotlin/1/pathData2/5.1/true?queryParam=notAnInt")
        )
        Robolectric.buildActivity(DeepLinkActivity::class.java, intent).create().get()

        val expectedDeepLinkParams =
            TestKotlinDeepLinkHandlerDeepLinkArgs(1, "pathData2", 5.1f, true, null, null)
        verify(exactly = 1) { SampleKotlinDeepLinkHandler.handleDeepLink(expectedDeepLinkParams) }
    }

    @Test
    fun testKotlinHandlerCallFloatArgumentIntegerValueInUrl() {
        mockkObject(SampleKotlinDeepLinkHandler)
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://airbnb.com/kotlin/1/pathData2/5/true?queryParam=17")
        )
        Robolectric.buildActivity(DeepLinkActivity::class.java, intent).create().get()

        val expectedDeepLinkParams =
            TestKotlinDeepLinkHandlerDeepLinkArgs(1, "pathData2", 5f, true, null, 17)
        verify(exactly = 1) { SampleKotlinDeepLinkHandler.handleDeepLink(expectedDeepLinkParams) }
    }

    @Test
    fun testJavaHandlerCallFloatArgumentIntegerValueInUrl() {
        mockkStatic(SampleJavaStaticTestHelper::class)
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://airbnb.com/java/1/pathData2/5/true?queryParam=17")
        )
        Robolectric.buildActivity(DeepLinkActivity::class.java, intent).create().get()

        val expectedDeepLinkParams =
            TestJavaDeepLinkHandlerDeepLinkArgs(1, "pathData2", 5f, true, null, 17)
        verify(exactly = 1) { SampleJavaStaticTestHelper.invokedHandler(expectedDeepLinkParams) }
    }
}

