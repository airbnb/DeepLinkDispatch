package com.airbnb.deeplinkdispatch

object ProcessorUtils {
    @JvmStatic
    fun String.decapitalizeIfNotTwoFirstCharsUpperCase(): String {
        return if (this.length > 1 && this[0].isUpperCase() && this[1].isUpperCase()) {
            this
        } else {
            this.replaceFirstChar { it.lowercase() }
        }
    }

    @JvmStatic
    fun Array<String>.hasEmptyOrNullString() = this.any { it.isNullOrEmpty() }
}
