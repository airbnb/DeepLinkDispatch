package com.airbnb.deeplinkdispatch.sample.handler

import android.content.Context
import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
import com.airbnb.deeplinkdispatch.sample.WebDeepLink

@WebDeepLink("/kotlin/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}/{path_segment_variable_4}?show_taxes={query_param_1}&queryParam={query_param_2}")
object SampleKotlinDeepLinkHandler :
    IntermediateDeepLinkHandler1<TestKotlinDeepLinkHandlerDeepLinkArgs>() {
    override fun handleDeepLink(
        context: Context,
        parameters: TestKotlinDeepLinkHandlerDeepLinkArgs
    ) {
        /**
         * From here any internal/3rd party navigation framework can be called the provided args.
         */
    }
}

@WebDeepLink("/partial/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}/{path_segment_variable_4}?show_taxes={query_param_1}&queryParam={query_param_2}")
object SamplePartialParamKotlinDeepLinkHandler :
    DeepLinkHandler<TestKotlinDeepLinkHandlerDeepLinkArgsMissingPathParamExtraQueryParm> {
    override fun handleDeepLink(
        context: Context,
        parameters: TestKotlinDeepLinkHandlerDeepLinkArgsMissingPathParamExtraQueryParm
    ) {
        /**
         * From here any internal/3rd party navigation framework can be called the provided args.
         */
    }
}

abstract class IntermediateDeepLinkHandler1<T> : IntermediateDeepLinkHandler2<T>()

abstract class IntermediateDeepLinkHandler2<T> : DeepLinkHandler<T> {
    override fun handleDeepLink(context: Context, parameters: T) {
        TODO("Not yet implemented")
    }
}

data class TestKotlinDeepLinkHandlerDeepLinkArgs(
    // path params can be non null
    @DeeplinkParam("path_segment_variable_1", DeepLinkParamType.Path) val uuid: Long,
    @DeeplinkParam("path_segment_variable_2", DeepLinkParamType.Path) val name: String,
    @DeeplinkParam("path_segment_variable_3", DeepLinkParamType.Path) val number: Float,
    @DeeplinkParam("path_segment_variable_4", DeepLinkParamType.Path) val flag: Boolean,
    @DeeplinkParam("show_taxes", DeepLinkParamType.Query) val show_taxes: Boolean?,
    @DeeplinkParam("queryParam", DeepLinkParamType.Query) var queryParam: Int?
)

class TestKotlinNoDataClassDeepLinkHandlerDeepLinkArgs(
    // path params can be non null
    @DeeplinkParam("path_segment_variable_1", DeepLinkParamType.Path) val uuid: Long,
    @DeeplinkParam("path_segment_variable_2", DeepLinkParamType.Path) val name: String,
    @DeeplinkParam("path_segment_variable_3", DeepLinkParamType.Path) val number: Float,
    @DeeplinkParam("path_segment_variable_4", DeepLinkParamType.Path) val flag: Boolean,
    @DeeplinkParam("show_taxes", DeepLinkParamType.Query) val show_taxes: Boolean?,
    @DeeplinkParam("queryParam", DeepLinkParamType.Query) var queryParam: Int?
)

data class TestKotlinDeepLinkHandlerDeepLinkArgsMissingPathParamExtraQueryParm(
    // path params can be non null
    @DeeplinkParam("path_segment_variable_1", DeepLinkParamType.Path) val uuid: Long,
    @DeeplinkParam("show_taxes", DeepLinkParamType.Query) val show_taxes: Boolean?,
    @DeeplinkParam("queryParam", DeepLinkParamType.Query) var queryParam: Int?,
    @DeeplinkParam("non_existent", DeepLinkParamType.Query) var nonExistent: Int?
)