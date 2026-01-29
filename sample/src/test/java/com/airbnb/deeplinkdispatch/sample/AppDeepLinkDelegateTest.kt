package com.airbnb.deeplinkdispatch.sample

import android.util.Log
import com.airbnb.deeplinkdispatch.BaseRegistry
import com.airbnb.deeplinkdispatch.DeepLinkEntry
import com.airbnb.deeplinkdispatch.Root
import com.airbnb.deeplinkdispatch.allDeepLinkEntries
import com.airbnb.deeplinkdispatch.duplicatedDeepLinkEntries
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
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@Config(sdk = [21])
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
                BenchmarkDeepLinkModuleRegistry(RuntimeEnvironment.getApplication().assets),
                KaptLibraryDeepLinkModuleRegistry(),
                KspLibraryDeepLinkModuleRegistry(RuntimeEnvironment.getApplication().assets),
                configurablePlaceholdersMap,
            )
        // The duplicate detection finds:
        // - dld://host/intent/{abc}, {def}, {geh} - these 3 wildcards match each other
        //   (Note: dld://host/intent/null is NOT a duplicate because it's concrete and has
        //    higher matching priority than the wildcards)
        // - Entries with different concreteness are NOT flagged as duplicates because
        //   the matching algorithm will correctly pick the more concrete one
        MatcherAssert.assertThat(
            deepLinkDelegate.duplicatedDeepLinkEntries.size,
            IsEqual.equalTo(3),
        )
        MatcherAssert.assertThat(deepLinkDelegate.allDeepLinkEntries.any { it.uriTemplate == "dld://host/intent/{abc}" }, Is.`is`(true))
        MatcherAssert.assertThat(deepLinkDelegate.allDeepLinkEntries.any { it.uriTemplate == "dld://host/intent/{def}" }, Is.`is`(true))
        MatcherAssert.assertThat(deepLinkDelegate.allDeepLinkEntries.any { it.uriTemplate == "dld://host/intent/{geh}" }, Is.`is`(true))
    }

    /**
     * Performance test for duplicate detection with a massive amount of deeplinks (~40k entries).
     *
     * This test creates deeplinks in two ways:
     * 1. Using placeholders with allowed values to generate many expanded entries
     *    e.g., "app://host/{type(a|b|c|...)}" expands to many concrete values
     * 2. Creating many distinct deeplink templates
     *
     * The goal is to assess performance of the duplication check algorithm.
     *
     * Entry count breakdown for ~40k raw DeepLinkEntry objects:
     * - 1000 entries with small expansions (section deeplinks)
     * - 2000 entries with moderate expansions
     * - 35000 concrete deeplinks (most common in real apps)
     * - 2000 entries with single placeholder (wildcard match)
     * - Plus some high-expansion entries to stress the comparison algorithm
     */
    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun testDuplicatedEntriesPerformance() {
        // Generate allowed values strings for placeholders
        val alphabet = ('a'..'z').toList()

        // Create a large list of deeplink entries
        val entries = mutableListOf<DeepLinkEntry>()

        // ========== HIGH EXPANSION ENTRIES (stress the allPossibleValues comparison) ==========

        // Strategy 1: Deeplinks with placeholders containing many allowed values
        // Each of these will expand to 26 * 26 = 676 possible URL matches
        // 50 such entries = 50 * 676 = ~33,800 expanded entries
        val allowedValuesType1 = alphabet.joinToString("|")
        val allowedValuesType2 = alphabet.joinToString("|") { "${it}1" }
        for (i in 0 until 50) {
            entries.add(
                DeepLinkEntry.ActivityDeeplinkEntry(
                    "perftest://expanded/section$i/{type($allowedValuesType1)}/{category($allowedValuesType2)}",
                    "com.test.Activity$i",
                ),
            )
        }

        // Strategy 2: Deeplinks with large placeholder expansions
        // 50 values * 50 values = 2,500 expanded entries per template
        // 20 such templates = 50,000 expanded entries
        val largeAllowedValues1 = (0 until 50).joinToString("|") { "v$it" }
        val largeAllowedValues2 = (0 until 50).joinToString("|") { "c$it" }
        for (i in 0 until 20) {
            entries.add(
                DeepLinkEntry.ActivityDeeplinkEntry(
                    "perftest://large/path$i/{value($largeAllowedValues1)}/{cat($largeAllowedValues2)}",
                    "com.test.LargeActivity$i",
                ),
            )
        }

        // Strategy 3: Triple placeholder deeplinks for even more expansion
        // 10 * 10 * 10 = 1,000 expanded entries per template
        // 30 such templates = 30,000 expanded entries
        val tripleAllowedValues = (0 until 10).joinToString("|") { "t$it" }
        for (i in 0 until 30) {
            entries.add(
                DeepLinkEntry.ActivityDeeplinkEntry(
                    "perftest://triple/seg$i/{a($tripleAllowedValues)}/{b($tripleAllowedValues)}/{c($tripleAllowedValues)}",
                    "com.test.TripleActivity$i",
                ),
            )
        }

        // ========== MODERATE EXPANSION ENTRIES ==========

        // Strategy 4: More deeplinks with moderate placeholder expansions
        // 15 values * 15 values = 225 expanded entries per template
        // 100 such templates = 22,500 expanded entries
        val moderateAllowedValues1 = (0 until 15).joinToString("|") { "m$it" }
        val moderateAllowedValues2 = (0 until 15).joinToString("|") { "n$it" }
        for (i in 0 until 100) {
            entries.add(
                DeepLinkEntry.ActivityDeeplinkEntry(
                    "perftest://moderate/area$i/{first($moderateAllowedValues1)}/{second($moderateAllowedValues2)}",
                    "com.test.ModerateActivity$i",
                ),
            )
        }

        // ========== BULK RAW ENTRIES (to reach ~40k raw entries) ==========

        // Strategy 5: Many distinct simple deeplinks with single placeholders
        // These each expand to just 1 entry (the .* wildcard)
        // Adding 2000 such entries
        for (i in 0 until 2000) {
            entries.add(
                DeepLinkEntry.ActivityDeeplinkEntry(
                    "perftest://simple/route$i/{id}",
                    "com.test.SimpleActivity$i",
                ),
            )
        }

        // Strategy 6: Concrete deeplinks (no placeholders) - these are fast to compare
        // This is the bulk of entries to reach ~40k total
        // 35000 concrete entries
        for (i in 0 until 35000) {
            entries.add(
                DeepLinkEntry.ActivityDeeplinkEntry(
                    "perftest://concrete/fixed/path$i",
                    "com.test.ConcreteActivity$i",
                ),
            )
        }

        // Strategy 7: Method deeplinks with various paths
        // 3000 method deeplinks
        for (i in 0 until 3000) {
            entries.add(
                DeepLinkEntry.MethodDeeplinkEntry(
                    "perftest://method/action$i/{param}",
                    "com.test.MethodClass$i",
                    "handleDeepLink",
                ),
            )
        }

        // Create a test registry with all entries
        val registry = TestRegistry(entries)

        // Log the total entry count before running duplicate detection
        val allEntries = listOf(registry).allDeepLinkEntries()
        println("Total deeplink entries: ${allEntries.size}")
        Log.d("PerfTest", "Total deeplink entries: ${allEntries.size}")

        // Calculate approximate expanded entry count for reference
        val approxExpandedCount =
            50 * 676 + // Strategy 1
                20 * 2500 + // Strategy 2
                30 * 1000 + // Strategy 3
                100 * 225 + // Strategy 4
                2000 + // Strategy 5 (wildcards)
                35000 + // Strategy 6 (concrete)
                3000 // Strategy 7 (wildcards)
        println("Approximate expanded entry count: $approxExpandedCount")

        // Measure time for duplicate detection
        val startTime = System.currentTimeMillis()
        val duplicates = listOf(registry).duplicatedDeepLinkEntries()
        val endTime = System.currentTimeMillis()

        println("Duplicate detection completed in ${endTime - startTime}ms")
        println("Found ${duplicates.size} entries with potential duplicates")
        Log.d("PerfTest", "Duplicate detection completed in ${endTime - startTime}ms")
        Log.d("PerfTest", "Found ${duplicates.size} entries with potential duplicates")

        // We expect around 40k raw entries
        MatcherAssert.assertThat(
            "Should have at least 40000 deeplink entries but was ${allEntries.size}",
            allEntries.size >= 40000,
            Is.`is`(true),
        )
    }

    /**
     * Test registry that can be created from a list of DeepLinkEntry objects
     */
    private class TestRegistry(
        entries: List<DeepLinkEntry>,
    ) : BaseRegistry(createMatchArray(entries), emptyArray()) {
        companion object {
            @OptIn(ExperimentalUnsignedTypes::class)
            private fun createMatchArray(entries: List<DeepLinkEntry>): ByteArray {
                val root = Root()
                entries.forEach { root.addToTrie(it) }
                return root.toUByteArray().toByteArray()
            }
        }
    }
}
