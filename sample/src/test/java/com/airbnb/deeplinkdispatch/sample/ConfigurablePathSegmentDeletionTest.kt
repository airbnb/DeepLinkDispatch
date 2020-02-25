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

    private val configurablePathSegmentReplacements = mapOf(
            "configurable-path-segment" to "",
            "configurable-path-segment-two" to "",
            "configurable-path-segment-three" to "three"
    )

    private val deepLinkDelegate = DeepLinkDelegate(SampleModuleRegistry(), LibraryDeepLinkModuleRegistry(), BenchmarkDeepLinkModuleRegistry(), configurablePathSegmentReplacements)

    @Test
    fun testTrailingDeletionMatches() {
        val trailingDeletionMatches = deepLinkDelegate.supportsUri("https://www.configure.com/me?q=e")
        assertThat(trailingDeletionMatches, equalTo<Boolean>(true))
    }

    @Test
    fun testTrailingDeletionExtraSlashNotMatches() {
        val trailingDeletionExtraSlashNotMatches = deepLinkDelegate.supportsUri("https://www.configure.com/me//?q=e")
        assertThat(trailingDeletionExtraSlashNotMatches, equalTo(false))
    }

    @Test
    fun testInterpolatedDeletionMatches() {
        val interpolatedDeletionMatches = deepLinkDelegate.supportsUri("https://www.example.com/bar")
        assertThat(interpolatedDeletionMatches, equalTo(true))
    }

    @Test
    fun testMultipleSegmentsDeletionMatches() {
        val complexDeletionMatches = deepLinkDelegate.supportsUri("https://www.example.com/three")
        assertThat(complexDeletionMatches, equalTo(true))
    }
}