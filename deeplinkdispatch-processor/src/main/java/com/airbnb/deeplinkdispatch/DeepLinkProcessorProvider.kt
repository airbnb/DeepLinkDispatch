package com.airbnb.deeplinkdispatch

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.squareup.kotlinpoet.javapoet.KotlinPoetJavaPoetPreview

class DeepLinkProcessorProvider : SymbolProcessorProvider {
    @KotlinPoetJavaPoetPreview
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = DeepLinkProcessor(environment)
}
