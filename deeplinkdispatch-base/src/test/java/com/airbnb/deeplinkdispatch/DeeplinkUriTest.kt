package com.airbnb.deeplinkdispatch

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import org.junit.Test

class DeeplinkUriTest {

    @Test
    fun testParseDeeplinkUri() {
        val deeplink = DeepLinkUri.parse("http://www.example.com/path1/path2")
        assertNotNull(deeplink)
        assertEquals("http", deeplink.scheme())
        assertEquals("www.example.com", deeplink.host())
        assertEquals(listOf("path1", "path2"), deeplink.pathSegments())
    }

    @Test
    fun testParseTemplateAsDeepLinkUri() {
        val deeplinkUriTemplate = DeepLinkUri.parse("http{scheme_suffix}://www.example.{url_domain_suffix}/path1/path2")
        assertNull(deeplinkUriTemplate)
    }

    @Test
    fun testParseDeepLinkUriTemplate() {
        val deeplinkUriTemplate = DeepLinkUri.parseTemplate("http{scheme_suffix}://www.example.{url_domain_suffix}/path1/path2")
        assertNotNull(deeplinkUriTemplate)
        assertEquals("http{scheme_suffix}", deeplinkUriTemplate.scheme())
        assertEquals("www.example.{url_domain_suffix}", deeplinkUriTemplate.host())
        assertEquals(listOf("path1", "path2"), deeplinkUriTemplate.pathSegments())
    }

    @Test
    fun testParseDeepLinkUriTemplateWithOnlyPlaceholderAsHostAndScheme() {
        val deeplinkUriTemplate = DeepLinkUri.parseTemplate("{scheme}://{host}/path1/path2")
        assertNotNull(deeplinkUriTemplate)
        assertEquals("{scheme}", deeplinkUriTemplate.scheme())
        assertEquals("{host}", deeplinkUriTemplate.host())
        assertEquals(listOf("path1", "path2"), deeplinkUriTemplate.pathSegments())
    }

    @Test
    fun testParseDeepLinkUriTemplateWithOnlyPlaceholderAsHostAndSchemeWithInvalidSchemeCharInPlaceholder() {
        val deeplinkUriTemplate = DeepLinkUri.parseTemplate("{scheme_test}://{host}/path1/path2")
        assertNotNull(deeplinkUriTemplate)
        assertEquals("{scheme_test}", deeplinkUriTemplate.scheme())
        assertEquals("{host}", deeplinkUriTemplate.host())
        assertEquals(listOf("path1", "path2"), deeplinkUriTemplate.pathSegments())
    }
}
