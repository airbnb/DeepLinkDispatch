package com.airbnb.deeplinkdispatch

import javax.lang.model.element.Element

class DeepLinkProcessorException @JvmOverloads constructor(
        message: String,
        val element: Element? = null
) : RuntimeException(message)
