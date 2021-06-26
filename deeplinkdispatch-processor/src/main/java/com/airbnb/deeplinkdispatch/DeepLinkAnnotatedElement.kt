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
import androidx.room.compiler.processing.XMethodElement
import androidx.room.compiler.processing.XTypeElement
import java.net.MalformedURLException
import kotlin.jvm.Throws

internal data class DeepLinkAnnotatedElement @Throws(MalformedURLException::class) constructor(
    val uri: String,
    val element: XElement
) {
    val annotatedClass: XTypeElement
    val method: String?
    val annotationType: DeepLinkEntry.Type

    init {
        DeepLinkUri.parseTemplate(uri)
            ?: throw MalformedURLException("Malformed Uri Template: $uri")
        if (element is XMethodElement) {
            annotatedClass = element.enclosingElement as XTypeElement
            method = element.name
            annotationType = DeepLinkEntry.Type.METHOD
        } else {
            annotatedClass = element as XTypeElement
            method = null
            annotationType = DeepLinkEntry.Type.CLASS
        }
    }
}
