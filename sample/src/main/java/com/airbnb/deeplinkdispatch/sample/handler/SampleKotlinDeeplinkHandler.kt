package com.airbnb.deeplinkdispatch.sample.handler

import android.content.Context
import android.util.Log
import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
import com.airbnb.deeplinkdispatch.sample.WebDeepLink

const val TAG = "KotlinDeepLinkHandler"

@WebDeepLink(
    "/kotlin/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}/{path_segment_variable_4}?show_taxes={query_param_1}&queryParam={query_param_2}",
)
object SampleKotlinDeepLinkHandler :
    IntermediateDeepLinkHandler1<TestKotlinDeepLinkHandlerDeepLinkArgs>() {
    override fun handleDeepLink(
        context: Context,
        deepLinkArgs: TestKotlinDeepLinkHandlerDeepLinkArgs,
    ) {
        /**
         * From here any internal/3rd party navigation framework can be called the provided args.
         */
        Log.d(TAG, "SampleKotlinDeepLinkHandler with $deepLinkArgs")
    }
}

@WebDeepLink(
    "/partial/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}/{path_segment_variable_4}?show_taxes={query_param_1}&queryParam={query_param_2}",
)
object SamplePartialParamKotlinDeepLinkHandler :
    DeepLinkHandler<TestKotlinDeepLinkHandlerDeepLinkArgsMissingPathParamExtraQueryParam> {
    override fun handleDeepLink(
        context: Context,
        deepLinkArgs: TestKotlinDeepLinkHandlerDeepLinkArgsMissingPathParamExtraQueryParam,
    ) {
        /**
         * From here any internal/3rd party navigation framework can be called the provided args.
         */
        Log.d(TAG, "SamplePartialParamKotlinDeepLinkHandler with $deepLinkArgs")
    }
}

@WebDeepLink(
    "/noparams/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}/{path_segment_variable_4}?show_taxes={query_param_1}&queryParam={query_param_2}",
)
object SampleNoParamsKotlinDeepLinkHandler :
    DeepLinkHandler<Any> {
    override fun handleDeepLink(
        context: Context,
        deepLinkArgs: Any,
    ) {
        /**
         * From here any internal/3rd party navigation framework can be called the provided args.
         */
        Log.d(TAG, "SampleNoParamsKotlinDeepLinkHandler with $deepLinkArgs")
    }
}

abstract class InBetweenAnnotatedAndImplementingClass : DeepLinkHandlerThatIsUsedAsExtensionByAnotherClass()

abstract class DeepLinkHandlerThatIsUsedAsExtensionByAnotherClass : DeepLinkHandler<Any> {
    override fun handleDeepLink(
        context: Context,
        deepLinkArgs: Any,
    ) {
        /**
         * From here any internal/3rd party navigation framework can be called the provided args.
         */
        Log.d(TAG, "SampleNoParamsKotlinDeepLinkHandler with $deepLinkArgs")
    }
}

@WebDeepLink(
    "/noparamsExtension/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}/{path_segment_variable_4}?show_taxes={query_param_1}&queryParam={query_param_2}",
)
object SomeObjectThatExtendsAClassThatImplementsTheDeepLinkHandler : InBetweenAnnotatedAndImplementingClass()

abstract class IntermediateDeepLinkHandler1<T> : IntermediateDeepLinkHandler2<T>()

abstract class IntermediateDeepLinkHandler2<T> : DeepLinkHandler<T> {
    override fun handleDeepLink(
        context: Context,
        deepLinkArgs: T,
    ) {
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
    @DeeplinkParam("queryParam", DeepLinkParamType.Query) var queryParam: Int?,
)

class TestKotlinNoDataClassDeepLinkHandlerDeepLinkArgs(
    // path params can be non null
    @DeeplinkParam("path_segment_variable_1", DeepLinkParamType.Path) val uuid: Long,
    @DeeplinkParam("path_segment_variable_2", DeepLinkParamType.Path) val name: String,
    @DeeplinkParam("path_segment_variable_3", DeepLinkParamType.Path) val number: Float,
    @DeeplinkParam("path_segment_variable_4", DeepLinkParamType.Path) val flag: Boolean,
    @DeeplinkParam("show_taxes", DeepLinkParamType.Query) val show_taxes: Boolean?,
    @DeeplinkParam("queryParam", DeepLinkParamType.Query) var queryParam: Int?,
)

data class TestKotlinDeepLinkHandlerDeepLinkArgsMissingPathParamExtraQueryParam(
    // path params can be non null
    @DeeplinkParam("path_segment_variable_1", DeepLinkParamType.Path) val uuid: Long,
    @DeeplinkParam("show_taxes", DeepLinkParamType.Query) val show_taxes: Boolean?,
    @DeeplinkParam("queryParam", DeepLinkParamType.Query) var queryParam: Int?,
    @DeeplinkParam("non_existent", DeepLinkParamType.Query) var nonExistent: Int?,
)
