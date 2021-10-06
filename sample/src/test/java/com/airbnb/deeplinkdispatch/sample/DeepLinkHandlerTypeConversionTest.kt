package com.airbnb.deeplinkdispatch.sample

import android.content.Intent
import android.net.Uri
import android.os.Looper.getMainLooper
import com.airbnb.deeplinkdispatch.sample.typeconversion.ComparableColorDrawable
import com.airbnb.deeplinkdispatch.sample.typeconversion.TypeConversionErrorHandlerCustomTypeDeepLinkActivity
import com.airbnb.deeplinkdispatch.sample.typeconversion.TypeConversionTestArgs
import com.airbnb.deeplinkdispatch.sample.typeconversion.TypeConversionTestDeepLinkHandler
import com.airbnb.deeplinkdispatch.sample.typeconversion.TypeConversionTestParametrizedArgs
import com.airbnb.deeplinkdispatch.sample.typeconversion.TypeConversionTestWihtParametrizedTypeDeepLinkHandler
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.lang.NumberFormatException

@Config(sdk = [21], manifest = "../sample/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner::class)
class DeepLinkHandlerTypeConversionTest {

    @Test
    fun testCustomTypeConversion() {
        mockkObject(TypeConversionTestDeepLinkHandler)
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://testing.com/typeConversion/5/red")
        )
        val context = Robolectric.buildActivity(TypeConversionErrorHandlerCustomTypeDeepLinkActivity::class.java, intent).create().get()
        shadowOf(getMainLooper()).idle()
        val expectedDeepLinkParams =
            TypeConversionTestArgs(5, ComparableColorDrawable(0xff0000ff.toInt()))
        shadowOf(getMainLooper()).idle()
        verify(exactly = 1) {
            TypeConversionTestDeepLinkHandler.handleDeepLink(
                context,
                expectedDeepLinkParams
            )
        }
    }

    @Test
    fun testCustomTypeConversionUnknownColor() {
        mockkObject(TypeConversionTestDeepLinkHandler)
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://testing.com/typeConversion/5/pink")
        )
        val context = Robolectric.buildActivity(TypeConversionErrorHandlerCustomTypeDeepLinkActivity::class.java, intent).create().get()

        val expectedDeepLinkParams =
            TypeConversionTestArgs(5, ComparableColorDrawable(0xffffffff.toInt()))
        verify(exactly = 1) {
            TypeConversionTestDeepLinkHandler.handleDeepLink(
                context,
                expectedDeepLinkParams
            )
        }
    }

    @Test
    fun testCustomTypeConversionParametrizedType() {
        mockkObject(TypeConversionTestWihtParametrizedTypeDeepLinkHandler)
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://testing.com/typeConversionParameter/5/one,two,three")
        )
        val context = Robolectric.buildActivity(
            TypeConversionErrorHandlerCustomTypeDeepLinkActivity::class.java,
            intent
        ).create().get()

        val expectedDeepLinkParams =
            TypeConversionTestParametrizedArgs(5, listOf("one", "two", "three"))
        verify(exactly = 1) {
            TypeConversionTestWihtParametrizedTypeDeepLinkHandler.handleDeepLink(
                context,
                expectedDeepLinkParams
            )
        }
    }

    @Test(expected = NumberFormatException::class)
    fun testCustomThrowingTypeConversionErrorHandler() {
        mockkObject(TypeConversionTestDeepLinkHandler)
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://testing.com/typeConversion/no_number/red")
        )
        Robolectric.buildActivity(TypeConversionErrorHandlerCustomTypeDeepLinkActivity::class.java, intent).create().get()
    }
}
