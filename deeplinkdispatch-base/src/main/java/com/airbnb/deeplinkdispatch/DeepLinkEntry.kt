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
    val parameterMap: Map<DeepLinkUri, Map<String, String>>,
) : Comparable<DeepLinkMatchResult> {
    /**
     * Generates a map of parameters and the values from the given deep link.
     *
     * @param inputUri the intent Uri used to launch the Activity
     * @return the map of parameter values, where all values will be strings.
     */
    fun getParameters(inputUri: DeepLinkUri): Map<String, String> = parameterMap[inputUri] ?: emptyMap()

    override fun toString(): String =
        "uriTemplate: ${deeplinkEntry.uriTemplate} " +
            "activity: ${deeplinkEntry.clazz.name} " +
            "${if (deeplinkEntry is DeepLinkEntry.MethodDeeplinkEntry) "method: ${deeplinkEntry.method} " else ""}" +
            "parameters: $parameterMap"

    /**
     * Whatever template has the first placeholder (and then configurable path segment) is the less
     * concrete one.
     * Because if they would have been all in the same index those elements would have been on the
     * same level and in the same "list" of elements we compare in order.
     * In this case the one with the more concete element would have won and the same is true here.
     */
    override fun compareTo(other: DeepLinkMatchResult): Int = this.deeplinkEntry.compareTo(other.deeplinkEntry)
}

