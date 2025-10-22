@file:Suppress("ktlint:standard:import-ordering")

package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.handler.TypeConverter
import com.airbnb.deeplinkdispatch.handler.TypeConverters

import org.assertj.core.api.Assertions.assertThat

import org.junit.Test

class TypeConverterTests {
    private val stringTypeConverter =
        object : TypeConverter<String> {
            override fun convert(value: String): String = value
        }

    private val longTypeConverter =
        object : TypeConverter<Long> {
            override fun convert(value: String): Long = value.toLong()
        }

    @Test
    fun testAddingSameTypeTwice() {
        val typeConverters1 = TypeConverters()
        typeConverters1.put(String::class.java, stringTypeConverter)
        val typeConverters2 = TypeConverters()
        typeConverters2.put(String::class.java, stringTypeConverter)
        val existingTypeConverters = typeConverters1.putAll(typeConverters2)
        assertThat(existingTypeConverters).hasSize(1)
    }

    @Test
    fun testAddingDifferentTypes() {
        val typeConverters1 = TypeConverters()
        typeConverters1.put(String::class.java, stringTypeConverter)
        val typeConverters2 = TypeConverters()
        typeConverters2.put(Long::class.java, longTypeConverter)
        val existingTypeConverters = typeConverters1.putAll(typeConverters2)
        assertThat(existingTypeConverters).isEmpty()
    }
}
