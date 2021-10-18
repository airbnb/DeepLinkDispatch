package com.airbnb.deeplinkdispatch.handler

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class DeeplinkParam(val name: String, val type: DeepLinkParamType)

enum class DeepLinkParamType { Path, Query }
