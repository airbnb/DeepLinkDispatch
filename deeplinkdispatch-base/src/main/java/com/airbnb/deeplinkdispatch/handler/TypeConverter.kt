package com.airbnb.deeplinkdispatch.handler

import java.lang.reflect.Type

interface TypeConverter<T> {
    fun convert(value: String): T
}

class TypeConverters {
    private val backedMap = mutableMapOf<Type, TypeConverter<*>>()

    fun <T> put(type: Class<T>, typeConverter: TypeConverter<T>) =
        backedMap.put(type, typeConverter)

    fun <T> put(type: Type, typeConverter: TypeConverter<T>) =
        backedMap.put(type, typeConverter)

    operator fun get(key: Type) = backedMap[key]
}
