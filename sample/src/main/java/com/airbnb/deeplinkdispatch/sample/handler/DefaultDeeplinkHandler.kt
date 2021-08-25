package com.airbnb.deeplinkdispatch.sample.handler

import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
import com.airbnb.deeplinkdispatch.handler.PathParam
import com.airbnb.deeplinkdispatch.handler.QueryParam
import com.airbnb.deeplinkdispatch.sample.WebDeepLink
import java.time.LocalDate

@WebDeepLink("pathSegment/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}?queryParam={query_param_1}")
class TestDeepLinkHandler : InBetweenDeeplinkHandler<TestDeepLinkHandlerDeepLinkArgs>() {

}

open class InBetweenDeeplinkHandler<T>() : DeepLinkHandler<T>() {

    override fun handleDeepLink(parameters: T) {
        TODO("Not yet implemented")
    }
}

data class TestDeepLinkHandlerDeepLinkArgs(
    // path params can be non null
    @PathParam("path_segment_variable_1") val uuid: Long,
    @PathParam("path_segment_variable_2") val name: String,
    @PathParam("path_segment_variable_3") val date: LocalDate,
    @QueryParam("show_taxes") val boolean: Boolean?
)
