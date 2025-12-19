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
abstract class BaseProcessor(
    val symbolProcessorEnvironment: SymbolProcessorEnvironment?,
) : AbstractProcessor(),
    SymbolProcessor {
    lateinit var environment: XProcessingEnv
        private set

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.RELEASE_8

    final override fun init(processingEnv: ProcessingEnvironment) {
        super<AbstractProcessor>.init(processingEnv)
        environment = XProcessingEnv.create(processingEnv)
    }

    final override fun process(
        annotations: Set<TypeElement>,
        roundEnv: RoundEnvironment,
    ): Boolean {
        if (roundEnv.errorRaised()) {
            onError()
        }

        process(annotations.map { it.toXProcessing(environment) }.toSet(), environment, XRoundEnv.create(environment, roundEnv))

        if (roundEnv.processingOver()) {
            onProcessingFinished()
        }

        return false
    }

    final override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbolProcessorEnvironment = requireNotNull(symbolProcessorEnvironment)
        environment =
            XProcessingEnv.create(
                symbolProcessorEnvironment,
                resolver,
            )
        process(null, environment, XRoundEnv.create(environment))
        return emptyList()
    }

    /**
     * Called by KSP when processing is complete.
     * This is the KSP equivalent of processingOver() in APT.
     */
    final override fun finish() {
        onProcessingFinished()
    }

    abstract fun process(
        annotations: Set<XTypeElement>?,
        environment: XProcessingEnv,
        round: XRoundEnv,
    )

    /**
     * Called when all processing rounds are complete.
     * Override this to perform any finalization work like writing aggregated outputs.
     */
    open fun onProcessingFinished() {
        // Default no-op implementation
    }
}
