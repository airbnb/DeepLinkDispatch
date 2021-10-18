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

    /**
     * Add all TypeConverters from the argument.
     * Returns map of TypeConverters that exist in both this TypeConverters and in the argument
     * TypeConverters.
     */
    fun putAll(typeConverters: TypeConverters): Map<Type, TypeConverter<*>> {
        val existingConverters = backedMap.filterKeys { typeConverters.backedMap.keys.contains(it) }
        backedMap.putAll(typeConverters.backedMap)
        return existingConverters
    }

    operator fun get(key: Type) = backedMap[key]
}
