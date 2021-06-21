package com.airbnb.deeplinkdispatch

import androidx.room.compiler.processing.XTypeElement
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.net.MalformedURLException

class DeepLinkAnnotatedElementTest {
    @Mock
    var element: XTypeElement? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    @Throws(MalformedURLException::class)
    fun testValidUri() {
        val uri = DeepLinkAnnotatedElement(
            "airbnb://example.com/{foo}/bar", element!!, DeepLinkEntry.Type.CLASS
        )
        Assertions.assertThat(uri).isEqualTo("airbnb://example.com/{foo}/bar")
    }

    @Test
    @Throws(MalformedURLException::class)
    fun testQueryParam() {
        val (uri) = DeepLinkAnnotatedElement(
            "airbnb://classDeepLink?foo=bar", element!!, DeepLinkEntry.Type.CLASS
        )
        Assertions.assertThat(uri).isEqualTo("airbnb://classDeepLink?foo=bar")
    }

    @Test
    fun testInvalidUri() {
        try {
            DeepLinkAnnotatedElement("http", element!!, DeepLinkEntry.Type.CLASS)
            Assert.fail()
        } catch (ignored: MalformedURLException) {
        }
    }

    @Test
    fun testMissingScheme() {
        try {
            DeepLinkAnnotatedElement("example.com/something", element!!, DeepLinkEntry.Type.CLASS)
            Assert.fail()
        } catch (ignored: MalformedURLException) {
        }
    }

    @Test
    @Throws(MalformedURLException::class)
    fun testPlaceholderInScheme() {
        val (uri) = DeepLinkAnnotatedElement(
            "http{scheme}://example.com/{foo}/bar", element!!, DeepLinkEntry.Type.CLASS
        )
        Assertions.assertThat(uri).isEqualTo("http{scheme}://example.com/{foo}/bar")
    }

    @Test
    @Throws(MalformedURLException::class)
    fun testPlaceholderInHost() {
        val (uri) = DeepLinkAnnotatedElement(
            "http://{host}example.com/{foo}/bar", element!!, DeepLinkEntry.Type.CLASS
        )
        Assertions.assertThat(uri).isEqualTo("http://{host}example.com/{foo}/bar")
    }
}