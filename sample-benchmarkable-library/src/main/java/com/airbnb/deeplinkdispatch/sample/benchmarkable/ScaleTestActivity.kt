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
package com.airbnb.deeplinkdispatch.sample.benchmarkable

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.airbnb.deeplinkdispatch.DeepLink

/**
 * Activity with 2k deeplinks to test DLD performance at scale
 */
class ScaleTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scale_activity_main)

        val intent = intent
        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            val parameters = intent.extras
            Log.d(TAG, "Deeplink params: $parameters")

            var toastMessage = when {
                ACTION_DEEP_LINK_METHOD == intent.action -> "method with param1:" + parameters?.getString("param1")
                ACTION_DEEP_LINK_COMPLEX == intent.action -> parameters?.getString("arbitraryNumber") ?: ""
                parameters?.containsKey("arg") == true -> "class and found arg:" + parameters.getString("arg")
                else -> "class"
            }

            // You can pass a query parameter with the URI, and it's also in parameters, like
            // dld://classDeepLink?qp=123
            if (parameters?.containsKey("qp") == true) {
                toastMessage += " with query parameter " + parameters.getString("qp")
            }
            val referrer = ActivityCompat.getReferrer(this)
            if (referrer != null) {
                toastMessage += " and referrer: $referrer"
            }

            showToast(toastMessage)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, "Deep Link: $message", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ACTION_DEEP_LINK_METHOD = "deep_link_method"
        private const val ACTION_DEEP_LINK_COMPLEX = "deep_link_complex"
        private val TAG = ScaleTestActivity::class.java.simpleName

        @DeepLink(value = ["dld://methodDeepLink1/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink2/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod2(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink3/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod3(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink4/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod4(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink5/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod5(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink6/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod6(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink7/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod7(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink8/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod8(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink9/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod9(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink10/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod10(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink11/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod11(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink12/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod12(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink13/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod13(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink14/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod14(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink15/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod15(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink16/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod16(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink17/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod17(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink18/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod18(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink19/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod19(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink20/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod20(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink21/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod21(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink22/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod22(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink23/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod23(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink24/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod24(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink25/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod25(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink26/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod26(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink27/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod27(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink28/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod28(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink29/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod29(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink30/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod30(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink31/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod31(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink32/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod32(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink33/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod33(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink34/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod34(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink35/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod35(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink36/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod36(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink37/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod37(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink38/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod38(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink39/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod39(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink40/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod40(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink41/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod41(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink42/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod42(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink43/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod43(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink44/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod44(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink45/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod45(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink46/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod46(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink47/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod47(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink48/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod48(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink49/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod49(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink50/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod50(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink51/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod51(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink52/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod52(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink53/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod53(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink54/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod54(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink55/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod55(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink56/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod56(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink57/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod57(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink58/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod58(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink59/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod59(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink60/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod60(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink61/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod61(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink62/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod62(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink63/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod63(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink64/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod64(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink65/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod65(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink66/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod66(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink67/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod67(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink68/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod68(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink69/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod69(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink70/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod70(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink71/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod71(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink72/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod72(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink73/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod73(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink74/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod74(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink75/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod75(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink76/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod76(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink77/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod77(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink78/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod78(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink79/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod79(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink80/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod80(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink81/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod81(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink82/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod82(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink83/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod83(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink84/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod84(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink85/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod85(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink86/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod86(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink87/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod87(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink88/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod88(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink89/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod89(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink90/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod90(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink91/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod91(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink92/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod92(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink93/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod93(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink94/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod94(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink95/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod95(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink96/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod96(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink97/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod97(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink98/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod98(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink99/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod99(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink100/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod100(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink101/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod101(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink102/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod102(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink103/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod103(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink104/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod104(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink105/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod105(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink106/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod106(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink107/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod107(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink108/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod108(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink109/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod109(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink110/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod110(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink111/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod111(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink112/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod112(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink113/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod113(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink114/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod114(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink115/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod115(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink116/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod116(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink117/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod117(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink118/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod118(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink119/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod119(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink120/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod120(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink121/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod121(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink122/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod122(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink123/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod123(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink124/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod124(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink125/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod125(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink126/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod126(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink127/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod127(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink128/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod128(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink129/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod129(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink130/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod130(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink131/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod131(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink132/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod132(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink133/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod133(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink134/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod134(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink135/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod135(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink136/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod136(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink137/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod137(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink138/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod138(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink139/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod139(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink140/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod140(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink141/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod141(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink142/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod142(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink143/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod143(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink144/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod144(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink145/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod145(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink146/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod146(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink147/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod147(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink148/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod148(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink149/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod149(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink150/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod150(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink151/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod151(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink152/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod152(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink153/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod153(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink154/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod154(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink155/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod155(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink156/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod156(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink157/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod157(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink158/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod158(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink159/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod159(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink160/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod160(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink161/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod161(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink162/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod162(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink163/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod163(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink164/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod164(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink165/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod165(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink166/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod166(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink167/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod167(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink168/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod168(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink169/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod169(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink170/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod170(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink171/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod171(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink172/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod172(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink173/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod173(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink174/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod174(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink175/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod175(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink176/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod176(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink177/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod177(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink178/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod178(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink179/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod179(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink180/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod180(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink181/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod181(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink182/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod182(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink183/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod183(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink184/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod184(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink185/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod185(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink186/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod186(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink187/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod187(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink188/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod188(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink189/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod189(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink190/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod190(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink191/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod191(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink192/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod192(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink193/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod193(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink194/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod194(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink195/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod195(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink196/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod196(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink197/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod197(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink198/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod198(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink199/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod199(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink200/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod200(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink201/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod201(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink202/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod202(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink203/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod203(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink204/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod204(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink205/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod205(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink206/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod206(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink207/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod207(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink208/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod208(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink209/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod209(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink210/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod210(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink211/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod211(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink212/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod212(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink213/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod213(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink214/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod214(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink215/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod215(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink216/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod216(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink217/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod217(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink218/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod218(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink219/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod219(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink220/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod220(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink221/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod221(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink222/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod222(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink223/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod223(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink224/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod224(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink225/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod225(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink226/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod226(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink227/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod227(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink228/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod228(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink229/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod229(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink230/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod230(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink231/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod231(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink232/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod232(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink233/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod233(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink234/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod234(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink235/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod235(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink236/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod236(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink237/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod237(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink238/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod238(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink239/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod239(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink240/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod240(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink241/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod241(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink242/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod242(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink243/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod243(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink244/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod244(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink245/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod245(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink246/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod246(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink247/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod247(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink248/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod248(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink249/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod249(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink250/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod250(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink251/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod251(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink252/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod252(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink253/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod253(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink254/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod254(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink255/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod255(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink256/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod256(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink257/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod257(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink258/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod258(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink259/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod259(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink260/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod260(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink261/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod261(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink262/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod262(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink263/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod263(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink264/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod264(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink265/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod265(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink266/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod266(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink267/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod267(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink268/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod268(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink269/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod269(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink270/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod270(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink271/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod271(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink272/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod272(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink273/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod273(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink274/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod274(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink275/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod275(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink276/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod276(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink277/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod277(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink278/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod278(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink279/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod279(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink280/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod280(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink281/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod281(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink282/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod282(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink283/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod283(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink284/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod284(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink285/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod285(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink286/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod286(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink287/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod287(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink288/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod288(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink289/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod289(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink290/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod290(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink291/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod291(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink292/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod292(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink293/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod293(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink294/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod294(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink295/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod295(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink296/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod296(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink297/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod297(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink298/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod298(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink299/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod299(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink300/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod300(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink301/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod301(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink302/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod302(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink303/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod303(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink304/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod304(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink305/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod305(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink306/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod306(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink307/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod307(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink308/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod308(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink309/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod309(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink310/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod310(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink311/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod311(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink312/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod312(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink313/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod313(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink314/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod314(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink315/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod315(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink316/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod316(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink317/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod317(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink318/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod318(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink319/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod319(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink320/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod320(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink321/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod321(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink322/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod322(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink323/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod323(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink324/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod324(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink325/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod325(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink326/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod326(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink327/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod327(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink328/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod328(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink329/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod329(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink330/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod330(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink331/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod331(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink332/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod332(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink333/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod333(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink334/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod334(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink335/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod335(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink336/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod336(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink337/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod337(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink338/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod338(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink339/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod339(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink340/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod340(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink341/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod341(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink342/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod342(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink343/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod343(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink344/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod344(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink345/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod345(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink346/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod346(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink347/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod347(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink348/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod348(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink349/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod349(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink350/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod350(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink351/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod351(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink352/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod352(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink353/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod353(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink354/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod354(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink355/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod355(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink356/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod356(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink357/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod357(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink358/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod358(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink359/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod359(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink360/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod360(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink361/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod361(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink362/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod362(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink363/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod363(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink364/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod364(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink365/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod365(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink366/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod366(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink367/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod367(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink368/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod368(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink369/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod369(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink370/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod370(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink371/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod371(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink372/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod372(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink373/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod373(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink374/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod374(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink375/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod375(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink376/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod376(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink377/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod377(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink378/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod378(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink379/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod379(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink380/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod380(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink381/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod381(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink382/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod382(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink383/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod383(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink384/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod384(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink385/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod385(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink386/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod386(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink387/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod387(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink388/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod388(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink389/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod389(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink390/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod390(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink391/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod391(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink392/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod392(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink393/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod393(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink394/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod394(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink395/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod395(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink396/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod396(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink397/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod397(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink398/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod398(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink399/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod399(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink400/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod400(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink401/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod401(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink402/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod402(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink403/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod403(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink404/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod404(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink405/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod405(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink406/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod406(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink407/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod407(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink408/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod408(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink409/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod409(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink410/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod410(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink411/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod411(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink412/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod412(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink413/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod413(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink414/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod414(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink415/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod415(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink416/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod416(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink417/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod417(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink418/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod418(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink419/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod419(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink420/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod420(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink421/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod421(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink422/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod422(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink423/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod423(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink424/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod424(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink425/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod425(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink426/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod426(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink427/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod427(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink428/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod428(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink429/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod429(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink430/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod430(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink431/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod431(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink432/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod432(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink433/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod433(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink434/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod434(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink435/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod435(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink436/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod436(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink437/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod437(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink438/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod438(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink439/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod439(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink440/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod440(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink441/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod441(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink442/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod442(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink443/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod443(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink444/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod444(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink445/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod445(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink446/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod446(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink447/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod447(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink448/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod448(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink449/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod449(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink450/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod450(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink451/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod451(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink452/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod452(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink453/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod453(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink454/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod454(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink455/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod455(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink456/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod456(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink457/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod457(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink458/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod458(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink459/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod459(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink460/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod460(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink461/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod461(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink462/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod462(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink463/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod463(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink464/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod464(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink465/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod465(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink466/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod466(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink467/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod467(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink468/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod468(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink469/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod469(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink470/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod470(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink471/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod471(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink472/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod472(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink473/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod473(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink474/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod474(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink475/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod475(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink476/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod476(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink477/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod477(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink478/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod478(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink479/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod479(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink480/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod480(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink481/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod481(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink482/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod482(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink483/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod483(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink484/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod484(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink485/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod485(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink486/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod486(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink487/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod487(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink488/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod488(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink489/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod489(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink490/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod490(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink491/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod491(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink492/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod492(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink493/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod493(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink494/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod494(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink495/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod495(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink496/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod496(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink497/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod497(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink498/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod498(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink499/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod499(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink500/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod500(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink501/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod501(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink502/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod502(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink503/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod503(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink504/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod504(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink505/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod505(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink506/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod506(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink507/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod507(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink508/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod508(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink509/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod509(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink510/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod510(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink511/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod511(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink512/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod512(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink513/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod513(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink514/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod514(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink515/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod515(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink516/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod516(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink517/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod517(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink518/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod518(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink519/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod519(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink520/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod520(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink521/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod521(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink522/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod522(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink523/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod523(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink524/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod524(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink525/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod525(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink526/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod526(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink527/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod527(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink528/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod528(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink529/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod529(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink530/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod530(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink531/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod531(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink532/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod532(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink533/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod533(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink534/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod534(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink535/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod535(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink536/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod536(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink537/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod537(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink538/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod538(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink539/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod539(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink540/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod540(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink541/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod541(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink542/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod542(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink543/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod543(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink544/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod544(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink545/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod545(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink546/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod546(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink547/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod547(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink548/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod548(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink549/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod549(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink550/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod550(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink551/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod551(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink552/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod552(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink553/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod553(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink554/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod554(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink555/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod555(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink556/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod556(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink557/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod557(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink558/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod558(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink559/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod559(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink560/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod560(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink561/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod561(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink562/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod562(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink563/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod563(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink564/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod564(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink565/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod565(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink566/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod566(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink567/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod567(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink568/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod568(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink569/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod569(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink570/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod570(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink571/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod571(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink572/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod572(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink573/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod573(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink574/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod574(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink575/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod575(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink576/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod576(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink577/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod577(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink578/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod578(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink579/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod579(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink580/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod580(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink581/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod581(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink582/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod582(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink583/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod583(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink584/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod584(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink585/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod585(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink586/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod586(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink587/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod587(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink588/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod588(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink589/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod589(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink590/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod590(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink591/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod591(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink592/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod592(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink593/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod593(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink594/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod594(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink595/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod595(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink596/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod596(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink597/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod597(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink598/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod598(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink599/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod599(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink600/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod600(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink601/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod601(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink602/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod602(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink603/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod603(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink604/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod604(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink605/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod605(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink606/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod606(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink607/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod607(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink608/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod608(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink609/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod609(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink610/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod610(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink611/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod611(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink612/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod612(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink613/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod613(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink614/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod614(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink615/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod615(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink616/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod616(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink617/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod617(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink618/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod618(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink619/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod619(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink620/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod620(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink621/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod621(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink622/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod622(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink623/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod623(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink624/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod624(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink625/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod625(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink626/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod626(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink627/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod627(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink628/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod628(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink629/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod629(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink630/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod630(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink631/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod631(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink632/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod632(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink633/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod633(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink634/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod634(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink635/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod635(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink636/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod636(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink637/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod637(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink638/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod638(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink639/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod639(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink640/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod640(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink641/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod641(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink642/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod642(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink643/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod643(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink644/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod644(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink645/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod645(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink646/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod646(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink647/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod647(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink648/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod648(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink649/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod649(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink650/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod650(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink651/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod651(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink652/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod652(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink653/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod653(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink654/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod654(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink655/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod655(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink656/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod656(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink657/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod657(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink658/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod658(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink659/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod659(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink660/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod660(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink661/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod661(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink662/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod662(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink663/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod663(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink664/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod664(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink665/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod665(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink666/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod666(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink667/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod667(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink668/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod668(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink669/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod669(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink670/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod670(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink671/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod671(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink672/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod672(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink673/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod673(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink674/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod674(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink675/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod675(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink676/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod676(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink677/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod677(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink678/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod678(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink679/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod679(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink680/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod680(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink681/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod681(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink682/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod682(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink683/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod683(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink684/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod684(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink685/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod685(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink686/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod686(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink687/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod687(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink688/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod688(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink689/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod689(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink690/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod690(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink691/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod691(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink692/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod692(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink693/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod693(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink694/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod694(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink695/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod695(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink696/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod696(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink697/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod697(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink698/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod698(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink699/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod699(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink700/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod700(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink701/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod701(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink702/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod702(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink703/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod703(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink704/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod704(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink705/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod705(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink706/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod706(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink707/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod707(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink708/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod708(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink709/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod709(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink710/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod710(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink711/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod711(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink712/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod712(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink713/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod713(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink714/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod714(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink715/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod715(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink716/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod716(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink717/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod717(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink718/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod718(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink719/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod719(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink720/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod720(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink721/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod721(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink722/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod722(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink723/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod723(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink724/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod724(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink725/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod725(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink726/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod726(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink727/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod727(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink728/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod728(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink729/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod729(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink730/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod730(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink731/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod731(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink732/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod732(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink733/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod733(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink734/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod734(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink735/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod735(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink736/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod736(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink737/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod737(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink738/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod738(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink739/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod739(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink740/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod740(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink741/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod741(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink742/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod742(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink743/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod743(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink744/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod744(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink745/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod745(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink746/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod746(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink747/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod747(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink748/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod748(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink749/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod749(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink750/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod750(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink751/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod751(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink752/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod752(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink753/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod753(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink754/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod754(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink755/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod755(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink756/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod756(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink757/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod757(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink758/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod758(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink759/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod759(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink760/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod760(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink761/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod761(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink762/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod762(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink763/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod763(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink764/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod764(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink765/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod765(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink766/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod766(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink767/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod767(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink768/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod768(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink769/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod769(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink770/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod770(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink771/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod771(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink772/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod772(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink773/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod773(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink774/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod774(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink775/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod775(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink776/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod776(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink777/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod777(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink778/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod778(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink779/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod779(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink780/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod780(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink781/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod781(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink782/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod782(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink783/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod783(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink784/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod784(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink785/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod785(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink786/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod786(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink787/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod787(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink788/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod788(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink789/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod789(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink790/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod790(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink791/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod791(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink792/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod792(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink793/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod793(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink794/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod794(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink795/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod795(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink796/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod796(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink797/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod797(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink798/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod798(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink799/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod799(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink800/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod800(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink801/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod801(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink802/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod802(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink803/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod803(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink804/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod804(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink805/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod805(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink806/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod806(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink807/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod807(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink808/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod808(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink809/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod809(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink810/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod810(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink811/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod811(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink812/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod812(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink813/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod813(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink814/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod814(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink815/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod815(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink816/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod816(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink817/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod817(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink818/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod818(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink819/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod819(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink820/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod820(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink821/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod821(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink822/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod822(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink823/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod823(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink824/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod824(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink825/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod825(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink826/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod826(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink827/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod827(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink828/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod828(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink829/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod829(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink830/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod830(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink831/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod831(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink832/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod832(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink833/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod833(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink834/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod834(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink835/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod835(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink836/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod836(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink837/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod837(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink838/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod838(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink839/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod839(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink840/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod840(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink841/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod841(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink842/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod842(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink843/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod843(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink844/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod844(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink845/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod845(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink846/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod846(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink847/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod847(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink848/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod848(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink849/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod849(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink850/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod850(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink851/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod851(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink852/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod852(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink853/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod853(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink854/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod854(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink855/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod855(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink856/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod856(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink857/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod857(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink858/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod858(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink859/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod859(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink860/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod860(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink861/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod861(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink862/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod862(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink863/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod863(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink864/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod864(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink865/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod865(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink866/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod866(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink867/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod867(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink868/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod868(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink869/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod869(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink870/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod870(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink871/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod871(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink872/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod872(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink873/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod873(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink874/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod874(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink875/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod875(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink876/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod876(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink877/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod877(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink878/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod878(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink879/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod879(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink880/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod880(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink881/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod881(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink882/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod882(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink883/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod883(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink884/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod884(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink885/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod885(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink886/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod886(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink887/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod887(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink888/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod888(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink889/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod889(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink890/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod890(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink891/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod891(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink892/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod892(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink893/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod893(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink894/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod894(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink895/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod895(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink896/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod896(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink897/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod897(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink898/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod898(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink899/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod899(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink900/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod900(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink901/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod901(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink902/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod902(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink903/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod903(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink904/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod904(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink905/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod905(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink906/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod906(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink907/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod907(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink908/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod908(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink909/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod909(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink910/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod910(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink911/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod911(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink912/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod912(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink913/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod913(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink914/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod914(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink915/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod915(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink916/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod916(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink917/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod917(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink918/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod918(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink919/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod919(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink920/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod920(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink921/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod921(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink922/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod922(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink923/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod923(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink924/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod924(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink925/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod925(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink926/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod926(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink927/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod927(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink928/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod928(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink929/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod929(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink930/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod930(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink931/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod931(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink932/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod932(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink933/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod933(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink934/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod934(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink935/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod935(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink936/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod936(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink937/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod937(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink938/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod938(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink939/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod939(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink940/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod940(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink941/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod941(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink942/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod942(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink943/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod943(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink944/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod944(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink945/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod945(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink946/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod946(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink947/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod947(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink948/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod948(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink949/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod949(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink950/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod950(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink951/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod951(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink952/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod952(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink953/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod953(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink954/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod954(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink955/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod955(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink956/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod956(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink957/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod957(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink958/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod958(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink959/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod959(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink960/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod960(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink961/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod961(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink962/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod962(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink963/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod963(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink964/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod964(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink965/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod965(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink966/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod966(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink967/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod967(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink968/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod968(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink969/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod969(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink970/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod970(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink971/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod971(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink972/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod972(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink973/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod973(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink974/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod974(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink975/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod975(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink976/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod976(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink977/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod977(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink978/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod978(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink979/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod979(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink980/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod980(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink981/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod981(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink982/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod982(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink983/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod983(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink984/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod984(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink985/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod985(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink986/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod986(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink987/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod987(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink988/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod988(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink989/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod989(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink990/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod990(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink991/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod991(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink992/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod992(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink993/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod993(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink994/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod994(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink995/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod995(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink996/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod996(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink997/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod997(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink998/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod998(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink999/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod999(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1000/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1000(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1001/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1001(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1002/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1002(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1003/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1003(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1004/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1004(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1005/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1005(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1006/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1006(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1007/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1007(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1008/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1008(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1009/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1009(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1010/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1010(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1011/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1011(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1012/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1012(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1013/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1013(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1014/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1014(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1015/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1015(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1016/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1016(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1017/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1017(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1018/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1018(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1019/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1019(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1020/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1020(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1021/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1021(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1022/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1022(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1023/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1023(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1024/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1024(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1025/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1025(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1026/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1026(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1027/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1027(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1028/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1028(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1029/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1029(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1030/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1030(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1031/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1031(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1032/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1032(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1033/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1033(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1034/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1034(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1035/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1035(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1036/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1036(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1037/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1037(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1038/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1038(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1039/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1039(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1040/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1040(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1041/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1041(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1042/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1042(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1043/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1043(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1044/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1044(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1045/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1045(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1046/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1046(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1047/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1047(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1048/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1048(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1049/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1049(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1050/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1050(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1051/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1051(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1052/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1052(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1053/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1053(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1054/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1054(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1055/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1055(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1056/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1056(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1057/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1057(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1058/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1058(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1059/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1059(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1060/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1060(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1061/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1061(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1062/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1062(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1063/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1063(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1064/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1064(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1065/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1065(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1066/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1066(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1067/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1067(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1068/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1068(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1069/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1069(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1070/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1070(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1071/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1071(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1072/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1072(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1073/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1073(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1074/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1074(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1075/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1075(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1076/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1076(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1077/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1077(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1078/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1078(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1079/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1079(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1080/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1080(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1081/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1081(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1082/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1082(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1083/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1083(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1084/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1084(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1085/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1085(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1086/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1086(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1087/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1087(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1088/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1088(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1089/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1089(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1090/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1090(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1091/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1091(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1092/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1092(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1093/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1093(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1094/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1094(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1095/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1095(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1096/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1096(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1097/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1097(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1098/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1098(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1099/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1099(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1100/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1100(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1101/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1101(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1102/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1102(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1103/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1103(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1104/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1104(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1105/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1105(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1106/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1106(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1107/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1107(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1108/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1108(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1109/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1109(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1110/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1110(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1111/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1111(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1112/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1112(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1113/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1113(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1114/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1114(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1115/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1115(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1116/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1116(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1117/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1117(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1118/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1118(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1119/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1119(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1120/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1120(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1121/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1121(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1122/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1122(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1123/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1123(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1124/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1124(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1125/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1125(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1126/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1126(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1127/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1127(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1128/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1128(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1129/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1129(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1130/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1130(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1131/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1131(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1132/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1132(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1133/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1133(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1134/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1134(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1135/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1135(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1136/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1136(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1137/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1137(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1138/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1138(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1139/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1139(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1140/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1140(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1141/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1141(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1142/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1142(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1143/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1143(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1144/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1144(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1145/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1145(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1146/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1146(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1147/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1147(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1148/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1148(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1149/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1149(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1150/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1150(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1151/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1151(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1152/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1152(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1153/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1153(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1154/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1154(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1155/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1155(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1156/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1156(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1157/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1157(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1158/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1158(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1159/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1159(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1160/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1160(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1161/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1161(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1162/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1162(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1163/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1163(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1164/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1164(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1165/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1165(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1166/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1166(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1167/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1167(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1168/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1168(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1169/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1169(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1170/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1170(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1171/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1171(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1172/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1172(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1173/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1173(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1174/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1174(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1175/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1175(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1176/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1176(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1177/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1177(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1178/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1178(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1179/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1179(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1180/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1180(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1181/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1181(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1182/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1182(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1183/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1183(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1184/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1184(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1185/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1185(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1186/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1186(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1187/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1187(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1188/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1188(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1189/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1189(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1190/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1190(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1191/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1191(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1192/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1192(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1193/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1193(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1194/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1194(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1195/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1195(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1196/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1196(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1197/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1197(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1198/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1198(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1199/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1199(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1200/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1200(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1201/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1201(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1202/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1202(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1203/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1203(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1204/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1204(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1205/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1205(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1206/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1206(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1207/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1207(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1208/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1208(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1209/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1209(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1210/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1210(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1211/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1211(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1212/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1212(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1213/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1213(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1214/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1214(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1215/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1215(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1216/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1216(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1217/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1217(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1218/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1218(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1219/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1219(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1220/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1220(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1221/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1221(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1222/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1222(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1223/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1223(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1224/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1224(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1225/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1225(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1226/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1226(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1227/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1227(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1228/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1228(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1229/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1229(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1230/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1230(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1231/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1231(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1232/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1232(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1233/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1233(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1234/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1234(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1235/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1235(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1236/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1236(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1237/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1237(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1238/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1238(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1239/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1239(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1240/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1240(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1241/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1241(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1242/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1242(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1243/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1243(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1244/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1244(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1245/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1245(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1246/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1246(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1247/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1247(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1248/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1248(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1249/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1249(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1250/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1250(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1251/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1251(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1252/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1252(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1253/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1253(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1254/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1254(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1255/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1255(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1256/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1256(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1257/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1257(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1258/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1258(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1259/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1259(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1260/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1260(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1261/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1261(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1262/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1262(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1263/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1263(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1264/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1264(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1265/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1265(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1266/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1266(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1267/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1267(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1268/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1268(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1269/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1269(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1270/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1270(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1271/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1271(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1272/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1272(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1273/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1273(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1274/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1274(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1275/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1275(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1276/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1276(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1277/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1277(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1278/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1278(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1279/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1279(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1280/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1280(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1281/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1281(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1282/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1282(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1283/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1283(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1284/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1284(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1285/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1285(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1286/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1286(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1287/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1287(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1288/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1288(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1289/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1289(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1290/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1290(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1291/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1291(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1292/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1292(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1293/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1293(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1294/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1294(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1295/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1295(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1296/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1296(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1297/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1297(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1298/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1298(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1299/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1299(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1300/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1300(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1301/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1301(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1302/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1302(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1303/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1303(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1304/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1304(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1305/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1305(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1306/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1306(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1307/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1307(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1308/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1308(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1309/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1309(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1310/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1310(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1311/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1311(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1312/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1312(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1313/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1313(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1314/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1314(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1315/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1315(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1316/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1316(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1317/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1317(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1318/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1318(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1319/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1319(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1320/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1320(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1321/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1321(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1322/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1322(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1323/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1323(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1324/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1324(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1325/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1325(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1326/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1326(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1327/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1327(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1328/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1328(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1329/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1329(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1330/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1330(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1331/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1331(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1332/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1332(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1333/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1333(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1334/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1334(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1335/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1335(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1336/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1336(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1337/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1337(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1338/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1338(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1339/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1339(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1340/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1340(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1341/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1341(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1342/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1342(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1343/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1343(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1344/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1344(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1345/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1345(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1346/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1346(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1347/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1347(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1348/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1348(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1349/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1349(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1350/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1350(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1351/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1351(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1352/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1352(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1353/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1353(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1354/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1354(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1355/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1355(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1356/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1356(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1357/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1357(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1358/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1358(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1359/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1359(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1360/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1360(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1361/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1361(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1362/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1362(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1363/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1363(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1364/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1364(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1365/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1365(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1366/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1366(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1367/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1367(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1368/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1368(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1369/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1369(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1370/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1370(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1371/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1371(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1372/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1372(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1373/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1373(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1374/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1374(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1375/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1375(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1376/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1376(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1377/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1377(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1378/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1378(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1379/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1379(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1380/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1380(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1381/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1381(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1382/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1382(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1383/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1383(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1384/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1384(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1385/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1385(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1386/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1386(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1387/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1387(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1388/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1388(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1389/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1389(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1390/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1390(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1391/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1391(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1392/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1392(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1393/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1393(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1394/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1394(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1395/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1395(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1396/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1396(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1397/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1397(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1398/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1398(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1399/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1399(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1400/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1400(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1401/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1401(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1402/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1402(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1403/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1403(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1404/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1404(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1405/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1405(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1406/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1406(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1407/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1407(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1408/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1408(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1409/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1409(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1410/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1410(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1411/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1411(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1412/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1412(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1413/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1413(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1414/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1414(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1415/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1415(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1416/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1416(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1417/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1417(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1418/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1418(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1419/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1419(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1420/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1420(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1421/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1421(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1422/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1422(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1423/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1423(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1424/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1424(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1425/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1425(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1426/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1426(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1427/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1427(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1428/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1428(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1429/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1429(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1430/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1430(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1431/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1431(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1432/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1432(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1433/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1433(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1434/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1434(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1435/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1435(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1436/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1436(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1437/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1437(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1438/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1438(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1439/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1439(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1440/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1440(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1441/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1441(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1442/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1442(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1443/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1443(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1444/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1444(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1445/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1445(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1446/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1446(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1447/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1447(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1448/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1448(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1449/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1449(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1450/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1450(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1451/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1451(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1452/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1452(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1453/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1453(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1454/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1454(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1455/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1455(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1456/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1456(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1457/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1457(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1458/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1458(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1459/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1459(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1460/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1460(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1461/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1461(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1462/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1462(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1463/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1463(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1464/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1464(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1465/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1465(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1466/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1466(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1467/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1467(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1468/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1468(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1469/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1469(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1470/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1470(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1471/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1471(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1472/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1472(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1473/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1473(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1474/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1474(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1475/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1475(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1476/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1476(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1477/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1477(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1478/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1478(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1479/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1479(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1480/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1480(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1481/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1481(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1482/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1482(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1483/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1483(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1484/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1484(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1485/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1485(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1486/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1486(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1487/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1487(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1488/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1488(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1489/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1489(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1490/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1490(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1491/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1491(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1492/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1492(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1493/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1493(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1494/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1494(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1495/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1495(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1496/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1496(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1497/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1497(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1498/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1498(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1499/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1499(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1500/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1500(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1501/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1501(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1502/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1502(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1503/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1503(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1504/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1504(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1505/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1505(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1506/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1506(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1507/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1507(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1508/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1508(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1509/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1509(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1510/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1510(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1511/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1511(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1512/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1512(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1513/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1513(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1514/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1514(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1515/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1515(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1516/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1516(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1517/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1517(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1518/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1518(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1519/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1519(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1520/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1520(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1521/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1521(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1522/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1522(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1523/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1523(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1524/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1524(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1525/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1525(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1526/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1526(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1527/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1527(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1528/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1528(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1529/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1529(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1530/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1530(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1531/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1531(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1532/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1532(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1533/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1533(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1534/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1534(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1535/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1535(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1536/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1536(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1537/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1537(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1538/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1538(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1539/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1539(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1540/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1540(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1541/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1541(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1542/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1542(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1543/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1543(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1544/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1544(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1545/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1545(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1546/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1546(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1547/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1547(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1548/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1548(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1549/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1549(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1550/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1550(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1551/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1551(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1552/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1552(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1553/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1553(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1554/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1554(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1555/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1555(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1556/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1556(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1557/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1557(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1558/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1558(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1559/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1559(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1560/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1560(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1561/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1561(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1562/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1562(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1563/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1563(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1564/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1564(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1565/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1565(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1566/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1566(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1567/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1567(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1568/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1568(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1569/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1569(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1570/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1570(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1571/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1571(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1572/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1572(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1573/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1573(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1574/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1574(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1575/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1575(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1576/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1576(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1577/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1577(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1578/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1578(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1579/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1579(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1580/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1580(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1581/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1581(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1582/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1582(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1583/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1583(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1584/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1584(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1585/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1585(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1586/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1586(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1587/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1587(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1588/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1588(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1589/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1589(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1590/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1590(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1591/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1591(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1592/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1592(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1593/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1593(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1594/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1594(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1595/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1595(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1596/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1596(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1597/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1597(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1598/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1598(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1599/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1599(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1600/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1600(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1601/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1601(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1602/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1602(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1603/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1603(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1604/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1604(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1605/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1605(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1606/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1606(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1607/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1607(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1608/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1608(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1609/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1609(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1610/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1610(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1611/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1611(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1612/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1612(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1613/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1613(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1614/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1614(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1615/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1615(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1616/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1616(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1617/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1617(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1618/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1618(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1619/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1619(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1620/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1620(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1621/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1621(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1622/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1622(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1623/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1623(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1624/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1624(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1625/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1625(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1626/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1626(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1627/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1627(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1628/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1628(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1629/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1629(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1630/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1630(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1631/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1631(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1632/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1632(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1633/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1633(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1634/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1634(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1635/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1635(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1636/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1636(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1637/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1637(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1638/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1638(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1639/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1639(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1640/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1640(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1641/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1641(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1642/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1642(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1643/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1643(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1644/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1644(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1645/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1645(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1646/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1646(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1647/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1647(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1648/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1648(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1649/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1649(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1650/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1650(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1651/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1651(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1652/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1652(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1653/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1653(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1654/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1654(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1655/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1655(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1656/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1656(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1657/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1657(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1658/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1658(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1659/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1659(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1660/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1660(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1661/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1661(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1662/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1662(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1663/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1663(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1664/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1664(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1665/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1665(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1666/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1666(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1667/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1667(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1668/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1668(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1669/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1669(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1670/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1670(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1671/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1671(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1672/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1672(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1673/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1673(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1674/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1674(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1675/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1675(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1676/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1676(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1677/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1677(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1678/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1678(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1679/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1679(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1680/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1680(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1681/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1681(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1682/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1682(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1683/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1683(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1684/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1684(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1685/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1685(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1686/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1686(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1687/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1687(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1688/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1688(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1689/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1689(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1690/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1690(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1691/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1691(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1692/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1692(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1693/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1693(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1694/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1694(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1695/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1695(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1696/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1696(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1697/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1697(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1698/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1698(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1699/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1699(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1700/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1700(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1701/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1701(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1702/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1702(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1703/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1703(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1704/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1704(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1705/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1705(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1706/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1706(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1707/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1707(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1708/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1708(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1709/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1709(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1710/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1710(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1711/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1711(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1712/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1712(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1713/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1713(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1714/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1714(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1715/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1715(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1716/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1716(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1717/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1717(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1718/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1718(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1719/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1719(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1720/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1720(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1721/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1721(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1722/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1722(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1723/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1723(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1724/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1724(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1725/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1725(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1726/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1726(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1727/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1727(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1728/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1728(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1729/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1729(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1730/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1730(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1731/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1731(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1732/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1732(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1733/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1733(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1734/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1734(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1735/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1735(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1736/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1736(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1737/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1737(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1738/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1738(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1739/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1739(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1740/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1740(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1741/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1741(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1742/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1742(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1743/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1743(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1744/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1744(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1745/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1745(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1746/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1746(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1747/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1747(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1748/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1748(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1749/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1749(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1750/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1750(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1751/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1751(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1752/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1752(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1753/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1753(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1754/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1754(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1755/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1755(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1756/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1756(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1757/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1757(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1758/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1758(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1759/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1759(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1760/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1760(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1761/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1761(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1762/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1762(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1763/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1763(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1764/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1764(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1765/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1765(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1766/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1766(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1767/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1767(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1768/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1768(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1769/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1769(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1770/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1770(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1771/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1771(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1772/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1772(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1773/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1773(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1774/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1774(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1775/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1775(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1776/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1776(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1777/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1777(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1778/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1778(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1779/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1779(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1780/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1780(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1781/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1781(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1782/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1782(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1783/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1783(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1784/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1784(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1785/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1785(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1786/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1786(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1787/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1787(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1788/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1788(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1789/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1789(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1790/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1790(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1791/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1791(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1792/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1792(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1793/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1793(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1794/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1794(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1795/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1795(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1796/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1796(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1797/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1797(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1798/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1798(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1799/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1799(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1800/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1800(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1801/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1801(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1802/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1802(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1803/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1803(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1804/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1804(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1805/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1805(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1806/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1806(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1807/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1807(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1808/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1808(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1809/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1809(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1810/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1810(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1811/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1811(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1812/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1812(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1813/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1813(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1814/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1814(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1815/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1815(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1816/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1816(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1817/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1817(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1818/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1818(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1819/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1819(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1820/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1820(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1821/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1821(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1822/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1822(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1823/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1823(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1824/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1824(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1825/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1825(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1826/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1826(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1827/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1827(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1828/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1828(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1829/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1829(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1830/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1830(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1831/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1831(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1832/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1832(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1833/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1833(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1834/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1834(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1835/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1835(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1836/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1836(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1837/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1837(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1838/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1838(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1839/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1839(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1840/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1840(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1841/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1841(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1842/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1842(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1843/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1843(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1844/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1844(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1845/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1845(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1846/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1846(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1847/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1847(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1848/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1848(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1849/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1849(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1850/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1850(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1851/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1851(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1852/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1852(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1853/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1853(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1854/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1854(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1855/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1855(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1856/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1856(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1857/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1857(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1858/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1858(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1859/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1859(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1860/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1860(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1861/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1861(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1862/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1862(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1863/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1863(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1864/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1864(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1865/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1865(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1866/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1866(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1867/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1867(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1868/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1868(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1869/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1869(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1870/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1870(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1871/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1871(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1872/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1872(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1873/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1873(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1874/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1874(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1875/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1875(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1876/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1876(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1877/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1877(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1878/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1878(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1879/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1879(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1880/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1880(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1881/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1881(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1882/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1882(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1883/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1883(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1884/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1884(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1885/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1885(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1886/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1886(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1887/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1887(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1888/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1888(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1889/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1889(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1890/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1890(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1891/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1891(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1892/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1892(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1893/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1893(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1894/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1894(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1895/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1895(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1896/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1896(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1897/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1897(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1898/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1898(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1899/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1899(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1900/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1900(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1901/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1901(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1902/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1902(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1903/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1903(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1904/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1904(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1905/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1905(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1906/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1906(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1907/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1907(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1908/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1908(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1909/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1909(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1910/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1910(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1911/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1911(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1912/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1912(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1913/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1913(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1914/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1914(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1915/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1915(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1916/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1916(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1917/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1917(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1918/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1918(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1919/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1919(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1920/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1920(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1921/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1921(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1922/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1922(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1923/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1923(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1924/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1924(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1925/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1925(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1926/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1926(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1927/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1927(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1928/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1928(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1929/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1929(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1930/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1930(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1931/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1931(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1932/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1932(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1933/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1933(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1934/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1934(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1935/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1935(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1936/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1936(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1937/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1937(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1938/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1938(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1939/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1939(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1940/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1940(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1941/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1941(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1942/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1942(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1943/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1943(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1944/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1944(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1945/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1945(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1946/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1946(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1947/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1947(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1948/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1948(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1949/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1949(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1950/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1950(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1951/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1951(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1952/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1952(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1953/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1953(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1954/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1954(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1955/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1955(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1956/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1956(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1957/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1957(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1958/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1958(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1959/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1959(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1960/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1960(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1961/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1961(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1962/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1962(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1963/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1963(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1964/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1964(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1965/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1965(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1966/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1966(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1967/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1967(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1968/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1968(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1969/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1969(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1970/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1970(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1971/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1971(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1972/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1972(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1973/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1973(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1974/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1974(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1975/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1975(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1976/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1976(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1977/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1977(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1978/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1978(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1979/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1979(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1980/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1980(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1981/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1981(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1982/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1982(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1983/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1983(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1984/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1984(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1985/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1985(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1986/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1986(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1987/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1987(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1988/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1988(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1989/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1989(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1990/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1990(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1991/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1991(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1992/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1992(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1993/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1993(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1994/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1994(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1995/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1995(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1996/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1996(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1997/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1997(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1998/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1998(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink1999/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod1999(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

        @DeepLink(value = ["dld://methodDeepLink2000/{param1}"], activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
        @JvmStatic
        fun intentForDeepLinkMethod2000(context: Context): Intent =
            Intent(context, ScaleTestActivity::class.java).setAction(ACTION_DEEP_LINK_METHOD)

    }
}
