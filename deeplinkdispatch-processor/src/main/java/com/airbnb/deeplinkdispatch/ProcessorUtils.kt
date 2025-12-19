package com.airbnb.deeplinkdispatch

import androidx.room.compiler.processing.XAnnotation
import androidx.room.compiler.processing.XAnnotationValue
import androidx.room.compiler.processing.XTypeElement
import androidx.room.compiler.processing.get

object ProcessorUtils {
    @JvmStatic
    fun String.decapitalizeIfNotTwoFirstCharsUpperCase(): String =
        if (this.length > 1 && this[0].isUpperCase() && this[1].isUpperCase()) {
            this
        } else {
            this.replaceFirstChar { it.lowercase() }
        }

    @JvmStatic
    fun Array<String>.hasEmptyOrNullString() = this.any { it.isNullOrEmpty() }
}

fun XTypeElement.implementedInterfaces(): List<XTypeElement> {
    // Implemented interfaces of supertype (recursively)
    return (superClass?.typeElement?.implementedInterfaces() ?: emptyList()) +
        // Implemented interface by this element
        getSuperInterfaceElements() +
        // Implemented interfaces the interfaces implemented by this type (recursively)
        getSuperInterfaceElements().flatMap { it.implementedInterfaces() }
}

fun XTypeElement.implementsInterfaces(fqnList: List<String>) =
    fqnList.all { interfaceFqn ->
        implementedInterfaces().any { typeElement -> typeElement.qualifiedName == interfaceFqn }
    }

fun XTypeElement.inheritanceHierarchy(): List<XTypeElement> =
    this.superClass?.typeElement?.let { it.inheritanceHierarchy() + listOf(it) }
        ?: emptyList()

fun XTypeElement.inheritanceHierarchyContains(fqnList: List<String>) =
    inheritanceHierarchy().any { typeElement ->
        typeElement.qualifiedName in fqnList
    }

fun XTypeElement.inheritanceHierarchyDoesNotContain(fqnList: List<String>) =
    inheritanceHierarchy().none { typeElement ->
        typeElement.qualifiedName in fqnList
    }

fun XTypeElement.directlyImplementsInterfaces(fqnList: List<String>): Boolean =
    fqnList.all { interfaceFqn ->
        getSuperInterfaceElements().any { typeElement -> typeElement.qualifiedName == interfaceFqn }
    }

@Suppress("UNCHECKED_CAST")
inline fun <reified T> XAnnotation.getAsList(method: String): List<T> {
    val originalList = get<List<T>>(method)
    // In new XProcessing versions List values are wrapped in XAnnotationValue but in old versions
    // they are the raw type.
    return if (originalList.firstOrNull() is XAnnotationValue) {
        // TODO: In the next full release of xprocessing we should be able to safely assume
        // the list type is always XAnnotationValue and can remove this if/else.
        (originalList as List<XAnnotationValue>).map { xAnnotationValue ->
            check(xAnnotationValue.value is T) {
                "Expected type ${T::class} but got ${xAnnotationValue.value?.javaClass}"
            }
            xAnnotationValue.value as T
        }
    } else {
        return originalList
    }
}
