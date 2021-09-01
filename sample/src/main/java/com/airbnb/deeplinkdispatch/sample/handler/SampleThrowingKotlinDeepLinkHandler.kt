package com.airbnb.deeplinkdispatch.sample.handler

import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
import com.airbnb.deeplinkdispatch.sample.WebDeepLink

@WebDeepLink("/kotlin/throws/{path_segment_variable_1}")
object SampleThrowingKotlinDeepLinkHandler : DeepLinkHandler<TestArgsForFailedTypeConversion>() {
    override fun handleDeepLink(parameters: TestArgsForFailedTypeConversion) {
        println("Received handler call with $parameters")
        /**
         * From here any internal/3rd party navigation framework can be called the provided args.
         */
    }

    override val throwOnTypeConversion = true
}

data class TestArgsForFailedTypeConversion(
    // path params can be non null
    @DeeplinkParam("path_segment_variable_1", DeepLinkParamType.Path) val uuid: Long,
)

