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

import java.util.regex.Pattern

data class DeepLinkEntry(
    val uriTemplate: String,
    val type: Type,
    /**
     * The class where the annotation corresponding to where an instance of DeepLinkEntry is declared.
     */
    val activityClass: Class<*>,
    val method: String? = null
) {
  /**
   * Thread safety mode is disable in this class in order to gain better performance.
   * Parallel usage is not expected.
   */
  private val prefix: String by lazy(mode = LazyThreadSafetyMode.NONE) {
    prefix(uriTemplate)
  }

  private val parsedUri: DeepLinkUri by lazy(mode = LazyThreadSafetyMode.NONE) {
    DeepLinkUri.parse(uriTemplate)
  }

  private val regex: Pattern by lazy(mode = LazyThreadSafetyMode.NONE) {
    Pattern.compile("${parsedUri.schemeHostAndPath().replace(PARAM_REGEX.toRegex(), PARAM_VALUE)}$")
  }

  private val parameters: Set<String> by lazy(mode = LazyThreadSafetyMode.NONE) {
    parsedUri.parameters()
  }

  enum class Type {
    CLASS,
    METHOD
  }

  /**
   * Generates a map of parameters and the values from the given deep link.
   *
   * @param inputUri the intent Uri used to launch the Activity
   * @return the map of parameter values, where all values will be strings.
   */
  fun getParameters(inputUri: String): MutableMap<String, String> {
    val paramsMap = mutableMapOf<String, String>()
    val deepLinkUri = DeepLinkUri.parse(inputUri)
    regex.matcher(deepLinkUri.schemeHostAndPath()).takeIf {
      it.matches()
    }?.let { matcher ->
      parameters.forEachIndexed { index, key ->
        val value = matcher.group(index + 1)
        if (!value?.trim().isNullOrEmpty()) {
          paramsMap[key] = value
        }
      }
    }
    return paramsMap
  }

  fun matchesPrefix(inputUri: String): Boolean = inputUri.startsWith(prefix)

  fun matches(inputUri: String): Boolean {
    val deepLinkUri = DeepLinkUri.parse(inputUri)
    return deepLinkUri != null && regex.matcher(deepLinkUri.schemeHostAndPath()).find()
  }

  companion object {
    private const val PARAM_VALUE = "([a-zA-Z0-9_#'!+%~,\\-\\.\\@\\$\\:]+)"
    private const val PARAM = "([a-zA-Z][a-zA-Z0-9_-]*)"
    private const val PARAM_REGEX = "%7B($PARAM)%7D"
    private val PARAM_PATTERN = Pattern.compile(PARAM_REGEX)

    /**
     * Gets the set of unique path parameters used in the given URI. If a parameter is used twice
     * in the URI, it will only show up once in the set.
     */
    private fun DeepLinkUri.parameters(): Set<String> {
      val matcher = PARAM_PATTERN.matcher("${encodedHost()}${encodedPath()}")
      val patterns = mutableSetOf<String>()
      while (matcher.find()) {
        patterns.add(matcher.group(1))
      }
      return patterns
    }

    private fun DeepLinkUri.schemeHostAndPath(): String = "${scheme()}://${encodedHost()}${encodedPath()}"

    private fun prefix(uriTemplate: String): String {
      val pos = uriTemplate.indexOf("{")
      return if (pos == -1) uriTemplate else uriTemplate.substring(0, pos)
    }
  }
}