sealed class DeepLinkEntry(
    open val uriTemplate: String,
    open val className: String,
) : Comparable<DeepLinkEntry> {
    data class ActivityDeeplinkEntry(
        override val uriTemplate: String,
        override val className: String,
    ) : DeepLinkEntry(uriTemplate, className)

    data class MethodDeeplinkEntry(
        override val uriTemplate: String,
        override val className: String,
        val method: String,
    ) : DeepLinkEntry(uriTemplate, className)

    data class HandlerDeepLinkEntry(
        override val uriTemplate: String,
        override val className: String,
    ) : DeepLinkEntry(uriTemplate, className)

    val clazz: Class<*> by lazy {
        try {
            Class.forName(className)
        } catch (e: ClassNotFoundException) {
            throw IllegalStateException(
                "Deeplink class $className not found. If you are using Proguard" +
                    "/R8/Dexguard please consult README.md for correct configuration.",
                e,
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
            CONFIGURABLE_PATH_SEGMENT_PREFIX_CHAR,
        )
    }
    private val firstPlaceholderIndex: Int by lazy {
        uriTemplate.indexOf(
            COMPONENT_PARAM_PREFIX_CHAR,
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

    /**
     * Count of placeholder parameters in the URI template
     */
    private val placeholderCount: Int by lazy {
        placeholderRegex.findAll(uriTemplate).count()
    }

    /**
     * Count of configurable path segments in the URI template
     */
    private val configurablePathSegmentCount: Int by lazy {
        uriTemplate.count { it == CONFIGURABLE_PATH_SEGMENT_PREFIX_CHAR }
    }

    /**
     * Total number of non-concrete elements (placeholders + configurable path segments)
     */
    private val totalNonConcreteElements: Int by lazy {
        placeholderCount + configurablePathSegmentCount
    }

    /**
     * List of all non-concrete element indices and their types, in order
     */
    private val nonConcreteElementIndicesAndTypes: List<Pair<Int, Char>> by lazy {
        val result = mutableListOf<Pair<Int, Char>>()
        var index = 0
        while (index < uriTemplate.length) {
            val char = uriTemplate[index]
            if (char == CONFIGURABLE_PATH_SEGMENT_PREFIX_CHAR || char == COMPONENT_PARAM_PREFIX_CHAR) {
                result.add(Pair(index, char))
            }
            index++
        }
        result
    }

    /**
     * Determines if two URI templates could potentially match the same URLs.
     *
     * This method checks:
     * 1. For placeholders with allowed values (e.g., {type(a|b)}): expands all possible values
     *    and checks if any could create a match with the other template
     * 2. For regular placeholders (without allowed values): uses regex matching since
     *    "..*" (the placeholder replacement) can match any non-empty string
     *
     * Note: Configurable path segments (<name>) are ignored as they have the same replacement
     * value within the same app.
     *
     * @return true if the templates could potentially match the same URLs
     */
    fun templatesMatchesSameUrls(other: DeepLinkEntry): Boolean {
        // Get all possible expanded values for both templates
        // This expands placeholders with allowed values to all their possible concrete forms
        // Regular placeholders (without allowed values) are replaced with "..*"
        val thisExpanded = uriTemplate.allPossibleValues()
        val otherExpanded = other.uriTemplate.allPossibleValues()

        // Check if any pair of expanded values could match
        for (thisValue in thisExpanded) {
            for (otherValue in otherExpanded) {
                if (expandedTemplatesCouldMatch(thisValue, otherValue)) {
                    return true
                }
            }
        }

        return false
    }

    /**
     * Checks if two expanded template values could match the same URL.
     *
     * After expansion by allPossibleValues():
     * - Placeholders with allowed values have been replaced with each allowed value
     * - Regular placeholders have been replaced with "..*" (matches one or more chars)
     *
     * We use "..*" as a regex pattern - it already means "any char followed by zero or more chars"
     * which effectively matches one or more characters.
     */
    private fun expandedTemplatesCouldMatch(
        template1: String,
        template2: String,
    ): Boolean {
        // If identical (including both having same wildcards), they match
        if (template1 == template2) {
            return true
        }

        // Convert template1 to regex (escape special chars, keep ..*) and check if template2 matches
        if (templateMatchesAsRegex(template1, template2)) {
            return true
        }

        // Also check reverse - template2 as regex against template1
        if (templateMatchesAsRegex(template2, template1)) {
            return true
        }

        return false
    }

    /**
     * Converts a template with "..*" wildcards to a regex and checks if the other template matches.
     */
    private fun templateMatchesAsRegex(
        templateWithWildcards: String,
        templateToMatch: String,
    ): Boolean {
        // If no wildcards, simple equality (already checked in caller)
        if (!templateWithWildcards.contains(SIMPLE_GLOB_PATTERN_MIN_ONE_CHAR)) {
            return templateWithWildcards == templateToMatch
        }

        // Build regex: escape everything except "..*" which becomes ".+" (one or more chars)
        val regexPattern =
            templateWithWildcards
                .split(SIMPLE_GLOB_PATTERN_MIN_ONE_CHAR)
                .joinToString(".+") { Regex.escape(it) }

        return Regex("^$regexPattern$").matches(templateToMatch)
    }

    /**
     * Compares two DeepLinkEntry instances by their concreteness (specificity).
     * More concrete (specific) entries are considered "less than" and will be matched first.
     *
     * Comparison logic:
     * 1. Fully concrete URLs (no placeholders/configurable segments) are most concrete
     * 2. URLs with earlier first non-concrete element position are more concrete
     * 3. When firstNonConcreteIndex is equal, we compare by:
     *    a. Total count of non-concrete elements (fewer = more concrete)
     *    b. Pairwise type and index comparison at each non-concrete position:
     *       - Iterate through all non-concrete element positions in order
     *       - At the first position where types differ, configurable path segment (<)
     *         is more concrete than placeholder ({})
     *       - If types are the same but indices differ, later index is more concrete
     *    c. Length of concrete parts (longer = more concrete)
     *
     * Examples of concreteness ordering (most to least concrete):
     * - scheme://host/path1/path2/path3 (fully concrete)
     * - scheme://host/path1/{param}/path3 (1 placeholder, later position)
     * - scheme://host/{param}/path2/path3 (1 placeholder, earlier position)
     * - scheme://host/{p1}/path2/path3/{p2} (2 placeholders, 2nd at later position)
     * - scheme://host/{p1}/path2/{p2}/path4 (2 placeholders, 2nd at earlier position)
     * - scheme://host/{param1}/path2/<config> (2 non-concrete, configurable at 2nd position)
     * - scheme://host/{param1}/path2/{param2} (2 non-concrete, placeholder at 2nd position)
     */
    override fun compareTo(other: DeepLinkEntry): Int {
        return when {
            /**
             * Specific conditions added for fully concrete links.
             * Concrete link will always return -1 for firstNonConcreteIndex,
             * so the general comparison logic will not work.
             */
            this.firstNonConcreteIndex < 0 && this.firstNonConcreteIndex != other.firstNonConcreteIndex -> -1
            other.firstNonConcreteIndex < 0 && other.firstNonConcreteIndex != this.firstNonConcreteIndex -> 1
            this.firstNonConcreteIndex < other.firstNonConcreteIndex -> 1
            this.firstNonConcreteIndex == other.firstNonConcreteIndex -> {
                when {
                    // Both are fully concrete
                    this.firstNonConcreteIndex == -1 -> 0
                    // Compare by total number of non-concrete elements (fewer is more concrete)
                    this.totalNonConcreteElements != other.totalNonConcreteElements ->
                        this.totalNonConcreteElements.compareTo(other.totalNonConcreteElements)
                    // Same number of non-concrete elements, compare types and positions pairwise
                    else -> {
                        val thisElements = this.nonConcreteElementIndicesAndTypes
                        val otherElements = other.nonConcreteElementIndicesAndTypes

                        // Compare types and indices at each non-concrete position
                        for (i in 0 until min(thisElements.size, otherElements.size)) {
                            val thisIndex = thisElements[i].first
                            val otherIndex = otherElements[i].first
                            val thisType = thisElements[i].second
                            val otherType = otherElements[i].second

                            // If types differ, configurable path segment is more concrete than placeholder
                            if (thisType != otherType) {
                                return if (thisType == CONFIGURABLE_PATH_SEGMENT_PREFIX_CHAR) {
                                    -1
                                } else {
                                    1
                                }
                            }

                            // Same type but different indices - later index is more concrete
                            if (thisIndex != otherIndex) {
                                return if (thisIndex > otherIndex) {
                                    -1
                                } else {
                                    1
                                }
                            }
                        }

                        // All types and positions match, compare by length of concrete parts (longer is more concrete)
                        if (this.uriTemplateWithoutPlaceholders.length != other.uriTemplateWithoutPlaceholders.length) {
                            -this.uriTemplateWithoutPlaceholders.length.compareTo(other.uriTemplateWithoutPlaceholders.length)
                        } else {
                            0
                        }
                    }
                }
            }
            else -> -1
        }
    }
}
