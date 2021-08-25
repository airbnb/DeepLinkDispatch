package com.airbnb.deeplinkdispatch

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

@kotlin.ExperimentalUnsignedTypes
class BaseDeepLinkDelegateTest {

    @Test
    fun testFindEntry() {
        val entry = deepLinkEntry("airbnb://foo/{bar}")
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        val foundEntry = testDelegate.findEntry("airbnb://foo/1")
        assertThat(foundEntry).isNotNull
        assertThat(foundEntry?.deeplinkEntry).isEqualTo(entry)
        assertThat(foundEntry?.parameterMap).isEqualTo(parameterMap(
            "airbnb://foo/1",
            mapOf("bar" to "1")
        ))
        assertThat( testDelegate.findEntry("airbnb://bar/1")).isNull()
    }

    @Test
    fun testDispatchNullActivity() {
        val entry = deepLinkEntry("airbnb://foo/{bar}")
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        var message: String? = null
        try {
            testDelegate.dispatchFrom(null)
        } catch (e: NullPointerException) {
            message = e.message
        }
        assertThat(message).isEqualTo("activity == null")
    }

    @Test
    fun testDispatchNullActivityNullIntent() {
        val entry = deepLinkEntry("airbnb://foo/{bar}")
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        var message: String? = null
        try {
            testDelegate.dispatchFrom(null, null)
        } catch (e: NullPointerException) {
            message = e.message
        }
        assertThat(message).isEqualTo("activity == null")
    }

    @Test
    fun testDispatchNullIntent() {
        val entry = deepLinkEntry("airbnb://foo/{bar}")
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        val activity = mockk<Activity>()
        every { activity.intent } returns null
        var message: String? = null
        try {
            testDelegate.dispatchFrom(activity)
        } catch (e: NullPointerException) {
            message = e.message
        }
        assertThat(message).isEqualTo("sourceIntent == null")
    }

    @Test
    fun testDispatchNonNullActivityNullIntent() {
        val entry = deepLinkEntry("airbnb://foo/{bar}")
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        val activity = mockk<Activity>()
        every { activity.intent } returns null
        var message: String? = null
        try {
            testDelegate.dispatchFrom(activity, null)
        } catch (e: NullPointerException) {
            message = e.message
        }
        assertThat(message).isEqualTo("sourceIntent == null")
    }

    @Test
    fun testCreateResultAllNull() {
        val entry = deepLinkEntry("airbnb://foo/{bar}")
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        var message: String? = null
        try {
            testDelegate.createResult(null, null, null)
        } catch (e: NullPointerException) {
            message = e.message
        }
        assertThat(message).isEqualTo("activity == null")
    }

    @Test
    fun testCreateResultNullIntent() {
        val entry = deepLinkEntry("airbnb://foo/{bar}")
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        val activity = mockk<Activity>()
        every { activity.intent } returns null
        var message: String? = null
        try {
            testDelegate.createResult(activity, null, null)
        } catch (e: NullPointerException) {
            message = e.message
        }
        assertThat(message).isEqualTo("sourceIntent == null")
    }

    @Test
    fun testCreateResultAllNullData() {
        val entry = deepLinkEntry("airbnb://foo/{bar}")
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        val intent = mockk<Intent>()
        every { intent.data } returns null
        val activity = mockk<Activity>()
        every { activity.intent } returns intent
        val result = testDelegate.createResult(activity, intent, null)
        assertThat(result).isEqualTo(DeepLinkResult(
            false, null, "No Uri in given activity's intent.", null, DeepLinkMethodResult(
                null,
                null
            )
        ))
    }

    @Test
    fun testErrorHandlerWithDuplicateMatch() {
        val deeplinkUrl = "airbnb://foo/{bar}"
        val matchUrl = "airbnb://foo/bar"
        val entry = deepLinkEntry(deeplinkUrl)
        val deeplinkMatchResult = DeepLinkMatchResult(entry, mapOf(DeepLinkUri.parse(matchUrl) to mapOf("bar" to "bar")))
        val uri = mockk<Uri>()
        every { uri.toString() } returns matchUrl
        val intent = mockk<Intent>(relaxed = true)
        every { intent.data } returns uri
        val appContext = mockk<Context>(relaxed = true)
        val activity = mockk<Activity>(relaxed = true)
        every { activity.intent } returns intent
        every { activity.applicationContext } returns appContext
        val errorHandler = DuplicatedMatchTestErrorHandler()
        val testDelegate = getTwoRegistriesTestDelegate(listOf(entry), listOf(entry), errorHandler)
        val (_, _, _, match) = testDelegate.dispatchFrom(activity, intent)
        assertThat(errorHandler.duplicateMatchCalled).isTrue
        assertThat(errorHandler.duplicatedMatches).isNotNull
        assertThat(errorHandler.duplicatedMatches!!.size).isEqualTo(2)
        assertThat(errorHandler.duplicatedMatches!![0]).isEqualTo(deeplinkMatchResult)
        assertThat(errorHandler.duplicatedMatches!![1]).isEqualTo(deeplinkMatchResult)
        assertThat(errorHandler.uriString).isEqualTo(matchUrl)
        assertThat(match).isEqualTo(deeplinkMatchResult)
    }

