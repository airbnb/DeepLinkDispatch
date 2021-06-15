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

import java.net.MalformedURLException
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import kotlin.jvm.Throws

internal data class DeepLinkAnnotatedElement @Throws(MalformedURLException::class) constructor(
    val uri: String,
    val element: Element,
    val annotationType: DeepLinkEntry.Type
) {
    val annotatedElement: TypeElement
    val method: String?

    init {
        DeepLinkUri.parseTemplate(uri)
            ?: throw MalformedURLException("Malformed Uri Template: $uri")
        if (annotationType === DeepLinkEntry.Type.METHOD) {
            annotatedElement = element.enclosingElement as TypeElement
            method = element.simpleName.toString()
        } else {
            annotatedElement = element as TypeElement
            method = null
        }
    }
}