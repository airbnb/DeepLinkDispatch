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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;

@DeepLink({ "dld://classDeepLink", "http://example.com/foo{arg}", "dld://example.com/deepLink" })
public class MainActivity extends AppCompatActivity {
  private static final String ACTION_DEEP_LINK_METHOD = "deep_link_method";
  private static final String ACTION_DEEP_LINK_COMPLEX = "deep_link_complex";
  private static final String TAG = SecondActivity.class.getSimpleName();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
      String toastMessage;
      Bundle parameters = getIntent().getExtras();
      Log.d(TAG, "Deeplink params: " + parameters);

      if (ACTION_DEEP_LINK_METHOD.equals(getIntent().getAction())) {
        toastMessage = "method with param1:"  + parameters.getString("param1");
      } else if (ACTION_DEEP_LINK_COMPLEX.equals(getIntent().getAction())) {
        toastMessage = parameters.getString("arbitraryNumber");
      } else if (parameters.containsKey("arg")){
          toastMessage = "class and found arg:" + parameters.getString("arg");
      } else {
        toastMessage = "class";
      }

      // You can pass a query parameter with the URI, and it's also in parameters, like
      // dld://classDeepLink?qp=123
      if (parameters.containsKey("qp")) {
        toastMessage += " with query parameter " + parameters.getString("qp");
      }

      showToast(toastMessage);
    }
  }

  @DeepLink("dld://methodDeepLink/{param1}")
  public static Intent intentForDeepLinkMethod(Context context) {
    return new Intent(context, MainActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
  }

  @DeepLink("dld://host/somePath/{arbitraryNumber}")
  public static Intent intentForComplexMethod(Context context) {
    return new Intent(context, MainActivity.class).setAction(ACTION_DEEP_LINK_COMPLEX);
  }

  private void showToast(String message) {
    Toast.makeText(this, "Deep Link: " + message, Toast.LENGTH_SHORT).show();
  }
}
