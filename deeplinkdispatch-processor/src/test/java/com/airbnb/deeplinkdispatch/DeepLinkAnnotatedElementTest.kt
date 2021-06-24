package com.airbnb.deeplinkdispatch

import androidx.room.compiler.processing.XTypeElement
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.Test
import java.net.MalformedURLException

class DeepLinkAnnotatedElementTest {

    var element = mockk<XTypeElement>()


    @Test
    @Throws(MalformedURLException::class)
    fun testValidUri() {
        val (uri) = DeepLinkAnnotatedElement(
            "airbnb://example.com/{foo}/bar", element
        )
        Assertions.assertThat(uri).isEqualTo("airbnb://example.com/{foo}/bar")
    }

    @Test
    @Throws(MalformedURLException::class)
    fun testQueryParam() {
        val (uri) = DeepLinkAnnotatedElement(
            "airbnb://classDeepLink?foo=bar", element
        )
        Assertions.assertThat(uri).isEqualTo("airbnb://classDeepLink?foo=bar")
    }

    @Test
    fun testInvalidUri() {
        try {
            DeepLinkAnnotatedElement("http", element)
            Assert.fail()
        } catch (ignored: MalformedURLException) {
        }
    }

    @Test
    fun testMissingScheme() {
        try {
            DeepLinkAnnotatedElement("example.com/something", element)
            Assert.fail()
        } catch (ignored: MalformedURLException) {
        }
    }

    @Test
    @Throws(MalformedURLException::class)
    fun testPlaceholderInScheme() {
        val (uri) = DeepLinkAnnotatedElement(
            "http{scheme}://example.com/{foo}/bar", element
        )
        Assertions.assertThat(uri).isEqualTo("http{scheme}://example.com/{foo}/bar")
    }

    @Test
    @Throws(MalformedURLException::class)
    fun testPlaceholderInHost() {
        val (uri) = DeepLinkAnnotatedElement(
            "http://{host}example.com/{foo}/bar", element
        )
        Assertions.assertThat(uri).isEqualTo("http://{host}example.com/{foo}/bar")
    }
}