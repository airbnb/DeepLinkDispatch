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
 * Register a class or method to handle a deep link.
 * <pre><code>
 * {@literal @}DeepLink(uri);
 * </code></pre>
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
// When using tools like Dexguard we require these annotations to still be inside the .dex files
// produced by D8 but because of this bug https://issuetracker.google.com/issues/168524920 they
// are not so we need to mark them as RetentionPolicy.RUNTIME.
@Retention(RetentionPolicy.RUNTIME)
public @interface DeepLink {
  String IS_DEEP_LINK = "is_deep_link_flag";
  String URI = "deep_link_uri";
  String REFERRER_URI = "android.intent.extra.REFERRER";

  String[] value();
}
