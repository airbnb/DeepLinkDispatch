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
}
