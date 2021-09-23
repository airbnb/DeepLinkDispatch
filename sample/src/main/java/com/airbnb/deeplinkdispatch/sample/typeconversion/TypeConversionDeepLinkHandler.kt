package com.airbnb.deeplinkdispatch.sample.typeconversion

import android.content.Context
import com.airbnb.deeplinkdispatch.DeepLink
import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
import com.airbnb.deeplinkdispatch.handler.DeeplinkParam

@DeepLink("http://testing.com/typeConversion/{number}/{color}")
object TypeConversionTestDeepLinkHandler : DeepLinkHandler<TypeConversionTestArgs> {
    override fun handleDeepLink(context: Context, parameters: TypeConversionTestArgs) {
        // Handle the deep link here
    }
}

data class TypeConversionTestArgs(
    @DeeplinkParam(name = "number", type = DeepLinkParamType.Path)val number: Long,
    @DeeplinkParam(name = "color", type = DeepLinkParamType.Path)val color: ComparableColorDrawable
)
