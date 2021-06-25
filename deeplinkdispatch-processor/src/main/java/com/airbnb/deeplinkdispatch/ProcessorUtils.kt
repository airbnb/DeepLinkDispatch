package com.airbnb.deeplinkdispatch

object ProcessorUtils {
    @JvmStatic
    fun decapitalize(str: String?): String? {
        return if (str == null || (str.length > 1 && str[1].isUpperCase() && str[0].isUpperCase())) {
            str
        } else {
            str.replaceFirstChar { it.lowercase() }
        }
    }

    @JvmStatic
    fun hasEmptyOrNullString(strings: Array<String>) = strings.any { it.isNullOrEmpty() }
}
