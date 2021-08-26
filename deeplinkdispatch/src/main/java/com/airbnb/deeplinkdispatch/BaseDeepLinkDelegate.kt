package com.airbnb.deeplinkdispatch

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.app.TaskStackBuilder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.airbnb.deeplinkdispatch.base.Utils.toByteArrayMap
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

open class BaseDeepLinkDelegate @JvmOverloads constructor(
    private val registries: List<BaseRegistry>,
    configurablePathSegmentReplacements: Map<String, String> = emptyMap(),
    private val errorHandler: ErrorHandler? = null
) {

    /**
     *
     * Mapping of values for DLD to substitute for annotation-declared configurablePathSegments.
     *
     *
     * Example
     * Given:
     *
     *  * <xmp>@DeepLink("https://www.example.com/<configurable-path-segment>/users/{param1}")
    </configurable-path-segment></xmp> *
     *  * mapOf("pathVariableReplacementValue" to "obamaOs")
     *
     * Then:
     *  * <xmp>https://www.example.com/obamaOs/users/{param1}</xmp> will match.
     */
    private val configurablePathSegmentReplacements: Map<ByteArray, ByteArray> =
        toByteArrayMap(configurablePathSegmentReplacements)

    init {
        validateConfigurablePathSegmentReplacements(
            registries,
            this.configurablePathSegmentReplacements
        )
    }

    /**
     * Calls [.createResult]. If the DeepLinkResult has
     * a non-null TaskStackBuilder or Intent, it starts it. It always calls
     * [.notifyListener].
     *
     * @param activity     used to startActivity() or notifyListener().
     * @param sourceIntent inbound Intent.
     * @return DeepLinkResult
     */
    @JvmOverloads
    fun dispatchFrom(
        activity: Activity,
        sourceIntent: Intent = activity.intent
    ): DeepLinkResult {
        val uri = sourceIntent.data
        val result = uri?.let {
            createResult(activity, sourceIntent, findEntry(uri.toString()))
        } ?: createResult(activity, sourceIntent, null)
        dispatchResult(
            result = result,
            activity = activity
        )
        notifyListener(
            context = activity,
            isError = !result.isSuccessful,
            uri = uri,
            uriTemplate = if (result.deepLinkMatchResult != null) result.deepLinkMatchResult.deeplinkEntry.uriTemplate else null,
            errorMessage = result.error
        )
        return result
    }

    private fun notifyListener(
        context: Context,
        isError: Boolean,
        uri: Uri?,
        uriTemplate: String?,
        errorMessage: String
    ) {
        val intent = Intent().apply {
            action = DeepLinkHandler.ACTION
            putExtra(DeepLinkHandler.EXTRA_URI, uri?.toString() ?: "")
            putExtra(DeepLinkHandler.EXTRA_URI_TEMPLATE, uriTemplate ?: "")
            putExtra(DeepLinkHandler.EXTRA_SUCCESSFUL, !isError)
            if (isError) {
                putExtra(DeepLinkHandler.EXTRA_ERROR_MESSAGE, errorMessage)
            }
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    private fun dispatchResult(
        result: DeepLinkResult,
        activity: Activity
    ) {
        result.deepLinkMatchResult?.let { matchedDeepLinkResult ->
            when (matchedDeepLinkResult.deeplinkEntry.type) {
                MatchType.Method -> result.methodResult.taskStackBuilder?.startActivities()
                    ?: activity.startActivity(result.methodResult.intent)
                MatchType.Activity -> activity.startActivity(result.methodResult.intent)
                MatchType.Handler -> TODO()
                /** Not implemented yet **/

            }
        }
    }

    /**
     * Create a [DeepLinkResult], whether we are able to match the uri on
     * {@param sourceIntent} or not.
     *
     * @param activity      used to startActivity() or notifyListener().
     * @param sourceIntent  inbound Intent.
     * @param deeplinkMatchResult deeplinkMatchResult that matches the sourceIntent's URI. Derived from
     * [.findEntry]. Can be injected for testing.
     * @return DeepLinkResult
     */
    fun createResult(
        activity: Activity,
        sourceIntent: Intent,
        deeplinkMatchResult: DeepLinkMatchResult?
    ): DeepLinkResult {
        val originalIntentUri = sourceIntent.data
            ?: return DeepLinkResult(
                isSuccessful = false,
                uriString = null,
                error = "No Uri in given activity's intent.",
                deepLinkMatchResult = deeplinkMatchResult,
                methodResult = DeepLinkMethodResult(null, null)
            )
        val deepLinkUri = DeepLinkUri.parse(originalIntentUri.toString())
        if (deeplinkMatchResult == null) {
            return DeepLinkResult(
                isSuccessful = false,
                uriString = null,
                error = "DeepLinkEntry cannot be null",
                deepLinkMatchResult = null,
                methodResult = DeepLinkMethodResult(null, null)
            )
        }
        val parameters = parameterBundle(
                deeplinkMatchResult = deeplinkMatchResult,
                deepLinkUri = deepLinkUri,
                originalIntentUri = originalIntentUri,
                sourceIntent = sourceIntent
            )
        return try {
            val intentAndTaskStackBuilderPair = intentAndTaskStackBuilderFromClass(
                matchedDeeplinkEntry = deeplinkMatchResult.deeplinkEntry,
                activity = activity,
                parameters = parameters
            )
            intentAndTaskStackBuilderPair.intent?.let { intent ->
                if (intent.action == null) {
                    intent.action = sourceIntent.action
                }
                if (intent.data == null) {
                    intent.data = sourceIntent.data
                }
                intent.putExtras(parameters)
                intent.putExtra(DeepLink.IS_DEEP_LINK, true)
                intent.putExtra(DeepLink.REFERRER_URI, originalIntentUri)
                if (activity.callingActivity != null) {
                    intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
                }
                DeepLinkResult(
                    isSuccessful = true,
                    uriString = originalIntentUri.toString(),
                    error = "",
                    deepLinkMatchResult = deeplinkMatchResult,
                    methodResult = DeepLinkMethodResult(
                        intent,
                        intentAndTaskStackBuilderPair.taskStackBuilder
                    )
                )
            } ?: return DeepLinkResult(
                isSuccessful = false,
                uriString = originalIntentUri.toString(),
                error = "Destination Intent is null!",
                deepLinkMatchResult = deeplinkMatchResult,
                methodResult = DeepLinkMethodResult(
                    intentAndTaskStackBuilderPair.intent,
                    intentAndTaskStackBuilderPair.taskStackBuilder
                )
            )
        } catch (taskStackError: TaskStackError) {
            return DeepLinkResult(
                isSuccessful = false,
                uriString = originalIntentUri.toString(),
                error = "Could not deep link to method: ${deeplinkMatchResult.deeplinkEntry.method} intents length == 0",
                deepLinkMatchResult = deeplinkMatchResult,
                methodResult = DeepLinkMethodResult(null, null)
            )
        } catch (exception: NoSuchMethodException) {
            DeepLinkResult(
                isSuccessful = false,
                uriString = originalIntentUri.toString(),
                error = "Deep link to non-existent method: ${deeplinkMatchResult.deeplinkEntry.method}",
                deepLinkMatchResult = deeplinkMatchResult,
                methodResult = DeepLinkMethodResult(null, null)
            )
        } catch (exception: IllegalAccessException) {
            DeepLinkResult(
                isSuccessful = false,
                uriString = originalIntentUri.toString(),
                error = "Could not deep link to method: ${deeplinkMatchResult.deeplinkEntry.method}",
                deepLinkMatchResult = deeplinkMatchResult,
                methodResult = DeepLinkMethodResult(null, null)
            )
        } catch (exception: InvocationTargetException) {
            DeepLinkResult(
                isSuccessful = false,
                uriString = originalIntentUri.toString(),
                error = "Could not deep link to method: ${deeplinkMatchResult.deeplinkEntry.method}",
                deepLinkMatchResult = deeplinkMatchResult,
                methodResult = DeepLinkMethodResult(null, null)
            )
        }
    }

    private fun intentAndTaskStackBuilderFromClass(
        matchedDeeplinkEntry: DeepLinkEntry,
        activity: Activity,
        parameters: Bundle
    ): IntentTaskStackBuilderPair {
        val clazz = matchedDeeplinkEntry.clazz
        return when (matchedDeeplinkEntry.type) {
            MatchType.Activity -> IntentTaskStackBuilderPair(Intent(activity, clazz), null)
            MatchType.Method -> {
                matchedDeeplinkEntry.method?.let { methodString ->
                    try {
                        val method = clazz.getMethod(methodString, Context::class.java)
                        intentFromDeeplinkMethod(method, method.invoke(clazz, activity))
                    } catch (exception: NoSuchMethodException) {
                        val method =
                            clazz.getMethod(methodString, Context::class.java, Bundle::class.java)
                        intentFromDeeplinkMethod(method, method.invoke(clazz, activity, parameters))
                    }
                } ?: IntentTaskStackBuilderPair(null, null)
            }
            MatchType.Handler -> IntentTaskStackBuilderPair(null, null) /** TODO: Implement **/
            else -> throw IllegalStateException("Unexpected value: " + matchedDeeplinkEntry.type)
        }
    }

    data class IntentTaskStackBuilderPair(
        val intent: Intent?,
        val taskStackBuilder: TaskStackBuilder?
    )

    private fun intentFromDeeplinkMethod(
        method: Method,
        methodInvocation: Any?
    ) = when (method.returnType) {
        TaskStackBuilder::class.java ->
            intentFromTaskStackBuilder(
                methodInvocation as TaskStackBuilder
            )
        DeepLinkMethodResult::class.java ->
            intentFromDeepLinkMethodResult(
                methodInvocation as DeepLinkMethodResult
            )
        else -> IntentTaskStackBuilderPair(methodInvocation as Intent, null)
    }

    private fun intentFromDeepLinkMethodResult(deepLinkMethodResult: DeepLinkMethodResult): IntentTaskStackBuilderPair {
        return if (deepLinkMethodResult.taskStackBuilder != null) {
            intentFromTaskStackBuilder(deepLinkMethodResult.taskStackBuilder)
        } else IntentTaskStackBuilderPair(deepLinkMethodResult.intent, null)
    }

    private fun intentFromTaskStackBuilder(taskStackBuilder: TaskStackBuilder): IntentTaskStackBuilderPair {
        return if (taskStackBuilder.intentCount == 0) {
            throw TaskStackError()
        } else {
            IntentTaskStackBuilderPair(
                taskStackBuilder.editIntentAt(taskStackBuilder.intentCount - 1),
                taskStackBuilder
            )
        }
    }

    class TaskStackError : IllegalStateException()

    /**
     * Retruns a bundle that contains all the parameter, either from placeholder (path/{parameterName})
     * elements or the query elements (?queryElement=value).
     */
    private fun parameterBundle(
        deeplinkMatchResult: DeepLinkMatchResult,
        deepLinkUri: DeepLinkUri,
        originalIntentUri: Uri,
        sourceIntent: Intent
    ): Bundle {
        val resultParameterMap = mutableMapOf<String, String>()
        resultParameterMap.putAll(deeplinkMatchResult.getParameters(deepLinkUri))
        for (queryParameter in deepLinkUri.queryParameterNames()) {
            for (queryParameterValue in deepLinkUri.queryParameterValues(queryParameter)) {
                if (resultParameterMap.containsKey(queryParameter)) {
                    Log.w(TAG, "Duplicate parameter name in path and query param: $queryParameter")
                }
                resultParameterMap[queryParameter] = queryParameterValue
            }
        }
        resultParameterMap[DeepLink.URI] = originalIntentUri.toString()
        val parameters: Bundle = if (sourceIntent.extras != null) {
            Bundle(sourceIntent.extras)
        } else {
            Bundle()
        }
        resultParameterMap.forEach { (key, value) ->
            parameters.putString(key, value)
        }
        return parameters
    }

    /**
     * Returns a raw match result for the given uriString. This is null if no match is found.
     * There is no general use for that during normal operation but it might be useful when writing
     * tests.
     *
     * @param uriString Uri to be matched
     * @return An instance of [DeepLinkMatchResult] if a match was found or null if not.
     */
    fun findEntry(uriString: String): DeepLinkMatchResult? {
        val uri = DeepLinkUri.parse(uriString)
        val entryIdxMatches =
            registries.mapNotNull { it.idxMatch(uri, configurablePathSegmentReplacements) }
        return when (entryIdxMatches.size) {
            // Found no match
            0 -> null
            // Found one match
            1 -> entryIdxMatches.first()
            // Found multiple matches. Sort matches by concreteness:
            // No variable element > containing placeholders >  are a configurable path segment
            else -> {
                val firstTwoSortedMatches = entryIdxMatches.sorted().take(2)
                if (firstTwoSortedMatches.first().compareTo(firstTwoSortedMatches.last()) == 0) {
                    errorHandler?.duplicateMatch(uriString, firstTwoSortedMatches)
                    Log.w(
                        TAG, "More than one match with the same concreteness!! ("
                                + firstTwoSortedMatches.first().toString() + ") vs. (" + firstTwoSortedMatches.last().toString() + ")"
                    )
                }
                firstTwoSortedMatches.first()
            }
        }
    }

    fun supportsUri(uriString: String?): Boolean {
        val uri = DeepLinkUri.parse(uriString)
        return registries.any { it.supports(uri, configurablePathSegmentReplacements) }
    }

    val allDeepLinkEntries by lazy {
        registries.map{it.getAllEntries()}.flatten()
    }

    companion object {
        protected const val TAG = "DeepLinkDelegate"
    }
}