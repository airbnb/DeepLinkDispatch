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
package com.airbnb.deeplinkdispatch;

/**
 * Provides callback methods for success/errors on dispatching deep links.
 */
public interface DeepLinkCallback {

  /**
   * Called on a successful deep link.
   *
   * @param uri the URI of deep link that was initiated
   */
  void onSuccess(String uri);

  /**
   * Called on an error when deep linking.
   *
   * @param error the error when calling the deep link
   */
  void onError(DeepLinkError error);
}
