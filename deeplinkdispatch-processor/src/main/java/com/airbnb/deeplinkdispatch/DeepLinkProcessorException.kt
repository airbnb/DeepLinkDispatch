package com.airbnb.deeplinkdispatch

import androidx.room.compiler.processing.XElement

class DeepLinkProcessorException
    @JvmOverloads
    constructor(
        val errorMessage: String,
        val element: XElement? = null,
    ) : RuntimeException(errorMessage)
