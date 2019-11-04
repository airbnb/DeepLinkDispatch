package com.airbnb.deeplinkdispatch

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log

import androidx.core.app.TaskStackBuilder
import androidx.localbroadcastmanager.content.LocalBroadcastManager

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

open class BaseDeepLinkDelegateKT(val loaders: List<Parser>) {

    private fun findEntry(uriString: String): DeepLinkEntry? {
        for (loader in loaders) {
            val entry = loader.parseUri(uriString)
            if (entry != null) {
                return entry
            }
        }
        return null
    }

    /**
     * Calls into [.dispatchFrom].
     *
     * @param activity activity with inbound Intent stored on it.
     * @return DeepLinkResult, whether success or error.
     */
    fun dispatchFrom(activity: Activity?): DeepLinkResult {
        if (activity == null) {
            throw NullPointerException("activity == null")
        }
        return dispatchFrom(activity, activity.intent)
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
    fun dispatchFrom(activity: Activity, sourceIntent: Intent): DeepLinkResult {
        val result = createResult(activity, sourceIntent,
                findEntry(sourceIntent.data!!.toString())
        )
        if (result.taskStackBuilder != null) {
            result.taskStackBuilder.startActivities()
        } else if (result.intent != null) {
            activity.startActivity(result.intent)
        }
        notifyListener(activity, !result.isSuccessful, sourceIntent.data,
                result.deepLinkEntry!!.uriTemplate, result.error)
        return result
    }

    /**
     * Create a [DeepLinkResult], whether we are able to match the uri on
     * {@param sourceIntent} or not.
     *
     * @param activity      used to startActivity() or notifyListener().
     * @param sourceIntent  inbound Intent.
     * @param deepLinkEntry deepLinkEntry that matches the sourceIntent's URI. Derived from
     * [.findEntry]. Can be injected for testing.
     * @return DeepLinkResult
     */
    fun createResult(
            activity: Activity?, sourceIntent: Intent?, deepLinkEntry: DeepLinkEntry?
    ): DeepLinkResult {
        if (activity == null) {
            throw NullPointerException("activity == null")
        }
        if (sourceIntent == null) {
            throw NullPointerException("sourceIntent == null")
        }
        val uri = sourceIntent.data ?: return DeepLinkResult(
                false, null, "No Uri in given activity's intent.", null, null, deepLinkEntry)
        val uriString = uri.toString()
        if (deepLinkEntry == null) {
            return DeepLinkResult(false, null, "DeepLinkEntry cannot be null", null, null, null)
        }
        val deepLinkUri = DeepLinkUri.parse(uriString)
        val parameterMap = deepLinkEntry.getParameters(uriString)
        for (queryParameter in deepLinkUri!!.queryParameterNames()) {
            for (queryParameterValue in deepLinkUri.queryParameterValues(queryParameter)) {
                if (parameterMap.containsKey(queryParameter)) {
                    Log.w(TAG, "Duplicate parameter name in path and query param: $queryParameter")
                }
                parameterMap[queryParameter] = queryParameterValue
            }
        }
        parameterMap[DeepLink.URI] = uri.toString()
        val parameters: Bundle = when {
            sourceIntent.extras != null -> Bundle(sourceIntent.extras)
            else -> Bundle()
        }
        for ((key, value) in parameterMap) {
            parameters.putString(key, value)
        }
        try {
            val c = deepLinkEntry.activityClass
            var newIntent: Intent? = null
            var taskStackBuilder: TaskStackBuilder? = null
            when {
                deepLinkEntry.type == DeepLinkEntry.Type.CLASS -> newIntent = Intent(activity, c)
                else -> {
                    var method: Method
                    val errorResult = DeepLinkResult(false, uriString,
                            "Could not deep link to method: " + deepLinkEntry.method + " intents length == 0", null, null, deepLinkEntry)
                    try {
                        method = c.getMethod(deepLinkEntry.method, Context::class.java)
                        if (method.returnType == TaskStackBuilder::class.java) {
                            taskStackBuilder = method.invoke(c, activity) as TaskStackBuilder
                            if (taskStackBuilder.intentCount == 0) {
                                return errorResult
                            }
                            newIntent = taskStackBuilder.editIntentAt(taskStackBuilder.intentCount - 1)
                        } else {
                            newIntent = method.invoke(c, activity) as Intent
                        }
                    } catch (exception: NoSuchMethodException) {
                        method = c.getMethod(deepLinkEntry.method, Context::class.java, Bundle::class.java)
                        if (method.returnType == TaskStackBuilder::class.java) {
                            taskStackBuilder = method.invoke(c, activity, parameters) as TaskStackBuilder
                            if (taskStackBuilder.intentCount == 0) {
                                return errorResult
                            }
                            newIntent = taskStackBuilder.editIntentAt(taskStackBuilder.intentCount - 1)
                        } else {
                            newIntent = method.invoke(c, activity, parameters) as Intent
                        }
                    }

                }
            }
            if (newIntent == null) {
                return DeepLinkResult(false, uriString, "Destination Intent is null!", null,
                        taskStackBuilder, deepLinkEntry)
            }
            if (newIntent.action == null) {
                newIntent.action = sourceIntent.action
            }
            if (newIntent.data == null) {
                newIntent.data = sourceIntent.data
            }
            newIntent.putExtras(parameters)
            newIntent.putExtra(DeepLink.IS_DEEP_LINK, true)
            newIntent.putExtra(DeepLink.REFERRER_URI, uri)
            if (activity.callingActivity != null) {
                newIntent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
            }
            return DeepLinkResult(true, uriString, "", newIntent, taskStackBuilder, deepLinkEntry)
        } catch (exception: NoSuchMethodException) {
            return DeepLinkResult(false, uriString, "Deep link to non-existent method: " + deepLinkEntry.method, null, null, deepLinkEntry)
        } catch (exception: IllegalAccessException) {
            return DeepLinkResult(false, uriString, "Could not deep link to method: " + deepLinkEntry.method, null, null, deepLinkEntry)
        } catch (exception: InvocationTargetException) {
            return DeepLinkResult(false, uriString, "Could not deep link to method: " + deepLinkEntry.method, null, null, deepLinkEntry)
        }

    }

    fun supportsUri(uriString: String): Boolean {
        return findEntry(uriString) != null
    }

    companion object {

        protected val TAG = "DeepLinkDelegate"

        private fun notifyListener(context: Context, isError: Boolean, uri: Uri?,
                                   uriTemplate: String?, errorMessage: String?) {
            val intent = Intent()
            intent.action = DeepLinkHandler.ACTION
            intent.putExtra(DeepLinkHandler.EXTRA_URI, uri?.toString() ?: "")
            intent.putExtra(DeepLinkHandler.EXTRA_URI_TEMPLATE, uriTemplate ?: "")
            intent.putExtra(DeepLinkHandler.EXTRA_SUCCESSFUL, !isError)
            if (isError) {
                intent.putExtra(DeepLinkHandler.EXTRA_ERROR_MESSAGE, errorMessage)
            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }
    }
}
