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

import androidx.room.compiler.processing.XElement
import androidx.room.compiler.processing.XMemberContainer
import androidx.room.compiler.processing.XMethodElement
import androidx.room.compiler.processing.XTypeElement
import java.net.MalformedURLException

sealed class DeepLinkAnnotatedElement @Throws(MalformedURLException::class) constructor(
    val uri: String,
    val element: XElement,
    val annotatedClass: XMemberContainer
) {
    class MethodAnnotatedElement(uri: String, element: XMethodElement) : DeepLinkAnnotatedElement(uri, element, element.enclosingElement) {
        val method = element.name
    }

    class ClassAnnotatedElement(uri: String, element: XTypeElement) : DeepLinkAnnotatedElement(uri, element, element)

    init {
        DeepLinkUri.parseTemplate(uri)
            ?: throw MalformedURLException("Malformed Uri Template: $uri")
    }
}
