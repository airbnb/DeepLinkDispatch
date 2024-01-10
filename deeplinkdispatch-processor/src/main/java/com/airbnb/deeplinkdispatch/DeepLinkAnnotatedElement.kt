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
    val uriTemplate: String,
    val activityClassFqn: String?,
    val element: XElement,
    val annotatedClass: XMemberContainer,
) {

    val deepLinkUri: DeepLinkUri by lazy {
        DeepLinkUri.parseTemplate(uriTemplate)
            ?: throw MalformedURLException("Malformed Uri Template: $uriTemplate")
    }

    val className: String by lazy { annotatedClass.className.reflectionName() ?: "" }

    class MethodAnnotatedElement(
        uri: String,
        activityClassFqn: String?,
        element: XMethodElement
    ) :
        DeepLinkAnnotatedElement(
            uriTemplate = uri,
            activityClassFqn = activityClassFqn,
            element = element,
            annotatedClass = element.enclosingElement
        ) {
        val method = element.name
    }

    class ActivityAnnotatedElement(
        uri: String,
        activityClassFqn: String?,
        element: XTypeElement
    ) :
        DeepLinkAnnotatedElement(
            uriTemplate = uri,
            activityClassFqn = activityClassFqn,
            element = element,
            annotatedClass = element
        )

    class HandlerAnnotatedElement(
        uri: String,
        activityClassFqn: String?,
        element: XTypeElement
    ) :
        DeepLinkAnnotatedElement(
            uriTemplate = uri,
            activityClassFqn = activityClassFqn,
            element = element,
            annotatedClass = element
        )

    init {
        DeepLinkUri.parseTemplate(uriTemplate)
            ?: throw MalformedURLException("Malformed Uri Template: $uriTemplate")
    }
}
