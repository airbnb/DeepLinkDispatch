/*
 * Copyright (C) 2015 Airbnb, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.airbnb.deeplinkdispatch

import androidx.room.compiler.codegen.toJavaPoet
import androidx.room.compiler.processing.XAnnotation
import androidx.room.compiler.processing.XElement
import androidx.room.compiler.processing.XExecutableParameterElement
import androidx.room.compiler.processing.XFiler
import androidx.room.compiler.processing.XMethodElement
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XRoundEnv
import androidx.room.compiler.processing.XTypeElement
import androidx.room.compiler.processing.addOriginatingElement
import androidx.room.compiler.processing.writeTo
import com.airbnb.deeplinkdispatch.ProcessorUtils.decapitalizeIfNotTwoFirstCharsUpperCase
import com.airbnb.deeplinkdispatch.ProcessorUtils.hasEmptyOrNullString
import com.airbnb.deeplinkdispatch.base.MatchIndex.ALLOWED_VALUES_DELIMITER
import com.airbnb.deeplinkdispatch.base.Utils
import com.airbnb.deeplinkdispatch.base.Utils.isConfigurablePathSegment
import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
import com.airbnb.deeplinkdispatch.handler.TypeConverters
import com.airbnb.deeplinkdispatch.metadata.Documentor
import com.airbnb.deeplinkdispatch.metadata.ManifestGenerator
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.WildcardTypeName
import com.squareup.kotlinpoet.javapoet.KotlinPoetJavaPoetPreview
import org.jetbrains.annotations.NotNull
import java.io.IOException
import java.lang.reflect.Type
import java.net.MalformedURLException
import java.util.Arrays
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.tools.Diagnostic
import kotlin.reflect.KClass

private const val CUSTOM_ANNOTATION_URL_PREFIX_DELIMITER = "#"

@KotlinPoetJavaPoetPreview
@ExperimentalUnsignedTypes
class DeepLinkProcessor(
    symbolProcessorEnvironment: SymbolProcessorEnvironment? = null,
) : BaseProcessor(symbolProcessorEnvironment) {
    private val documentor by lazy { Documentor(environment) }

    private val manifestGeneration by lazy { ManifestGenerator(environment) }

    private val deepLinkHandlerQName = "com.airbnb.deeplinkdispatch.handler.DeepLinkHandler"
    private val deepLinkHandlerHandleDeepLinkMethodName = "handleDeepLink"

    private val deepLinkHandlerInterface by lazy {
        environment.findTypeElement(deepLinkHandlerQName)!!
    }
    private val handleDeepLinkInterfaceMethod by lazy {
        deepLinkHandlerInterface
            .getAllMethods()
            .toList()
            .firstOrNull { it.name == "handleDeepLink" }
            ?: error("Interface has no single 'handleDeepLink' method which is impossible.")
    }

    private val incrementalMetadata: IncrementalMetadata by lazy {
        IncrementalMetadata(
            incremental = environment.options[OPTION_INCREMENTAL].toBoolean(),
            customAnnotationNames =
                environment.options[OPTION_CUSTOM_ANNOTATIONS]
                    ?.split("|")
                    ?.toSet() ?: emptySet(),
        )
    }

    private val supportedBaseAnnotations: Set<XTypeElement> by lazy {
        listOf(
            DeepLink::class,
            DeepLinkHandler::class,
            DeepLinkModule::class,
        ).map { environment.requireTypeElement(it) }.toSet()
    }

    override fun getSupportedAnnotationTypes(): Set<String> =
        if (incrementalMetadata.incremental) {
            (
                supportedBaseAnnotations.map { it.qualifiedName } +
                    incrementalMetadata.customAnnotationNames
            ).toSet()
        } else {
            setOf("*")
        }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun getSupportedOptions(): Set<String> {
        val supportedOptions =
            listOfNotNull(
                Documentor.DOC_OUTPUT_PROPERTY_NAME,
                OPTION_CUSTOM_ANNOTATIONS,
                OPTION_INCREMENTAL,
                if (incrementalMetadata.incremental) {
                    "org.gradle.annotation.processing.aggregating"
                } else {
                    null
                },
            )
        return supportedOptions.toSet()
    }

    /**
     * Called when all processing rounds are complete.
     * Writes the accumulated manifest containing deep links from all rounds.
     */
    override fun onProcessingFinished() {
        manifestGeneration.writeAccumulatedManifest()
    }

    override fun process(
        annotations: Set<XTypeElement>?,
        environment: XProcessingEnv,
        round: XRoundEnv,
    ) {
        try {
            // If we run KSP or this is configured to be incremental we need to rely on the
            // incrementalMetadata for custom annotations. If not filter them out of the
            // set of annotations we were given.
            val customAnnotations =
                if (incrementalMetadata.incremental ||
                    environment.backend == XProcessingEnv.Backend.KSP
                ) {
                    incrementalMetadata.customAnnotations
                } else {
                    annotations?.filterAnnotatedAnnotations(DeepLinkSpec::class) ?: emptySet()
                }
            val allDeepLinkAnnotatedElements =
                customAnnotations.flatMap { round.getElementsAnnotatedWith(it.qualifiedName) } +
                    round.getElementsAnnotatedWith(DEEP_LINK_CLASS)

            val annotatedMethodElements =
                allDeepLinkAnnotatedElements.filterIsInstance<XMethodElement>().toSet()
            val annotatedClassElements =
                allDeepLinkAnnotatedElements
                    .filterIsInstance<XTypeElement>()
                    .filter { it.isClass() }
                    .toSet()
            val annotatedObjectElements =
                allDeepLinkAnnotatedElements
                    .filterIsInstance<XTypeElement>()
                    .filter { it.isKotlinObject() }
                    .toSet()

            verifyAnnotatedType(
                allDeepLinkAnnotatedElements,
                annotatedClassElements,
                annotatedObjectElements,
                annotatedMethodElements,
            )

            // Create DeepLinkDelegate classes
            if (!createDeeplinkDelegates(roundEnv = round)) {
                return
            }

            // Create DeepLinkRegistry classes
            createDeeplinkRegistries(
                roundEnv = round,
                annotatedClassElements = annotatedClassElements,
                annotatedMethodElements = annotatedMethodElements,
                annotatedObjectElements = annotatedObjectElements,
                deepLinkElements =
                    collectDeepLinkElements(
                        prefixesAndFqn = customAnnotationPrefixes(customAnnotations),
                        classElementsToProcess = annotatedClassElements,
                        objectElementsToProcess = annotatedObjectElements,
                        methodElementsToProcess = annotatedMethodElements,
                    ),
            )
        } catch (e: Throwable) {
            if (e is DeepLinkProcessorException) {
                logError(
                    element = e.element,
                    message = e.errorMessage,
                )
            } else {
                // if it is an unexpected crash then the cause can get lost by KAPT unless we manually
                // catch and print the trace so that it is possible to debug.
                logError(
                    element = null,
                    message = "${e.javaClass.simpleName}: ${e.localizedMessage}\n${e.stackTraceToString()}",
                )
            }
        }
    }

    private fun collectDeepLinkElements(
        prefixesAndFqn: Map<String, Array<PrefixAndActivityFqn>>,
        classElementsToProcess: Set<XTypeElement>,
        objectElementsToProcess: Set<XTypeElement>,
        methodElementsToProcess: Set<XMethodElement>,
    ): List<DeepLinkAnnotatedElement> {
        val callDeeplinkAnnotatedElements =
            classElementsToProcess.flatMap { classElement ->
                verifyCass(classElement)
                mapUrisToDeepLinkAnnotatedElement(classElement, prefixesAndFqn)
            }
        val objectDeeplinkAnnotatedElements =
            objectElementsToProcess.flatMap { objectElement ->
                verifyObjectElement(objectElement)
                mapUrisToDeepLinkAnnotatedElement(objectElement, prefixesAndFqn)
            }
        val methodDeeplinkAnnotatedElements =
            methodElementsToProcess.flatMap { methodElement ->
                verifyMethod(methodElement)
                mapUrisToDeepLinkAnnotatedElement(methodElement, prefixesAndFqn)
            }
        return (callDeeplinkAnnotatedElements + objectDeeplinkAnnotatedElements + methodDeeplinkAnnotatedElements).filterNotNull()
    }

    private fun mapUrisToDeepLinkAnnotatedElement(
        element: XElement,
        prefixesAndFqn: Map<String, Array<PrefixAndActivityFqn>>,
    ): List<DeepLinkAnnotatedElement?> =
        getAllUrisAndActivityClassesForAnnotatedElement(
            element,
            prefixesAndFqn,
        ).mapNotNull { uriAndActivityFqn ->
            try {
                when {
                    element is XMethodElement ->
                        DeepLinkAnnotatedElement.MethodAnnotatedElement(
                            uri = uriAndActivityFqn.uri,
                            activityClassFqn = uriAndActivityFqn.activityClassFqn,
                            intentFilterAttributes = uriAndActivityFqn.intentFilterAttributes,
                            actions = uriAndActivityFqn.actions,
                            categories = uriAndActivityFqn.categories,
                            element = element,
                        )

                    element is XTypeElement && element.isActivity() ->
                        DeepLinkAnnotatedElement.ActivityAnnotatedElement(
                            uri = uriAndActivityFqn.uri,
                            activityClassFqn = uriAndActivityFqn.activityClassFqn,
                            intentFilterAttributes = uriAndActivityFqn.intentFilterAttributes,
                            actions = uriAndActivityFqn.actions,
                            categories = uriAndActivityFqn.categories,
                            element = element,
                        )

                    element is XTypeElement && element.isHandler() -> {
                        verifyHandlerMatchArgs(element, uriAndActivityFqn.uri)
                        DeepLinkAnnotatedElement.HandlerAnnotatedElement(
                            uri = uriAndActivityFqn.uri,
                            activityClassFqn = uriAndActivityFqn.activityClassFqn,
                            intentFilterAttributes = uriAndActivityFqn.intentFilterAttributes,
                            actions = uriAndActivityFqn.actions,
                            categories = uriAndActivityFqn.categories,
                            element = element,
                        )
                    }

                    else ->
                        error(
                            "Internal error: Elements can only be 'MethodAnnotatedElement', " +
                                "'ActivityAnnotatedElement' or 'HandlerAnnotatedElement'",
                        )
                }
            } catch (e: MalformedURLException) {
                environment.messager.printMessage(
                    kind = Diagnostic.Kind.ERROR,
                    msg = "Malformed Deep Link URL $uriAndActivityFqn.uri",
                )
                null
            }
        }

    private fun getAllUrisAndActivityClassesForAnnotatedElement(
        element: XElement,
        prefixesAndFqn: Map<String, Array<PrefixAndActivityFqn>>,
    ): List<UriAndActivityFqn> {
        val deepLinkAnnotation = element.getAnnotation(DEEP_LINK_CLASS)
        return getAllDeeplinkUrIsFromCustomDeepLinksOnElement(
            element = element,
            prefixesAndActivityFqnMap = prefixesAndFqn,
        ) +
            (deepLinkAnnotation?.getAsList<String>("value") ?: emptyList()).map { uri ->
                UriAndActivityFqn(
                    uri = uri,
                    activityClassFqn = (deepLinkAnnotation?.get("activityClassFqn")?.value as? String)?.ifEmpty { null },
                    actions = deepLinkAnnotation?.getAsList<String>("actions")?.toSet() ?: emptySet(),
                    categories = deepLinkAnnotation?.getAsList<String>("categories")?.toSet() ?: emptySet(),
                    intentFilterAttributes = deepLinkAnnotation?.getAsList<String>("intentFilterAttributes")?.toSet() ?: emptySet(),
                )
            }
    }

    private class UriAndActivityFqn(
        val uri: String,
        val activityClassFqn: String?,
        val actions: Set<String>,
        val categories: Set<String>,
        val intentFilterAttributes: Set<String>,
    )

    private class PrefixAndActivityFqn(
        val prefix: String,
        val activityClassFqn: String?,
        val actions: Set<String>,
        val categories: Set<String>,
        val intentFilterAttributes: Set<String>,
    )

    private fun verifyCass(classElement: XTypeElement) {
        if (!validClassElement(classElement)) {
            throw DeepLinkProcessorException(
                element = classElement,
                errorMessage =
                    "Only classes inheriting from either 'android.app.Activity' or public classes" +
                        " implementing the '$deepLinkHandlerQName' interface can be annotated with" +
                        " @DeepLink or another custom deep link annotation.",
            )
        }
    }

    private fun validClassElement(classElement: XTypeElement) = classElement.isActivity() || classElement.isHandler()

    private fun verifyMethod(methodElement: XMethodElement) {
        if (!methodElement.isStatic()) {
            throw DeepLinkProcessorException(
                element = methodElement,
                errorMessage = "Only static methods can be annotated with @${DEEP_LINK_CLASS.simpleName}",
            )
        } else if (methodElement.returnType.typeElement?.qualifiedName !in
            listOf(
                "android.content.Intent",
                "androidx.core.app.TaskStackBuilder",
                "com.airbnb.deeplinkdispatch.DeepLinkMethodResult",
            )
        ) {
            throw DeepLinkProcessorException(
                element = methodElement,
                errorMessage = (
                    "Only `Intent`, `androidx.core.app.TaskStackBuilder` or " +
                        "'com.airbnb.deeplinkdispatch.DeepLinkMethodResult' are supported. Please double " +
                        "check your imports and try again."
                ),
            )
        }
    }

    private fun verifyHandlerMatchArgs(
        element: XTypeElement,
        uriTemplate: String,
    ) {
        // Find a method on our element that override the DeepLinkHandler interface method
        val handlerMethod =
            element
                .getAllMethods()
                .singleOrNull {
                    it.overrides(
                        other = handleDeepLinkInterfaceMethod,
                        owner = element,
                    )
                }
                // The interface method might be on a superclass, in which case its arguments might be generic.
                // We want to make sure we're getting the concrete types of the final element.
                ?.asMemberOf(element.type)
                ?: error("Is not overriding method from interface. This is impossible.")

        val argsType = handlerMethod.parameterTypes.last()

        val argsTypeElement =
            argsType.typeElement
                ?: throw DeepLinkProcessorException(
                    element = element,
                    errorMessage = "Could not extract type element from handler interface type. Found type ${argsType.typeName}",
                )

        if (!argsTypeElement.isPublic()) {
            throw DeepLinkProcessorException(
                element = argsTypeElement,
                errorMessage = "Argument class must be public.",
            )
        }
        val argsConstructor =
            argsTypeElement.getConstructors().singleOrNull() ?: run {
                throw DeepLinkProcessorException(
                    element = argsTypeElement,
                    errorMessage = "Argument class for deeplink handler can only have a single constructor",
                )
            }
        val allArgParameters = argsConstructor.parameters
        val allPathParameters = allArgParameters.filterAnnotationType(DeepLinkParamType.Path)
        val allQueryParameters = allArgParameters.filterAnnotationType(DeepLinkParamType.Query)
        if (allPathParameters.size + allQueryParameters.size != allArgParameters.size) {
            throw DeepLinkProcessorException(
                element = argsTypeElement,
                errorMessage =
                    "All elements of the constructor need to be annotated with the @${DeeplinkParam::class.simpleName} annotation.\n" +
                        "Parameters: ${allArgParameters.joinToString { it.name }} " +
                        "Annotated parameters: ${(allPathParameters + allQueryParameters).joinToString { it.name }}",
            )
        }
        val deepLinkUriTemplate = DeepLinkUri.parseTemplate(uriTemplate)
        val templateHostPathSchemePlaceholders = deepLinkUriTemplate.schemeHostPathPlaceholders
        val annotatedPathParameterNames =
            allPathParameters
                .mapNotNull {
                    it.getAnnotation(DeeplinkParam::class)?.get("name")?.value as? String
                }.toSet()
        val annotatedPathParametersThatAreNotInUrlTemplate =
            annotatedPathParameterNames.filter { !templateHostPathSchemePlaceholders.contains(it) }
        if (annotatedPathParametersThatAreNotInUrlTemplate.isNotEmpty()) {
            throw DeepLinkProcessorException(
                element = argsTypeElement,
                errorMessage =
                    "The annotated path arguments in the arguments class must be a subset of" +
                        " the path placeholders contained in the url. Annotated in args class but not" +
                        " in uri template: ${annotatedPathParametersThatAreNotInUrlTemplate.joinToString()}",
            )
        }
    }

    private fun List<XExecutableParameterElement>.filterAnnotationType(deepLinkParamType: DeepLinkParamType) =
        filter { argParameter ->
            argParameter
                .getAllAnnotations()
                .find { annotation ->
                    annotation.qualifiedName == DeeplinkParam::class.qualifiedName
                }?.annotationValues
                ?.any { annotationValue ->
                    annotationValue.value.toString() == deepLinkParamType.toAnnotationValue()
                } ?: false
        }

    private fun <T : Enum<T>> Enum<T>.toAnnotationValue(): String =
        if (environment.backend == XProcessingEnv.Backend.KSP) {
            // KSP appends parent class name to annotation value
            "${declaringJavaClass.simpleName}.$this"
        } else {
            this.toString()
        }

    private fun verifyObjectElement(element: XTypeElement) {
        if (!element.isHandler()) {
            throw DeepLinkProcessorException(
                element = element,
                errorMessage =
                    "Only public objects implementing $deepLinkHandlerQName can be annotated" +
                        " with @${DEEP_LINK_CLASS.simpleName} or any custom deep link annotation",
            )
        }
        if (element
                .getAllMethods()
                .filter { it.name == deepLinkHandlerHandleDeepLinkMethodName && it.parameters.size == 2 }
                .count() != 1
        ) {
            throw DeepLinkProcessorException(
                element = element,
                errorMessage =
                    "More than one method with two parameters and" +
                        " $deepLinkHandlerHandleDeepLinkMethodName name found in handler class.",
            )
        }
    }

    private fun customAnnotationPrefixes(customAnnotations: Set<XTypeElement>): Map<String, Array<PrefixAndActivityFqn>> =
        customAnnotations
            .associate { customAnnotationTypeElement ->
                if (!customAnnotationTypeElement.isAnnotationClass()) {
                    logError(
                        element = customAnnotationTypeElement,
                        message = "Only annotation types can be annotated with @${DEEP_LINK_SPEC_CLASS.simpleName}",
                    )
                }
                val activityClassFqn: String? =
                    customAnnotationTypeElement
                        .getAnnotation(DEEP_LINK_SPEC_CLASS)
                        ?.let { xAnnotation ->
                            xAnnotation.get("activityClassFqn")?.value as? String
                        }?.ifEmpty { null }
                // The value of prefix is an array, however we do allow prefixes also to exist in a single string (within the array)
                // these are separated by a `#` char, which is not allowed to exist in the non path part of the URL without being escaped
                // and is not otherwise useed in the url template format of DLD.
                // Because the values within the array in the anotation definition have to be constant it is otherwise not possible to
                // easily configure multiple prefixes that are not hardcoded in the codebase of the app using DLD
                val prefixes =
                    customAnnotationTypeElement
                        .getArrayAnnotationParameter("prefix")
                        .map { singlePrefix ->
                            singlePrefix.split(CUSTOM_ANNOTATION_URL_PREFIX_DELIMITER)
                        }.flatten()
                        .toTypedArray()
                val actions = customAnnotationTypeElement.getArrayAnnotationParameter("actions")
                val categories = customAnnotationTypeElement.getArrayAnnotationParameter("categories")
                val intentFilterAttributes = customAnnotationTypeElement.getArrayAnnotationParameter("intentFilterAttributes")

                if (prefixes.hasEmptyOrNullString()) {
                    logError(
                        element = customAnnotationTypeElement,
                        message = "Prefix property cannot have null or empty strings",
                    )
                }
                if (prefixes.isEmpty()) {
                    logError(
                        element = customAnnotationTypeElement,
                        message = "Prefix property cannot be empty",
                    )
                }
                customAnnotationTypeElement.qualifiedName to
                    prefixes
                        .map {
                            PrefixAndActivityFqn(
                                prefix = it,
                                activityClassFqn = activityClassFqn,
                                actions = actions.toSet(),
                                categories = categories.toSet(),
                                intentFilterAttributes = intentFilterAttributes.toSet(),
                            )
                        }.toTypedArray()
            }

    private fun XTypeElement.getArrayAnnotationParameter(propertyName: String): Array<String> =
        getAnnotation(DEEP_LINK_SPEC_CLASS)?.getAsList<String>(propertyName)?.toTypedArray()
            ?: emptyArray()

    private fun verifyAnnotatedType(
        allAnnotatedElements: List<XElement>,
        annotatedClassElements: Set<XTypeElement>,
        annotatedObjectElements: Set<XTypeElement>,
        annotatedMethodElements: Set<XMethodElement>,
    ) {
        allAnnotatedElements
            .filter { annotatedElement ->
                !annotatedClassElements.contains(annotatedElement) &&
                    !annotatedMethodElements.contains(
                        annotatedElement,
                    ) &&
                    !annotatedObjectElements.contains(annotatedElement)
            }.forEach { annotatedElementThatIsNeitherClassNorMethod ->
                logError(
                    element = annotatedElementThatIsNeitherClassNorMethod,
                    message =
                        "Only classes, objects and methods can be annotated with @" +
                            "${DEEP_LINK_CLASS.simpleName} or custom deep link annotation",
                )
            }
    }

    private fun createDeeplinkDelegates(roundEnv: XRoundEnv): Boolean {
        val deeplinkHandlerAnnotatedElements =
            roundEnv
                .getElementsAnnotatedWith(DeepLinkHandler::class)
                .filterIsInstance<XTypeElement>()
        val packagesWithMoreThanOneDeepLinkHandler =
            deeplinkHandlerAnnotatedElements.groupBy { it.packageName }.filter { it.value.size > 1 }
        if (packagesWithMoreThanOneDeepLinkHandler.isNotEmpty()) {
            packagesWithMoreThanOneDeepLinkHandler.forEach { it ->
                logError(
                    element = it.value.first().enclosingTypeElement,
                    message =
                        "Only one @DeepLinkHandler annotated element allowed per package!" +
                            " ${it.key} has ${it.value.map(XTypeElement::qualifiedName).sorted().joinToString()}.",
                )
            }
            return false
        }
        deeplinkHandlerAnnotatedElements.forEach { deepLinkHandlerElement ->
            val deepLinkModuleElements =
                deepLinkHandlerElement
                    .getAnnotation(DeepLinkHandler::class)
                    ?.getAsTypeList("value")
                    ?.map { it.typeElement!! }
            if (deepLinkModuleElements != null) {
                tryCatchFileWriting {
                    generateDeepLinkDelegate(
                        deepLinkHandlerElement.packageName,
                        deepLinkModuleElements,
                        deepLinkHandlerElement,
                    )
                }
            }
        }
        return true
    }

    private fun createDeeplinkRegistries(
        roundEnv: XRoundEnv,
        annotatedClassElements: Set<XTypeElement>,
        annotatedMethodElements: Set<XMethodElement>,
        annotatedObjectElements: Set<XTypeElement>,
        deepLinkElements: List<DeepLinkAnnotatedElement>,
    ) {
        val deepLinkModuleAnnotatedElements =
            roundEnv
                .getElementsAnnotatedWith(DeepLinkModule::class)
                .filterIsInstance<XTypeElement>()
        validateAllowedPlaceholderValues(deepLinkElements)
        deepLinkModuleAnnotatedElements.forEach { deepLinkModuleElement ->
            tryCatchFileWriting {
                generateDeepLinkRegistry(
                    packageName = deepLinkModuleElement.packageName,
                    className = deepLinkModuleElement.asClassName().toJavaPoet().simpleName(),
                    deepLinkElements = deepLinkElements,
                    originatingElements =
                        annotatedClassElements + annotatedMethodElements + annotatedObjectElements + deepLinkModuleElement,
                )
            }
        }
    }

    private val placeholderRegex = "(?<=\\{)(.*?)(?=\\})".toRegex()

    private val allowedValuesRegex = "(?<=\\()(.*?)(?=\\))".toRegex()

    private fun validateAllowedPlaceholderValues(annotatedElements: List<DeepLinkAnnotatedElement>) {
        annotatedElements.forEach { annotatedElement ->
            val placeholderStrings = placeholderRegex.findAll(annotatedElement.uriTemplate).map { it.value }
            placeholderStrings.forEach { placeholderString ->
                val placeholderMatches = allowedValuesRegex.findAll(placeholderString)
                if (placeholderMatches.count() > 1) {
                    logError(
                        element = annotatedElement.element,
                        message = "Only one allowed placeholder values section allowed per placeholder.",
                    )
                }
                if (placeholderMatches.count() == 1 &&
                    placeholderString.substringAfter(
                        placeholderMatches.first().value,
                    ) != ALLOWED_VALUES_DELIMITER[1].toString()
                ) {
                    logError(
                        element = annotatedElement.element,
                        message = "Allowed placeholder values must be last in placeholder.",
                    )
                }
            }
        }
    }

    private fun tryCatchFileWriting(action: () -> Unit) {
        try {
            action.invoke()
        } catch (e: IOException) {
            environment.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "Error creating file: ${e.stackTraceToString()}",
            )
        } catch (e: RuntimeException) {
            environment.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "Internal error during annotation processing: ${e.stackTraceToString()}",
            )
        }
    }

    @Throws(IOException::class)
    private fun generateDeepLinkDelegate(
        packageName: String,
        registryClasses: List<XTypeElement>,
        originatingElement: XElement,
    ) {
        val deepLinkDelegateBuilder =
            TypeSpec
                .classBuilder("DeepLinkDelegate")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(CLASS_BASE_DEEP_LINK_DELEGATE)
                .addMethod(constructorWith(emptyList(), registryClasses))
                .addMethod(
                    constructorWith(
                        listOf(ConstructorParameters.ConfigurablePathSegments),
                        registryClasses,
                    ),
                ).addMethod(
                    constructorWith(
                        listOf(
                            ConstructorParameters.ConfigurablePathSegments,
                            ConstructorParameters.TypeConverters,
                        ),
                        registryClasses,
                    ),
                ).addMethod(
                    constructorWith(
                        listOf(
                            ConstructorParameters.ConfigurablePathSegments,
                            ConstructorParameters.TypeConverters,
                            ConstructorParameters.TypeConversionErrorNullable,
                            ConstructorParameters.TypeConversionErrorNonNullable,
                        ),
                        registryClasses,
                    ),
                ).addOriginatingElement(originatingElement)
        JavaFile
            .builder(packageName, deepLinkDelegateBuilder.build())
            .build()
            .writeTo(environment.filer, XFiler.Mode.Isolating)
    }

    private fun constructorWith(
        parameters: List<ConstructorParameters>,
        registryClasses: List<XTypeElement>,
    ): MethodSpec =
        MethodSpec
            .constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .apply {
                addParameters(
                    registryClasses
                        .map { registryClass ->
                            ParameterSpec
                                .builder(
                                    moduleElementToRegistryClassName(registryClass),
                                    moduleNameToRegistryName(registryClass).decapitalizeIfNotTwoFirstCharsUpperCase(),
                                ).addAnnotation(NotNull::class.java)
                                .build()
                        }.toList(),
                )
                if (parameters.contains(ConstructorParameters.ConfigurablePathSegments)) {
                    addParameter(
                        ParameterSpec
                            .builder(
                                ParameterizedTypeName.get(
                                    MutableMap::class.java,
                                    String::class.java,
                                    String::class.java,
                                ),
                                "configurablePathSegmentReplacements",
                            ).addAnnotation(NotNull::class.java)
                            .build(),
                    )
                }
                val superOfString = WildcardTypeName.supertypeOf(String::class.java)
                val function3 = Function3::class.java
                val functionalParameter =
                    ParameterizedTypeName.get(
                        ClassName.get(function3.packageName, function3.simpleName),
                        TypeName.get(DeepLinkUri::class.java),
                        TypeName.get(Type::class.java),
                        superOfString,
                        TypeName.get(Integer::class.java),
                    )
                if (parameters.contains(ConstructorParameters.TypeConverters)) {
                    val function0 = Function0::class.java
                    val typeConverterFunctionalParameter =
                        ParameterizedTypeName.get(
                            ClassName.get(function0.packageName, function0.simpleName),
                            TypeName.get(TypeConverters::class.java),
                        )
                    addParameter(
                        ParameterSpec
                            .builder(
                                typeConverterFunctionalParameter,
                                "typeConverters",
                            ).addAnnotation(NotNull::class.java)
                            .build(),
                    )
                }
                if (parameters.contains((ConstructorParameters.TypeConversionErrorNullable))) {
                    // Function1<? super String, Integer> typeConversionErrorNullable
                    addParameter(
                        ParameterSpec
                            .builder(
                                functionalParameter,
                                "typeConversionErrorNullable",
                            ).addAnnotation(NotNull::class.java)
                            .build(),
                    ).build()
                }
                if (parameters.contains(ConstructorParameters.TypeConversionErrorNonNullable)) {
                    addParameter(
                        ParameterSpec
                            .builder(
                                functionalParameter,
                                "typeConversionErrorNonNullable",
                            ).addAnnotation(NotNull::class.java)
                            .build(),
                    ).build()
                }
                addCode(initCodeblock(parameters, registryClasses))
            }.build()

    private fun initCodeblock(
        parameters: List<ConstructorParameters>,
        registryClasses: List<XTypeElement>,
    ): CodeBlock? =
        CodeBlock
            .builder()
            .apply {
                add("super(\$T.asList(\n", ClassName.get(Arrays::class.java))
                val moduleRegistriesArgument = CodeBlock.builder()
                val totalElements = registryClasses.size
                for (i in 0 until totalElements) {
                    moduleRegistriesArgument.add(
                        "\$L\$L",
                        moduleNameToRegistryName(registryClasses[i]).decapitalizeIfNotTwoFirstCharsUpperCase(),
                        if (i < totalElements - 1) ",\n" else "",
                    )
                }
                moduleRegistriesArgument.add(")")
                indent()
                add(moduleRegistriesArgument.build())
                if (parameters.contains(ConstructorParameters.ConfigurablePathSegments)) {
                    add(",\nconfigurablePathSegmentReplacements")
                }
                if (parameters.contains(ConstructorParameters.TypeConverters)) {
                    add(",\ntypeConverters")
                }
                if (parameters.contains((ConstructorParameters.TypeConversionErrorNullable)) ||
                    parameters.contains(
                        ConstructorParameters.TypeConversionErrorNonNullable,
                    )
                ) {
                    add(",\nnull")
                }
                if (parameters.contains((ConstructorParameters.TypeConversionErrorNullable))) {
                    add(",\ntypeConversionErrorNullable")
                }
                if (parameters.contains(ConstructorParameters.TypeConversionErrorNonNullable)) {
                    add(",\ntypeConversionErrorNonNullable")
                }
                unindent()
                add("\n);\n")
            }.build()

    enum class ConstructorParameters {
        ConfigurablePathSegments,
        TypeConverters,
        ErrorHandler,
        TypeConversionErrorNullable,
        TypeConversionErrorNonNullable,
    }

    private fun logError(
        element: XElement?,
        message: String,
    ) {
        if (element != null) {
            environment.messager.printMessage(Diagnostic.Kind.ERROR, message, element)
        } else {
            environment.messager.printMessage(Diagnostic.Kind.ERROR, message)
        }
    }

    @Throws(IOException::class)
    @ExperimentalUnsignedTypes
    private fun generateDeepLinkRegistry(
        packageName: String,
        className: String,
        deepLinkElements: List<DeepLinkAnnotatedElement>,
        originatingElements: Set<XElement>,
    ) {
        deepLinkElements.sortedWith(::deeplinkAnnotatedElementCompare)
        documentor.write(deepLinkElements)
        // Accumulate elements for manifest generation (written in onProcessingFinished)
        manifestGeneration.addElements(deepLinkElements, originatingElements.toList())
        val urisTrie = Root()
        val pathVariableKeys: MutableSet<String> = HashSet()
        for (element: DeepLinkAnnotatedElement in deepLinkElements) {
            val uriTemplate = element.uriTemplate
            try {
                when (element) {
                    is DeepLinkAnnotatedElement.ActivityAnnotatedElement ->
                        urisTrie.addToTrie(
                            DeepLinkEntry.ActivityDeeplinkEntry(
                                uriTemplate = uriTemplate,
                                className = element.className,
                            ),
                        )
                    is DeepLinkAnnotatedElement.MethodAnnotatedElement ->
                        urisTrie.addToTrie(
                            DeepLinkEntry.MethodDeeplinkEntry(
                                uriTemplate = uriTemplate,
                                className = element.className,
                                method = element.method,
                            ),
                        )
                    is DeepLinkAnnotatedElement.HandlerAnnotatedElement ->
                        urisTrie.addToTrie(
                            DeepLinkEntry.HandlerDeepLinkEntry(
                                uriTemplate = uriTemplate,
                                className = element.className,
                            ),
                        )
                }
            } catch (e: IllegalArgumentException) {
                logError(
                    element = element.annotatedClass,
                    message = e.message ?: "",
                )
            }
            val deeplinkUri = DeepLinkUri.parseTemplate(uriTemplate)
            // Keep track of pathVariables added in a module so that we can check at runtime to ensure
            // that all pathVariables have a corresponding entry provided to BaseDeepLinkDelegate.
            for (pathSegment: String in deeplinkUri.pathSegments()) {
                if (isConfigurablePathSegment(pathSegment)) {
                    pathVariableKeys.add(
                        pathSegment.substring(
                            CONFIGURABLE_PATH_SEGMENT_PREFIX.length,
                            pathSegment.length - CONFIGURABLE_PATH_SEGMENT_SUFFIX.length,
                        ),
                    )
                }
            }
        }
        val deepLinkRegistryBuilder =
            TypeSpec
                .classBuilder(
                    (className + REGISTRY_CLASS_SUFFIX),
                ).addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ClassName.get(BaseRegistry::class.java))
        val stringMethodNames = getStringMethodNames(urisTrie, deepLinkRegistryBuilder)
        val constructor =
            MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addCode(
                    CodeBlock
                        .builder()
                        .add(
                            "super(\$T.readMatchIndexFromStrings( new String[] {$stringMethodNames})",
                            CLASS_UTILS,
                        ).build(),
                ).addCode(generatePathVariableKeysBlock(pathVariableKeys))
                .build()

        // For debugging it is nice to have a file version of the index, just comment this in to get
        // on in the classpath
