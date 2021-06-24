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
import com.airbnb.deeplinkdispatch.ProcessorUtils.decapitalize
import com.airbnb.deeplinkdispatch.ProcessorUtils.hasEmptyOrNullString
import com.airbnb.deeplinkdispatch.base.Utils
import com.airbnb.deeplinkdispatch.base.Utils.isConfigurablePathSegment
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.javapoet.*
import java.io.IOException
import java.net.MalformedURLException
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.tools.Diagnostic
import kotlin.collections.HashSet
import kotlin.reflect.KClass

class DeepLinkProcessor(symbolProcessorEnvironment: SymbolProcessorEnvironment? = null) :
    BaseProcessor(symbolProcessorEnvironment) {

    private val documentor by lazy { Documentor(environment) }

    private val incrementalMetadata: IncrementalMetadata? by lazy {
        if (environment.options[OPTION_INCREMENTAL].toBoolean()) {
            environment.options.get(OPTION_CUSTOM_ANNOTATIONS)?.let { customAnnotations ->
                IncrementalMetadata(
                    customAnnotations.split(",")
                        .mapNotNull { environment.findTypeElement(it) }.toSet()
                )
            } ?: IncrementalMetadata(emptySet())
        } else null
    }

    private val supportedBaseAnnotations : Set<XTypeElement> by lazy {
        listOf(
            DeepLink::class,
            DeepLinkHandler::class,
            DeepLinkModule::class,
        ).mapNotNull { environment.findTypeElement(it) }.toSet()
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return incrementalMetadata?.let { incrementalMetadata ->
            (supportedBaseAnnotations + incrementalMetadata.customAnnotations).map { it.qualifiedName }
                .toSet()
        } ?: setOf("*")
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedOptions(): Set<String> {
        val supportedOptions = listOf(
            Documentor.DOC_OUTPUT_PROPERTY_NAME,
            OPTION_CUSTOM_ANNOTATIONS,
            OPTION_INCREMENTAL,
            if (incrementalMetadata != null) {
                "org.gradle.annotation.processing.aggregating"
            } else null
        ).filterNotNull()
        return supportedOptions.toSet()
    }

    override fun process(
        annotations: Set<XTypeElement>?,
        environment: XProcessingEnv,
        round: XRoundEnv
    ) {
        try {
            // If running KSP and non incremental we need to get the supported custom annotations
            // from the supplied list of annotations
            val supportedCustomAnnotations = incrementalMetadata?.let { it.customAnnotations }
                ?: annotations?.filterAnnotatedAnnotations(DeepLinkSpec::class) ?: emptySet()
            val prefixes: Map<XType, Array<String>> =
                supportedCustomAnnotations.map { customAnnotationTypeElement ->
                    if (!customAnnotationTypeElement.isAnnotationClass()) {
                        error(
                            customAnnotationTypeElement,
                            "Only annotation types can be annotated with @${DEEP_LINK_SPEC_CLASS.simpleName}"
                        )
                    }
                    val prefix: Array<String> =
                        customAnnotationTypeElement.getAnnotation(DEEP_LINK_SPEC_CLASS)
                            ?.let { it.value.prefix } ?: emptyArray()
                    if (hasEmptyOrNullString(prefix)) {
                        error(
                            customAnnotationTypeElement,
                            "Prefix property cannot have null or empty strings"
                        )
                    }
                    if (prefix.isEmpty()) error(
                        customAnnotationTypeElement,
                        "Prefix property cannot be empty"
                    )
                    customAnnotationTypeElement.type to prefix
                }.toMap()
            val allAnnotatedElements =
                (supportedCustomAnnotations.map { round.getElementsAnnotatedWith(it.qualifiedName) }
                .flatten() + round.getElementsAnnotatedWith(DEEP_LINK_CLASS))
            val annotatedMethodElements = allAnnotatedElements.filterIsInstance<XMethodElement>().toSet()
            val annotatedClassElements = allAnnotatedElements.filterIsInstance<XTypeElement>().filter { it.isClass() }.toSet()

            verifyAnnotatedType(
                allAnnotatedElements,
                annotatedClassElements,
                annotatedMethodElements
            )

            createDeeplinkDelegates(roundEnv = round)
            createDeeplinkRegistries(
                roundEnv = round,
                annotatedClassElements = annotatedClassElements,
                annotatedMethodElements = annotatedMethodElements,
                deepLinkElements = collectDeepLinkElements(
                    prefixes = prefixes,
                    classElementsToProcess = annotatedClassElements,
                    methodElementsToProcess = annotatedMethodElements
                ),
            )
        } catch (e: DeepLinkProcessorException) {
            error(e.element, e.message ?: "")
        }
    }

    private fun verifyAnnotatedType(
        allAnnotatedElements: List<XElement>,
        annotatedClassElements: Set<XTypeElement>,
        annotatedMethodElements: Set<XMethodElement>
    ) {
        allAnnotatedElements.filter {
            !annotatedClassElements.contains(it) && !annotatedMethodElements.contains(
                it
            )
        }.forEach { annotatedElementThatIsNeitherClassNorMethod ->
            error(
                annotatedElementThatIsNeitherClassNorMethod,
                "Only classes and methods can be annotated with @${DEEP_LINK_CLASS.simpleName}",
            )
        }
    }

    private fun createDeeplinkRegistries(
        roundEnv: XRoundEnv,
        annotatedClassElements: Set<XTypeElement>,
        annotatedMethodElements: Set<XMethodElement>,
        deepLinkElements: List<DeepLinkAnnotatedElement>
    ) {
        val deepLinkModuleElements = roundEnv.getElementsAnnotatedWith(DeepLinkModule::class)
            .filterIsInstance<XTypeElement>()
        deepLinkModuleElements.forEach { deepLinkModuleElement ->
            try {
                generateDeepLinkRegistry(
                    packageName = deepLinkModuleElement.packageName,
                    className = deepLinkModuleElement.className.simpleName(),
                    deepLinkElements = deepLinkElements,
                    originatingElements = annotatedClassElements + annotatedMethodElements + deepLinkModuleElement
                )
            } catch (e: IOException) {
                environment.messager.printMessage(Diagnostic.Kind.ERROR, "Error creating file")
            } catch (e: RuntimeException) {
                environment.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Internal error during annotation processing: ${e.stackTraceToString()}"
                )
            }
        }
    }

    private fun createDeeplinkDelegates(roundEnv: XRoundEnv) {
        val deepLinkHandlerElements = roundEnv.getElementsAnnotatedWith(DeepLinkHandler::class)
            .filterIsInstance<XTypeElement>()
        deepLinkHandlerElements.forEach { deepLinkHandlerElement ->
            val typeElements =
                deepLinkHandlerElement.getAnnotation(DeepLinkHandler::class)?.getAsTypeList("value")
                    ?.map { it.typeElement!! }
            if (typeElements != null) {
                try {
                    generateDeepLinkDelegate(
                        deepLinkHandlerElement.packageName,
                        typeElements,
                        deepLinkHandlerElement
                    )
                } catch (e: IOException) {
                    environment.messager.printMessage(Diagnostic.Kind.ERROR, "Error creating file")
                } catch (e: RuntimeException) {
                    environment.messager.printMessage(
                        Diagnostic.Kind.ERROR,
                        "Internal error during annotation processing: ${e.stackTraceToString()}"
                    )
                }
            }
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
                decapitalize(moduleNameToRegistryName(registryClasses[i])),
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
                        decapitalize(moduleNameToRegistryName(registryClass))
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
                        decapitalize(moduleNameToRegistryName(registryClass))
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

    private fun collectDeepLinkElements(
        prefixes: Map<XType, Array<String>>,
        classElementsToProcess: Set<XTypeElement>,
        methodElementsToProcess: Set<XMethodElement>
    ): List<DeepLinkAnnotatedElement> {
        return (classElementsToProcess.map { element ->
            mapUrisToDeepLinkAnnotatedElement(element, prefixes)
        }.flatten() + methodElementsToProcess.map { element ->
            verifyMethod(element)
            mapUrisToDeepLinkAnnotatedElement(element, prefixes)
        }.flatten()).filterNotNull()
    }

    private fun mapUrisToDeepLinkAnnotatedElement(
        element: XElement,
        prefixes: Map<XType, Array<String>>
    ): List<DeepLinkAnnotatedElement?> = getAllUrisForAnnotatedElement(element, prefixes).map { uri ->
        try {
            DeepLinkAnnotatedElement(uri, element)
        } catch (e: MalformedURLException) {
            environment.messager.printMessage(
                kind = Diagnostic.Kind.ERROR,
                msg = "Malformed Deep Link URL $uri"
            )
            null
        }
    }

    private fun getAllUrisForAnnotatedElement(
        element: XElement,
        prefixes: Map<XType, Array<String>>
    ): List<String> {
        return getAllDeeplinkUrIsFromCustomDeepLinksOnElement(
            element = element,
            prefixesMap = prefixes
        ) + (element.getAnnotation(DEEP_LINK_CLASS)?.value?.value?.toList() ?: emptyList())
    }

    private fun verifyMethod(methodElement: XMethodElement) {
        if (!methodElement.isStatic()) {
            error(
                methodElement,
                "Only static methods can be annotated with @${DEEP_LINK_CLASS.simpleName}",
            )
        }
        // FIXME This is crashing with an NPE on internal classes when accessing the returnType.
        // You can jut comment this check out for now if you need this to pass.
        if (methodElement.returnType.typeElement?.qualifiedName !in listOf(
                "android.content.Intent",
                "androidx.core.app.TaskStackBuilder",
                "com.airbnb.deeplinkdispatch.DeepLinkMethodResult"
            )
        ) {
            error(
                methodElement, ("Only `Intent`, `androidx.core.app.TaskStackBuilder` or "
                        + "'com.airbnb.deeplinkdispatch.DeepLinkMethodResult' are supported. Please double "
                        + "check your imports and try again.")
            )
        }
    }

    private fun error(e: XElement?, msg: String) {
        environment.messager.printMessage(Diagnostic.Kind.ERROR, msg, e)
    }

    @Throws(IOException::class)
    private fun generateDeepLinkRegistry(
        packageName: String,
        className: String,
        deepLinkElements: List<DeepLinkAnnotatedElement>,
        originatingElements: Set<XElement>
    ) {
        Collections.sort(deepLinkElements) { element1, element2 ->
            deeplinkAnnotatedElementCompare(element1, element2)
        }
        documentor.write(deepLinkElements)
        val urisTrie = Root()
        val pathVariableKeys: MutableSet<String> = HashSet()
        for (element: DeepLinkAnnotatedElement in deepLinkElements) {
            val uriTemplate = element.uri
            try {
                urisTrie.addToTrie(uriTemplate, element.annotatedClass?.className?.reflectionName() ?: "", element.method)
            } catch (e: IllegalArgumentException) {
                error(element.annotatedClass, e.message ?: "")
            }
            val deeplinkUri = DeepLinkUri.parseTemplate(uriTemplate)
            //Keep track of pathVariables added in a module so that we can check at runtime to ensure
            //that all pathVariables have a corresponding entry provided to BaseDeepLinkDelegate.
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
            .writeTo(environment.filer)
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
        var i = 0
        val stringMethodNames = StringBuilder()
        for (charSequence: CharSequence in urisTrie.getStrings()) {
            val methodName = "matchIndex$i"
            stringMethodNames.append(methodName).append("(), ")
            deepLinkRegistryBuilder.addMethod(
                MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                    .returns(String::class.java)
                    .addCode(CodeBlock.builder().add("return \$S;", charSequence).build()).build()
            )
            i++
        }
        return stringMethodNames
    }

    private class IncrementalMetadata(val customAnnotations: Set<XTypeElement>)

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
            return element.annotatedAnnotation(DEEP_LINK_SPEC_CLASS).flatMap { customAnnotation ->
                val suffixes = customAnnotation.get<List<String>>("value")
                val prefixes = prefixesMap[customAnnotation.type]
                    ?: throw DeepLinkProcessorException(
                        "Unable to find annotation '${customAnnotation.qualifiedName}' you must " +
                                "update 'deepLink.customAnnotations' within the build.gradle"
                    )
                prefixes.flatMap { prefix -> suffixes.map { suffix -> prefix + suffix } }
            }
        }

        private fun XElement.annotatedAnnotation(klass: KClass<*>): List<XAnnotation> =
            getAllAnnotations().filter { annotation ->
                annotation.type.typeElement?.getAllAnnotations()?.any {
                    it.qualifiedName == klass.qualifiedName
                } ?: false
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
                val element1Representation =
                    element1.uri + element1.method + element1.annotationType
                val element2Representation =
                    element2.uri + element2.method + element2.annotationType
                comparisonResult = element1Representation.compareTo(element2Representation)
            }
            return comparisonResult
        }

    }
}