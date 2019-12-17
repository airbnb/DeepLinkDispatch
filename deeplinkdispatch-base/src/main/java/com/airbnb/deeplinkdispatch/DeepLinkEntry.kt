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

class DeepLinkEntry(val uriTemplate: String,
                    val type: Type,
                    /**
                     * The class where the annotation corresponding to where an instance of DeepLinkEntry is declared.
                     */
                    val activityClass: Class<*>, val method: String?) {

    enum class Type {
        CLASS,
        METHOD
    }

    private val parameterMap: MutableMap<DeepLinkUri, Map<String, String>> = mutableMapOf()

    /**
     * Generates a map of parameters and the values from the given deep link.
     *
     * @param inputUri the intent Uri used to launch the Activity
     * @return the map of parameter values, where all values will be strings.
     */
    fun getParameters(inputUri: DeepLinkUri): Map<String, String> {
        return parameterMap.get(inputUri) ?: emptyMap()
    }

    fun setParameters(deepLinkUri: DeepLinkUri, parameterMap: Map<String, String>) {
        this.parameterMap[deepLinkUri] = parameterMap
    }
}