//    FileObject indexResource = filer.createResource(StandardLocation.CLASS_OUTPUT, "",
//    MatchIndex.getMatchIdxFileName(className));
//    urisTrie.writeToOutoutStream(indexResource.openOutputStream());
        deepLinkRegistryBuilder.addMethod(constructor)
        originatingElements.forEach { originatingElement ->
            deepLinkRegistryBuilder.addOriginatingElement(originatingElement)
        }
        JavaFile
            .builder(packageName, deepLinkRegistryBuilder.build())
            .build()
            .writeTo(environment.filer, XFiler.Mode.Aggregating)
    }

    private fun generatePathVariableKeysBlock(pathVariableKeys: Set<String>): CodeBlock {
        val pathVariableKeysBuilder = CodeBlock.builder()
        pathVariableKeysBuilder.add(
            ",\n" + "new \$T[]{",
            ClassName.get(String::class.java),
        )
        val pathVariableKeysArray = pathVariableKeys.toTypedArray()
        for (i in pathVariableKeysArray.indices) {
            pathVariableKeysBuilder.add("\$S", pathVariableKeysArray[i])
            if (i < pathVariableKeysArray.size - 1) {
                pathVariableKeysBuilder.add(", ")
            }
        }
        pathVariableKeysBuilder.add("});\n")
        return pathVariableKeysBuilder.build()
    }

    /**
     * Add methods containing the Strings to store the match index to the deepLinkRegistryBuilder and
     * return a string which contains the calls to those methods.
     *
     *
     * e.g. "method1(), method2()" etc.
     *
     * @param urisTrie                The [UrlTree] containing all Urls that can be matched.
     * @param deepLinkRegistryBuilder The builder used to add the methods
     * @return
     */
    private fun getStringMethodNames(
        urisTrie: Root,
        deepLinkRegistryBuilder: TypeSpec.Builder,
    ): StringBuilder {
        val stringMethodNames = StringBuilder()
        for ((i, charSequence: CharSequence) in urisTrie.getStrings().withIndex()) {
            val methodName = "matchIndex$i"
            stringMethodNames.append(methodName).append("(), ")
            deepLinkRegistryBuilder.addMethod(
                MethodSpec
                    .methodBuilder(methodName)
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                    .returns(String::class.java)
                    .addCode(CodeBlock.builder().add("return \$S;", charSequence).build())
                    .build(),
            )
        }
        return stringMethodNames
    }

    private class IncrementalMetadata(
        val incremental: Boolean,
        val customAnnotationNames: Set<String>,
    )

    private val IncrementalMetadata.customAnnotations: Set<XTypeElement>
        get() =
            customAnnotationNames
                .mapNotNull { environment.findTypeElement(it) }
                .toSet()

    companion object {
        private const val PACKAGE_NAME = "com.airbnb.deeplinkdispatch"
        private const val OPTION_CUSTOM_ANNOTATIONS = "deepLink.customAnnotations"
        private const val OPTION_INCREMENTAL = "deepLink.incremental"
        private val CLASS_BASE_DEEP_LINK_DELEGATE =
            ClassName.get(PACKAGE_NAME, "BaseDeepLinkDelegate")
        private val CLASS_UTILS = ClassName.get(Utils::class.java)
        private val DEEP_LINK_CLASS = DeepLink::class
        private val DEEP_LINK_SPEC_CLASS = DeepLinkSpec::class
        const val REGISTRY_CLASS_SUFFIX = "Registry"

        /**
         * For the given element find all custom deeplink elements on it and build all possible
         * URIs -- and (if defined) the activity fqns used to dispatch this deep link via the Manifest
         * -- that are supported by the set custom deeplinks.
         */
        private fun getAllDeeplinkUrIsFromCustomDeepLinksOnElement(
            element: XElement,
            prefixesAndActivityFqnMap: Map<String, Array<PrefixAndActivityFqn>>,
        ): List<UriAndActivityFqn> =
            element.findAnnotatedAnnotation<DeepLinkSpec>().flatMap { customAnnotation ->
                val suffixes = customAnnotation.getAsList<String>("value")
                val prefixesAndActivityFqns =
                    prefixesAndActivityFqnMap[customAnnotation.qualifiedName]
                        ?: throw DeepLinkProcessorException(
                            "Unable to find annotation '${customAnnotation.qualifiedName}' you must" +
                                " update 'deepLink.customAnnotations' within the build.gradle",
                        )
                prefixesAndActivityFqns.flatMap { prefixesAndActivityFqn ->
                    suffixes.map { suffix ->
                        UriAndActivityFqn(
                            uri = prefixesAndActivityFqn.prefix + suffix,
                            activityClassFqn = prefixesAndActivityFqn.activityClassFqn,
                            actions = prefixesAndActivityFqn.actions,
                            categories = prefixesAndActivityFqn.categories,
                            intentFilterAttributes = prefixesAndActivityFqn.intentFilterAttributes,
                        )
                    }
                }
            }

        internal inline fun <reified T : Annotation> XElement.findAnnotatedAnnotation(): List<XAnnotation> =
            getAllAnnotations().filter { annotation ->
                annotation.type.typeElement?.hasAnnotation(
                    T::class,
                ) == true
            }

        private fun Set<XTypeElement>.filterAnnotatedAnnotations(klass: KClass<*>): Set<XTypeElement> =
            filter {
                it.isAnnotationClass() &&
                    it
                        .getAllAnnotations()
                        .any { it.qualifiedName == klass.qualifiedName }
            }.toSet()

        private fun moduleNameToRegistryName(element: XTypeElement) =
            element.asClassName().toJavaPoet().simpleName() + REGISTRY_CLASS_SUFFIX

        private fun moduleElementToRegistryClassName(element: XTypeElement): ClassName =
            ClassName.get(
                getPackage(element),
                element.asClassName().toJavaPoet().simpleName() + REGISTRY_CLASS_SUFFIX,
            )

        private fun moduleElementToRegistryKClassName(element: XTypeElement): com.squareup.kotlinpoet.ClassName =
            com.squareup.kotlinpoet.ClassName(
                getPackage(element),
                element.asClassName().toJavaPoet().simpleName() + REGISTRY_CLASS_SUFFIX,
            )

        private fun getPackage(element: XTypeElement) = element.packageName

        private fun deeplinkAnnotatedElementCompare(
            element1: DeepLinkAnnotatedElement,
            element2: DeepLinkAnnotatedElement,
        ): Int {
            val uri1 = DeepLinkUri.parseTemplate(element1.uriTemplate)
            val uri2 = DeepLinkUri.parseTemplate(element2.uriTemplate)
            var comparisonResult = uri2.pathSegments().size - uri1.pathSegments().size
            if (comparisonResult == 0) {
                comparisonResult = uri2.queryParameterNames().size - uri1.queryParameterNames().size
            }
            if (comparisonResult == 0) {
                comparisonResult = uri1
                    .encodedPath()
                    .split("%7B".toRegex())
                    .toTypedArray()
                    .size -
                    uri2
                        .encodedPath()
                        .split("%7B".toRegex())
                        .toTypedArray()
                        .size
            }
            if (comparisonResult == 0) {
                val element1Representation =
                    when (element1) {
                        is DeepLinkAnnotatedElement.ActivityAnnotatedElement ->
                            element1.uriTemplate +
                                DeepLinkAnnotatedElement.ActivityAnnotatedElement::class.simpleName
                        is DeepLinkAnnotatedElement.HandlerAnnotatedElement ->
                            "handler_" + element1.uriTemplate +
                                DeepLinkAnnotatedElement.ActivityAnnotatedElement::class.simpleName
                        is DeepLinkAnnotatedElement.MethodAnnotatedElement ->
                            element1.uriTemplate + element1.method +
                                DeepLinkAnnotatedElement.MethodAnnotatedElement::class.simpleName
                    }
                val element2Representation =
                    when (element2) {
                        is DeepLinkAnnotatedElement.ActivityAnnotatedElement ->
                            element2.uriTemplate +
                                DeepLinkAnnotatedElement.ActivityAnnotatedElement::class.simpleName
                        is DeepLinkAnnotatedElement.HandlerAnnotatedElement ->
                            "handler_" + element2.uriTemplate +
                                DeepLinkAnnotatedElement.ActivityAnnotatedElement::class.simpleName
                        is DeepLinkAnnotatedElement.MethodAnnotatedElement ->
                            element2.uriTemplate + element2.method +
                                DeepLinkAnnotatedElement.MethodAnnotatedElement::class.simpleName
                    }
                comparisonResult = element1Representation.compareTo(element2Representation)
            }
            return comparisonResult
        }
    }

    private fun XTypeElement.isActivity() = inheritanceHierarchyContains(listOf("android.app.Activity"))

    private fun XTypeElement.isHandler() = implementsInterfaces(listOf(deepLinkHandlerQName)) && isPublic()
}
