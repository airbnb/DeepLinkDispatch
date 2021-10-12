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
package com.airbnb.deeplinkdispatch.sample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.DeepLinkMethodResult;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.TaskStackBuilder;

import org.jetbrains.annotations.NotNull;

@DeepLink({
  "dld://classDeepLink",
  "http://example.com/foo{arg_end}",
  "http://example.com/{arg_start}bar",
  "dld://example.com/deepLink",
  "https://www.example.com/<configurable-path-segment>/bar",
  "https://www.example.com/<configurable-path-segment-one>/<configurable-path-segment-two>/foo",
  "https://www.example.com/cereal/<configurable-path-segment>",
  "https://www.example.com/nothing-special"})
public class MainActivity extends AppCompatActivity {
  private static final String ACTION_DEEP_LINK_METHOD = "deep_link_method";
  private static final String ACTION_DEEP_LINK_COMPLEX = "deep_link_complex";
  public static final String ACTION_DEEP_LINK_INNER = "deep_link_inner";
  public static final String ACTION_DEEP_LINK_INTENT = "deep_link_intent";
  public static final String ACTION_DEEP_LINK_TASK_STACK_BUILDER = "deep_link_taskstackbuilder";
  public static final String ACTION_DEEP_LINK_INTENT_AND_TASK_STACK_BUILDER =
    "deep_link_intent_and_taskstackbuilder";

  private static final String TAG = MainActivity.class.getSimpleName();

  public static class InnerClass {
    @DeepLink("dld://innerClassDeeplink")
    public static Intent intentForDeepLinkMethod(Context context) {
      return new Intent(context, SecondActivity.class).setAction(ACTION_DEEP_LINK_INNER);
    }
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.sample_activity_main);

    Intent intent = getIntent();
    if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
      String toastMessage;
      Bundle parameters = intent.getExtras();
      Log.d(TAG, "Deeplink params: " + parameters);

      if (ACTION_DEEP_LINK_METHOD.equals(intent.getAction())) {
        toastMessage = "method with param1:" + parameters.getString("param1");
      } else if (ACTION_DEEP_LINK_COMPLEX.equals(intent.getAction())) {
        toastMessage = parameters.getString("arbitraryNumber");
      } else if (ACTION_DEEP_LINK_INNER.equals(intent.getAction())) {
        toastMessage = "Deeplink on method in inner class";
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
   * @return A intent to start {@link MainActivity}
   */
  @DeepLink("dld://methodDeepLink/{param1}")
  public static Intent intentForDeepLinkMethod(Context context) {
    return new Intent(context, MainActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink("dld://host/somePath/{arbitraryNumber}")
  public static Intent intentForParamDeepLinkMethod(Context context) {
    return new Intent(context, MainActivity.class).setAction(ACTION_DEEP_LINK_COMPLEX);
  }

  /**
   * Handles deep link with params.
   * @param context of the activity
   * @param bundle expected to contain the key {@code qp}.
   * @return TaskStackBuilder set with first intent to start {@link MainActivity} and second intent
   * to start {@link SecondActivity}.
   */
  @DeepLink("http://example.com/deepLink/{id}/{name}/{place}")
  public static TaskStackBuilder intentForTaskStackBuilderMethods(Context context, Bundle bundle) {
    Log.d(TAG, "without query parameter :");
    if (bundle != null && bundle.containsKey("qp")) {
      Log.d(TAG, "found new parameter :with query parameter :" + bundle.getString("qp"));
    }
    TaskStackBuilder taskStackBuilder = getTaskStackBuilder(context, ACTION_DEEP_LINK_COMPLEX);
    return taskStackBuilder;
  }

  @NotNull
  private static TaskStackBuilder getTaskStackBuilder(Context context, String action) {
    Intent detailsIntent =
        new Intent(context, SecondActivity.class).setAction(action);
    Intent parentIntent =
        new Intent(context, MainActivity.class).setAction(action);
    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
    taskStackBuilder.addNextIntent(parentIntent);
    taskStackBuilder.addNextIntent(detailsIntent);
    return taskStackBuilder;
  }

  @DeepLink("dld://host/somePathOne/{arbitraryNumber}/otherPath")
  public static Intent intentForComplexMethod(Context context, Bundle bundle) {
    if (bundle != null && bundle.containsKey("qp")) {
      Log.d(TAG, "found new parameter :with query parameter :" + bundle.getString("qp"));
    }
    return new Intent(context, MainActivity.class);
  }

  @DeepLink("dld://host/methodResult/intent")
  public static DeepLinkMethodResult intentViaDeeplinkMethodResult(Context context) {
    return new DeepLinkMethodResult(new Intent(context, SecondActivity.class)
      .setAction(ACTION_DEEP_LINK_INTENT), null);
  }

  @DeepLink("dld://host/methodResult/intent/{parameter}")
  public static DeepLinkMethodResult intentViaDeeplinkMethodResult(Context context, Bundle parameter) {
    return new DeepLinkMethodResult(new Intent(context, SecondActivity.class)
      .setAction(ACTION_DEEP_LINK_INTENT), null);
  }

  @DeepLink("dld://host/methodResult/taskStackBuilder")
  public static
  DeepLinkMethodResult taskStackBuilderViaDeeplinkMethodResult(Context context) {
    return new DeepLinkMethodResult(null,
      getTaskStackBuilder(context, ACTION_DEEP_LINK_TASK_STACK_BUILDER));
  }

  @DeepLink("dld://host/methodResult/intentAndTaskStackBuilder")
  public static
  DeepLinkMethodResult taskStackBuilderAndIntentViaDeeplinkMethodResult(Context context) {
    return new DeepLinkMethodResult(new Intent(context, SecondActivity.class)
      .setAction(ACTION_DEEP_LINK_INTENT_AND_TASK_STACK_BUILDER),
      getTaskStackBuilder(context, ACTION_DEEP_LINK_INTENT_AND_TASK_STACK_BUILDER));
  }

  @DeepLink("dld://host/methodResult/null")
  public static DeepLinkMethodResult nullDeepLinkMethiodResult(Context context) {
    return null;
  }

  @DeepLink("dld://host/taskStackBuilder/null")
  public static TaskStackBuilder nullTaskStackBuilder(Context context) {
    return null;
  }

  @DeepLink("dld://host/intent/null")
  public static Intent nullIntent(Context context) {
    return null;
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
  public static Intent lessConcreteMatch(Context context, Bundle bundle) {
    if (bundle != null && bundle.containsKey("param1")) {
      Log.d(TAG, "matched less concrete url in sample project :" + bundle.getString("param1"));
    }
    return new Intent(context, MainActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  private void showToast(String message) {
    Toast.makeText(this, "Deep Link: " + message, Toast.LENGTH_SHORT).show();
  }
}
