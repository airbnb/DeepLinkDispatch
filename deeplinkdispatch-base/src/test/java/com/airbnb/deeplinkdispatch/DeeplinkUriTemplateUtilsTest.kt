package com.airbnb.deeplinkdispatch

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class DeeplinkUriTemplateUtilsTest {
    @Test
    fun testGetPlaceholders() {
        val uriTemplate = "http{scheme(|s)}://{host_prefix(|de.|ro.|www.)}airbnb.com/guests/{num_guests}"
        val placeholders = uriTemplate.placeholders()
        assertNotNull(placeholders)
        assertEquals(3, placeholders!!.size)
        assertEquals("{scheme(|s)}", placeholders[0])
        assertEquals("{host_prefix(|de.|ro.|www.)}", placeholders[1])
        assertEquals("{num_guests}", placeholders[2])
    }

    @Test
    fun testGetPlaceholdersNoPlaceholders() {
        val uriTemplate = "https://www.airbnb.com/guests"
        val placeholders = uriTemplate.placeholders()
        assertNull(placeholders)
    }

    @Test
    fun `placeholderValues returns correct values when allowed values are present`() {
        val input = "{placeholder_name(allowed|values)}"
        val expected = listOf("allowed", "values")
        assertEquals(expected, input.placeholderValues())
    }

    @Test
    fun `placeholderValues returns "something" when no allowed values are present`() {
        val input = "{placeholder_name}"
        assertEquals(listOf("..*"), input.placeholderValues())
    }

    @Test
    fun `placeholderValues returns empty list when allowed values are empty`() {
        val input = "{placeholder_name()}"
        assertEquals(listOf(""), input.placeholderValues())
    }

    @Test
    fun `placeholderValues handles multiple allowed values correctly with one value being the empty string`() {
        val input = "{placeholder_name(|value1|value2|value3)}"
        val expected = listOf("", "value1", "value2", "value3")
        assertEquals(expected, input.placeholderValues())
    }

    @Test
    fun `placeholderValues handles multiple allowed values correctly`() {
        val input = "{placeholder_name(value1|value2|value3)}"
        val expected = listOf("value1", "value2", "value3")
        assertEquals(expected, input.placeholderValues())
    }

    @Test
    fun `placeholderValues handles single allowed value correctly`() {
        val input = "{placeholder_name(value)}"
        val expected = listOf("value")
        assertEquals(expected, input.placeholderValues())
    }

    @Test
    fun testAllPossibleValuesNoPlaceholder() {
        val input = "https"
        assertEquals(
            listOf(
                "https",
            ),
            input.allPossibleValues(),
        )
    }

    @Test
    fun testAllPossibleValuesWithSinglePlaceholder() {
        val input = "http{scheme(|s)}"
        assertEquals(
            listOf(
                "http",
                "https",
            ),
            input.allPossibleValues(),
        )
    }

    @Test
    fun testAllPossibleValuesWithMultiplePlaceholders() {
        val input = "http{scheme(|s)}://{host_prefix(|de.|ro.|www.)}airbnb.com/guests"
        assertEquals(
            listOf(
                "http://airbnb.com/guests",
                "http://de.airbnb.com/guests",
                "http://ro.airbnb.com/guests",
                "http://www.airbnb.com/guests",
                "https://airbnb.com/guests",
                "https://de.airbnb.com/guests",
                "https://ro.airbnb.com/guests",
                "https://www.airbnb.com/guests",
            ),
            input.allPossibleValues(),
        )
    }

    @Test
    fun testAllPossibleValuesWithMultiplePlaceholdersAndPlaceholderWithoutAllowedValues() {
        val input =
            "http{scheme(|s)}://{host_prefix(|de.|ro.|www.)}airbnb.com/guests/{number_of_guests}"
        assertEquals(
            listOf(
                "http://airbnb.com/guests/..*",
                "http://de.airbnb.com/guests/..*",
                "http://ro.airbnb.com/guests/..*",
                "http://www.airbnb.com/guests/..*",
                "https://airbnb.com/guests/..*",
                "https://de.airbnb.com/guests/..*",
                "https://ro.airbnb.com/guests/..*",
                "https://www.airbnb.com/guests/..*",
            ),
            input.allPossibleValues(),
        )
    }
}
