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

import androidx.room.compiler.processing.XAnnotation
import androidx.room.compiler.processing.XElement
import androidx.room.compiler.processing.XFiler
import androidx.room.compiler.processing.XMethodElement
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XRoundEnv
import androidx.room.compiler.processing.XType
import androidx.room.compiler.processing.XTypeElement
import androidx.room.compiler.processing.addOriginatingElement
import androidx.room.compiler.processing.get
import androidx.room.compiler.processing.writeTo
import com.airbnb.deeplinkdispatch.ProcessorUtils.decapitalizeIfNotTwoFirstCharsUpperCase
import com.airbnb.deeplinkdispatch.ProcessorUtils.hasEmptyOrNullString
import com.airbnb.deeplinkdispatch.base.Utils
import com.airbnb.deeplinkdispatch.base.Utils.isConfigurablePathSegment
import com.airbnb.deeplinkdispatch.handler.DEEP_LINK_HANDLER_METHOD_NAME
import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeSpec
import java.io.IOException
import java.net.MalformedURLException
import java.util.Arrays
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.tools.Diagnostic
import kotlin.collections.HashSet
import kotlin.reflect.KClass

class DeepLinkProcessor(symbolProcessorEnvironment: SymbolProcessorEnvironment? = null) :
    BaseProcessor(symbolProcessorEnvironment) {

    private val documentor by lazy { Documentor(environment) }

    private val incrementalMetadata: IncrementalMetadata by lazy {
        IncrementalMetadata(
            incremental = environment.options[OPTION_INCREMENTAL].toBoolean(),
            customAnnotations = environment.options[OPTION_CUSTOM_ANNOTATIONS]
                ?.split("|")
                ?.mapNotNull { environment.findTypeElement(it) }
                ?.toSet() ?: emptySet()
        )
    }

    private val supportedBaseAnnotations: Set<XTypeElement> by lazy {
        listOf(
            DeepLink::class,
            DeepLinkHandler::class,
            DeepLinkModule::class,
        ).map { environment.requireTypeElement(it) }.toSet()
    }

    /**
     * Overrides for AbstractProcessor
     */

    override fun getSupportedAnnotationTypes(): Set<String> {
        return if (incrementalMetadata.incremental) {
            (supportedBaseAnnotations + incrementalMetadata.customAnnotations)
                .map { it.qualifiedName }.toSet()
        } else setOf("*")
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedOptions(): Set<String> {
        val supportedOptions = listOf(
            Documentor.DOC_OUTPUT_PROPERTY_NAME,
            OPTION_CUSTOM_ANNOTATIONS,
            OPTION_INCREMENTAL,
            if (incrementalMetadata.incremental) {
                "org.gradle.annotation.processing.aggregating"
            } else null
        ).filterNotNull()
        return supportedOptions.toSet()
    }

    /**
     * End overrides for AbstractProcessor
     */

    override fun process(
        annotations: Set<XTypeElement>?,
        environment: XProcessingEnv,
        round: XRoundEnv
    ) {
        try {
            // If we run KSP or this is configured to be incremental we need to rely on the
            // incrementalMetadata for custom annotations. If not filter them out of the
            // set of annotations we were given.
            val customAnnotations = if (incrementalMetadata.incremental ||
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
                allDeepLinkAnnotatedElements.filterIsInstance<XTypeElement>()
                    .filter { it.isClass() }.toSet()
            val annotatedObjectElements =
                allDeepLinkAnnotatedElements.filterIsInstance<XTypeElement>()
                    .filter { it.isKotlinObject() }.toSet()

            verifyAnnotatedType(
                allDeepLinkAnnotatedElements,
                annotatedClassElements,
                annotatedObjectElements,
                annotatedMethodElements,
            )

            // Create DeepLinkDelegate classes
            createDeeplinkDelegates(roundEnv = round)

            // Create DeepLinkRegistry classes
            createDeeplinkRegistries(
                roundEnv = round,
                annotatedClassElements = annotatedClassElements,
                annotatedMethodElements = annotatedMethodElements,
                annotatedObjectElements = annotatedObjectElements,
                deepLinkElements = collectDeepLinkElements(
                    prefixes = customAnnotationPrefixes(customAnnotations),
                    classElementsToProcess = annotatedClassElements,
                    objectElementsToProcess = annotatedObjectElements,
                    methodElementsToProcess = annotatedMethodElements,
                ),
            )
        } catch (e: DeepLinkProcessorException) {
            logError(
                element = e.element,
                message = e.message ?: ""
            )
        }
    }

    private fun collectDeepLinkElements(
        prefixes: Map<XType, Array<String>>,
        classElementsToProcess: Set<XTypeElement>,
        objectElementsToProcess: Set<XTypeElement>,
        methodElementsToProcess: Set<XMethodElement>
    ): List<DeepLinkAnnotatedElement> {
        return (
            classElementsToProcess.flatMap { element ->
                if (verifyCass(element)) {
                    mapUrisToDeepLinkAnnotatedElement(element, prefixes)
                } else emptyList()
            } + objectElementsToProcess.flatMap { element ->
                if (verifyObjectElement(element)) {
                    mapUrisToDeepLinkAnnotatedElement(element, prefixes)
                } else emptyList()
            } + methodElementsToProcess.flatMap { element ->
                if (verifyMethod(element)) {
                    mapUrisToDeepLinkAnnotatedElement(element, prefixes)
                } else emptyList()
            }
            ).filterNotNull()
    }

    private fun mapUrisToDeepLinkAnnotatedElement(
        element: XElement,
        prefixes: Map<XType, Array<String>>
    ): List<DeepLinkAnnotatedElement?> =
        getAllUrisForAnnotatedElement(element, prefixes).mapNotNull { uri ->
            try {
                when {
                    element is XMethodElement -> DeepLinkAnnotatedElement.MethodAnnotatedElement(
                        uri = uri,
                        element = element
                    )
                    element is XTypeElement && element.isActivityElement() ->
                        DeepLinkAnnotatedElement.ActivityAnnotatedElement(
                            uri = uri,
                            element = element
                        )
                    element is XTypeElement && element.isHandlerElement() -> {
                        verifyHandlerMatchArgs(element, uri)
                        DeepLinkAnnotatedElement.HandlerAnnotatedElement(
                            uri = uri,
                            element = element
                        )
                    }
                    else -> error(
                        "Internal error: Elements can only be 'MethodAnnotatedElement', " +
                            "'ActivityAnnotatedElement' or 'HandlerAnnotatedElement'"
                    )
                }
            } catch (e: MalformedURLException) {
                environment.messager.printMessage(
                    kind = Diagnostic.Kind.ERROR,
                    msg = "Malformed Deep Link URL $uri"
                )
                null
            }
        }

    private fun XTypeElement.isActivityElement() = inheritanceHierarchyContains(listOf("android.app.Activity"))

    private fun XTypeElement.isHandlerElement() = inheritanceHierarchyContains(listOf(com.airbnb.deeplinkdispatch.handler.DeepLinkHandler::class.qualifiedName!!))

    private fun getAllUrisForAnnotatedElement(
        element: XElement,
        prefixes: Map<XType, Array<String>>
    ): List<String> {
        return getAllDeeplinkUrIsFromCustomDeepLinksOnElement(
            element = element,
            prefixesMap = prefixes
        ) + (element.getAnnotation(DEEP_LINK_CLASS)?.value?.value?.toList() ?: emptyList())
    }

    private fun verifyCass(classElement: XTypeElement): Boolean {
        return if (!validClassElement(classElement)) {
            logError(
                element = classElement,
                message =
                "Only classes inheriting from either 'android.app.Activity' or '" +
                    "${com.airbnb.deeplinkdispatch.handler.DeepLinkHandler::class.qualifiedName}" +
                    "' can be annotated with @DeepLink or another custom deep link annotation."
            )
            false
        } else true
    }

    private fun validClassElement(classElement: XTypeElement) =
        classElement.inheritanceHierarchyContains(
            listOf(
                "android.app.Activity",
                com.airbnb.deeplinkdispatch.handler.DeepLinkHandler::class.qualifiedName!!
            )
        )

    private fun verifyMethod(methodElement: XMethodElement): Boolean {
        return if (!methodElement.isStatic()) {
            logError(
                element = methodElement,
                message = "Only static methods can be annotated with @${DEEP_LINK_CLASS.simpleName}",
            )
            false
        } else
        // FIXME This is crashing with an NPE on internal classes when accessing the returnType.
        // You can jut comment this check out for now if you need this to pass.
            if (methodElement.returnType.typeElement?.qualifiedName !in listOf(
                    "android.content.Intent",
                    "androidx.core.app.TaskStackBuilder",
                    "com.airbnb.deeplinkdispatch.DeepLinkMethodResult"
                )
            ) {
                logError(
                    element = methodElement,
                    message = (
                        "Only `Intent`, `androidx.core.app.TaskStackBuilder` or " +
                            "'com.airbnb.deeplinkdispatch.DeepLinkMethodResult' are supported. Please double " +
                            "check your imports and try again."
                        )
                )
                false
            } else true
    }

    private fun verifyHandlerMatchArgs(element: XTypeElement, uriTemplate: String) {
        // The error here should be unreachable as we already checked before that com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
        // is on the inheritance stack which would force a type element.
        val argsTypes = element.superType?.typeArguments
            ?: error("Super type does not exist or does not have type argument")
        val argsType = argsTypes.singleOrNull() ?: run {
            logError(
                element = element,
                message = "Only one type argument allowed for DeepLinkHandler objects"
            )
            return
        }
        val argsConstructor = argsType.typeElement?.getConstructors()?.singleOrNull() ?: run {
            logError(
                element = argsType.typeElement ?: element,
                message = "Argument class can only have one constructor"
            )
            return
        }
        val allArgParameters = argsConstructor.parameters
        val allPathParameters = allArgParameters.filter { argParameter ->
            argParameter.getAnnotation(DeeplinkParam::class)?.value?.type == DeepLinkParamType.Path
        }
        val allQueryParameters = allArgParameters.filter { argParameter ->
            argParameter.getAnnotation(DeeplinkParam::class)?.value?.type == DeepLinkParamType.Query
        }
        if (allPathParameters.size + allQueryParameters.size != allArgParameters.size) {
            logError(
                element = argsType.typeElement ?: element,
                message = "All elements of the constructor need to be annotated with the @${DeeplinkParam::class.simpleName} annotation.\n" +
                    "Parameters: ${allArgParameters.joinToString { it.name }} " +
                    "Annotated parameters: ${(allPathParameters + allQueryParameters).joinToString { it.name }}"
            )
            return
        }
        if (allPathParameters.any { !isAllowedNonNullableType(it.type) }) {
            logError(
                element = argsType.typeElement ?: element,
                message = "For args constructor elements of type ${DeepLinkParamType.Path.name} only the following simple types are allowed: ${allowedNonNullableTypes.joinToString()}"
            )
            return
        }
        if (allQueryParameters.any { !isAllowedNullableType(it.type) }) {
            logError(
                element = argsType.typeElement ?: element,
                message = "For args constructor elements of type ${DeepLinkParamType.Query.name} only the following simple types are allowed: ${allowedNullableTypes.joinToString()}"
            )
            return
        }
        val deepLinkUriTemplate = DeepLinkUri.parseTemplate(uriTemplate)
        val templateHostPathSchemePlaceholders = deepLinkUriTemplate.schemeHostPathPlaceholders
        val annotatedPathParameterNames = allPathParameters.mapNotNull {
            it.getAnnotation(DeeplinkParam::class)?.value?.name
        }.toSet()
        if (annotatedPathParameterNames != templateHostPathSchemePlaceholders) {
            logError(
                element = argsType.typeElement ?: element,
                message = "All scheme/host/path placeholders in the uri template must be annotated in the argument class constructor. " +
                    "Present in urlTemplate: ${templateHostPathSchemePlaceholders.joinToString()} " +
                    "Present in constructor: ${annotatedPathParameterNames.joinToString()}"
            )
        }
        val templateQueryParameters = deepLinkUriTemplate.queryParameterNames()
        val annotatedQueryParameterNames = allQueryParameters.mapNotNull {
            it.getAnnotation(DeeplinkParam::class)?.value?.name
        }.toSet()
        if (annotatedQueryParameterNames != templateQueryParameters) {
            logError(
                element = argsType.typeElement ?: element,
                message = "All query elements in the uri template must be annotated in the argument class constructor. " +
                    "Present in urlTemplate: ${templateQueryParameters.joinToString()} " +
                    "Present in constructor: ${annotatedQueryParameterNames.joinToString()}"
            )
        }
    }

    private fun verifyObjectElement(element: XTypeElement): Boolean {
        if (!element.isHandlerElement()) {
            logError(
                element = element,
                message = "Only objects extending ${com.airbnb.deeplinkdispatch.handler.DeepLinkHandler::class.java.canonicalName} " +
                    "can be annotated with @${DEEP_LINK_CLASS.simpleName}"
            )
            return false
        }
        if (element.getAllMethods()
            .filter { it.name == DEEP_LINK_HANDLER_METHOD_NAME && it.parameters.size == 1 }.size != 1
        ) {
            logError(
                element = element,
                message = "More than one method with a single parameter and $DEEP_LINK_HANDLER_METHOD_NAME name found in handler class."
            )
            return false
        }
        return true
    }

    private val allowedNullableTypes = listOf(
        "java.lang.Byte",
        "java.lang.Short",
        "java.lang.Integer",
        "java.lang.Long",
        "java.lang.Float",
        "java.lang.Double",
        "java.lang.Boolean",
        "java.lang.String"
    )

    private fun isAllowedNullableType(type: XType) =
        allowedNullableTypes.any { it == type.typeName.toString() }

    private val allowedNonNullableTypes = listOf(
        "byte",
        "short",
        "int",
        "long",
        "float",
        "double",
        "boolean",
        "java.lang.String"
    )

    private fun isAllowedNonNullableType(type: XType) =
        allowedNonNullableTypes.any { it == type.typeName.toString() }

    private fun customAnnotationPrefixes(customAnnotations: Set<XTypeElement>): Map<XType, Array<String>> {
        return customAnnotations.map { customAnnotationTypeElement ->
            if (!customAnnotationTypeElement.isAnnotationClass()) {
                logError(
                    element = customAnnotationTypeElement,
                    message = "Only annotation types can be annotated with @${DEEP_LINK_SPEC_CLASS.simpleName}"
                )
            }
            val prefix: Array<String> =
                customAnnotationTypeElement.getAnnotation(DEEP_LINK_SPEC_CLASS)
                    ?.let { it.value.prefix } ?: emptyArray()
            if (prefix.hasEmptyOrNullString()) {
                logError(
                    element = customAnnotationTypeElement,
                    message = "Prefix property cannot have null or empty strings"
                )
            }
            if (prefix.isEmpty()) logError(
                element = customAnnotationTypeElement,
                message = "Prefix property cannot be empty"
            )
            customAnnotationTypeElement.type to prefix
        }.toMap()
    }

    private fun verifyAnnotatedType(
        allAnnotatedElements: List<XElement>,
        annotatedClassElements: Set<XTypeElement>,
        annotatedObjectElements: Set<XTypeElement>,
        annotatedMethodElements: Set<XMethodElement>
    ) {
        allAnnotatedElements.filter { annotatedElement ->
            !annotatedClassElements.contains(annotatedElement) && !annotatedMethodElements.contains(
                annotatedElement
            ) && !annotatedObjectElements.contains(annotatedElement)
        }.forEach { annotatedElementThatIsNeitherClassNorMethod ->
            logError(
                element = annotatedElementThatIsNeitherClassNorMethod,
                message = "Only classes, objects and methods can be annotated with @${DEEP_LINK_CLASS.simpleName}",
            )
        }
    }

    private fun createDeeplinkDelegates(roundEnv: XRoundEnv) {
        val deeplinkHandlerAnnotatedElements =
            roundEnv.getElementsAnnotatedWith(DeepLinkHandler::class)
                .filterIsInstance<XTypeElement>()
        deeplinkHandlerAnnotatedElements.forEach { deepLinkHandlerElement ->
            val deepLinkModuleElements =
                deepLinkHandlerElement.getAnnotation(DeepLinkHandler::class)?.getAsTypeList("value")
                    ?.map { it.typeElement!! }
            if (deepLinkModuleElements != null) {
                tryCatchFileWriting {
                    generateDeepLinkDelegate(
                        deepLinkHandlerElement.packageName,
                        deepLinkModuleElements,
                        deepLinkHandlerElement
                    )
                }
            }
        }
    }

    private fun createDeeplinkRegistries(
        roundEnv: XRoundEnv,
        annotatedClassElements: Set<XTypeElement>,
        annotatedMethodElements: Set<XMethodElement>,
        annotatedObjectElements: Set<XTypeElement>,
        deepLinkElements: List<DeepLinkAnnotatedElement>
    ) {
        val deepLinkModuleAnnotatedElements =
            roundEnv.getElementsAnnotatedWith(DeepLinkModule::class)
                .filterIsInstance<XTypeElement>()
        deepLinkModuleAnnotatedElements.forEach { deepLinkModuleElement ->
            tryCatchFileWriting {
                generateDeepLinkRegistry(
                    packageName = deepLinkModuleElement.packageName,
                    className = deepLinkModuleElement.className.simpleName(),
                    deepLinkElements = deepLinkElements,
                    originatingElements = annotatedClassElements + annotatedMethodElements + annotatedObjectElements + deepLinkModuleElement
                )
            }
        }
    }

    private fun tryCatchFileWriting(action: () -> Unit) {
        try {
            action.invoke()
        } catch (e: IOException) {
            environment.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "Error creating file: ${e.stackTraceToString()}"
            )
        } catch (e: RuntimeException) {
            environment.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "Internal error during annotation processing: ${e.stackTraceToString()}"
            )
        }
    }

    @Throws(IOException::class)
    private fun generateDeepLinkDelegate(
        packageName: String,
        registryClasses: List<XTypeElement>,
        originatingElement: XElement
    ) {
        val moduleRegistriesArgument = CodeBlock.builder()
        val totalElements = registryClasses.size
        for (i in 0 until totalElements) {
            moduleRegistriesArgument.add(
                "\$L\$L",
                moduleNameToRegistryName(registryClasses[i]).decapitalizeIfNotTwoFirstCharsUpperCase(),
                if (i < totalElements - 1) ",\n" else ""
            )
        }
        val registriesInitializerBuilder = CodeBlock.builder()
            .add("super(\$T.asList(\n", ClassName.get(Arrays::class.java))
            .indent()
            .add(moduleRegistriesArgument.build())
            .add("\n").unindent().add("));\n")
            .build()
        val registriesInitializerBuilderWithPathVariables = CodeBlock.builder()
            .add("super(\$T.asList(\n", ClassName.get(Arrays::class.java))
            .indent()
            .add(moduleRegistriesArgument.build())
            .add("),\nconfigurablePathSegmentReplacements").unindent().add("\n);\n")
            .build()
        val constructor = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameters(
                registryClasses.map { registryClass ->
                    ParameterSpec.builder(
                        moduleElementToRegistryClassName(registryClass),
                        moduleNameToRegistryName(registryClass).decapitalizeIfNotTwoFirstCharsUpperCase()
                    ).build()
                }.toList()
            )
            .addCode(registriesInitializerBuilder)
            .build()
        val configurablePathSegmentReplacementsParam = ParameterSpec.builder(
            ParameterizedTypeName.get(
                MutableMap::class.java,
                String::class.java,
                String::class.java
            ),
            "configurablePathSegmentReplacements"
        )
            .build()
        val constructorWithPathVariables = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameters(
                registryClasses.map { registryClass ->
                    ParameterSpec.builder(
                        moduleElementToRegistryClassName(registryClass),
                        moduleNameToRegistryName(registryClass).decapitalizeIfNotTwoFirstCharsUpperCase()
                    )
                        .build()
                }.toList()
            )
            .addParameter(configurablePathSegmentReplacementsParam)
            .addCode(registriesInitializerBuilderWithPathVariables)
            .build()
        val deepLinkDelegateBuilder = TypeSpec.classBuilder("DeepLinkDelegate")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(CLASS_BASE_DEEP_LINK_DELEGATE)
            .addMethod(constructor)
            .addMethod(constructorWithPathVariables)
            .addOriginatingElement(originatingElement)
        JavaFile.builder(packageName, deepLinkDelegateBuilder.build())
            .build()
            .writeTo(environment.filer, XFiler.Mode.Isolating)
    }

    private fun logError(element: XElement?, message: String) {
        environment.messager.printMessage(Diagnostic.Kind.ERROR, message, element)
    }

    @Throws(IOException::class)
    private fun generateDeepLinkRegistry(
        packageName: String,
        className: String,
        deepLinkElements: List<DeepLinkAnnotatedElement>,
        originatingElements: Set<XElement>
    ) {
        deepLinkElements.sortedWith(::deeplinkAnnotatedElementCompare)
        documentor.write(deepLinkElements)
        val urisTrie = Root()
        val pathVariableKeys: MutableSet<String> = HashSet()
        for (element: DeepLinkAnnotatedElement in deepLinkElements) {
            val uriTemplate = element.uri
            try {
                when (element) {
                    is DeepLinkAnnotatedElement.ActivityAnnotatedElement ->
                        urisTrie.addToTrie(
                            DeepLinkEntry.ActivityDeeplinkEntry(
                                uriTemplate = uriTemplate,
                                className = element.annotatedClass.className.reflectionName() ?: ""
                            )
                        )
                    is DeepLinkAnnotatedElement.MethodAnnotatedElement ->
                        urisTrie.addToTrie(
                            DeepLinkEntry.MethodDeeplinkEntry(
                                uriTemplate = uriTemplate,
                                className = element.annotatedClass.className.reflectionName() ?: "",
                                method = element.method
                            )
                        )
                    is DeepLinkAnnotatedElement.HandlerAnnotatedElement ->
                        urisTrie.addToTrie(
                            DeepLinkEntry.HandlerDeepLinkEntry(
                                uriTemplate = uriTemplate,
                                className = element.annotatedClass.className.reflectionName() ?: "",
                            )
                        )
                }
            } catch (e: IllegalArgumentException) {
                logError(
                    element = element.annotatedClass,
                    message = e.message ?: ""
                )
            }
            val deeplinkUri = DeepLinkUri.parseTemplate(uriTemplate)
            // Keep track of pathVariables added in a module so that we can check at runtime to ensure
            // that all pathVariables have a corresponding entry provided to BaseDeepLinkDelegate.
            for (pathSegment: String in deeplinkUri.pathSegments()) {
                if (isConfigurablePathSegment(pathSegment)) {
                    pathVariableKeys.add(
                        pathSegment.substring(
                            configurablePathSegmentPrefix.length,
                            pathSegment.length - configurablePathSegmentSuffix.length
                        )
                    )
                }
            }
        }
        val deepLinkRegistryBuilder = TypeSpec.classBuilder(
            (className + REGISTRY_CLASS_SUFFIX)
        ).addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(ClassName.get(BaseRegistry::class.java))
        val stringMethodNames = getStringMethodNames(urisTrie, deepLinkRegistryBuilder)
        val constructor = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addCode(
                CodeBlock.builder()
                    .add(
                        "super(\$T.readMatchIndexFromStrings( new String[] {$stringMethodNames})",
                        CLASS_UTILS
                    ).build()
            )
            .addCode(generatePathVariableKeysBlock(pathVariableKeys))
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
        JavaFile.builder(packageName, deepLinkRegistryBuilder.build()).build()
            .writeTo(environment.filer, XFiler.Mode.Aggregating)
    }

    private fun generatePathVariableKeysBlock(pathVariableKeys: Set<String>): CodeBlock {
        val pathVariableKeysBuilder = CodeBlock.builder()
        pathVariableKeysBuilder.add(
            ",\n" + "new \$T[]{",
            ClassName.get(String::class.java)
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
        deepLinkRegistryBuilder: TypeSpec.Builder
    ): StringBuilder {
        val stringMethodNames = StringBuilder()
        for ((i, charSequence: CharSequence) in urisTrie.getStrings().withIndex()) {
            val methodName = "matchIndex$i"
            stringMethodNames.append(methodName).append("(), ")
            deepLinkRegistryBuilder.addMethod(
                MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                    .returns(String::class.java)
                    .addCode(CodeBlock.builder().add("return \$S;", charSequence).build()).build()
            )
        }
        return stringMethodNames
    }

    private class IncrementalMetadata(
        val incremental: Boolean,
        val customAnnotations: Set<XTypeElement>
    )

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
         * URIs that are supported by theset custom deeplinks.
         */
        private fun getAllDeeplinkUrIsFromCustomDeepLinksOnElement(
            element: XElement,
            prefixesMap: Map<XType, Array<String>>
        ): List<String> {
            return element.findAnnotatedAnnotation<DeepLinkSpec>().flatMap { customAnnotation ->
                val suffixes = customAnnotation.get<List<String>>("value")
                val prefixes = prefixesMap[customAnnotation.type]
                    ?: throw DeepLinkProcessorException(
                        "Unable to find annotation '${customAnnotation.qualifiedName}' you must " +
                            "update 'deepLink.customAnnotations' within the build.gradle"
                    )
                prefixes.flatMap { prefix -> suffixes.map { suffix -> prefix + suffix } }
            }
        }

        internal inline fun <reified T : Annotation> XElement.findAnnotatedAnnotation(): List<XAnnotation> {
            return getAllAnnotations().filter { annotation ->
                annotation.type.typeElement?.hasAnnotation(
                    T::class
                ) == true
            }
        }

        private fun Set<XTypeElement>.filterAnnotatedAnnotations(klass: KClass<*>): Set<XTypeElement> =
            filter {
                it.isAnnotationClass() && it.getAllAnnotations()
                    .any { it.qualifiedName == klass.qualifiedName }
            }.toSet()

        private fun moduleNameToRegistryName(element: XTypeElement) =
            element.className.simpleName() + REGISTRY_CLASS_SUFFIX

        private fun moduleElementToRegistryClassName(element: XTypeElement): ClassName {
            return ClassName.get(
                getPackage(element),
                element.className.simpleName() + REGISTRY_CLASS_SUFFIX
            )
        }

        private fun getPackage(element: XTypeElement) = element.packageName

        private fun deeplinkAnnotatedElementCompare(
            element1: DeepLinkAnnotatedElement,
            element2: DeepLinkAnnotatedElement
        ): Int {
            val uri1 = DeepLinkUri.parseTemplate(element1.uri)
            val uri2 = DeepLinkUri.parseTemplate(element2.uri)
            var comparisonResult = uri2.pathSegments().size - uri1.pathSegments().size
            if (comparisonResult == 0) {
                comparisonResult = uri2.queryParameterNames().size - uri1.queryParameterNames().size
            }
            if (comparisonResult == 0) {
                comparisonResult = uri1.encodedPath().split("%7B".toRegex())
                    .toTypedArray().size - uri2.encodedPath().split("%7B".toRegex())
                    .toTypedArray().size
            }
            if (comparisonResult == 0) {
                val element1Representation = when (element1) {
                    is DeepLinkAnnotatedElement.ActivityAnnotatedElement -> element1.uri + DeepLinkAnnotatedElement.ActivityAnnotatedElement::class.simpleName
                    is DeepLinkAnnotatedElement.HandlerAnnotatedElement -> "handler_" + element1.uri + DeepLinkAnnotatedElement.ActivityAnnotatedElement::class.simpleName
                    is DeepLinkAnnotatedElement.MethodAnnotatedElement -> element1.uri + element1.method + DeepLinkAnnotatedElement.MethodAnnotatedElement::class.simpleName
                }
                val element2Representation = when (element2) {
                    is DeepLinkAnnotatedElement.ActivityAnnotatedElement -> element2.uri + DeepLinkAnnotatedElement.ActivityAnnotatedElement::class.simpleName
                    is DeepLinkAnnotatedElement.HandlerAnnotatedElement -> "handler_" + element2.uri + DeepLinkAnnotatedElement.ActivityAnnotatedElement::class.simpleName
                    is DeepLinkAnnotatedElement.MethodAnnotatedElement -> element2.uri + element2.method + DeepLinkAnnotatedElement.MethodAnnotatedElement::class.simpleName
                }
                comparisonResult = element1Representation.compareTo(element2Representation)
            }
            return comparisonResult
        }
    }
}
