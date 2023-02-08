/*
 * Copyright (C) 2015 Airbnb, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.airbnb.deeplinkdispatch.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.TaskStackBuilder
import com.airbnb.deeplinkdispatch.DeepLink
import com.airbnb.deeplinkdispatch.DeepLinkMethodResult

@DeepLink(
    "dld://classDeepLink",
    "http://example.com/foo{arg_end}",
    "http://example.com/{arg_start}bar",
    "dld://example.com/deepLink",
    "https://www.example.com/<configurable-path-segment>/bar",
    "https://www.example.com/<configurable-path-segment-one>/<configurable-path-segment-two>/foo",
    "https://www.example.com/cereal/<configurable-path-segment>",
    "https://www.example.com/nothing-special"
)
class MainActivity : AppCompatActivity() {

    object InnerClass {
        @DeepLink("dld://innerClassDeeplink")
        @JvmStatic
        fun intentForDeepLinkMethod(context: Context): Intent {
            return Intent(context, MainActivity::class.java).setAction(ACTION_DEEP_LINK_INNER)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_activity_main)
        val intent = intent
        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            var toastMessage: String
            val parameters = intent.extras
            Log.d(TAG, "Deeplink params: $parameters")
            toastMessage = if (ACTION_DEEP_LINK_METHOD == intent.action) {
                "method with param1:" + parameters!!.getString("param1")
            } else if (ACTION_DEEP_LINK_COMPLEX == intent.action) {
                parameters?.getString("arbitraryNumber").orEmpty()
            } else if (ACTION_DEEP_LINK_INNER == intent.action) {
                "Deeplink on method in inner class"
            } else if (parameters!!.containsKey("arg")) {
                "class and found arg:" + parameters.getString("arg")
            } else {
                "class"
            }

            // You can pass a query parameter with the URI, and it's also in parameters, like
            // dld://classDeepLink?qp=123
            if (parameters!!.containsKey("qp")) {
                toastMessage += " with query parameter " + parameters.getString("qp")
            }
            val referrer = ActivityCompat.getReferrer(this)
            if (referrer != null) {
                toastMessage += " and referrer: $referrer"
            }
            showToast(toastMessage)
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, "Deep Link: $message", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val ACTION_DEEP_LINK_METHOD = "deep_link_method"
        const val ACTION_DEEP_LINK_COMPLEX = "deep_link_complex"
        const val ACTION_DEEP_LINK_INNER = "deep_link_inner"
        const val ACTION_DEEP_LINK_INTENT = "deep_link_intent"
        const val ACTION_DEEP_LINK_TASK_STACK_BUILDER = "deep_link_taskstackbuilder"
        const val ACTION_DEEP_LINK_INTENT_AND_TASK_STACK_BUILDER =
            "deep_link_intent_and_taskstackbuilder"
        val TAG = MainActivity::class.java.simpleName
    }
}

object MainActivityDeeplinks {
    /**
     * Handles deep link with one param, doc does not contain "param"
     * @return A intent to start [MainActivity]
     */
    @DeepLink("dld://methodDeepLink/{param1}")
    @JvmStatic
    fun intentForDeepLinkMethod(context: Context?): Intent {
        return Intent(
            context,
            MainActivity::class.java
        ).setAction(MainActivity.ACTION_DEEP_LINK_METHOD)
    }

    @DeepLink("dld://methodDeepLinkOverride/{param1}")
    @JvmStatic
    fun intentForDeepLinkMethodOverrideParam(context: Context?): Intent {
        return Intent(context, MainActivity::class.java)
            .setAction(MainActivity.ACTION_DEEP_LINK_INTENT)
            .putExtra("param1", "set_in_app")
            .putExtra("queryParam", "set_in_app")
    }

    @DeepLink("dld://host/somePath/{arbitraryNumber}")
    @JvmStatic
    fun intentForParamDeepLinkMethod(context: Context?): Intent {
        return Intent(
            context,
            MainActivity::class.java
        ).setAction(MainActivity.ACTION_DEEP_LINK_COMPLEX)
    }

    /**
     * Handles deep link with params.
     * @param context of the activity
     * @param bundle expected to contain the key `qp`.
     * @return TaskStackBuilder set with first intent to start [MainActivity] and second intent
     * to start [SecondActivity].
     */
    @DeepLink("http://example.com/deepLink/{id}/{name}/{place}")
    @JvmStatic
    fun intentForTaskStackBuilderMethods(
        context: Context,
        bundle: Bundle?
    ): TaskStackBuilder {
        Log.d(MainActivity.TAG, "without query parameter :")
        if (bundle != null && bundle.containsKey("qp")) {
            Log.d(
                MainActivity.TAG,
                "found new parameter :with query parameter :" + bundle.getString("qp")
            )
        }
        return getTaskStackBuilder(context, MainActivity.ACTION_DEEP_LINK_COMPLEX)
    }

    private fun getTaskStackBuilder(context: Context, action: String): TaskStackBuilder {
        val detailsIntent = Intent(context, SecondActivity::class.java).setAction(action)
        val parentIntent = Intent(context, MainActivity::class.java).setAction(action)
        val taskStackBuilder = TaskStackBuilder.create(context)
        taskStackBuilder.addNextIntent(parentIntent)
        taskStackBuilder.addNextIntent(detailsIntent)
        return taskStackBuilder
    }

    @DeepLink("dld://host/somePathOne/{arbitraryNumber}/otherPath")
    @JvmStatic
    fun intentForComplexMethod(context: Context?, bundle: Bundle?): Intent {
        if (bundle != null && bundle.containsKey("qp")) {
            Log.d(
                MainActivity.TAG,
                "found new parameter :with query parameter :" + bundle.getString("qp")
            )
        }
        return Intent(context, MainActivity::class.java)
    }

    @DeepLink("dld://host/methodResult/intent")
    @JvmStatic
    fun intentViaDeeplinkMethodResult(context: Context?): DeepLinkMethodResult {
        return DeepLinkMethodResult(
            Intent(context, SecondActivity::class.java)
                .setAction(MainActivity.ACTION_DEEP_LINK_INTENT),
            null
        )
    }

    @DeepLink("dld://host/methodResultFinalParameter/intent")
    @JvmStatic
    fun intentViaDeeplinkMethodResultWithFinalParameter(context: Context?): DeepLinkMethodResult {
        return DeepLinkMethodResult(
            Intent(context, SecondActivity::class.java)
                .setAction(MainActivity.ACTION_DEEP_LINK_INTENT)
                .putExtra("finalExtra", "set in app"),
            null
        )
    }

    @DeepLink("dld://host/methodResult/intent/{parameter}")
    @JvmStatic
    fun intentViaDeeplinkMethodResult(
        context: Context?,
        parameter: Bundle?
    ): DeepLinkMethodResult {
        return DeepLinkMethodResult(
            Intent(context, SecondActivity::class.java)
                .setAction(MainActivity.ACTION_DEEP_LINK_INTENT),
            null
        )
    }

    @DeepLink("dld://host/methodResult/taskStackBuilder")
    @JvmStatic
    fun taskStackBuilderViaDeeplinkMethodResult(context: Context): DeepLinkMethodResult {
        return DeepLinkMethodResult(
            null,
            getTaskStackBuilder(context, MainActivity.ACTION_DEEP_LINK_TASK_STACK_BUILDER)
        )
    }

    @DeepLink("dld://host/methodResult/intentAndTaskStackBuilder")
    @JvmStatic
    fun taskStackBuilderAndIntentViaDeeplinkMethodResult(context: Context): DeepLinkMethodResult {
        return DeepLinkMethodResult(
            Intent(context, SecondActivity::class.java)
                .setAction(MainActivity.ACTION_DEEP_LINK_INTENT_AND_TASK_STACK_BUILDER),
            getTaskStackBuilder(
                context,
                MainActivity.ACTION_DEEP_LINK_INTENT_AND_TASK_STACK_BUILDER
            )
        )
    }

    @DeepLink("dld://host/methodResult/null")
    @JvmStatic
    fun nullDeepLinkMethiodResult(context: Context?): DeepLinkMethodResult? {
        return null
    }

    @DeepLink("dld://host/taskStackBuilder/null")
    @JvmStatic
    fun nullTaskStackBuilder(context: Context?): TaskStackBuilder? {
        return null
    }

    @DeepLink("dld://host/intent/null")
    @JvmStatic
    fun nullIntent(context: Context?): Intent? {
        return null
    }

    /**
     * This will not create an error during index creation but could be found by writing a test
     */
    @DeepLink("dld://host/intent/{abc}")
    @JvmStatic
    fun sampleDuplicatedUrlWithDifferentPlaceholderName1(context: Context?): Intent? {
        return null
    }

    /**
     * This will not create an error during index creation but could be found by writing a test
     */
    @DeepLink("dld://host/intent/{def}")
    @JvmStatic
    fun sampleDuplicatedUrlWithDifferentPlaceholderName2(context: Context?): Intent? {
        return null
    }

    /**
     * This method is a less concrete match for the URI
     * dld://host/somePathOne/somePathTwo/somePathThree to a annotated method in `sample-library`
     * that is annotated with @DeepLink("dld://host/somePathOne/somePathTwo/somePathThree") and thus
     * will always be picked over this method when matching.
     *
     * @param context
     * @param bundle
     * @return
     */
    @DeepLink("placeholder://host/somePathOne/{param1}/somePathThree")
    @JvmStatic
    fun lessConcreteMatch(context: Context?, bundle: Bundle?): Intent {
        if (bundle != null && bundle.containsKey("param1")) {
            Log.d(
                MainActivity.TAG,
                "matched less concrete url in sample project :" + bundle.getString("param1")
            )
        }
        return Intent(
            context,
            MainActivity::class.java
        ).setAction(MainActivity.ACTION_DEEP_LINK_METHOD)
    }

    @DeepLink("placeholder://host/{param1}/somePathTwoAlt/somePathThreeAlt")
    public static Intent lessConcreteMatchAlt(Context context, Bundle bundle) {
    if (bundle != null && bundle.containsKey("param1")) {
      Log.d(TAG, "matched less concrete url in sample project :" + bundle.getString("param1"));
    }
    return new Intent(context, MainActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
    }

    @DeepLink("placeholder://host/{param1}/{param2}/{param3}")
    public static Intent lessConcreteMatchMany(Context context, Bundle bundle) {
    if (bundle != null && bundle.containsKey("param1")) {
      Log.d(TAG, "matched less concrete url in sample project :" + bundle.getString("param1"));
    }
    return new Intent(context, MainActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
    }
}
