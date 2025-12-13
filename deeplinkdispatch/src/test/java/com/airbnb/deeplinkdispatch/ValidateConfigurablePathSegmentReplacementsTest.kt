package com.airbnb.deeplinkdispatch

import org.junit.Test

class ValidateConfigurablePathSegmentReplacementsTest {
    // Tests for empty and valid replacement values

    @Test
    fun testEmptyReplacementValueIsAllowed() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to ""),
        )
    }

    @Test
    fun testReplacementValueWithLeadingSlashIsAllowed() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/replacement"),
        )
    }

    @Test
    fun testReplacementValueWithOnlySlashIsAllowed() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/"),
        )
    }

    @Test
    fun testMixedValidReplacementsAllowed() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("key1", "key2", "key3"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf(
                "key1" to "",
                "key2" to "/validReplacement",
                "key3" to "/",
            ),
        )
    }

    @Test
    fun testEmptyRegistryWithValidReplacements() {
        val registry = TestRegistryWithPathSegmentKeys(emptyArray())
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("extra" to "/validReplacement"),
        )
    }

    @Test
    fun testMultipleRegistriesWithValidReplacements() {
        val registry1 = TestRegistryWithPathSegmentKeys(arrayOf("key1"))
        val registry2 = TestRegistryWithPathSegmentKeys(arrayOf("key2"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry1, registry2),
            mapOf(
                "key1" to "/replacement1",
                "key2" to "",
            ),
        )
    }

    // Tests for leading slash validation

    @Test(expected = IllegalArgumentException::class)
    fun testNonEmptyReplacementValueWithoutLeadingSlashThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "replacement"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testMultipleNonEmptyReplacementsWithoutLeadingSlashThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("key1", "key2"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("key1" to "value1", "key2" to "value2"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testMixedReplacementsWithOneInvalidThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("key1", "key2", "key3"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf(
                "key1" to "",
                "key2" to "/validReplacement",
                "key3" to "invalidWithoutSlash",
            ),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testEmptyRegistryWithInvalidReplacementThrows() {
        val registry = TestRegistryWithPathSegmentKeys(emptyArray())
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("extra" to "invalidWithoutSlash"),
        )
    }

    // Tests for URL path safe characters validation

    @Test
    fun testUrlPathSafeUnreservedCharsAllowed() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/abcABC123-._~"),
        )
    }

    @Test
    fun testUrlPathSafeSubDelimitersAllowed() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path!$&'()*+,;="),
        )
    }

    @Test
    fun testUrlPathSafeColonAndAtAllowed() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/user:pass@host"),
        )
    }

    @Test
    fun testUrlPathSafePercentEncodedAllowed() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path%20with%2Fencoded"),
        )
    }

    @Test
    fun testUrlPathSafeMultipleSegmentsAllowed() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path/to/resource"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathUnsafeSpaceThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path with space"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathUnsafeLeadingSpaceThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to " /path"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathUnsafeTrailingSpaceThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path "),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathUnsafeTabThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path\twith\ttab"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathUnsafeNewlineThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path\n"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathUnsafeBracketThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path[with]brackets"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathUnsafeCurlyBracesThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path{with}braces"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathUnsafeQuestionMarkThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path?query"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathUnsafeHashThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path#fragment"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathUnsafeBackslashThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path\\backslash"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathUnsafeDoubleQuoteThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path\"quote"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathUnsafeLessThanThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path<less"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathUnsafeGreaterThanThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path>greater"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathUnsafeCaretThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path^caret"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathUnsafeBacktickThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path`backtick"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathUnsafePipeThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path|pipe"),
        )
    }

    // Tests for invalid percent-encoding

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathInvalidPercentEncodingSingleCharThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path%2"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathInvalidPercentEncodingNoHexThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path%GG"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathInvalidPercentEncodingAtEndThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path%"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUrlPathInvalidPercentEncodingPartialHexThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path%2G"),
        )
    }

    @Test
    fun testUrlPathValidPercentEncodingMixedCaseAllowed() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("configurable"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf("configurable" to "/path%2f%2F%aB%Ab"),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testMultipleReplacementsWithOneUnsafeThrows() {
        val registry = TestRegistryWithPathSegmentKeys(arrayOf("key1", "key2"))
        validateConfigurablePathSegmentReplacements(
            listOf(registry),
            mapOf(
                "key1" to "/valid",
                "key2" to "/invalid path",
            ),
        )
    }

    private class TestRegistryWithPathSegmentKeys(
        pathSegmentReplacementKeys: Array<String>,
    ) : BaseRegistry(ByteArray(0), pathSegmentReplacementKeys)
}
