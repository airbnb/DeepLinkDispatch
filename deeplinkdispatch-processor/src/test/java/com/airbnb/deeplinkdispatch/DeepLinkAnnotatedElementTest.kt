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
    fun testValidUri() {
        val dlElement =
            DeepLinkAnnotatedElement.ActivityAnnotatedElement(
                uri = "airbnb://example.com/{foo}/bar",
                activityClassFqn = null,
                element = element,
            )
        Assertions.assertThat(dlElement.uriTemplate).isEqualTo("airbnb://example.com/{foo}/bar")
        Assertions.assertThat(dlElement.activityClassFqn).isNull()
    }

    @Test
    fun testValidUriWithActivityFqn() {
        val dlElement =
            DeepLinkAnnotatedElement.ActivityAnnotatedElement(
                uri = "airbnb://example.com/{foo}/bar",
                activityClassFqn = "com.airbnb.SomeDeepLinkActivity",
                element = element,
            )
        Assertions.assertThat(dlElement.uriTemplate).isEqualTo("airbnb://example.com/{foo}/bar")
        Assertions.assertThat(dlElement.activityClassFqn).isEqualTo("com.airbnb.SomeDeepLinkActivity")
    }

    @Test
    fun testQueryParam() {
        val dlElement =
            DeepLinkAnnotatedElement.ActivityAnnotatedElement(
                uri = "airbnb://classDeepLink?foo=bar",
                activityClassFqn = null,
                element = element,
            )
        Assertions.assertThat(dlElement.uriTemplate).isEqualTo("airbnb://classDeepLink?foo=bar")

    }

    @Test(expected = MalformedURLException::class)
    fun testInvalidUri() {
        DeepLinkAnnotatedElement.ActivityAnnotatedElement(
            uri = "http",
            activityClassFqn = null,
            element = element,
        )
        Assert.fail()
    }

    @Test(expected = MalformedURLException::class)
    fun testMissingScheme() {
        DeepLinkAnnotatedElement.ActivityAnnotatedElement(
            uri = "example.com/something",
            activityClassFqn = null,
            element = element,
        )
        Assert.fail()
    }

    @Test
    fun testPlaceholderInScheme() {
        val dlElement =
            DeepLinkAnnotatedElement.ActivityAnnotatedElement(
                uri = "http{scheme}://example.com/{foo}/bar",
                activityClassFqn = null,
                element = element,
            )
        Assertions.assertThat(dlElement.uriTemplate).isEqualTo("http{scheme}://example.com/{foo}/bar")
    }

    @Test
    fun testPlaceholderInHost() {
        val dlElement =
            DeepLinkAnnotatedElement.ActivityAnnotatedElement(
                uri = "http://{host}example.com/{foo}/bar",
                activityClassFqn = null,
                element = element,
            )
        Assertions.assertThat(dlElement.uriTemplate).isEqualTo("http://{host}example.com/{foo}/bar")
    }
}
