package com.airbnb.deeplinkdispatch.sample.benchmark

import android.content.Intent
import android.content.res.AssetManager
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.airbnb.deeplinkdispatch.BaseDeepLinkDelegate
import com.airbnb.deeplinkdispatch.BaseRegistry
import com.airbnb.deeplinkdispatch.DeepLinkMatchResult
import com.airbnb.deeplinkdispatch.DeepLinkResult
import com.airbnb.deeplinkdispatch.DeepLinkUri
import com.airbnb.deeplinkdispatch.sample.benchmarkable.BenchmarkDeepLinkModuleRegistry
import com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val DEEPLINK_1 = "dld://methodDeepLink1/test1234"
private const val DEEPLINK_500 = "dld://methodDeepLink500/test1234"
private const val DEEPLINK_1000 = "dld://methodDeepLink1000/test1234"
private const val DEEPLINK_1500 = "dld://methodDeepLink1500/test1234"
private const val DEEPLINK_2000 = "dld://methodDeepLink2000/test1234"

/**
 * Benchmark, which will execute on an Android device. Measured values are depending on actual
 * device used.
 *
 * The body of [BenchmarkRule.measureRepeated] is measured in a loop, and Studio will
 * output the result. When running this test via `./gradlew sample-benchmark:connectedCheck`
 * (with a device connected), the outoput can be also be found in
 * `sample-benchmark/build/outputs/connected_android_test_additional_output/`.
 */
@RunWith(AndroidJUnit4::class)
class DeeplinkBenchmarks {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val activityRule = ActivityTestRule(ScaleTestActivity::class.java)

    private val assetManager: AssetManager
        get() = InstrumentationRegistry.getInstrumentation().targetContext.assets

    @Test
    fun newRegistry() {
        val assets = assetManager
        benchmarkRule.measureRepeated {
            BenchmarkDeepLinkModuleRegistry(assets)
        }
    }

    @Test
    fun parseDeeplinkUrl() {
        benchmarkRule.measureRepeated {
            DeepLinkUri.parse(DEEPLINK_1)
        }
    }

    @Test
    fun match1() {
        Assert.assertNotNull(testMatch(DeepLinkUri.parse(DEEPLINK_1)))
    }

    @Test
    fun match500() {
        Assert.assertNotNull(testMatch(DeepLinkUri.parse(DEEPLINK_500)))
    }

    @Test
    fun match1000() {
        Assert.assertNotNull(testMatch(DeepLinkUri.parse(DEEPLINK_1000)))
    }

    @Test
    fun match1500() {
        Assert.assertNotNull(testMatch(DeepLinkUri.parse(DEEPLINK_1500)))
    }

    @Test
    fun match2000() {
        Assert.assertNotNull(testMatch(DeepLinkUri.parse(DEEPLINK_2000)))
    }

    /**
     * Note: This contains a call to match for DEEPLINK_1
     */
    @Test
    fun createResultDeeplink1() {
        val delegate = DeepLinkDelegate(assetManager)
        val intent = intent(DEEPLINK_1)
        val entry = entry(DEEPLINK_1)
        val activity = activityRule.activity
        var result: DeepLinkResult? = null
        benchmarkRule.measureRepeated {
            result = delegate.createResult(activity, intent, entry)
        }
        Assert.assertEquals("", result?.error)
    }

    fun registry() = BenchmarkDeepLinkModuleRegistry(assetManager)

    fun intent(uri: String): Intent {
        val intent = Intent.parseUri(DEEPLINK_1, 0)
        intent.setAction(Intent.ACTION_VIEW)
        return intent
    }

    fun entry(uri: String): DeepLinkMatchResult? = registry().idxMatch(DeepLinkUri.parse(uri))

    fun testMatch(uri: DeepLinkUri): DeepLinkMatchResult? {
        var result: DeepLinkMatchResult? = null
        val registry = registry()
        benchmarkRule.measureRepeated {
            result = registry.idxMatch(uri)
        }
        return result
    }
}

class DeepLinkDelegate(
    assetManager: AssetManager,
) : BaseDeepLinkDelegate(listOf<BaseRegistry>(BenchmarkDeepLinkModuleRegistry(assetManager)))
