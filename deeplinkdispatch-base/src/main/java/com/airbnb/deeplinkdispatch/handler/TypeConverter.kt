package com.airbnb.deeplinkdispatch.handler

interface TypeConverter<T> {
    fun convert(value: String): T
}

class TypeConverters {
    private val backedMap = mutableMapOf<Class<*>, TypeConverter<*>>()

    fun <T> put(type: Class<T>, typeConverter: TypeConverter<T>) =
        backedMap.put(type, typeConverter)

    operator fun <T> get(key: Class<T>) = backedMap[key]
}
