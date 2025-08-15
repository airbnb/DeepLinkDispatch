package com.airbnb.deeplinkdispatch

import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XRoundEnv
import androidx.room.compiler.processing.XTypeElement
import androidx.room.compiler.processing.compat.XConverters.toXProcessing
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

/**
 * Creates a unified abstraction for processors of both KSP and java annotation processing.
 */
abstract class BaseProcessor(val symbolProcessorEnvironment: SymbolProcessorEnvironment?) :
    AbstractProcessor(),
    SymbolProcessor {

    lateinit var environment: XProcessingEnv
        private set

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.RELEASE_8

    final override fun init(processingEnv: ProcessingEnvironment) {
        super<AbstractProcessor>.init(processingEnv)
        environment = XProcessingEnv.create(processingEnv)
    }

    final override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (roundEnv.errorRaised()) {
            onError()
        }

        process(annotations.map { it.toXProcessing(environment) }.toSet(), environment, XRoundEnv.create(environment, roundEnv))

        if (roundEnv.processingOver()) {
            finish()
        }

        return false
    }

    final override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbolProcessorEnvironment = requireNotNull(symbolProcessorEnvironment)
        environment = XProcessingEnv.create(
            symbolProcessorEnvironment,
            resolver,
        )
        process(null, environment, XRoundEnv.create(environment), resolver)
        return emptyList()
    }

    abstract fun process(
        annotations: Set<XTypeElement>?,
        environment: XProcessingEnv,
        round: XRoundEnv,
        resolver: Resolver? = null,
    )
}
