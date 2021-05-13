package com.airbnb.deeplinkdispatch.sample

import com.airbnb.deeplinkdispatch.sample.benchmarkable.BenchmarkDeepLinkModuleRegistry
import com.airbnb.deeplinkdispatch.sample.library.LibraryDeepLinkModuleRegistry
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [21], manifest = "../sample/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner::class)
class ConfigurablePathSegmentTest {

    private val configurablePathSegmentReplacementsAllEmpty = mapOf(
            "configurable-path-segment" to "",
            "configurable-path-segment-one" to "",
            "configurable-path-segment-two" to ""
    )

    private val configurablePathSegmentReplacementFirstSet = mapOf(
            "configurable-path-segment" to "foo",
            "configurable-path-segment-one" to "",
            "configurable-path-segment-two" to ""
    )

    private val configurablePathSegmentReplacementsOneEmpty = mapOf(
            "configurable-path-segment" to "",
            "configurable-path-segment-one" to "",
            "configurable-path-segment-two" to "bar"
    )

    private val configurablePathSegmentReplacementsTwoEmpty = mapOf(
            "configurable-path-segment" to "",
            "configurable-path-segment-one" to "bar",
            "configurable-path-segment-two" to ""
    )

    @Test
    fun testMatchLinkwithNoPathSegmentsButOverlappingEmptyConfigurablePathSegments() {
        val deepLinkDelegate = DeepLinkDelegate(SampleModuleRegistry(), LibraryDeepLinkModuleRegistry(), BenchmarkDeepLinkModuleRegistry(), configurablePathSegmentReplacementsAllEmpty)
        val oneEmptyReplacementMatches = deepLinkDelegate.supportsUri("https://www.example.com/nothing-special")
        assertThat(oneEmptyReplacementMatches, equalTo<Boolean>(true))
    }

    @Test
    fun testReplaceableLastNonEmptyMatches() {
        // "https://www.example.com/cereal/<configurable-path-segment>"
        val deepLinkDelegate = DeepLinkDelegate(SampleModuleRegistry(), LibraryDeepLinkModuleRegistry(), BenchmarkDeepLinkModuleRegistry(), configurablePathSegmentReplacementFirstSet)
        val oneEmptyReplacementMatches = deepLinkDelegate.supportsUri("https://www.example.com/cereal/foo")
        assertThat(oneEmptyReplacementMatches, equalTo<Boolean>(true))
    }

    @Test
    fun testOneEmptyReplacementMatches() {
        val deepLinkDelegate = DeepLinkDelegate(SampleModuleRegistry(), LibraryDeepLinkModuleRegistry(), BenchmarkDeepLinkModuleRegistry(), configurablePathSegmentReplacementsAllEmpty)
        val oneEmptyReplacementMatches = deepLinkDelegate.supportsUri("https://www.example.com/bar?q=e")
        assertThat(oneEmptyReplacementMatches, equalTo<Boolean>(true))
    }

    @Test
    fun testOtherPathElememntsBeforeAndAfterDeletionDontMatch() {
        val deepLinkDelegate = DeepLinkDelegate(SampleModuleRegistry(), LibraryDeepLinkModuleRegistry(), BenchmarkDeepLinkModuleRegistry(), configurablePathSegmentReplacementsAllEmpty)
        val somePathElementBefore = deepLinkDelegate.supportsUri("https://www.example.com/somevalue/bar/?q=e")
        val somePathElementAfter = deepLinkDelegate.supportsUri("https://www.example.com/bar/somevalue/?q=e")
        assertThat(somePathElementBefore, equalTo(false))
        assertThat(somePathElementAfter, equalTo(false))
    }

    @Test
    fun testTwoEmptyReplacementMatches() {
        val deepLinkDelegate = DeepLinkDelegate(SampleModuleRegistry(), LibraryDeepLinkModuleRegistry(), BenchmarkDeepLinkModuleRegistry(), configurablePathSegmentReplacementsAllEmpty)
        val oneEmptyReplacementMatches = deepLinkDelegate.supportsUri("https://www.example.com/foo?q=e")
        assertThat(oneEmptyReplacementMatches, equalTo<Boolean>(true))
    }

    @Test
    fun testFirstOneEmpty() {
        val deepLinkDelegate = DeepLinkDelegate(SampleModuleRegistry(), LibraryDeepLinkModuleRegistry(), BenchmarkDeepLinkModuleRegistry(), configurablePathSegmentReplacementsOneEmpty)
        val oneEmptyReplacementMatches = deepLinkDelegate.supportsUri("https://www.example.com/bar/foo?q=e")
        assertThat(oneEmptyReplacementMatches, equalTo<Boolean>(true))
    }

    @Test
    fun testSecondOneEmpty() {
        val deepLinkDelegate = DeepLinkDelegate(SampleModuleRegistry(), LibraryDeepLinkModuleRegistry(), BenchmarkDeepLinkModuleRegistry(), configurablePathSegmentReplacementsTwoEmpty)
        val oneEmptyReplacementMatches = deepLinkDelegate.supportsUri("https://www.example.com/bar/foo?q=e")
        assertThat(oneEmptyReplacementMatches, equalTo<Boolean>(true))
    }
}