    @Test
    fun testErrorHandlerNotGettingCalled() {
        val deeplinkUrl1 = "airbnb://foo/{bar}"
        val deeplinkUrl2 = "airbnb://bar/{foo}"
        val matchUrl = "airbnb://bar/foo"
        val entry1 = deepLinkEntry(deeplinkUrl1)
        val entry2 = deepLinkEntry(deeplinkUrl2)
        val uri = mockk<Uri>()
        every { uri.toString() } returns matchUrl
        val intent = mockk<Intent>(relaxed = true)
        every { intent.data } returns uri
        val appContext = mockk<Context>(relaxed = true)
        val activity = mockk<Activity>(relaxed = true)
        every { activity.intent } returns intent
        every { activity.applicationContext } returns appContext
        val errorHandler = DuplicatedMatchTestErrorHandler()
        val testDelegate = getTwoRegistriesTestDelegate(listOf(entry1), listOf(entry2), errorHandler)
        val (_, _, _, deepLinkEntry) = testDelegate.dispatchFrom(activity, intent)
        assertThat(errorHandler.duplicateMatchCalled).isFalse
        assertThat(deepLinkEntry!!.equals(entry2))
    }

    private class TestDeepLinkRegistry(registry: List<DeepLinkEntry>) : BaseRegistry(getSearchIndex(registry), arrayOf()) {
        companion object {
            private fun getSearchIndex(deepLinkEntries: List<DeepLinkEntry>): ByteArray {
                val trieRoot = Root()
                for (entry in deepLinkEntries) {
                    trieRoot.addToTrie(entry.type, entry.uriTemplate, entry.className, entry.method)
                }
                return trieRoot.toUByteArray().toByteArray()
            }
        }
    }

    private fun parameterMap(
        url: String,
        parameterMap: Map<String, String>
    ) = mapOf(DeepLinkUri.parse(url) to parameterMap)

    private class DuplicatedMatchTestErrorHandler : ErrorHandler {
        var className: String = ""
        var duplicatedMatches: List<DeepLinkMatchResult>? = null
        var duplicateMatchCalled: Boolean = false
        var uriString: String? = null

        override fun duplicateMatch(uriString: String, duplicatedMatches: List<DeepLinkMatchResult>) {
            this.uriString = uriString
            this.duplicatedMatches = duplicatedMatches
            duplicateMatchCalled = true
        }
    }

    private class TestDeepLinkDelegate(registries: List<BaseRegistry?>?, errorHandler: ErrorHandler?) : BaseDeepLinkDelegate(registries, errorHandler)
    companion object {
        private fun deepLinkEntry(uri: String, type: MatchType = MatchType.Activity, className: String = Any::class.java.name): DeepLinkEntry {
            return DeepLinkEntry(type, uri, className, null)
        }

        /**
         * Helper method to get a class extending [BaseRegistry] acting as the delegate
         * for the
         *
         * @param deepLinkEntries
         * @return
         */
        private fun getTestRegistry(deepLinkEntries: List<DeepLinkEntry>): TestDeepLinkRegistry {
            return TestDeepLinkRegistry(deepLinkEntries)
        }

        private fun getTwoRegistriesTestDelegate(entriesFirstRegistry: List<DeepLinkEntry>, entriesSecondRegistry: List<DeepLinkEntry>, errorHandler: ErrorHandler): TestDeepLinkDelegate {
            return TestDeepLinkDelegate(listOf(getTestRegistry(entriesFirstRegistry), getTestRegistry(entriesSecondRegistry)), errorHandler)
        }

        private fun getOneRegistryTestDelegate(entries: List<DeepLinkEntry>, errorHandler: ErrorHandler?): TestDeepLinkDelegate {
            return TestDeepLinkDelegate(listOf(getTestRegistry(entries)), errorHandler)
        }
    }
}
