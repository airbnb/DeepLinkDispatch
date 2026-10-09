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
package com.airbnb.deeplinkdispatch

/**
 * Declare a specification for a type of DeepLink. For example:
 *
 * ```
 * @DeepLinkSpec(
 * prefix = { "http://example.com", "https://example.com" })
 * public@interface WebDeepLink {
 * String[] value();
 * }
 * ```
 *
 * `@WebDeepLink({ "/foo", "/bar" })` will match any of
 *
 *  `http://example.com/foo`,
 *  `https://example.com/foo`,
 *  `http://example.com/bar`,
 *  `https://example.com/bar`
 *
 * Mandatorily you only need to provide a prefix. But by providing an activity class fqn you can
 * enable manifest generation. In that case an AndroidManifest.xml entry will be automatically
 * generated (requires applying the gradle plugin in the projects gradle file).
 *
 * @property prefix The prefix used to create all supported URLs (placeholders with allowed values are allowed (e.g. http{scheme(|s)})
 * @property activityClassFqn The fqn of an activity class that is going to handle the deep link.
 * If this is present and the Gradle plugin is applied DLD will automatically generate a manifest
 * entry for the deep link.
 * @property intentFilterAttributes Additional attributes that you want to add to the generated
 * `intent-filter`.
 * @property actions The actions that should be listed for the `intent-filter`. Default:
 * `android.intent.action.VIEW`
 * @property categories The categories that should be listed for the `intent-filter`. Default:
 * `android.intent.category.DEFAULT` and  `android.intent.category.BROWSABLE`.
 * @property unverifiedPrefix Prefixes that match exactly like [prefix], but whose generated
 * `intent-filter` leaves out [intentFilterAttributes]. Use it for hosts that can never pass App Links
 * verification, e.g. internal hosts only reachable on a VPN, in a spec that sets
 * `android:autoVerify="true"`: links to them still route in the app and from other apps, without
 * failing the verification of the hosts in [prefix]. Same format as [prefix], except that empty
 * strings are ignored, so a build config constant can be empty for some builds.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS) // When using tools like Dexguard we require these annotations to still be inside the .dex files
// produced by D8 but because of this bug https://issuetracker.google.com/issues/168524920 they
// are not so we need to mark them as RetentionPolicy.RUNTIME.
@Retention(AnnotationRetention.RUNTIME)
annotation class DeepLinkSpec(
    val prefix: Array<String>,
    val activityClassFqn: String = "",
    val intentFilterAttributes: Array<String> = [],
    val actions: Array<String> = ["android.intent.action.VIEW"],
    val categories: Array<String> = ["android.intent.category.DEFAULT", "android.intent.category.BROWSABLE"],
    val unverifiedPrefix: Array<String> = [],
)
