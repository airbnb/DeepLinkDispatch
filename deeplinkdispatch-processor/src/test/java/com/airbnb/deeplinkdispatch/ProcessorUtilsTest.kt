package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.ProcessorUtils.decapitalizeIfNotTwoFirstCharsUpperCase
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ProcessorUtilsTest {

    @Test
    fun testDecapitalize() {
        assertThat("test".decapitalizeIfNotTwoFirstCharsUpperCase()).isEqualTo("test")
        assertThat("Test".decapitalizeIfNotTwoFirstCharsUpperCase()).isEqualTo("test")
        assertThat("TEst".decapitalizeIfNotTwoFirstCharsUpperCase()).isEqualTo("TEst")
        assertThat("tEst".decapitalizeIfNotTwoFirstCharsUpperCase()).isEqualTo("tEst")
        assertThat("T".decapitalizeIfNotTwoFirstCharsUpperCase()).isEqualTo("t")
    }
}
