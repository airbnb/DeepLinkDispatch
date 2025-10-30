package com.airbnb.deeplinkdispatch

import androidx.room.compiler.processing.XTypeElement
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.Test
import java.net.MalformedURLException

class DeepLinkAnnotatedElementTest {
    private var element = mockk<XTypeElement>()

    @Test
    @Throws(MalformedURLException::class)
    fun testValidUri() {
        val dlElement =
            DeepLinkAnnotatedElement.ActivityAnnotatedElement(
                uri = "airbnb://example.com/{foo}/bar",
                element = element,
            )
        Assertions.assertThat(dlElement.uri).isEqualTo("airbnb://example.com/{foo}/bar")
    }

    @Test
    @Throws(MalformedURLException::class)
    fun testQueryParam() {
        val dlElement =
            DeepLinkAnnotatedElement.ActivityAnnotatedElement(
                uri = "airbnb://classDeepLink?foo=bar",
                element = element,
            )
        Assertions.assertThat(dlElement.uri).isEqualTo("airbnb://classDeepLink?foo=bar")
    }

    @Test
    fun testInvalidUri() {
        try {
            DeepLinkAnnotatedElement.ActivityAnnotatedElement("http", element)
            Assert.fail()
        } catch (ignored: MalformedURLException) {
        }
    }

    @Test
    fun testMissingScheme() {
        try {
            DeepLinkAnnotatedElement.ActivityAnnotatedElement("example.com/something", element)
            Assert.fail()
        } catch (ignored: MalformedURLException) {
        }
    }

    @Test
    @Throws(MalformedURLException::class)
    fun testPlaceholderInScheme() {
        val dlElement =
            DeepLinkAnnotatedElement.ActivityAnnotatedElement(
                "http{scheme}://example.com/{foo}/bar",
                element,
            )
        Assertions.assertThat(dlElement.uri).isEqualTo("http{scheme}://example.com/{foo}/bar")
    }

    @Test
    @Throws(MalformedURLException::class)
    fun testPlaceholderInHost() {
        val dlElement =
            DeepLinkAnnotatedElement.ActivityAnnotatedElement(
                "http://{host}example.com/{foo}/bar",
                element,
            )
        Assertions.assertThat(dlElement.uri).isEqualTo("http://{host}example.com/{foo}/bar")
    }
}
