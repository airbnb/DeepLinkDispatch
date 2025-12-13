package com.airbnb.deeplinkdispatch

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.app.TaskStackBuilder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.airbnb.deeplinkdispatch.base.Utils.toByteArrayMap
import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
import com.airbnb.deeplinkdispatch.handler.TypeConverters
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

open class BaseDeepLinkDelegate
    @JvmOverloads
    constructor(
        val registries: List<BaseRegistry>,
        configurablePathSegmentReplacements: Map<String, String> = emptyMap(),
        private val typeConverters: () -> TypeConverters = { TypeConverters() },
        private val errorHandler: ErrorHandler? = null,
        private val typeConversionErrorNullable: (DeepLinkUri, Type, String) -> Int? = { _, _, _: String -> null },
        private val typeConversionErrorNonNullable: (DeepLinkUri, Type, String) -> Int = { _, _, _: String -> 0 },
    ) {
        /**
         *
         * Mapping of values for DLD to substitute for annotation-declared configurablePathSegments.
         *
         * Example
         * Given:
         *
         *  * <xmp>@DeepLink("https://www.example.com/<configurable-path-segment>/users/{param1}")
         *  * </configurable-path-segment></xmp> *
         *  * mapOf("pathVariableReplacementValue" to "/obamaOs")
         *
         * Then:
         *  * <xmp>https://www.example.com/obamaOs/users/{param1}</xmp> will match.
         *
         *  Note: The non empty string value of a configurable path segment mapping needs to start
         *  with a '/' as in case it is empty the whole path segment (with the '/') gets removed.
         */
        private val configurablePathSegmentReplacements: Map<ByteArray, ByteArray> =
            toByteArrayMap(configurablePathSegmentReplacements)

        init {
            validateConfigurablePathSegmentReplacements(
                registries,
                configurablePathSegmentReplacements,
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
            sourceIntent: Intent = activity.intent,
        ): DeepLinkResult {
            val uri = sourceIntent.data
            val result =
                uri?.let {
                    createResult(activity, sourceIntent, findEntry(uri.toString()))
                } ?: createResult(activity, sourceIntent, null)
            dispatchResult(
                result = result,
                activity = activity,
            )
            notifyListener(
                context = activity,
                isError = !result.isSuccessful,
                uri = uri,
                uriTemplate = if (result.deepLinkMatchResult != null) result.deepLinkMatchResult.deeplinkEntry.uriTemplate else null,
                errorMessage = result.error,
            )
            return result
        }

        private fun notifyListener(
            context: Context,
            isError: Boolean,
            uri: Uri?,
            uriTemplate: String?,
            errorMessage: String,
        ) {
            val intent =
                Intent().apply {
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
            activity: Activity,
        ) {
            if (result.isSuccessful) {
                when (result.deepLinkMatchResult?.deeplinkEntry) {
                    is DeepLinkEntry.MethodDeeplinkEntry ->
                        result.methodResult.taskStackBuilder?.startActivities()
                            ?: result.methodResult.intent?.let { activity.startActivity(it) }
                    is DeepLinkEntry.ActivityDeeplinkEntry ->
                        result.methodResult.intent?.let { activity.startActivity(it) }
                    is DeepLinkEntry.HandlerDeepLinkEntry ->
                        callDeeplinkHandler(activity, result)
                    null -> {
                        // No - op
                    }
                }
            }
        }

        private fun Array<Type>.getDeepLinkArgClassFromTypeArguments(): Class<*>? =
            filterIsInstance<Class<*>>().singleOrNull { typeArgumentClass ->
                typeArgumentClass == Object::class.java ||
                    typeArgumentClass.constructors.any { constructor ->
                        constructor.parameterAnnotations.any { parameter ->
                            parameter.any { annotation -> annotation.annotationClass == DeeplinkParam::class }
                        }
                    }
            }

        @SuppressLint("NewApi")
        private fun callDeeplinkHandler(
            context: Context,
            result: DeepLinkResult,
        ) {
            result.deepLinkHandlerResult?.let { deepLinkHandlerResult ->
                deepLinkHandlerResult.deepLinkHandler.handleDeepLink(
                    context = context,
                    deepLinkArgs = deepLinkHandlerResult.deepLinkHandlerArgs,
                )
            }
        }

        private fun deepLinkHandlerInstance(handlerClazz: Class<*>): com.airbnb.deeplinkdispatch.handler.DeepLinkHandler<Any> =
            try {
                handlerClazz.getField("INSTANCE").get(null)
            } catch (e: NoSuchFieldException) {
                handlerClazz.constructors.singleOrNull()?.let {
                    if (it.typeParameters.isNotEmpty()) null else it.newInstance()
                } ?: error("Handler class must have single zero argument constructor.")
            } as com.airbnb.deeplinkdispatch.handler.DeepLinkHandler<Any>

        private fun getDeepLinkArgs(
            deepLinkArgsClazz: Class<out Any>,
            parameters: Map<String, String>,
            uriTemplate: DeepLinkUri,
        ): Any =
            if (deepLinkArgsClazz == Object::class.java) {
                Object()
            } else {
                val deepLinkArgsClazzConstructor =
                    deepLinkArgsClazz.constructors.singleOrNull()
                        ?: error("Handler parameter class can only have one constructor.")
                val annotationList: List<DeeplinkParam> =
                    deepLinkArgsClazzConstructor.parameterAnnotations
                        .flatten()
                        .filterNotNull()
                        .filter { it.annotationClass == DeeplinkParam::class }
                        .map { it as DeeplinkParam }
                val typeNameMap =
                    deepLinkArgsClazzConstructor.genericParameterTypes.let {
                        // Check if we have as many parameters as annotations, the DeeplinkParam annotation is not
                        // repeatable we cannot have multiple for just one element.
                        if (annotationList.size == it.size) {
                            annotationList.zip(it)
                        } else {
                            error("There are ${annotationList.size} annotations but ${it.size} parameters!")
                        }
                    }
                val deepLinkArgsConstructorParams =
                    createParamArray(
                        typeNameMap = typeNameMap,
                        parameters = parameters,
                        uriTemplate = uriTemplate,
                    )
                deepLinkArgsClazzConstructor.newInstance(*deepLinkArgsConstructorParams)
            }

        private fun createParamArray(
            typeNameMap: List<Pair<DeeplinkParam, Type>>,
            parameters: Map<String, String>,
            uriTemplate: DeepLinkUri,
        ): Array<Any?> =
            typeNameMap
                .map { (annotation, type) ->
                    when (annotation.type) {
                        DeepLinkParamType.Path ->
                            mapNotNullableType(
                                value =
                                    parameters.getOrElse(
                                        annotation.name,
                                    ) { error("Non existent non nullable element for name: ${annotation.name}") },
                                type = type,
                                uriTemplate = uriTemplate,
                            )
                        DeepLinkParamType.Query ->
                            mapNullableType(
                                value = parameters[annotation.name],
                                type = type,
                                uriTemplate = uriTemplate,
                            )
                    }
                }.toTypedArray()

        private fun mapNullableType(
            value: String?,
            type: Type,
            uriTemplate: DeepLinkUri,
        ): Any? {
            if (value == null) return null
            return try {
                typeConverters()[type]?.convert(value = value) ?: when (type) {
                    Boolean::class.javaObjectType -> value.toBoolean()
                    Int::class.javaObjectType -> value.toInt()
                    Long::class.javaObjectType -> value.toLong()
                    Short::class.javaObjectType -> value.toShort()
                    Byte::class.javaObjectType -> value.toByte()
                    Double::class.javaObjectType -> value.toDouble()
                    Float::class.javaObjectType -> value.toFloat()
                    String::class.javaObjectType -> value
                    else ->
                        error(
                            "Missing type converter for type $type! You must register a custom" +
                                " type converter via the DeepLinkDelegate constructor element for all" +
                                " but simple data types.",
                        )
                }
            } catch (e: NumberFormatException) {
                typeConversionErrorNullable(uriTemplate, type, value)
            }
        }

        private fun mapNotNullableType(
            value: String,
            type: Type,
            uriTemplate: DeepLinkUri,
        ): Any =
            try {
                typeConverters()[type]?.convert(value = value) ?: when (type) {
                    Boolean::class.javaPrimitiveType -> value.toBoolean()
                    Int::class.javaPrimitiveType -> value.toInt()
                    Long::class.javaPrimitiveType -> value.toLong()
                    Short::class.javaPrimitiveType -> value.toShort()
                    Byte::class.javaPrimitiveType -> value.toByte()
                    Double::class.javaPrimitiveType -> value.toDouble()
                    Float::class.javaPrimitiveType -> value.toFloat()
                    String::class.javaObjectType -> value
                    else ->
                        error(
                            "Missing type converter for type $type! You must register a custom" +
                                " type converter via the DeepLinkDelegate constructor element for all" +
                                " but simple data types.",
                        )
                }
            } catch (e: NumberFormatException) {
                typeConversionErrorNonNullable(uriTemplate, type, value)
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
            deeplinkMatchResult: DeepLinkMatchResult?,
        ): DeepLinkResult {
            val originalIntentUri =
                sourceIntent.data
                    ?: return DeepLinkResult(
                        isSuccessful = false,
                        error = "No Uri in given activity's intent.",
                        deepLinkMatchResult = deeplinkMatchResult,
                    )
            val deepLinkUri = DeepLinkUri.parse(originalIntentUri.toString())
            if (deeplinkMatchResult == null) {
                return DeepLinkResult(
                    isSuccessful = false,
                    error = "DeepLinkEntry cannot be null",
                )
            }
            val queryAndPathParameters =
                queryAndPathParameters(
                    deeplinkMatchResult = deeplinkMatchResult,
                    deepLinkUri = deepLinkUri,
                )

            val intentBundle =
                createIntentBundle(sourceIntent, originalIntentUri, queryAndPathParameters)
            return try {
                val intermediateDeepLinkResult =
                    processResultForType(
                        matchedDeeplinkEntry = deeplinkMatchResult.deeplinkEntry,
                        parameters = queryAndPathParameters,
                        activity = activity,
                        intentBundle = intentBundle,
                    )
                intermediateDeepLinkResult.intent?.let { intent ->
                    if (intent.action == null) {
                        intent.action = sourceIntent.action
                    }
                    if (intent.data == null) {
                        intent.data = sourceIntent.data
                    }
                    intent.putExtras(intentBundle.filter { key, _ -> intent.extras?.containsKey(key) != true })
                    intent.putExtra(DeepLink.IS_DEEP_LINK, true)
                    intent.putExtra(DeepLink.REFERRER_URI, originalIntentUri)
                    if (activity.callingActivity != null) {
                        intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
                    }
                    DeepLinkResult(
                        isSuccessful = true,
                        uriString = originalIntentUri.toString(),
                        deepLinkMatchResult = deeplinkMatchResult,
                        methodResult =
                            DeepLinkMethodResult(
                                intent,
                                intermediateDeepLinkResult.taskStackBuilder,
                            ),
                        parameters = queryAndPathParameters,
                        deepLinkHandlerResult = intermediateDeepLinkResult.deepLinkHandlerResult,
                    )
                } ?: return DeepLinkResult(
                    isSuccessful = false,
                    uriString = originalIntentUri.toString(),
                    error = "Destination Intent is null!",
                    deepLinkMatchResult = deeplinkMatchResult,
                    methodResult =
                        DeepLinkMethodResult(
                            intermediateDeepLinkResult.intent,
                            intermediateDeepLinkResult.taskStackBuilder,
                        ),
                    deepLinkHandlerResult = intermediateDeepLinkResult.deepLinkHandlerResult,
                )
            } catch (deepLinkMethodError: DeeplLinkMethodError) {
                return DeepLinkResult(
                    isSuccessful = false,
                    uriString = originalIntentUri.toString(),
                    error = deepLinkMethodError.message ?: "",
                    errorThrowable = deepLinkMethodError,
                    deepLinkMatchResult = deeplinkMatchResult,
                )
            }
        }

        private fun createIntentBundle(
            sourceIntent: Intent,
            originalIntentUri: Uri,
            queryAndPathParameters: Map<String, String>,
        ): Bundle {
            val intentBundle: Bundle =
                if (sourceIntent.extras != null) {
                    Bundle(sourceIntent.extras)
                } else {
                    Bundle()
                }
            queryAndPathParameters.forEach { (key, value) -> intentBundle.putString(key, value) }
            intentBundle.putString(DeepLink.URI, originalIntentUri.toString())
            return intentBundle
        }

        private fun processResultForType(
            matchedDeeplinkEntry: DeepLinkEntry,
            parameters: Map<String, String>,
            activity: Activity,
            intentBundle: Bundle,
        ): IntermediateDeepLinkResult {
            val clazz = matchedDeeplinkEntry.clazz
            return when (matchedDeeplinkEntry) {
                is DeepLinkEntry.ActivityDeeplinkEntry ->
                    IntermediateDeepLinkResult(Intent(activity, clazz), null, null)
                is DeepLinkEntry.MethodDeeplinkEntry -> {
                    try {
                        try {
                            val method =
                                clazz.getMethod(matchedDeeplinkEntry.method, Context::class.java)
                            intentFromDeeplinkMethod(method, method.invoke(clazz, activity))
                        } catch (exception: NoSuchMethodException) {
                            val method =
                                clazz.getMethod(
                                    matchedDeeplinkEntry.method,
                                    Context::class.java,
                                    Bundle::class.java,
                                )
                            intentFromDeeplinkMethod(
                                method,
                                method.invoke(clazz, activity, intentBundle),
                            )
                        }
                    } catch (exception: NoSuchMethodException) {
                        throw DeeplLinkMethodError(
                            message = "Deep link to non-existent method: ${matchedDeeplinkEntry.method}",
                            cause = exception,
                        )
                    } catch (exception: IllegalAccessException) {
                        throw DeeplLinkMethodError(
                            message = "Could not deep link to method: ${matchedDeeplinkEntry.method}",
                            cause = exception,
                        )
                    } catch (exception: InvocationTargetException) {
                        throw DeeplLinkMethodError(
                            message = "Could not deep link to method: ${matchedDeeplinkEntry.method}",
                            cause = exception,
                        )
                    }
                }
                is DeepLinkEntry.HandlerDeepLinkEntry -> {
                    IntermediateDeepLinkResult(
                        Intent(activity, clazz),
                        null,
                        DeepLinkHandlerResult(
                            deepLinkHandler = deepLinkHandlerInstance(matchedDeeplinkEntry.clazz),
                            deepLinkHandlerArgs = deepLinkHandlerArgs(matchedDeeplinkEntry, parameters),
                        ),
                    )
                }
            }
        }

        private fun deepLinkHandlerArgs(
            matchedDeeplinkEntry: DeepLinkEntry,
            parameters: Map<String, String>,
        ): Any {
            val handlerClazz = matchedDeeplinkEntry.clazz

            val deepLinkArgsClazz =
                argsClazz(handlerClazz) ?: run {
                    errorHandler?.unableToDetermineHandlerArgsType(
                        uriTemplate = matchedDeeplinkEntry.uriTemplate,
                        className = matchedDeeplinkEntry.className,
                    )
                    // We are assuming there is no type argument if we cannot determine one.
                    // This might still crash (if there actually is a type argument other than
                    // Any::class.java
                    Any::class.java
                }
                    ?: error("Unable to determine parameter class type for ${handlerClazz.name}.")
            return getDeepLinkArgs(
                deepLinkArgsClazz,
                parameters,
                DeepLinkUri.parseTemplate(matchedDeeplinkEntry.uriTemplate),
            )
        }

        private fun argsClazz(handlerClazz: Class<*>): Class<*>? {
            // This relies on the fact that the Processor already checked that every annotated class
            // correctly implements the DeepLinkHandler<T> interface.
            //
            // First check if the handler directly implements the interface. If so get the type from
            // the interface.
            return handlerClazz.genericInterfaces
                .filterIsInstance<ParameterizedType>()
                .singleOrNull {
                    (it.rawType as Class<*>).canonicalName.startsWith(
                        com.airbnb.deeplinkdispatch.handler.DeepLinkHandler::class.java.name,
                    )
                }?.actualTypeArguments
                ?.getDeepLinkArgClassFromTypeArguments()
                // If we cannot get the type from the interface the handler does not directly implement it
                // need to look at the super class and check its type arguments.
                ?: if (handlerClazz.genericSuperclass is ParameterizedType) {
                    (handlerClazz.genericSuperclass as ParameterizedType).actualTypeArguments.getDeepLinkArgClassFromTypeArguments()
                } else {
                    argsClazz(
                        handlerClazz.genericSuperclass as Class<*>,
                    )
                }
        }

        private data class IntermediateDeepLinkResult(
            val intent: Intent?,
            val taskStackBuilder: TaskStackBuilder?,
            val deepLinkHandlerResult: DeepLinkHandlerResult<Any>?,
        )

        private fun intentFromDeeplinkMethod(
            method: Method,
            methodInvocation: Any?,
        ): IntermediateDeepLinkResult =
            when (method.returnType) {
                TaskStackBuilder::class.java ->
                    intentFromTaskStackBuilder(
                        methodInvocation as TaskStackBuilder?,
                        method.name,
                    )
                DeepLinkMethodResult::class.java ->
                    intentFromDeepLinkMethodResult(
                        methodInvocation as DeepLinkMethodResult?,
                        method.name,
                    )
                else -> IntermediateDeepLinkResult(methodInvocation as Intent?, null, null)
            }

        private fun intentFromDeepLinkMethodResult(
            deepLinkMethodResult: DeepLinkMethodResult?,
            methodName: String,
        ): IntermediateDeepLinkResult =
            if (deepLinkMethodResult?.taskStackBuilder != null) {
                intentFromTaskStackBuilder(deepLinkMethodResult.taskStackBuilder, methodName)
            } else {
                IntermediateDeepLinkResult(deepLinkMethodResult?.intent, null, null)
            }

        private fun intentFromTaskStackBuilder(
            taskStackBuilder: TaskStackBuilder?,
            methodName: String,
        ): IntermediateDeepLinkResult =
            if (taskStackBuilder?.intentCount == 0) {
                throw DeeplLinkMethodError("Could not deep link to method: $methodName intents length == 0")
            } else {
                IntermediateDeepLinkResult(
                    taskStackBuilder?.editIntentAt(taskStackBuilder.intentCount - 1),
                    taskStackBuilder,
                    null,
                )
            }

        class DeeplLinkMethodError(
            message: String,
            override val cause: Throwable? = null,
        ) : IllegalStateException(message, cause)

        /**
         * Retruns a bundle that contains all the parameter, either from placeholder (path/{parameterName})
         * elements or the query elements (?queryElement=value).
         */
        private fun queryAndPathParameters(
            deeplinkMatchResult: DeepLinkMatchResult,
            deepLinkUri: DeepLinkUri,
        ): Map<String, String> {
            val resultParameterMap = mutableMapOf<String, String>()
            resultParameterMap.putAll(deeplinkMatchResult.getParameters(deepLinkUri))
            for (queryParameter in deepLinkUri.queryParameterNames()) {
                for (queryParameterValue in deepLinkUri.queryParameterValues(queryParameter)) {
                    if (resultParameterMap.containsKey(queryParameter)) {
                        Log.w(TAG, "Duplicate parameter name in path and query param: $queryParameter")
                    }
                    resultParameterMap[queryParameter] = queryParameterValue ?: ""
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
                            TAG,
                            "More than one match with the same concreteness!! " +
                                "(${firstTwoSortedMatches.first()}) vs. (${firstTwoSortedMatches.last()})",
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

        /**
         * Get all DeepLinkEntry objects that are handled by all registries handled by this
         * DeepLinkDelegate.
         */
        val allDeepLinkEntries by lazy {
            registries.allDeepLinkEntries()
        }

        /**
         * Get a map of all DeepLinkEntries and its duplicates, DeepLinkEntry objects that
         * might be slightly different but will map to the same URL during app operation.
         */
        val duplicatedDeepLinkEntries: Map<DeepLinkEntry, List<DeepLinkEntry>> by lazy {
            registries.duplicatedDeepLinkEntries()
        }

        companion object {
            protected const val TAG = "DeepLinkDelegate"
        }
    }

/**
 * Creates a grouping key for an entry based on scheme, host, and path segment count.
 * Entries can only be duplicates if they have the same scheme, host, and number of path segments.
 */
private fun DeepLinkEntry.groupingKey(): String {
    val uri = DeepLinkUri.parseTemplate(uriTemplate)
    val scheme = uri.scheme()
    val host = uri.host() ?: ""
    val pathCount = uri.pathSegments()?.size ?: 0
    return "$scheme://$host/$pathCount"
}

/**
 * Checks if two structural patterns could potentially match the same URLs.
 *
 * Two patterns can overlap if and only if, for each position:
 * - Both have placeholders, OR
 * - One has a placeholder and the other has a concrete value, OR
 * - Both have the same concrete value
 *
 * If they have different concrete values at any position, they CANNOT overlap.
 *
 * Examples:
 * - "app://host/foo/_" and "app://host/foo/_" → CAN overlap (same structure)
 * - "app://host/foo/_" and "app://host/bar/_" → CANNOT overlap (foo ≠ bar)
 * - "app://host/foo/baz" and "app://host/foo/_" → CAN overlap (placeholder can match baz)
 * - "app://host/_/_" and "app://host/foo/bar" → CAN overlap (placeholders can match anything)
 */
private fun couldPatternsOverlap(
    pattern1: String,
    pattern2: String,
): Boolean {
    // Quick check: if patterns are identical, they definitely overlap
    if (pattern1 == pattern2) return true

    // Parse patterns by splitting on '/'
    val parts1 = pattern1.split("/")
    val parts2 = pattern2.split("/")

    // Must have same structure length
    if (parts1.size != parts2.size) return false

    // Check each segment position
    for (i in parts1.indices) {
        val part1 = parts1[i]
        val part2 = parts2[i]

        // If both are concrete (not "_") and different, they cannot overlap
        if (part1 != "_" && part2 != "_" && part1 != part2) {
            return false
        }
    }

    // All positions are compatible
    return true
}

/**
 * Creates a structural pattern for an entry where concrete segments keep their value
 * and placeholders become "_". Two entries with the same pattern and different placeholder
 * names are definitely duplicates.
 *
 * For example:
 * - "app://host/foo/{id}/bar" -> "app://host/foo/_/bar"
 * - "app://host/foo/{x}/bar" -> "app://host/foo/_/bar" (same - duplicates)
 * - "app://host/{type}/foo/bar" -> "app://host/_/foo/bar"
 */
private fun DeepLinkEntry.structuralPattern(): String {
    val uri = DeepLinkUri.parseTemplate(uriTemplate)
    val scheme = uri.scheme()
    val host = uri.host() ?: ""
    val pathSegments = uri.pathSegments() ?: emptyList()

    val normalizedPath =
        pathSegments.joinToString("/") { segment ->
            if (segment.startsWith("{") && segment.endsWith("}")) "_" else segment
        }
    return "$scheme://$host/$normalizedPath"
}

/**
 * Returns true if this entry has any placeholder segments in its PATH (not scheme or host).
 * Placeholders in scheme (like http{s}) or host are not considered for duplicate detection
 * because they don't affect the matching priority of path segments.
 */
private fun DeepLinkEntry.hasPathPlaceholders(): Boolean {
    val uri = DeepLinkUri.parseTemplate(uriTemplate)
    val pathSegments = uri.pathSegments() ?: return false
    return pathSegments.any { it.contains("{") && it.contains("}") }
}

/**
 * Get a map of all DeepLinkEntries and its duplicates, DeepLinkEntry objects that
 * might be slightly different but will map to the same URL during app operation.
 *
 * Optimized approach:
 * 1. Group entries by scheme + host + path segment count
 * 2. Within each group, separate into concrete (no placeholders) and wildcard entries
 * 3. Concrete entries with the same exact template are duplicates
 * 4. Wildcard entries with the same structural pattern are definitely duplicates
 * 5. Wildcard entries need to be compared against concrete entries that might match
 *
 * This dramatically reduces comparisons by:
 * - Concrete vs concrete: only exact matches (hash lookup)
 * - Same-pattern wildcards: all are duplicates (no comparison needed)
 * - Cross-pattern wildcards and concrete vs wildcard: still need templatesMatchesSameUrls
 */
fun List<BaseRegistry>.duplicatedDeepLinkEntries(): Map<DeepLinkEntry, List<DeepLinkEntry>> {
    val allDeepLinkEntries = this.allDeepLinkEntries()

    // Fast path: if we have 0 or 1 entries, no duplicates possible
    if (allDeepLinkEntries.size <= 1) {
        return emptyMap()
    }

    // Group entries by scheme + host + path count
    val groups = allDeepLinkEntries.groupBy { it.groupingKey() }

    val result = mutableMapOf<DeepLinkEntry, MutableList<DeepLinkEntry>>()

    fun addDuplicate(
        entry1: DeepLinkEntry,
        entry2: DeepLinkEntry,
    ) {
        if (!result.getOrPut(entry1) { mutableListOf() }.contains(entry2)) {
            result[entry1]!!.add(entry2)
        }
        if (!result.getOrPut(entry2) { mutableListOf() }.contains(entry1)) {
            result[entry2]!!.add(entry1)
        }
    }

    var processedGroups = 0
    var totalWildcardPatternComparisons = 0

    // Process each group independently
    for ((groupKey, entries) in groups) {
        if (entries.size <= 1) continue

        processedGroups++

        // Separate into concrete and wildcard entries
        val concreteEntries = mutableListOf<DeepLinkEntry>()
        val wildcardEntries = mutableListOf<DeepLinkEntry>()

        for (entry in entries) {
            if (entry.hasPathPlaceholders()) {
                wildcardEntries.add(entry)
            } else {
                concreteEntries.add(entry)
            }
        }

        // 1. Group wildcard entries by structural pattern - same pattern = duplicates
        val wildcardByPattern = wildcardEntries.groupBy { it.structuralPattern() }
        for ((_, patternGroup) in wildcardByPattern) {
            if (patternGroup.size > 1) {
                // All entries with the same pattern are duplicates of each other
                for (i in patternGroup.indices) {
                    for (j in (i + 1) until patternGroup.size) {
                        addDuplicate(patternGroup[i], patternGroup[j])
                    }
                }
            }
        }

        // 2. Compare wildcard entries across different patterns (they might still overlap)
        val patternKeys = wildcardByPattern.keys.toList()
        var skippedPatternComparisons = 0

        for (i in patternKeys.indices) {
            for (j in (i + 1) until patternKeys.size) {
                val pattern1 = patternKeys[i]
                val pattern2 = patternKeys[j]

                // Quick check: if patterns can't possibly overlap, skip expensive comparison
                if (!couldPatternsOverlap(pattern1, pattern2)) {
                    skippedPatternComparisons++
                    continue
                }

                totalWildcardPatternComparisons++

                val group1 = wildcardByPattern[pattern1]!!
                val group2 = wildcardByPattern[pattern2]!!
                // Only need to compare one entry from each group since same-pattern entries are equivalent
                val representative1 = group1.first()
                val representative2 = group2.first()
                if (representative1.templatesMatchesSameUrls(representative2)) {
                    // Only flag as duplicate if they have the same concreteness
                    // If one is more concrete, the matching algorithm will correctly pick it
                    if (representative1.compareTo(representative2) == 0) {
                        // All entries in both groups are duplicates of each other
                        for (e1 in group1) {
                            for (e2 in group2) {
                                addDuplicate(e1, e2)
                            }
                        }
                    }
                }
            }
        }

        // 3. Concrete entries with the same template are duplicates (hash lookup)
        val concreteByTemplate = concreteEntries.groupBy { it.uriTemplate }
        for ((_, templateGroup) in concreteByTemplate) {
            if (templateGroup.size > 1) {
                for (i in templateGroup.indices) {
                    for (j in (i + 1) until templateGroup.size) {
                        addDuplicate(templateGroup[i], templateGroup[j])
                    }
                }
            }
        }

        // 4. Skip concrete vs wildcard comparison
        // A concrete entry and a wildcard entry are NOT duplicates even if they match
        // the same URLs, because the concrete entry has higher priority in matching.
        // For example:
        //   - "app://host/foo/bar" (concrete)
        //   - "app://host/foo/{id}" (wildcard)
        // When a URL "app://host/foo/bar" comes in, the concrete entry matches first.
        // When "app://host/foo/other" comes in, only the wildcard matches.
        // So they serve different purposes and are not duplicates.
        //
        // We only flag as duplicates:
        //   - Concrete entries with identical templates
        //   - Wildcard entries with the same structure (same placeholders in same positions)
    }
    return result
}

fun List<BaseRegistry>.allDeepLinkEntries(): List<DeepLinkEntry> = this.flatMap { it.getAllEntries() }
