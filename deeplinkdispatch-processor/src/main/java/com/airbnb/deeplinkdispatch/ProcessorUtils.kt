package com.airbnb.deeplinkdispatch

import androidx.room.compiler.processing.XTypeElement

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

fun XTypeElement.inheritanceHierarchy(): List<XTypeElement> {
    return this.superType?.typeElement?.let { it.inheritanceHierarchy() + listOf(it) }
        ?: emptyList()
}

fun XTypeElement.inheritanceHierarchyContains(fqnList: List<String>) =
    inheritanceHierarchy().any { typeElement ->
        typeElement.qualifiedName in fqnList
    }

fun XTypeElement.inheritanceHierarchyDoesNotContain(fqnList: List<String>) =
    inheritanceHierarchy().none { typeElement ->
        typeElement.qualifiedName in fqnList
    }
