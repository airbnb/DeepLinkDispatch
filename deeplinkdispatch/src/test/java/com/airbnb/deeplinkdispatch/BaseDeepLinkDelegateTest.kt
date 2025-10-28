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
    fun testFindMethodDeepLinkWrongMethod() {
        val uriTemplate = "airbnb://foo/{bar}"
        val uriString = "airbnb://foo/1"
        val uri = mockk<Uri>()
        every { uri.toString() } returns uriString
        val entry =
            methodDeepLinkEntry(
                uriTemplate,
                TestDeepLinkClassStatic::class.java.name,
                "wrongMethod",
            )
        val activityMock = mockk<Activity>()
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        val intent = mockk<Intent>(relaxed = true)
        every { intent.data } returns uri
        every { activityMock.intent } returns intent
        val result =
            testDelegate.createResult(activityMock, intent, testDelegate.findEntry(uriTemplate))
        assertThat(result.isSuccessful).isFalse
        assertThat(result.uriString).isEqualTo(uriString)
        assertThat(result.error).isEqualTo("Deep link to non-existent method: wrongMethod")
        assertThat(result.errorThrowable).isInstanceOf(BaseDeepLinkDelegate.DeeplLinkMethodError::class.java)
        assertThat(result.errorThrowable?.cause).isInstanceOf(NoSuchMethodException::class.java)
    }

    @Test
    fun testFindMethodDeepLinkethod() {
        val uriTemplate = "airbnb://foo/{bar}"
        val uriString = "airbnb://foo/1"
        val uri = mockk<Uri>()
        every { uri.toString() } returns uriString
        val entry =
            methodDeepLinkEntry(uriTemplate, TestDeepLinkClassStatic::class.java.name, "testMethod")
        val activityMock = mockk<Activity>(relaxed = true)
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        val intent = mockk<Intent>(relaxed = true)
        every { intent.data } returns uri
        every { activityMock.intent } returns intent
        val result =
            testDelegate.createResult(activityMock, intent, testDelegate.findEntry(uriTemplate))
        assertThat(result.isSuccessful).isTrue
        assertThat(result.uriString).isEqualTo(uriString)
        assertThat(result.error).isEqualTo("")
        assertThat(result.errorThrowable).isNull()
    }

    @Test
    fun testFindEntry() {
        val entry = activityDeepLinkEntry("airbnb://foo/{bar}")
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        val foundEntry = testDelegate.findEntry("airbnb://foo/1")
        assertThat(foundEntry).isNotNull
        assertThat(foundEntry?.deeplinkEntry).isEqualTo(entry)
        assertThat(foundEntry?.parameterMap).isEqualTo(
            parameterMap(
                "airbnb://foo/1",
                mapOf("bar" to "1"),
            ),
        )
        assertThat(testDelegate.findEntry("airbnb://bar/1")).isNull()
    }

    @Test
    fun testAllowedPlaceholderValueOrdering() {
        val entry = activityDeepLinkEntry("airbn{scheme(c|b|a)}://foo{host(1|3|2)}/{bar(someValue|someOtherValue)}")
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        val matchArrayDump =
            testDelegate.registries
                .singleOrNull()
                ?.matchIndex()
                ?.byteArray
                ?.let { String(it) } ?: ""
        assertThat(matchArrayDump).contains("(a|b|c)")
        assertThat(matchArrayDump).contains("(1|2|3)")
        assertThat(matchArrayDump).contains("(someOtherValue|someValue)")
    }

    @Test(expected = NullPointerException::class)
    @Suppress("UNREACHABLE_CODE")
    fun testDispatchNullActivity() {
        val entry = activityDeepLinkEntry("airbnb://foo/{bar}")
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        testDelegate.dispatchFrom(null!!)
    }

    @Test(expected = NullPointerException::class)
    @Suppress("UNREACHABLE_CODE")
    fun testDispatchNullActivityNullIntent() {
        val entry = activityDeepLinkEntry("airbnb://foo/{bar}")
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        testDelegate.dispatchFrom(null!!, null!!)
    }

    @Test(expected = NullPointerException::class)
    fun testDispatchNullIntent() {
        val entry = activityDeepLinkEntry("airbnb://foo/{bar}")
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        val activity = mockk<Activity>()
        every { activity.intent } returns null
        testDelegate.dispatchFrom(activity)
    }

    @Test(expected = NullPointerException::class)
    @Suppress("UNREACHABLE_CODE")
    fun testDispatchNonNullActivityNullIntent() {
        val entry = activityDeepLinkEntry("airbnb://foo/{bar}")
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        val activity = mockk<Activity>()
        every { activity.intent } returns null
        testDelegate.dispatchFrom(activity, null!!)
    }

    @Test(expected = NullPointerException::class)
    @Suppress("UNREACHABLE_CODE")
    fun testCreateResultAllNull() {
        val entry = activityDeepLinkEntry("airbnb://foo/{bar}")
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        testDelegate.createResult(null!!, null!!, null)
    }

    @Test(expected = NullPointerException::class)
    @Suppress("UNREACHABLE_CODE")
    fun testCreateResultNullIntent() {
        val entry = activityDeepLinkEntry("airbnb://foo/{bar}")
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        val activity = mockk<Activity>()
        every { activity.intent } returns null
        testDelegate.createResult(activity, null!!, null)
    }

    @Test
    fun testCreateResultAllNullData() {
        val entry = activityDeepLinkEntry("airbnb://foo/{bar}")
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        val intent = mockk<Intent>()
        every { intent.data } returns null
        val activity = mockk<Activity>()
        every { activity.intent } returns intent
        val result = testDelegate.createResult(activity, intent, null)
        assertThat(result).isEqualTo(
            DeepLinkResult(
                isSuccessful = false,
                error = "No Uri in given activity's intent.",
                deepLinkHandlerResult = null,
            ),
        )
    }

    @Test
    fun testErrorHandlerWithDuplicateMatch() {
        val deeplinkUrl = "airbnb://foo/{bar}"
        val matchUrl = "airbnb://foo/bar"
        val entry = activityDeepLinkEntry(deeplinkUrl)
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
        val (_, _, _, _, match) = testDelegate.dispatchFrom(activity, intent)
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
        val entry1 = activityDeepLinkEntry(deeplinkUrl1)
        val entry2 = activityDeepLinkEntry(deeplinkUrl2)
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
        val (_, _, _, _, deepLinkEntry) = testDelegate.dispatchFrom(activity, intent)
        assertThat(errorHandler.duplicateMatchCalled).isFalse
        assertThat(deepLinkEntry!!.equals(entry2))
    }

    @Test
    fun testMissingParameterValueReturnsEmptyString() {
        val uriTemplate = "airbnb://foo"
        val uri = mockk<Uri>()
        every { uri.toString() } returns "airbnb://foo?q"
        val entry = methodDeepLinkEntry(uriTemplate, TestDeepLinkClassStatic::class.java.name, "testMethod")
        val activityMock = mockk<Activity>(relaxed = true)
        val testDelegate = getOneRegistryTestDelegate(listOf(entry), null)
        val intent = mockk<Intent>(relaxed = true)
        every { intent.data } returns uri
        every { activityMock.intent } returns intent
        val result = testDelegate.createResult(activityMock, intent, testDelegate.findEntry(uriTemplate))
        assertThat(result.parameters).isEqualTo(mapOf("q" to ""))
    }

    private fun parameterMap(
        url: String,
        parameterMap: Map<String, String>,
    ) = mapOf(DeepLinkUri.parse(url) to parameterMap)

    private class DuplicatedMatchTestErrorHandler : ErrorHandler() {
        var className: String = ""
        var duplicatedMatches: List<DeepLinkMatchResult>? = null
        var duplicateMatchCalled: Boolean = false
        var uriString: String? = null

        override fun duplicateMatch(
            uriString: String,
            duplicatedMatches: List<DeepLinkMatchResult>,
        ) {
            this.uriString = uriString
            this.duplicatedMatches = duplicatedMatches
            duplicateMatchCalled = true
        }
    }

    private class TestDeepLinkDelegate(
        registries: List<BaseRegistry>,
        errorHandler: ErrorHandler?,
    ) : BaseDeepLinkDelegate(registries = registries, errorHandler = errorHandler)

    companion object {
        private fun activityDeepLinkEntry(
            uri: String,
            className: String = Any::class.java.name,
        ): DeepLinkEntry = DeepLinkEntry.ActivityDeeplinkEntry(uri, className)

        private fun methodDeepLinkEntry(
            uri: String,
            className: String,
            methodName: String,
        ): DeepLinkEntry.MethodDeeplinkEntry = DeepLinkEntry.MethodDeeplinkEntry(uri, className, methodName)

        private fun handlerDeepLinkEntry(
            uri: String,
            className: String,
            methodName: String,
        ): DeepLinkEntry.MethodDeeplinkEntry = DeepLinkEntry.MethodDeeplinkEntry(uri, className, methodName)

        private fun getTwoRegistriesTestDelegate(
            entriesFirstRegistry: List<DeepLinkEntry>,
            entriesSecondRegistry: List<DeepLinkEntry>,
            errorHandler: ErrorHandler,
        ): TestDeepLinkDelegate =
            TestDeepLinkDelegate(listOf(testRegistry(entriesFirstRegistry), testRegistry(entriesSecondRegistry)), errorHandler)

        private fun getOneRegistryTestDelegate(
            entries: List<DeepLinkEntry>,
            errorHandler: ErrorHandler?,
        ): TestDeepLinkDelegate = TestDeepLinkDelegate(listOf(testRegistry(entries)), errorHandler)
    }
}

object TestDeepLinkClassStatic {
    @DeepLink("airbnb://foo/{bar}")
    @JvmStatic
    fun testMethod(context: Context): Intent = Intent()
}
