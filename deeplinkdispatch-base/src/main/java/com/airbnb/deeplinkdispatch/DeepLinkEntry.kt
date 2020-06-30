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

import kotlin.math.min

class DeepLinkEntry(val uriTemplate: String,
                    val type: Type,
                    /**
                     * The class where the annotation corresponding to where an instance of DeepLinkEntry is declared.
                     */
                    val activityClass: Class<*>, val method: String?) : Comparable<DeepLinkEntry> {

    enum class Type {
        CLASS,
        METHOD
    }

    private val parameterMap: MutableMap<DeepLinkUri, Map<String, String>> = mutableMapOf()

    private val firstConfigurablePathSegmentIndex: Int by lazy { uriTemplate.indexOf(configurablePathSegmentPrefixChar) }
    private val firstPlaceholderIndex: Int by lazy { uriTemplate.indexOf(componentParamPrefixChar) }
    private val firstNonConcreteIndex: Int by lazy {
        if (firstPlaceholderIndex == -1 && firstConfigurablePathSegmentIndex == -1) {
            -1
        } else {
            if (firstConfigurablePathSegmentIndex == -1) {
                firstPlaceholderIndex
            } else {
                if (firstPlaceholderIndex == -1) {
                    firstConfigurablePathSegmentIndex
                } else {
                    min(firstConfigurablePathSegmentIndex, firstPlaceholderIndex)
                }
            }
        }
    }

    /**
     * Generates a map of parameters and the values from the given deep link.
     *
     * @param inputUri the intent Uri used to launch the Activity
     * @return the map of parameter values, where all values will be strings.
     */
    fun getParameters(inputUri: DeepLinkUri): Map<String, String> {
        return parameterMap[inputUri] ?: emptyMap()
    }

    fun setParameters(deepLinkUri: DeepLinkUri, parameterMap: Map<String, String>) {
        this.parameterMap[deepLinkUri] = parameterMap
    }

    override fun toString(): String {
        return "uriTemplate: $uriTemplate type: $type activity: ${activityClass.simpleName} method: $method parameters: $parameterMap"
    }

    /**
     * Whatever template has the first placeholder (and then configurable path segment) is the less
     * concrete one.
     * Because if they would have been all in the same index those elements would have been on the
     * same level and in the same "list" of elements we compare in order.
     * In this case the one with the more concete element would have won and the same is true here.
     */
    override fun compareTo(other: DeepLinkEntry): Int {
        return when {
            this.firstNonConcreteIndex < other.firstNonConcreteIndex -> -1
            this.firstNonConcreteIndex == other.firstNonConcreteIndex -> {
                if (this.firstNonConcreteIndex == -1 || uriTemplate[firstNonConcreteIndex] == other.uriTemplate[firstNonConcreteIndex]) {
                    0
                } else if (this.uriTemplate[firstNonConcreteIndex] == configurablePathSegmentPrefixChar) {
                    -1
                } else 1
            }
            else -> 1
        }
    }
}