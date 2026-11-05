package com.airbnb.deeplinkdispatch

import org.assertj.core.api.Assertions
import org.junit.Test

class UrlTreeTests {
    @Test
    fun testAllowedPlaceholderValueOrdering() {
        Assertions
            .assertThat("some_string{id(h|g|f|e|d|c|b|a)}".orderPlaceholderValues())
            .isEqualTo("some_string{id(a|b|c|d|e|f|g|h)}")
        Assertions
            .assertThat("some_string{id(last|first)}".orderPlaceholderValues())
            .isEqualTo("some_string{id(first|last)}")
        Assertions
            .assertThat("some_string{id(one)}".orderPlaceholderValues())
            .isEqualTo("some_string{id(one)}")
        Assertions
            .assertThat("some_string{id}".orderPlaceholderValues())
            .isEqualTo("some_string{id}")
    }

    @Test
    fun testWildcardsAreRejected() {
        listOf(
            "https://www.example.com/renewals/..*" to "..*",
            "https://www.example.com/renewals/.*" to ".*",
            "https://www.example.com/renewals/*" to "*",
            "https://www.example.com/renewals/opt-*/confirm" to "opt-*",
            "https://*.example.com/renewals" to "*.example.com",
        ).forEach { (template, component) ->
            Assertions
                .assertThatThrownBy { Root().addToTrie(entry(template)) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageStartingWith("Invalid URI component: $component. DeepLinkDispatch does not support wildcards")
        }
    }

    @Test
    fun testTemplatesWithoutWildcardsAreAccepted() {
        listOf(
            "https://www.example.com/renewals/{page}",
            "https://www.example.com/robots.txt",
            "https://www.example.com/<configurable>/renewals",
            "http{scheme(|s)}://{host_prefix(|www.)}example.com/renewals",
        ).forEach { template ->
            Root().addToTrie(entry(template))
        }
    }

    private fun entry(template: String) = DeepLinkEntry.ActivityDeeplinkEntry(template, "com.example.SampleActivity")
}
