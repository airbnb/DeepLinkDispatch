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
package com.airbnb.deeplinkdispatch.sample.benchmarkable;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.airbnb.deeplinkdispatch.DeepLink;

/**
 * Activity with 2k deeplinks to test DLD performance at scale
 */
public class ScaleTestActivity extends AppCompatActivity {
  private static final String ACTION_DEEP_LINK_METHOD = "deep_link_method";
  private static final String ACTION_DEEP_LINK_COMPLEX = "deep_link_complex";
  private static final String TAG = ScaleTestActivity.class.getSimpleName();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.scale_activity_main);

    Intent intent = getIntent();
    if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
      String toastMessage;
      Bundle parameters = intent.getExtras();
      Log.d(TAG, "Deeplink params: " + parameters);

      if (ACTION_DEEP_LINK_METHOD.equals(intent.getAction())) {
        toastMessage = "method with param1:" + parameters.getString("param1");
      } else if (ACTION_DEEP_LINK_COMPLEX.equals(intent.getAction())) {
        toastMessage = parameters.getString("arbitraryNumber");
      } else if (parameters.containsKey("arg")) {
        toastMessage = "class and found arg:" + parameters.getString("arg");
      } else {
        toastMessage = "class";
      }

      // You can pass a query parameter with the URI, and it's also in parameters, like
      // dld://classDeepLink?qp=123
      if (parameters.containsKey("qp")) {
        toastMessage += " with query parameter " + parameters.getString("qp");
      }
      Uri referrer = ActivityCompat.getReferrer(this);
      if (referrer != null) {
        toastMessage += " and referrer: " + referrer.toString();
      }

      showToast(toastMessage);
    }
  }

  /**
   * Handles deep link with one param, doc does not contain "param"
   * @return A intent to start {@link ScaleTestActivity}
   */
  @DeepLink(value = "dld://methodDeepLink1/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink2/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod2(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink3/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod3(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink4/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod4(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink5/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod5(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink6/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod6(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink7/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod7(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink8/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod8(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink9/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod9(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink10/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod10(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink11/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod11(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink12/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod12(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink13/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod13(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink14/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod14(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink15/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod15(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink16/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod16(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink17/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod17(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink18/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod18(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink19/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod19(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink20/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod20(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink21/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod21(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink22/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod22(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink23/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod23(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink24/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod24(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink25/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod25(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink26/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod26(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink27/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod27(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink28/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod28(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink29/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod29(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink30/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod30(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink31/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod31(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink32/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod32(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink33/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod33(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink34/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod34(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink35/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod35(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink36/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod36(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink37/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod37(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink38/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod38(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink39/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod39(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink40/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod40(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink41/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod41(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink42/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod42(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink43/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod43(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink44/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod44(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink45/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod45(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink46/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod46(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink47/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod47(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink48/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod48(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink49/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod49(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink50/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod50(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink51/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod51(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink52/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod52(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink53/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod53(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink54/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod54(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink55/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod55(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink56/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod56(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink57/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod57(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink58/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod58(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink59/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod59(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink60/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod60(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink61/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod61(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink62/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod62(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink63/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod63(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink64/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod64(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink65/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod65(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink66/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod66(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink67/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod67(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink68/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod68(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink69/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod69(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink70/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod70(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink71/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod71(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink72/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod72(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink73/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod73(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink74/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod74(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink75/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod75(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink76/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod76(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink77/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod77(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink78/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod78(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink79/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod79(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink80/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod80(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink81/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod81(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink82/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod82(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink83/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod83(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink84/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod84(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink85/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod85(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink86/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod86(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink87/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod87(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink88/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod88(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink89/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod89(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink90/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod90(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink91/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod91(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink92/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod92(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink93/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod93(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink94/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod94(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink95/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod95(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink96/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod96(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink97/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod97(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink98/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod98(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink99/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod99(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink100/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod100(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink101/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod101(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink102/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod102(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink103/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod103(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink104/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod104(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink105/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod105(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink106/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod106(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink107/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod107(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink108/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod108(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink109/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod109(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink110/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod110(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink111/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod111(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink112/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod112(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink113/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod113(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink114/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod114(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink115/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod115(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink116/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod116(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink117/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod117(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink118/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod118(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink119/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod119(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink120/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod120(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink121/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod121(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink122/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod122(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink123/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod123(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink124/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod124(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink125/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod125(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink126/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod126(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink127/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod127(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink128/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod128(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink129/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod129(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink130/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod130(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink131/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod131(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink132/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod132(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink133/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod133(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink134/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod134(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink135/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod135(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink136/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod136(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink137/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod137(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink138/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod138(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink139/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod139(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink140/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod140(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink141/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod141(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink142/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod142(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink143/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod143(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink144/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod144(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink145/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod145(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink146/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod146(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink147/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod147(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink148/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod148(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink149/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod149(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink150/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod150(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink151/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod151(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink152/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod152(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink153/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod153(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink154/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod154(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink155/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod155(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink156/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod156(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink157/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod157(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink158/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod158(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink159/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod159(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink160/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod160(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink161/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod161(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink162/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod162(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink163/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod163(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink164/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod164(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink165/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod165(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink166/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod166(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink167/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod167(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink168/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod168(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink169/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod169(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink170/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod170(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink171/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod171(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink172/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod172(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink173/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod173(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink174/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod174(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink175/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod175(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink176/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod176(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink177/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod177(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink178/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod178(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink179/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod179(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink180/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod180(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink181/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod181(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink182/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod182(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink183/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod183(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink184/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod184(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink185/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod185(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink186/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod186(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink187/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod187(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink188/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod188(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink189/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod189(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink190/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod190(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink191/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod191(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink192/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod192(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink193/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod193(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink194/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod194(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink195/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod195(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink196/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod196(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink197/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod197(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink198/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod198(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink199/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod199(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink200/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod200(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink201/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod201(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink202/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod202(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink203/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod203(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink204/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod204(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink205/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod205(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink206/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod206(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink207/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod207(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink208/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod208(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink209/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod209(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink210/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod210(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink211/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod211(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink212/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod212(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink213/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod213(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink214/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod214(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink215/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod215(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink216/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod216(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink217/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod217(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink218/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod218(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink219/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod219(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink220/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod220(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink221/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod221(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink222/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod222(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink223/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod223(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink224/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod224(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink225/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod225(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink226/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod226(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink227/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod227(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink228/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod228(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink229/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod229(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink230/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod230(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink231/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod231(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink232/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod232(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink233/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod233(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink234/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod234(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink235/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod235(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink236/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod236(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink237/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod237(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink238/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod238(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink239/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod239(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink240/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod240(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink241/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod241(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink242/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod242(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink243/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod243(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink244/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod244(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink245/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod245(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink246/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod246(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink247/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod247(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink248/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod248(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink249/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod249(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink250/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod250(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink251/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod251(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink252/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod252(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink253/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod253(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink254/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod254(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink255/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod255(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink256/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod256(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink257/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod257(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink258/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod258(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink259/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod259(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink260/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod260(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink261/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod261(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink262/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod262(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink263/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod263(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink264/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod264(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink265/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod265(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink266/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod266(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink267/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod267(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink268/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod268(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink269/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod269(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink270/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod270(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink271/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod271(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink272/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod272(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink273/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod273(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink274/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod274(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink275/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod275(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink276/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod276(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink277/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod277(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink278/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod278(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink279/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod279(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink280/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod280(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink281/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod281(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink282/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod282(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink283/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod283(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink284/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod284(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink285/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod285(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink286/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod286(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink287/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod287(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink288/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod288(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink289/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod289(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink290/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod290(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink291/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod291(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink292/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod292(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink293/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod293(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink294/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod294(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink295/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod295(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink296/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod296(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink297/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod297(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink298/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod298(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink299/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod299(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink300/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod300(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink301/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod301(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink302/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod302(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink303/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod303(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink304/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod304(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink305/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod305(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink306/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod306(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink307/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod307(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink308/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod308(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink309/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod309(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink310/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod310(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink311/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod311(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink312/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod312(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink313/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod313(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink314/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod314(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink315/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod315(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink316/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod316(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink317/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod317(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink318/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod318(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink319/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod319(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink320/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod320(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink321/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod321(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink322/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod322(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink323/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod323(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink324/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod324(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink325/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod325(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink326/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod326(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink327/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod327(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink328/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod328(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink329/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod329(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink330/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod330(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink331/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod331(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink332/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod332(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink333/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod333(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink334/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod334(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink335/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod335(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink336/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod336(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink337/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod337(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink338/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod338(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink339/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod339(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink340/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod340(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink341/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod341(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink342/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod342(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink343/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod343(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink344/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod344(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink345/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod345(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink346/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod346(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink347/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod347(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink348/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod348(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink349/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod349(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink350/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod350(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink351/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod351(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink352/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod352(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink353/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod353(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink354/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod354(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink355/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod355(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink356/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod356(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink357/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod357(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink358/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod358(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink359/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod359(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink360/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod360(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink361/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod361(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink362/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod362(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink363/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod363(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink364/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod364(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink365/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod365(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink366/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod366(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink367/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod367(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink368/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod368(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink369/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod369(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink370/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod370(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink371/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod371(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink372/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod372(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink373/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod373(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink374/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod374(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink375/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod375(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink376/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod376(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink377/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod377(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink378/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod378(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink379/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod379(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink380/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod380(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink381/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod381(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink382/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod382(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink383/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod383(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink384/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod384(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink385/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod385(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink386/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod386(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink387/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod387(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink388/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod388(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink389/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod389(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink390/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod390(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink391/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod391(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink392/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod392(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink393/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod393(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink394/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod394(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink395/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod395(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink396/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod396(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink397/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod397(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink398/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod398(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink399/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod399(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink400/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod400(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink401/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod401(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink402/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod402(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink403/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod403(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink404/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod404(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink405/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod405(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink406/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod406(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink407/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod407(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink408/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod408(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink409/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod409(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink410/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod410(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink411/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod411(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink412/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod412(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink413/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod413(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink414/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod414(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink415/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod415(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink416/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod416(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink417/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod417(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink418/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod418(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink419/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod419(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink420/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod420(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink421/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod421(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink422/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod422(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink423/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod423(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink424/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod424(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink425/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod425(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink426/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod426(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink427/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod427(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink428/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod428(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink429/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod429(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink430/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod430(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink431/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod431(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink432/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod432(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink433/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod433(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink434/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod434(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink435/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod435(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink436/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod436(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink437/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod437(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink438/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod438(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink439/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod439(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink440/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod440(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink441/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod441(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink442/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod442(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink443/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod443(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink444/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod444(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink445/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod445(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink446/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod446(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink447/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod447(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink448/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod448(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink449/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod449(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink450/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod450(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink451/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod451(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink452/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod452(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink453/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod453(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink454/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod454(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink455/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod455(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink456/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod456(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink457/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod457(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink458/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod458(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink459/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod459(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink460/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod460(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink461/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod461(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink462/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod462(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink463/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod463(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink464/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod464(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink465/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod465(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink466/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod466(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink467/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod467(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink468/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod468(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink469/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod469(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink470/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod470(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink471/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod471(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink472/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod472(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink473/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod473(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink474/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod474(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink475/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod475(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink476/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod476(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink477/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod477(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink478/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod478(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink479/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod479(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink480/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod480(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink481/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod481(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink482/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod482(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink483/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod483(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink484/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod484(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink485/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod485(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink486/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod486(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink487/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod487(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink488/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod488(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink489/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod489(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink490/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod490(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink491/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod491(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink492/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod492(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink493/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod493(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink494/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod494(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink495/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod495(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink496/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod496(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink497/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod497(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink498/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod498(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink499/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod499(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink500/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod500(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink501/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod501(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink502/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod502(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink503/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod503(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink504/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod504(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink505/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod505(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink506/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod506(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink507/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod507(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink508/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod508(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink509/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod509(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink510/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod510(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink511/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod511(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink512/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod512(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink513/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod513(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink514/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod514(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink515/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod515(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink516/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod516(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink517/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod517(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink518/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod518(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink519/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod519(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink520/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod520(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink521/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod521(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink522/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod522(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink523/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod523(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink524/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod524(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink525/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod525(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink526/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod526(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink527/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod527(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink528/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod528(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink529/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod529(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink530/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod530(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink531/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod531(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink532/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod532(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink533/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod533(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink534/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod534(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink535/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod535(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink536/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod536(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink537/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod537(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink538/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod538(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink539/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod539(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink540/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod540(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink541/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod541(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink542/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod542(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink543/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod543(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink544/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod544(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink545/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod545(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink546/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod546(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink547/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod547(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink548/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod548(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink549/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod549(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink550/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod550(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink551/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod551(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink552/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod552(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink553/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod553(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink554/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod554(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink555/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod555(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink556/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod556(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink557/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod557(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink558/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod558(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink559/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod559(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink560/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod560(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink561/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod561(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink562/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod562(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink563/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod563(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink564/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod564(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink565/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod565(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink566/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod566(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink567/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod567(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink568/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod568(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink569/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod569(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink570/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod570(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink571/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod571(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink572/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod572(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink573/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod573(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink574/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod574(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink575/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod575(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink576/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod576(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink577/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod577(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink578/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod578(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink579/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod579(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink580/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod580(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink581/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod581(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink582/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod582(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink583/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod583(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink584/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod584(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink585/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod585(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink586/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod586(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink587/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod587(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink588/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod588(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink589/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod589(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink590/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod590(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink591/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod591(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink592/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod592(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink593/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod593(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink594/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod594(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink595/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod595(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink596/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod596(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink597/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod597(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink598/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod598(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink599/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod599(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink600/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod600(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink601/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod601(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink602/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod602(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink603/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod603(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink604/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod604(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink605/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod605(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink606/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod606(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink607/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod607(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink608/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod608(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink609/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod609(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink610/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod610(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink611/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod611(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink612/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod612(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink613/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod613(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink614/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod614(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink615/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod615(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink616/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod616(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink617/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod617(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink618/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod618(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink619/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod619(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink620/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod620(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink621/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod621(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink622/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod622(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink623/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod623(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink624/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod624(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink625/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod625(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink626/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod626(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink627/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod627(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink628/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod628(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink629/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod629(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink630/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod630(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink631/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod631(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink632/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod632(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink633/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod633(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink634/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod634(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink635/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod635(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink636/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod636(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink637/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod637(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink638/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod638(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink639/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod639(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink640/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod640(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink641/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod641(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink642/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod642(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink643/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod643(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink644/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod644(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink645/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod645(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink646/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod646(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink647/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod647(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink648/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod648(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink649/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod649(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink650/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod650(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink651/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod651(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink652/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod652(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink653/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod653(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink654/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod654(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink655/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod655(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink656/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod656(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink657/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod657(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink658/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod658(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink659/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod659(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink660/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod660(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink661/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod661(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink662/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod662(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink663/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod663(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink664/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod664(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink665/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod665(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink666/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod666(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink667/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod667(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink668/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod668(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink669/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod669(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink670/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod670(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink671/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod671(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink672/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod672(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink673/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod673(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink674/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod674(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink675/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod675(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink676/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod676(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink677/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod677(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink678/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod678(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink679/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod679(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink680/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod680(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink681/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod681(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink682/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod682(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink683/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod683(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink684/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod684(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink685/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod685(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink686/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod686(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink687/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod687(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink688/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod688(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink689/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod689(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink690/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod690(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink691/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod691(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink692/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod692(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink693/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod693(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink694/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod694(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink695/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod695(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink696/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod696(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink697/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod697(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink698/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod698(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink699/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod699(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink700/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod700(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink701/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod701(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink702/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod702(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink703/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod703(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink704/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod704(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink705/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod705(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink706/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod706(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink707/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod707(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink708/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod708(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink709/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod709(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink710/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod710(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink711/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod711(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink712/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod712(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink713/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod713(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink714/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod714(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink715/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod715(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink716/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod716(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink717/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod717(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink718/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod718(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink719/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod719(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink720/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod720(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink721/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod721(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink722/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod722(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink723/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod723(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink724/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod724(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink725/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod725(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink726/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod726(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink727/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod727(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink728/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod728(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink729/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod729(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink730/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod730(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink731/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod731(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink732/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod732(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink733/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod733(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink734/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod734(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink735/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod735(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink736/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod736(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink737/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod737(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink738/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod738(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink739/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod739(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink740/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod740(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink741/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod741(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink742/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod742(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink743/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod743(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink744/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod744(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink745/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod745(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink746/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod746(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink747/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod747(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink748/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod748(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink749/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod749(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink750/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod750(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink751/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod751(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink752/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod752(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink753/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod753(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink754/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod754(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink755/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod755(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink756/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod756(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink757/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod757(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink758/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod758(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink759/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod759(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink760/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod760(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink761/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod761(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink762/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod762(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink763/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod763(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink764/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod764(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink765/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod765(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink766/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod766(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink767/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod767(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink768/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod768(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink769/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod769(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink770/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod770(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink771/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod771(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink772/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod772(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink773/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod773(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink774/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod774(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink775/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod775(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink776/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod776(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink777/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod777(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink778/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod778(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink779/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod779(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink780/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod780(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink781/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod781(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink782/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod782(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink783/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod783(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink784/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod784(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink785/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod785(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink786/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod786(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink787/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod787(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink788/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod788(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink789/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod789(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink790/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod790(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink791/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod791(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink792/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod792(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink793/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod793(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink794/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod794(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink795/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod795(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink796/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod796(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink797/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod797(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink798/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod798(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink799/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod799(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink800/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod800(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink801/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod801(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink802/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod802(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink803/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod803(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink804/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod804(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink805/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod805(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink806/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod806(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink807/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod807(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink808/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod808(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink809/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod809(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink810/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod810(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink811/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod811(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink812/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod812(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink813/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod813(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink814/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod814(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink815/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod815(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink816/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod816(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink817/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod817(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink818/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod818(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink819/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod819(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink820/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod820(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink821/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod821(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink822/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod822(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink823/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod823(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink824/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod824(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink825/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod825(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink826/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod826(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink827/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod827(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink828/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod828(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink829/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod829(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink830/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod830(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink831/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod831(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink832/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod832(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink833/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod833(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink834/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod834(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink835/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod835(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink836/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod836(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink837/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod837(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink838/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod838(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink839/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod839(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink840/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod840(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink841/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod841(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink842/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod842(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink843/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod843(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink844/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod844(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink845/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod845(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink846/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod846(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink847/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod847(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink848/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod848(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink849/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod849(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink850/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod850(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink851/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod851(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink852/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod852(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink853/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod853(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink854/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod854(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink855/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod855(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink856/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod856(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink857/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod857(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink858/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod858(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink859/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod859(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink860/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod860(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink861/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod861(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink862/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod862(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink863/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod863(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink864/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod864(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink865/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod865(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink866/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod866(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink867/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod867(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink868/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod868(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink869/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod869(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink870/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod870(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink871/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod871(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink872/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod872(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink873/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod873(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink874/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod874(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink875/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod875(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink876/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod876(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink877/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod877(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink878/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod878(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink879/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod879(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink880/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod880(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink881/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod881(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink882/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod882(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink883/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod883(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink884/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod884(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink885/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod885(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink886/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod886(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink887/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod887(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink888/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod888(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink889/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod889(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink890/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod890(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink891/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod891(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink892/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod892(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink893/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod893(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink894/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod894(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink895/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod895(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink896/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod896(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink897/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod897(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink898/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod898(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink899/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod899(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink900/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod900(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink901/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod901(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink902/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod902(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink903/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod903(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink904/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod904(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink905/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod905(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink906/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod906(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink907/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod907(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink908/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod908(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink909/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod909(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink910/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod910(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink911/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod911(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink912/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod912(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink913/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod913(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink914/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod914(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink915/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod915(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink916/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod916(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink917/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod917(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink918/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod918(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink919/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod919(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink920/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod920(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink921/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod921(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink922/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod922(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink923/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod923(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink924/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod924(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink925/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod925(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink926/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod926(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink927/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod927(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink928/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod928(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink929/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod929(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink930/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod930(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink931/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod931(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink932/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod932(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink933/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod933(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink934/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod934(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink935/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod935(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink936/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod936(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink937/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod937(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink938/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod938(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink939/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod939(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink940/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod940(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink941/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod941(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink942/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod942(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink943/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod943(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink944/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod944(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink945/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod945(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink946/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod946(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink947/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod947(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink948/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod948(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink949/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod949(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink950/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod950(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink951/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod951(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink952/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod952(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink953/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod953(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink954/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod954(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink955/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod955(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink956/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod956(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink957/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod957(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink958/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod958(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink959/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod959(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink960/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod960(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink961/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod961(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink962/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod962(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink963/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod963(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink964/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod964(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink965/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod965(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink966/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod966(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink967/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod967(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink968/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod968(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink969/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod969(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink970/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod970(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink971/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod971(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink972/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod972(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink973/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod973(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink974/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod974(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink975/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod975(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink976/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod976(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink977/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod977(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink978/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod978(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink979/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod979(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink980/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod980(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink981/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod981(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink982/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod982(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink983/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod983(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink984/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod984(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink985/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod985(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink986/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod986(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink987/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod987(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink988/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod988(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink989/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod989(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink990/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod990(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink991/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod991(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink992/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod992(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink993/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod993(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink994/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod994(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink995/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod995(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink996/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod996(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink997/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod997(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink998/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod998(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink999/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod999(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1000/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1000(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1001/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1001(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1002/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1002(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1003/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1003(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1004/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1004(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1005/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1005(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1006/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1006(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1007/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1007(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1008/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1008(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1009/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1009(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1010/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1010(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1011/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1011(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1012/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1012(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1013/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1013(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1014/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1014(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1015/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1015(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1016/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1016(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1017/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1017(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1018/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1018(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1019/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1019(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1020/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1020(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1021/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1021(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1022/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1022(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1023/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1023(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1024/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1024(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1025/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1025(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1026/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1026(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1027/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1027(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1028/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1028(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1029/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1029(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1030/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1030(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1031/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1031(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1032/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1032(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1033/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1033(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1034/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1034(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1035/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1035(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1036/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1036(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1037/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1037(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1038/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1038(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1039/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1039(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1040/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1040(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1041/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1041(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1042/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1042(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1043/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1043(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1044/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1044(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1045/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1045(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1046/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1046(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1047/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1047(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1048/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1048(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1049/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1049(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1050/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1050(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1051/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1051(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1052/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1052(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1053/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1053(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1054/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1054(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1055/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1055(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1056/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1056(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1057/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1057(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1058/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1058(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1059/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1059(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1060/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1060(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1061/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1061(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1062/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1062(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1063/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1063(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1064/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1064(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1065/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1065(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1066/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1066(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1067/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1067(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1068/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1068(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1069/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1069(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1070/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1070(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1071/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1071(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1072/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1072(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1073/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1073(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1074/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1074(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1075/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1075(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1076/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1076(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1077/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1077(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1078/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1078(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1079/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1079(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1080/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1080(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1081/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1081(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1082/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1082(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1083/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1083(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1084/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1084(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1085/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1085(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1086/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1086(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1087/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1087(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1088/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1088(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1089/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1089(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1090/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1090(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1091/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1091(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1092/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1092(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1093/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1093(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1094/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1094(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1095/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1095(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1096/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1096(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1097/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1097(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1098/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1098(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1099/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1099(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1100/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1100(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1101/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1101(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1102/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1102(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1103/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1103(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1104/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1104(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1105/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1105(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1106/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1106(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1107/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1107(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1108/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1108(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1109/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1109(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1110/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1110(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1111/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1111(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1112/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1112(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1113/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1113(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1114/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1114(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1115/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1115(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1116/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1116(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1117/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1117(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1118/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1118(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1119/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1119(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1120/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1120(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1121/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1121(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1122/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1122(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1123/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1123(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1124/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1124(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1125/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1125(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1126/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1126(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1127/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1127(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1128/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1128(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1129/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1129(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1130/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1130(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1131/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1131(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1132/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1132(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1133/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1133(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1134/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1134(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1135/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1135(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1136/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1136(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1137/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1137(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1138/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1138(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1139/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1139(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1140/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1140(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1141/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1141(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1142/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1142(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1143/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1143(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1144/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1144(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1145/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1145(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1146/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1146(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1147/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1147(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1148/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1148(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1149/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1149(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1150/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1150(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1151/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1151(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1152/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1152(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1153/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1153(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1154/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1154(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1155/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1155(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1156/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1156(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1157/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1157(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1158/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1158(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1159/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1159(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1160/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1160(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1161/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1161(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1162/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1162(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1163/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1163(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1164/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1164(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1165/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1165(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1166/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1166(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1167/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1167(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1168/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1168(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1169/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1169(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1170/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1170(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1171/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1171(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1172/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1172(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1173/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1173(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1174/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1174(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1175/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1175(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1176/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1176(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1177/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1177(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1178/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1178(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1179/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1179(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1180/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1180(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1181/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1181(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1182/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1182(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1183/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1183(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1184/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1184(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1185/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1185(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1186/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1186(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1187/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1187(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1188/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1188(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1189/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1189(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1190/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1190(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1191/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1191(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1192/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1192(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1193/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1193(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1194/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1194(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1195/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1195(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1196/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1196(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1197/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1197(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1198/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1198(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1199/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1199(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1200/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1200(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1201/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1201(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1202/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1202(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1203/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1203(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1204/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1204(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1205/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1205(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1206/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1206(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1207/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1207(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1208/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1208(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1209/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1209(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1210/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1210(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1211/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1211(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1212/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1212(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1213/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1213(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1214/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1214(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1215/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1215(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1216/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1216(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1217/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1217(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1218/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1218(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1219/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1219(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1220/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1220(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }
  @DeepLink(value = "dld://methodDeepLink1221/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1221(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1222/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1222(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1223/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1223(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1224/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1224(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1225/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1225(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1226/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1226(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1227/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1227(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1228/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1228(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1229/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1229(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1230/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1230(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }
  @DeepLink(value = "dld://methodDeepLink1231/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1231(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1232/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1232(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1233/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1233(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1234/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1234(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1235/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1235(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1236/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1236(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1237/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1237(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1238/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1238(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1239/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1239(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1240/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1240(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }
  @DeepLink(value = "dld://methodDeepLink1241/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1241(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1242/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1242(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1243/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1243(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1244/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1244(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1245/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1245(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1246/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1246(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1247/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1247(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1248/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1248(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1249/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1249(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1250/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1250(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }
  @DeepLink(value = "dld://methodDeepLink1251/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1251(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1252/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1252(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1253/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1253(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1254/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1254(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1255/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1255(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1256/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1256(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1257/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1257(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1258/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1258(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1259/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1259(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1260/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1260(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }
  @DeepLink(value = "dld://methodDeepLink1261/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1261(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1262/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1262(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1263/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1263(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1264/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1264(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1265/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1265(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1266/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1266(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1267/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1267(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1268/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1268(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1269/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1269(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1270/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1270(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }
  @DeepLink(value = "dld://methodDeepLink1271/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1271(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1272/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1272(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1273/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1273(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1274/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1274(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1275/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1275(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1276/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1276(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1277/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1277(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1278/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1278(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1279/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1279(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1280/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1280(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }
  @DeepLink(value = "dld://methodDeepLink1281/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1281(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1282/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1282(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1283/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1283(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1284/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1284(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1285/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1285(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1286/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1286(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1287/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1287(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1288/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1288(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1289/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1289(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1290/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1290(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }
  @DeepLink(value = "dld://methodDeepLink1291/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1291(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1292/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1292(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1293/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1293(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1294/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1294(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1295/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1295(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1296/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1296(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1297/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1297(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1298/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1298(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1299/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1299(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1300/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1300(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1301/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1301(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1302/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1302(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1303/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1303(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1304/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1304(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1305/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1305(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1306/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1306(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1307/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1307(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1308/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1308(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1309/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1309(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1310/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1310(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1311/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1311(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1312/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1312(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1313/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1313(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1314/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1314(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1315/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1315(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1316/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1316(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1317/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1317(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1318/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1318(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1319/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1319(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1320/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1320(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1321/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1321(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1322/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1322(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1323/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1323(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1324/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1324(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1325/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1325(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1326/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1326(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1327/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1327(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1328/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1328(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1329/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1329(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1330/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1330(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1331/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1331(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1332/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1332(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1333/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1333(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1334/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1334(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1335/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1335(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1336/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1336(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1337/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1337(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1338/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1338(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1339/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1339(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1340/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1340(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1341/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1341(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1342/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1342(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1343/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1343(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1344/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1344(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1345/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1345(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1346/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1346(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1347/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1347(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1348/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1348(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1349/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1349(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1350/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1350(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1351/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1351(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1352/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1352(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1353/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1353(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1354/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1354(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1355/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1355(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1356/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1356(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1357/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1357(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1358/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1358(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1359/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1359(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1360/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1360(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1361/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1361(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1362/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1362(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1363/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1363(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1364/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1364(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1365/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1365(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1366/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1366(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1367/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1367(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1368/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1368(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1369/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1369(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1370/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1370(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1371/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1371(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1372/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1372(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1373/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1373(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1374/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1374(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1375/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1375(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1376/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1376(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1377/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1377(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1378/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1378(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1379/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1379(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1380/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1380(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1381/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1381(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1382/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1382(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1383/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1383(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1384/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1384(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1385/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1385(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1386/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1386(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1387/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1387(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1388/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1388(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1389/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1389(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1390/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1390(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1391/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1391(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1392/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1392(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1393/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1393(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1394/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1394(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1395/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1395(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1396/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1396(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1397/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1397(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1398/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1398(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1399/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1399(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1400/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1400(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1401/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1401(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1402/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1402(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1403/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1403(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1404/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1404(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1405/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1405(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1406/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1406(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1407/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1407(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1408/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1408(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1409/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1409(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1410/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1410(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1411/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1411(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1412/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1412(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1413/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1413(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1414/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1414(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1415/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1415(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1416/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1416(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1417/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1417(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1418/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1418(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1419/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1419(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1420/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1420(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1421/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1421(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1422/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1422(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1423/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1423(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1424/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1424(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1425/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1425(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1426/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1426(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1427/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1427(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1428/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1428(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1429/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1429(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1430/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1430(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1431/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1431(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1432/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1432(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1433/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1433(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1434/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1434(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1435/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1435(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1436/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1436(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1437/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1437(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1438/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1438(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1439/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1439(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1440/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1440(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1441/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1441(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1442/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1442(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1443/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1443(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1444/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1444(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1445/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1445(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1446/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1446(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1447/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1447(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1448/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1448(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1449/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1449(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1450/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1450(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1451/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1451(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1452/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1452(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1453/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1453(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1454/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1454(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1455/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1455(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1456/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1456(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1457/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1457(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1458/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1458(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1459/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1459(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1460/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1460(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1461/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1461(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1462/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1462(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1463/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1463(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1464/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1464(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1465/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1465(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1466/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1466(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1467/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1467(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1468/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1468(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1469/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1469(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1470/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1470(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1471/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1471(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1472/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1472(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1473/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1473(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1474/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1474(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1475/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1475(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1476/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1476(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1477/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1477(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1478/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1478(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1479/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1479(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1480/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1480(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1481/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1481(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1482/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1482(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1483/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1483(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1484/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1484(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1485/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1485(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1486/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1486(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1487/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1487(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1488/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1488(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1489/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1489(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1490/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1490(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1491/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1491(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1492/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1492(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1493/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1493(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1494/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1494(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1495/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1495(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1496/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1496(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1497/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1497(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1498/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1498(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1499/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1499(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1500/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1500(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1501/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1501(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1502/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1502(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1503/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1503(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1504/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1504(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1505/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1505(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1506/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1506(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1507/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1507(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1508/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1508(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1509/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1509(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1510/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1510(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1511/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1511(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1512/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1512(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1513/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1513(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1514/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1514(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1515/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1515(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1516/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1516(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1517/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1517(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1518/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1518(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1519/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1519(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1520/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1520(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1521/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1521(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1522/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1522(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1523/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1523(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1524/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1524(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1525/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1525(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1526/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1526(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1527/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1527(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1528/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1528(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1529/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1529(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1530/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1530(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1531/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1531(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1532/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1532(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1533/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1533(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1534/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1534(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1535/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1535(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1536/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1536(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1537/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1537(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1538/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1538(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1539/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1539(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1540/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1540(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1541/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1541(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1542/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1542(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1543/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1543(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1544/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1544(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1545/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1545(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1546/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1546(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1547/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1547(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1548/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1548(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1549/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1549(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1550/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1550(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1551/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1551(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1552/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1552(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1553/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1553(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1554/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1554(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1555/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1555(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1556/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1556(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1557/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1557(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1558/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1558(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1559/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1559(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1560/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1560(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1561/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1561(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1562/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1562(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1563/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1563(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1564/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1564(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1565/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1565(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1566/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1566(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1567/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1567(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1568/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1568(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1569/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1569(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1570/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1570(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1571/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1571(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1572/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1572(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1573/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1573(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1574/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1574(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1575/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1575(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1576/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1576(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1577/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1577(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1578/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1578(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1579/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1579(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1580/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1580(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1581/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1581(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1582/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1582(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1583/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1583(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1584/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1584(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1585/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1585(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1586/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1586(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1587/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1587(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1588/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1588(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1589/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1589(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1590/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1590(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1591/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1591(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1592/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1592(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1593/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1593(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1594/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1594(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1595/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1595(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1596/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1596(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1597/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1597(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1598/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1598(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1599/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1599(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1600/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1600(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1601/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1601(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1602/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1602(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1603/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1603(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1604/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1604(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1605/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1605(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1606/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1606(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1607/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1607(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1608/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1608(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1609/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1609(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1610/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1610(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1611/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1611(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1612/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1612(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1613/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1613(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1614/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1614(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1615/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1615(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1616/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1616(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1617/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1617(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1618/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1618(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1619/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1619(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1620/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1620(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1621/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1621(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1622/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1622(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1623/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1623(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1624/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1624(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1625/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1625(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1626/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1626(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1627/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1627(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1628/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1628(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1629/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1629(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1630/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1630(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1631/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1631(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1632/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1632(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1633/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1633(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1634/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1634(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1635/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1635(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1636/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1636(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1637/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1637(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1638/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1638(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1639/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1639(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1640/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1640(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1641/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1641(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1642/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1642(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1643/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1643(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1644/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1644(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1645/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1645(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1646/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1646(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1647/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1647(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1648/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1648(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1649/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1649(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1650/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1650(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1651/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1651(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1652/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1652(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1653/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1653(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1654/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1654(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1655/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1655(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1656/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1656(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1657/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1657(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1658/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1658(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1659/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1659(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1660/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1660(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1661/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1661(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1662/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1662(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1663/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1663(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1664/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1664(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1665/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1665(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1666/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1666(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1667/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1667(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1668/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1668(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1669/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1669(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1670/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1670(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1671/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1671(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1672/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1672(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1673/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1673(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1674/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1674(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1675/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1675(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1676/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1676(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1677/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1677(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1678/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1678(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1679/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1679(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1680/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1680(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1681/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1681(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1682/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1682(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1683/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1683(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1684/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1684(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1685/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1685(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1686/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1686(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1687/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1687(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1688/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1688(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1689/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1689(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1690/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1690(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1691/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1691(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1692/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1692(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1693/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1693(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1694/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1694(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1695/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1695(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1696/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1696(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1697/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1697(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1698/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1698(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1699/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1699(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1700/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1700(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1701/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1701(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1702/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1702(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1703/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1703(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1704/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1704(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1705/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1705(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1706/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1706(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1707/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1707(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1708/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1708(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1709/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1709(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1710/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1710(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1711/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1711(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1712/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1712(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1713/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1713(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1714/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1714(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1715/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1715(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1716/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1716(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1717/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1717(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1718/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1718(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1719/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1719(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1720/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1720(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1721/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1721(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1722/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1722(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1723/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1723(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1724/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1724(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1725/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1725(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1726/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1726(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1727/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1727(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1728/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1728(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1729/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1729(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1730/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1730(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1731/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1731(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1732/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1732(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1733/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1733(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1734/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1734(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1735/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1735(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1736/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1736(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1737/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1737(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1738/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1738(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1739/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1739(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1740/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1740(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1741/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1741(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1742/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1742(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1743/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1743(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1744/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1744(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1745/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1745(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1746/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1746(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1747/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1747(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1748/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1748(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1749/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1749(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1750/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1750(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1751/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1751(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1752/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1752(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1753/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1753(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1754/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1754(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1755/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1755(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1756/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1756(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1757/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1757(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1758/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1758(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1759/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1759(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1760/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1760(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1761/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1761(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1762/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1762(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1763/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1763(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1764/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1764(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1765/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1765(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1766/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1766(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1767/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1767(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1768/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1768(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1769/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1769(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1770/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1770(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1771/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1771(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1772/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1772(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1773/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1773(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1774/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1774(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1775/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1775(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1776/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1776(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1777/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1777(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1778/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1778(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1779/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1779(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1780/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1780(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1781/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1781(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1782/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1782(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1783/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1783(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1784/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1784(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1785/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1785(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1786/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1786(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1787/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1787(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1788/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1788(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1789/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1789(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1790/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1790(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1791/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1791(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1792/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1792(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1793/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1793(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1794/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1794(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1795/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1795(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1796/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1796(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1797/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1797(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1798/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1798(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1799/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1799(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1800/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1800(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1801/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1801(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1802/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1802(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1803/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1803(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1804/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1804(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1805/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1805(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1806/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1806(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1807/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1807(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1808/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1808(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1809/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1809(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1810/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1810(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1811/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1811(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1812/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1812(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1813/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1813(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1814/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1814(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1815/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1815(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1816/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1816(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1817/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1817(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1818/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1818(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1819/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1819(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1820/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1820(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1821/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1821(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1822/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1822(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1823/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1823(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1824/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1824(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1825/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1825(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1826/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1826(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1827/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1827(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1828/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1828(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1829/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1829(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1830/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1830(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1831/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1831(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1832/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1832(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1833/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1833(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1834/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1834(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1835/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1835(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1836/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1836(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1837/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1837(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1838/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1838(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1839/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1839(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1840/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1840(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1841/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1841(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1842/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1842(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1843/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1843(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1844/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1844(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1845/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1845(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1846/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1846(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1847/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1847(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1848/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1848(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1849/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1849(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1850/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1850(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1851/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1851(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1852/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1852(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1853/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1853(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1854/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1854(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1855/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1855(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1856/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1856(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1857/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1857(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1858/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1858(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1859/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1859(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1860/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1860(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1861/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1861(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1862/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1862(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1863/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1863(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1864/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1864(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1865/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1865(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1866/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1866(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1867/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1867(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1868/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1868(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1869/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1869(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1870/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1870(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1871/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1871(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1872/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1872(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1873/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1873(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1874/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1874(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1875/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1875(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1876/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1876(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1877/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1877(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1878/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1878(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1879/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1879(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1880/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1880(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1881/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1881(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1882/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1882(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1883/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1883(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1884/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1884(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1885/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1885(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1886/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1886(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1887/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1887(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1888/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1888(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1889/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1889(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1890/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1890(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1891/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1891(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1892/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1892(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1893/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1893(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1894/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1894(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1895/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1895(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1896/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1896(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1897/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1897(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1898/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1898(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1899/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1899(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1900/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1900(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1901/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1901(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1902/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1902(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1903/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1903(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1904/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1904(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1905/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1905(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1906/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1906(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1907/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1907(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1908/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1908(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1909/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1909(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1910/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1910(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1911/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1911(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1912/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1912(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1913/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1913(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1914/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1914(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1915/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1915(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1916/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1916(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1917/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1917(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1918/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1918(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1919/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1919(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1920/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1920(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1921/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1921(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1922/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1922(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1923/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1923(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1924/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1924(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1925/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1925(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1926/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1926(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1927/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1927(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1928/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1928(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1929/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1929(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1930/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1930(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1931/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1931(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1932/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1932(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1933/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1933(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1934/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1934(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1935/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1935(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1936/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1936(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1937/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1937(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1938/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1938(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1939/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1939(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1940/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1940(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1941/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1941(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1942/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1942(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1943/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1943(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1944/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1944(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1945/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1945(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1946/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1946(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1947/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1947(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1948/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1948(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1949/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1949(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1950/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1950(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1951/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1951(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1952/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1952(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1953/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1953(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1954/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1954(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1955/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1955(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1956/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1956(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1957/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1957(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1958/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1958(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1959/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1959(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1960/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1960(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1961/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1961(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1962/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1962(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1963/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1963(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1964/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1964(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1965/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1965(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1966/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1966(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1967/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1967(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1968/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1968(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1969/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1969(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1970/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1970(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1971/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1971(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1972/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1972(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1973/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1973(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1974/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1974(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1975/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1975(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1976/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1976(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1977/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1977(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1978/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1978(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1979/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1979(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1980/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1980(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1981/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1981(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1982/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1982(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1983/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1983(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1984/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1984(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1985/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1985(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1986/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1986(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1987/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1987(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1988/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1988(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1989/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1989(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1990/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1990(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1991/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1991(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1992/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1992(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1993/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1993(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1994/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1994(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1995/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1995(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1996/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1996(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1997/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1997(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1998/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1998(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink1999/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod1999(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink(value = "dld://methodDeepLink2000/{param1}", activityClassFqn = "com.airbnb.deeplinkdispatch.sample.benchmarkable.ScaleTestActivity")
  public static Intent intentForDeepLinkMethod2000(Context context) {
    return new Intent(context, ScaleTestActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  private void showToast(String message) {
    Toast.makeText(this, "Deep Link: " + message, Toast.LENGTH_SHORT).show();
  }
}
