package com.airbnb.deeplinkdispatch.sample

import com.airbnb.deeplinkdispatch.sample.benchmarkable.BenchmarkDeepLinkModuleRegistry
import com.airbnb.deeplinkdispatch.sample.library.LibraryDeepLinkModuleRegistry
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.assertThat
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

    private val configurablePathSegmentReplacementsFirstEmpty = mapOf(
            "configurable-path-segment" to "",
            "configurable-path-segment-one" to "",
            "configurable-path-segment-two" to "bar"
    )

    private val configurablePathSegmentReplacementsLastEmpty = mapOf(
            "configurable-path-segment" to "",
            "configurable-path-segment-one" to "bar",
            "configurable-path-segment-two" to ""
    )

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
        val deepLinkDelegate = DeepLinkDelegate(SampleModuleRegistry(), LibraryDeepLinkModuleRegistry(), BenchmarkDeepLinkModuleRegistry(), configurablePathSegmentReplacementsFirstEmpty)
        val oneEmptyReplacementMatches = deepLinkDelegate.supportsUri("https://www.example.com/bar/foo?q=e")
        assertThat(oneEmptyReplacementMatches, equalTo<Boolean>(true))
    }

    @Test
    fun testSecondOneEmpty() {
        val deepLinkDelegate = DeepLinkDelegate(SampleModuleRegistry(), LibraryDeepLinkModuleRegistry(), BenchmarkDeepLinkModuleRegistry(), configurablePathSegmentReplacementsLastEmpty)
        val oneEmptyReplacementMatches = deepLinkDelegate.supportsUri("https://www.example.com/bar/foo?q=e")
        assertThat(oneEmptyReplacementMatches, equalTo<Boolean>(true))
    }
}