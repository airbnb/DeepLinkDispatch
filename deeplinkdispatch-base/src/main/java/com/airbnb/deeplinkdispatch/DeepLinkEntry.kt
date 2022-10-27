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

data class DeepLinkMatchResult(
    val deeplinkEntry: DeepLinkEntry,
    val parameterMap: Map<DeepLinkUri, Map<String, String>>
) : Comparable<DeepLinkMatchResult> {
    /**
     * Generates a map of parameters and the values from the given deep link.
     *
     * @param inputUri the intent Uri used to launch the Activity
     * @return the map of parameter values, where all values will be strings.
     */
    fun getParameters(inputUri: DeepLinkUri): Map<String, String> {
        return parameterMap[inputUri] ?: emptyMap()
    }

    override fun toString(): String {
        return "uriTemplate: ${deeplinkEntry.uriTemplate} " +
                "activity: ${deeplinkEntry.clazz.name} " +
                "${if (deeplinkEntry is DeepLinkEntry.MethodDeeplinkEntry) "method: ${deeplinkEntry.method} " else ""}" +
                "parameters: $parameterMap"
    }

    /**
     * Whatever template has the first placeholder (and then configurable path segment) is the less
     * concrete one.
     * Because if they would have been all in the same index those elements would have been on the
     * same level and in the same "list" of elements we compare in order.
     * In this case the one with the more concete element would have won and the same is true here.
     */
    override fun compareTo(other: DeepLinkMatchResult): Int {
        return this.deeplinkEntry.compareTo(other.deeplinkEntry)
    }
}

sealed class DeepLinkEntry(open val uriTemplate: String, open val className: String) :
    Comparable<DeepLinkEntry> {

    data class ActivityDeeplinkEntry(
        override val uriTemplate: String,
        override val className: String
    ) : DeepLinkEntry(uriTemplate, className)

    data class MethodDeeplinkEntry(
        override val uriTemplate: String,
        override val className: String,
        val method: String
    ) : DeepLinkEntry(uriTemplate, className)

    data class HandlerDeepLinkEntry(
        override val uriTemplate: String,
        override val className: String
    ) : DeepLinkEntry(uriTemplate, className)

    val clazz: Class<*> by lazy {
        try {
            Class.forName(className)
        } catch (e: ClassNotFoundException) {
            throw IllegalStateException(
                "Deeplink class $className not found. If you are using Proguard" +
                        "/R8/Dexguard please consult README.md for correct configuration.",
                e
            )
        }
    }

    companion object {
        private val placeholderRegex = "\\{.*?\\}".toRegex()
    }

    private val uriTemplateWithoutPlaceholders: String by lazy {
        uriTemplate.replace(placeholderRegex, "")
    }

    private val firstConfigurablePathSegmentIndex: Int by lazy {
        uriTemplate.indexOf(
            configurablePathSegmentPrefixChar
        )
    }
    private val firstPlaceholderIndex: Int by lazy {
        uriTemplate.indexOf(
            componentParamPrefixChar
        )
    }
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

    fun templatesMatchesSameUrls(other: DeepLinkEntry) = uriTemplateWithoutPlaceholders == other.uriTemplateWithoutPlaceholders

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
                } else if (uriTemplate[firstNonConcreteIndex] == configurablePathSegmentPrefixChar) {
                    -1
                } else 1
            }
            else -> 1
        }
    }
}
