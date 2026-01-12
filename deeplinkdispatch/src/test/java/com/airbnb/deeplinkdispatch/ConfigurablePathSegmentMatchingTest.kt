package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.Utils.toByteArrayMap
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Test for a bug where URLs with fewer path segments incorrectly match patterns
 * expecting more segments when configurablePathSegmentReplacements is non-empty.
 */
@kotlin.ExperimentalUnsignedTypes
class ConfigurablePathSegmentMatchingTest {
    /**
     * When configurablePathSegmentReplacements is non-empty, a URL with 4 path
     * parameter values should match a 4-parameter pattern, NOT a 5-parameter pattern.
     *
     * Bug behavior: The 5-param pattern matches with the last URL value ("v4")
     * duplicated into both placeholder_4 and placeholder_5 parameters.
     */
    @Test
    fun `URL with 4 values should match 4-param pattern not 5-param pattern`() {
        val pattern4Params = activityDeepLinkEntry("test://host/path/{placeholder_1}/{placeholder_2}/{placeholder_3}/{placeholder_6}")
        val pattern5Params =
            activityDeepLinkEntry("test://host/path/{placeholder_1}/{placeholder_2}/{placeholder_3}/{placeholder_4}/{placeholder_5}")

        val root = Root()
        root.addToTrie(pattern5Params)
        root.addToTrie(pattern4Params)
        val testRegistry = TestRegistry(root.toUByteArray().toByteArray())

        // URL with exactly 4 placeholder values
        val testUrl = "test://host/path/id123/details/amenities/add"

        // Non-empty configurablePathSegmentReplacements triggers the bug
        val pathSegmentReplacements = toByteArrayMap(mapOf("configurable_path_segment" to ""))

        val match = testRegistry.idxMatch(DeepLinkUri.parse(testUrl), pathSegmentReplacements)

        assertThat(match).isNotNull

        // Should match 4-param pattern
        assertThat(match!!.deeplinkEntry.uriTemplate)
            .describedAs("URL with 4 values should match 4-param pattern, not 5-param pattern")
            .isEqualTo(pattern4Params.uriTemplate)

        val parameters = match.getParameters(DeepLinkUri.parse(testUrl))

        // Should have exactly 4 parameters
        assertThat(parameters)
            .describedAs("Should have exactly 4 parameters")
            .hasSize(4)

        // Verify correct parameter values
        assertThat(parameters["placeholder_1"]).isEqualTo("id123")
        assertThat(parameters["placeholder_2"]).isEqualTo("details")
        assertThat(parameters["placeholder_3"]).isEqualTo("amenities")
        assertThat(parameters["placeholder_6"]).isEqualTo("add")

        // Should NOT have parameters from the 5-param pattern
        assertThat(parameters)
            .describedAs("Should not contain placeholder_4 from the 5-param pattern")
            .doesNotContainKey("placeholder_4")
        assertThat(parameters)
            .describedAs("Should not contain placeholder_5 from the 5-param pattern")
            .doesNotContainKey("placeholder_5")
    }

    companion object {
        private fun activityDeepLinkEntry(
            uriTemplate: String,
            className: String = "com.example.TestActivity",
        ): DeepLinkEntry = DeepLinkEntry.ActivityDeeplinkEntry(uriTemplate, className)
    }
}
