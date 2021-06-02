package com.airbnb.deeplinkdispatch

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito

@kotlin.ExperimentalUnsignedTypes
class BaseDeepLinkDelegateTest {
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
        val activity = Mockito.mock(Activity::class.java)
        Mockito.`when`(activity.intent)
                .thenReturn(null)
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
        val activity = Mockito.mock(Activity::class.java)
        Mockito.`when`(activity.intent)
                .thenReturn(null)
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
        val activity = Mockito.mock(Activity::class.java)
        Mockito.`when`(activity.intent)
                .thenReturn(null)
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
        val intent = Mockito.mock(Intent::class.java)
        Mockito.`when`(intent.data)
                .thenReturn(null)
        val activity = Mockito.mock(Activity::class.java)
        Mockito.`when`(activity.intent)
                .thenReturn(intent)
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
        val uri = Mockito.mock(Uri::class.java)
        Mockito.`when`(uri.toString())
                .thenReturn(matchUrl)
        val intent = Mockito.mock(Intent::class.java)
        Mockito.`when`(intent.data)
                .thenReturn(uri)
        val appContext = Mockito.mock(Context::class.java)
        val activity = Mockito.mock(Activity::class.java)
        Mockito.`when`(activity.intent)
                .thenReturn(intent)
        Mockito.`when`(activity.applicationContext)
                .thenReturn(appContext)
        val errorHandler = TestErrorHandler()
        val testDelegate = getTwoRegistriesTestDelegate(listOf(entry), listOf(entry), errorHandler)
        val (_, _, _, match) = testDelegate.dispatchFrom(activity, intent)
        assertThat(errorHandler.duplicatedMatchCalled()).isTrue
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
        val uri = Mockito.mock(Uri::class.java)
        Mockito.`when`(uri.toString())
                .thenReturn(matchUrl)
        val intent = Mockito.mock(Intent::class.java)
        Mockito.`when`(intent.data)
                .thenReturn(uri)
        val appContext = Mockito.mock(Context::class.java)
        val activity = Mockito.mock(Activity::class.java)
        Mockito.`when`(activity.intent)
                .thenReturn(intent)
        Mockito.`when`(activity.applicationContext)
                .thenReturn(appContext)
        val errorHandler = TestErrorHandler()
        val testDelegate = getTwoRegistriesTestDelegate(listOf(entry1), listOf(entry2), errorHandler)
        val (_, _, _, deepLinkEntry) = testDelegate.dispatchFrom(activity, intent)
        assertThat(errorHandler.duplicatedMatchCalled()).isFalse
        assertThat(deepLinkEntry!!.equals(entry2))
    }

    private class TestDeepLinkRegistry(registry: List<DeepLinkEntry>) : BaseRegistry(getSearchIndex(registry), arrayOf()) {
        companion object {
            private fun getSearchIndex(deepLinkEntries: List<DeepLinkEntry>): ByteArray {
                val trieRoot = Root()
                for (entry in deepLinkEntries) {
                    trieRoot.addToTrie(entry.uriTemplate, entry.className, entry.method)
                }
                return trieRoot.toUByteArray().toByteArray()
            }
        }
    }

    private class TestErrorHandler : ErrorHandler {
        var duplicatedMatches: List<DeepLinkMatchResult>? = null
        var duplicateMatchCalled = false
        var uriString: String? = null
            private set

        fun duplicatedMatchCalled(): Boolean {
            return duplicateMatchCalled
        }

        override fun duplicateMatch(uriString: String, duplicatedMatches: List<DeepLinkMatchResult>) {
            this.uriString = uriString
            this.duplicatedMatches = duplicatedMatches
            duplicateMatchCalled = true
        }
    }

    private class TestDeepLinkDelegate(registries: List<BaseRegistry?>?, errorHandler: ErrorHandler?) : BaseDeepLinkDelegate(registries, errorHandler)
    companion object {
        private fun deepLinkEntry(uri: String): DeepLinkEntry {
            return DeepLinkEntry(uri, Any::class.java.name, null)
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
