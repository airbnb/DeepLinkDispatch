package com.airbnb.deeplinkdispatch.sample

import com.airbnb.deeplinkdispatch.sample.benchmarkable.BenchmarkDeepLinkModuleRegistry
import com.airbnb.deeplinkdispatch.sample.kaptlibrary.KaptLibraryDeepLinkModuleRegistry
import com.airbnb.deeplinkdispatch.sample.ksplibrary.KspLibraryDeepLinkModuleRegistry
import com.airbnb.deeplinkdispatch.sample.library.LibraryDeepLinkModuleRegistry
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.hamcrest.core.IsEqual
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [21], manifest = "../sample/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner::class)
class AppDeepLinkDelegateTest {
    // Demo test to find duplicate URLs across all modules
    @Test
    fun testDuplicatedEntries() {
        val configurablePlaceholdersMap =
            mapOf(
                "configPathOne" to "/somePathThree",
                "configurable-path-segment-one" to "",
                "configurable-path-segment" to "",
                "configurable-path-segment-two" to "",
                "configPathOne" to "/somePathOne",
            )
        val deepLinkDelegate =
            DeepLinkDelegate(
                SampleModuleRegistry(),
                LibraryDeepLinkModuleRegistry(),
                BenchmarkDeepLinkModuleRegistry(),
                KaptLibraryDeepLinkModuleRegistry(),
                KspLibraryDeepLinkModuleRegistry(),
                configurablePlaceholdersMap,
            )
        MatcherAssert.assertThat(
            deepLinkDelegate.duplicatedDeepLinkEntries.size,
            IsEqual.equalTo(3),
        )
        MatcherAssert.assertThat(deepLinkDelegate.allDeepLinkEntries.any { it.uriTemplate == "dld://host/intent/{abc}" }, Is.`is`(true))
        MatcherAssert.assertThat(deepLinkDelegate.allDeepLinkEntries.any { it.uriTemplate == "dld://host/intent/{def}" }, Is.`is`(true))
        MatcherAssert.assertThat(deepLinkDelegate.allDeepLinkEntries.any { it.uriTemplate == "dld://host/intent/{geh}" }, Is.`is`(true))
    }
}
