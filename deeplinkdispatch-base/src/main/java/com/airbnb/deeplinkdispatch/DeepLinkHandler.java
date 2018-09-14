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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated {@code Activity} will receive deep links and handle them. This
 * will tell DeepLinkDispatch to generate a {@code DeepLinkDelegate} class that forwards the
 * incoming {@code Intent} to the correct Activities annotated with {@link DeepLink}.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.SOURCE)
public @interface DeepLinkHandler {
  /**
   * A list of {@link DeepLinkModule} annotated classes used for collecting all the deep links in
   * an application. The generated {@code DeepLinkDelegate} class will query the provided modules
   * in order to decide which {@code Activity} should receive the incoming deep link.
   */
  Class<?>[] value();

  String ACTION = "com.airbnb.deeplinkdispatch.DEEPLINK_ACTION";
  String EXTRA_SUCCESSFUL = "com.airbnb.deeplinkdispatch.EXTRA_SUCCESSFUL";
  String EXTRA_URI = "com.airbnb.deeplinkdispatch.EXTRA_URI";
  String EXTRA_URI_TEMPLATE = "com.airbnb.deeplinkdispatch.EXTRA_URI_TEMPLATE";
  String EXTRA_ERROR_MESSAGE = "com.airbnb.deeplinkdispatch.EXTRA_ERROR_MESSAGE";
}
