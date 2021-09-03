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
import com.airbnb.deeplinkdispatch.handler.DEEP_LINK_HANDLER_METHOD_NAME
import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
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
            when (matchedDeepLinkResult.deeplinkEntry) {
                is DeepLinkEntry.MethodDeeplinkEntry ->
                    result.methodResult.taskStackBuilder?.startActivities()
                        ?: activity.startActivity(result.methodResult.intent)
                is DeepLinkEntry.ActivityDeeplinkEntry ->
                    activity.startActivity(result.methodResult.intent)
                is DeepLinkEntry.HandlerDeepLinkEntry ->
                    if (result.isSuccessful) callDeeplinkHandler(result)
            }
        }
    }

    private fun callDeeplinkHandler(result: DeepLinkResult) {
        val handlerClazz = result.deepLinkMatchResult?.deeplinkEntry?.clazz!!
        val handlerMethod = handlerClazz.methods.singleOrNull { method ->
            method.name == DEEP_LINK_HANDLER_METHOD_NAME && method.genericParameterTypes.let { parameterTypes ->
                parameterTypes.singleOrNull()?.let { it != Object::class.java } ?: false
            }
        } ?: error("DeepLinkHandler class ${handlerClazz.name} has more than one methods " +
                "\"$DEEP_LINK_HANDLER_METHOD_NAME\" methods with a single parameter.")
        // Ok to call single here as we would have failed above if we would have more than one type.
        val handlerParameterClazz = handlerMethod.genericParameterTypes.single() as Class<*>
        val handlerParameterClazzConstructor = handlerParameterClazz.constructors.singleOrNull()
            ?: error("Handler parameter class can only have one constructor.")
        val annotationList: List<DeeplinkParam> =
            handlerParameterClazzConstructor.parameterAnnotations.flatten()
                .filterNotNull().filter { it.annotationClass == DeeplinkParam::class }
                .map { it as DeeplinkParam }
        val typeNameMap = handlerParameterClazzConstructor.parameterTypes.let {
            // Check if we have as many parameters as annotations, the DeeplinkParam annotation is not
            // repeatable we cannot have multiple for just one element.
            if (annotationList.size == it.size) {
                annotationList.zip(it)
            } else error("There are ${annotationList.size} annotations but ${it.size} parameters!")
        }

        val handlerInstance = try {
            handlerClazz.getField("INSTANCE").get(null)
        } catch (e: NoSuchFieldException) {
            handlerClazz.constructors.singleOrNull()?.let {
                if (it.typeParameters.isNotEmpty()) null else it.newInstance()
            } ?: error("Handler class must have single zero argument constructor.")
        } as com.airbnb.deeplinkdispatch.handler.DeepLinkHandler<Any>
        val handlerArgsConstructorParams = createParamArray(
            typeNameMap = typeNameMap,
            parameters = result.parameters,
            throwOnTypeConversion = handlerInstance.throwOnTypeConversion
        )
        val handlerParameters = handlerParameterClazzConstructor.newInstance(*handlerArgsConstructorParams)
        handlerInstance.handleDeepLink(handlerParameters)
    }

    private fun createParamArray(
        typeNameMap: List<Pair<DeeplinkParam, Class<*>>>,
        parameters: Map<String, String>,
        throwOnTypeConversion: Boolean
    ): Array<Any?> {
        return typeNameMap.map { (annotation, type) ->
            when(annotation.type) {
                DeepLinkParamType.Path ->
                    mapNotNullableType(
                        value = parameters.getOrElse(annotation.name) { error("Non existent non nullable element for name: ${annotation.name}") },
                        type = type,
                        throwOnTypeConversion = throwOnTypeConversion
                    )
                DeepLinkParamType.Query ->
                    mapNullableType(
                        value = parameters[annotation.name],
                        type = type,
                        throwOnTypeConversion = throwOnTypeConversion
                    )
            }
        }.toTypedArray()
    }

    private fun mapNullableType(value: String?, type: Class<*>, throwOnTypeConversion: Boolean): Any? {
        if (value == null) return null
        return try {
            when (type) {
                Boolean::class.javaObjectType -> value.toBoolean()
                Int::class.javaObjectType -> value.toInt()
                Long::class.javaObjectType -> value.toLong()
                Short::class.javaObjectType -> value.toShort()
                Byte::class.javaObjectType -> value.toByte()
                Double::class.javaObjectType -> value.toDouble()
                Float::class.javaObjectType -> value.toFloat()
                else -> value
            }
        } catch (e: NumberFormatException) {
            if (throwOnTypeConversion) throw e else null
        }
    }

    private fun mapNotNullableType(value: String, type: Class<*>, throwOnTypeConversion: Boolean): Any {
        return try {
            when (type) {
                Boolean::class.javaPrimitiveType -> value.toBoolean()
                Int::class.javaPrimitiveType -> value.toInt()
                Long::class.javaPrimitiveType -> value.toLong()
                Short::class.javaPrimitiveType -> value.toShort()
                Byte::class.javaPrimitiveType -> value.toByte()
                Double::class.javaPrimitiveType -> value.toDouble()
                Float::class.javaPrimitiveType -> value.toFloat()
                else -> value
            }
        } catch (e: NumberFormatException) {
            if (throwOnTypeConversion) throw e else 0
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
        val queryAndPathParameters = queryAndPathParameters(
            deeplinkMatchResult = deeplinkMatchResult,
            deepLinkUri = deepLinkUri,
        )

        val intentBundle = createIntentBundle(sourceIntent, originalIntentUri, queryAndPathParameters)
        return try {
            val intentAndTaskStackBuilderPair = intentAndTaskStackBuilderFromClass(
                matchedDeeplinkEntry = deeplinkMatchResult.deeplinkEntry,
                activity = activity,
                intentBundle = intentBundle
            )
            intentAndTaskStackBuilderPair.intent?.let { intent ->
                if (intent.action == null) {
                    intent.action = sourceIntent.action
                }
                if (intent.data == null) {
                    intent.data = sourceIntent.data
                }
                intent.putExtras(intentBundle)
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
                    ),
                    parameters = queryAndPathParameters
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
        } catch (deepLinkMethodError: DeeplLinkMethodError) {
            return DeepLinkResult(
                isSuccessful = false,
                uriString = originalIntentUri.toString(),
                error = deepLinkMethodError.message ?: "",
                deepLinkMatchResult = deeplinkMatchResult,
                methodResult = DeepLinkMethodResult(null, null)
            )
        }
    }

    private fun createIntentBundle(
        sourceIntent: Intent,
        originalIntentUri: Uri,
        queryAndPathParameters: Map<String, String>
    ): Bundle {
        val intentBundle: Bundle = if (sourceIntent.extras != null) {
            Bundle(sourceIntent.extras)
        } else {
            Bundle()
        }
        queryAndPathParameters.forEach { (key, value) -> intentBundle.putString(key, value) }
        intentBundle.putString(DeepLink.URI, originalIntentUri.toString())
        return intentBundle
    }

    private fun intentAndTaskStackBuilderFromClass(
        matchedDeeplinkEntry: DeepLinkEntry,
        activity: Activity,
        intentBundle: Bundle
    ): IntentTaskStackBuilderPair {
        val clazz = matchedDeeplinkEntry.clazz
        return when (matchedDeeplinkEntry) {
            is DeepLinkEntry.ActivityDeeplinkEntry ->
                IntentTaskStackBuilderPair(Intent(activity, clazz), null)
            is DeepLinkEntry.MethodDeeplinkEntry -> {
                try {
                    try {
                        val method = clazz.getMethod(matchedDeeplinkEntry.method, Context::class.java)
                        intentFromDeeplinkMethod(method, method.invoke(clazz, activity))
                    } catch (exception: NoSuchMethodException) {
                        val method = clazz.getMethod(
                            matchedDeeplinkEntry.method,
                            Context::class.java,
                            Bundle::class.java
                        )
                        intentFromDeeplinkMethod(method, method.invoke(clazz, activity, intentBundle))
                    }
                } catch (exception: NoSuchMethodException) {
                    throw DeeplLinkMethodError("Deep link to non-existent method: ${matchedDeeplinkEntry.method}")
                } catch (exception: IllegalAccessException) {
                    throw DeeplLinkMethodError("Could not deep link to method: ${matchedDeeplinkEntry.method}")
                } catch (exception: InvocationTargetException) {
                    throw DeeplLinkMethodError("Could not deep link to method: ${matchedDeeplinkEntry.method}")
                }
            }
            is DeepLinkEntry.HandlerDeepLinkEntry ->
                // The handler does not need any Intent, we will call it directly, but we just make
                // one here to to not fail later.
                IntentTaskStackBuilderPair(Intent(activity, clazz), null)
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
                methodInvocation as TaskStackBuilder,
                method.name
            )
        DeepLinkMethodResult::class.java ->
            intentFromDeepLinkMethodResult(
                methodInvocation as DeepLinkMethodResult,
                method.name
            )
        else -> IntentTaskStackBuilderPair(methodInvocation as Intent, null)
    }

    private fun intentFromDeepLinkMethodResult(deepLinkMethodResult: DeepLinkMethodResult, methodName: String): IntentTaskStackBuilderPair {
        return if (deepLinkMethodResult.taskStackBuilder != null) {
            intentFromTaskStackBuilder(deepLinkMethodResult.taskStackBuilder,methodName)
        } else IntentTaskStackBuilderPair(deepLinkMethodResult.intent, null)
    }

    private fun intentFromTaskStackBuilder(taskStackBuilder: TaskStackBuilder, methodName: String): IntentTaskStackBuilderPair {
        return if (taskStackBuilder.intentCount == 0) {
            throw DeeplLinkMethodError("Could not deep link to method: $methodName intents length == 0")
        } else {
            IntentTaskStackBuilderPair(
                taskStackBuilder.editIntentAt(taskStackBuilder.intentCount - 1),
                taskStackBuilder
            )
        }
    }

    class DeeplLinkMethodError(message: String) : IllegalStateException(message)

    /**
     * Retruns a bundle that contains all the parameter, either from placeholder (path/{parameterName})
     * elements or the query elements (?queryElement=value).
     */
    private fun queryAndPathParameters(
        deeplinkMatchResult: DeepLinkMatchResult,
        deepLinkUri: DeepLinkUri
    ): Map<String, String> {
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
        return resultParameterMap
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