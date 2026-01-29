package com.airbnb.deeplinkdispatch.sample

import android.content.Intent
import android.net.Uri
import android.os.Looper
import com.airbnb.deeplinkdispatch.sample.handler.SampleJavaStaticTestHelper
import com.airbnb.deeplinkdispatch.sample.handler.SampleKotlinDeepLinkHandler
import com.airbnb.deeplinkdispatch.sample.handler.SampleNoParamsKotlinDeepLinkHandler
import com.airbnb.deeplinkdispatch.sample.handler.SamplePartialParamKotlinDeepLinkHandler
import com.airbnb.deeplinkdispatch.sample.handler.SomeObjectThatExtendsAClassThatImplementsTheDeepLinkHandler
import com.airbnb.deeplinkdispatch.sample.handler.TestJavaDeepLinkHandlerDeepLinkArgs
import com.airbnb.deeplinkdispatch.sample.handler.TestKotlinDeepLinkHandlerDeepLinkArgs
import com.airbnb.deeplinkdispatch.sample.handler.TestKotlinDeepLinkHandlerDeepLinkArgsMissingPathParamExtraQueryParam
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@Config(sdk = [21])
@RunWith(RobolectricTestRunner::class)
class DeepLinkHandlerTest {
    @Test
    fun testKotlinCorrectHandlerCall() {
        mockkObject(SampleKotlinDeepLinkHandler)
        val intent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://airbnb.com/kotlin/1/pathData2/5.1/true?queryParam=17"),
            )
        val context = Robolectric.buildActivity(DeepLinkActivity::class.java, intent).create().get()

        val expectedDeepLinkParams =
            TestKotlinDeepLinkHandlerDeepLinkArgs(1, "pathData2", 5.1f, true, null, 17)
        verify(exactly = 1) {
            SampleKotlinDeepLinkHandler.handleDeepLink(
                context,
                expectedDeepLinkParams,
            )
        }
    }

    @Test
    fun testKotlinHandlerCallWrongTypePath() {
        mockkObject(SampleKotlinDeepLinkHandler)
        val intent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://airbnb.com/kotlin/notAnInt/pathData2/5.1/true?queryParam=17"),
            )
        val context = Robolectric.buildActivity(DeepLinkActivity::class.java, intent).create().get()

        val expectedDeepLinkParams =
            TestKotlinDeepLinkHandlerDeepLinkArgs(0, "pathData2", 5.1f, true, null, 17)
        verify(exactly = 1) {
            SampleKotlinDeepLinkHandler.handleDeepLink(
                context,
                expectedDeepLinkParams,
            )
        }
    }

    @Test
    fun testKotlinHandlerCallWrongTypeQuery() {
        mockkObject(SampleKotlinDeepLinkHandler)
        val intent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://airbnb.com/kotlin/1/pathData2/5.1/true?queryParam=notAnInt"),
            )
        val context = Robolectric.buildActivity(DeepLinkActivity::class.java, intent).create().get()

        val expectedDeepLinkParams =
            TestKotlinDeepLinkHandlerDeepLinkArgs(1, "pathData2", 5.1f, true, null, null)
        verify(exactly = 1) {
            SampleKotlinDeepLinkHandler.handleDeepLink(
                context,
                expectedDeepLinkParams,
            )
        }
    }

    @Test
    fun testKotlinHandlerCallFloatArgumentIntegerValueInUrl() {
        mockkObject(SampleKotlinDeepLinkHandler)
        val intent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://airbnb.com/kotlin/1/pathData2/5/true?queryParam=17"),
            )
        val context = Robolectric.buildActivity(DeepLinkActivity::class.java, intent).create().get()

        val expectedDeepLinkParams =
            TestKotlinDeepLinkHandlerDeepLinkArgs(1, "pathData2", 5f, true, null, 17)
        verify(exactly = 1) {
            SampleKotlinDeepLinkHandler.handleDeepLink(
                context,
                expectedDeepLinkParams,
            )
        }
    }

    @Test
    fun testKotlinHandlerPartialArgumentClass() {
        mockkObject(SamplePartialParamKotlinDeepLinkHandler)
        val intent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://airbnb.com/partial/1/pathData2/5/true?queryParam=17"),
            )
        val context = Robolectric.buildActivity(DeepLinkActivity::class.java, intent).create().get()

        val expectedDeepLinkParams =
            TestKotlinDeepLinkHandlerDeepLinkArgsMissingPathParamExtraQueryParam(1, null, 17, null)
        verify(exactly = 1) {
            SamplePartialParamKotlinDeepLinkHandler.handleDeepLink(
                context,
                expectedDeepLinkParams,
            )
        }
    }

    @Test
    fun testKotlinHandlerNoArgumentClass() {
        mockkObject(SampleNoParamsKotlinDeepLinkHandler)
        val intent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://airbnb.com/noparams/1/pathData2/5/true?queryParam=17"),
            )
        val context = Robolectric.buildActivity(DeepLinkActivity::class.java, intent).create().get()
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        verify(exactly = 1) {
            SampleNoParamsKotlinDeepLinkHandler.handleDeepLink(
                context,
                any(),
            )
        }
    }

    @Test
    fun testKotlinHandlerNoArgumentClassWhenExtendingActualHandler() {
        mockkObject(SomeObjectThatExtendsAClassThatImplementsTheDeepLinkHandler)
        val intent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://airbnb.com/noparamsExtension/1/pathData2/5/true?queryParam=17"),
            )
        val context = Robolectric.buildActivity(DeepLinkActivity::class.java, intent).create().get()
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        verify(exactly = 1) {
            SomeObjectThatExtendsAClassThatImplementsTheDeepLinkHandler.handleDeepLink(
                context,
                any(),
            )
        }
    }

    @Test
    fun testJavaHandlerCallFloatArgumentIntegerValueInUrl() {
        mockkStatic(SampleJavaStaticTestHelper::class)
        val intent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://airbnb.com/java/1/pathData2/5/true?queryParam=17"),
            )
        val context = Robolectric.buildActivity(DeepLinkActivity::class.java, intent).create().get()

        val expectedDeepLinkParams =
            TestJavaDeepLinkHandlerDeepLinkArgs(1, "pathData2", 5f, true, null, 17)
        verify(exactly = 1) {
            SampleJavaStaticTestHelper.invokedHandler(
                context,
                expectedDeepLinkParams,
            )
        }
    }
}
