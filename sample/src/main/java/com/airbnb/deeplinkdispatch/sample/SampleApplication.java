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

import android.app.Application;
import android.util.Log;

import com.airbnb.deeplinkdispatch.DeepLinkCallback;
import com.airbnb.deeplinkdispatch.DeepLinkError;

public class SampleApplication extends Application implements DeepLinkCallback {

  private static final String TAG = "DeepLinkDispatch";

  @Override
  public void onSuccess(String uri) {
    Log.i(TAG, "Successful deep link: " + uri.toString());
  }

  @Override
  public void onError(DeepLinkError error) {
    Log.e(TAG, "Deep Link Error: " + error.getErrorMessage());
  }
}
