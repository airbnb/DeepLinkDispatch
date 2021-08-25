package com.airbnb.deeplinkdispatch.handler

@Target(AnnotationTarget.VALUE_PARAMETER, )
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class PathParam(val name: String)

@Target(AnnotationTarget.VALUE_PARAMETER, )
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class QueryParam(val name: String)