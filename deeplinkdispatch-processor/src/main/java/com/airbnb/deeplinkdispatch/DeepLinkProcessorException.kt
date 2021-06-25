package com.airbnb.deeplinkdispatch

import androidx.room.compiler.processing.XElement

class DeepLinkProcessorException @JvmOverloads constructor(
    message: String,
    val element: XElement? = null
) : RuntimeException(message)
