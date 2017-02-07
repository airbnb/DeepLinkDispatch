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
 * Declare a specification for a type of DeepLink. For example:
 * <pre><code>
 * {@literal @}DeepLinkSpec(
 *    prefix = { "http://example.com", "https://example.com" })
 *  public{@literal @}interface WebDeepLink {
 *    String[] value();
 *  }
 * </code></pre>
 * <p>
 * <code>{@literal @}WebDeepLink({ "/foo", "/bar" })</code> will match any of
 * <ul>
 * <li>http://example.com/foo</li>
 * <li>https://example.com/foo</li>
 * <li>http://example.com/bar</li>
 * <li>https://example.com/bar</li>
 * </ul>
 */
@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.CLASS)
public @interface DeepLinkSpec {
  String[] prefix();
}
