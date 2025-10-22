package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.test.Source
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.OptionName
import com.tschuchort.compiletesting.OptionValue
import com.tschuchort.compiletesting.kspArgs
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.assertj.core.api.Assertions
import java.io.File

open class BaseDeepLinkProcessorTest {
    @JvmField
    protected val fakeBaseDeeplinkDelegate =
        Source.KotlinSource(
            "com/airbnb/deeplinkdispatch/BaseDeepLinkDelegate.kt",
            """
                package com.airbnb.deeplinkdispatch
                
                import com.airbnb.deeplinkdispatch.handler.TypeConverters
                import java.lang.reflect.Type

                open class BaseDeepLinkDelegate @JvmOverloads constructor(
                    private val registries: List<BaseRegistry>,
                    configurablePathSegmentReplacements: Map<String, String> = emptyMap(),
                    private val typeConverters: () -> TypeConverters = { TypeConverters() },
                    private val errorHandler: ErrorHandler? = null,
                    private val typeConversionErrorNullable: (DeepLinkUri, Type, String) -> Int? = { _, _, _: String -> null },
                    private val typeConversionErrorNonNullable: (DeepLinkUri, Type, String) -> Int = { _, _, _: String -> 0 }
                ) {}
                """,
        )

    internal val module =
        Source.JavaSource(
            "com.example.SampleModule",
            """
                 package com.example;
                 import com.airbnb.deeplinkdispatch.DeepLinkModule;
                 
                 @DeepLinkModule
                 public class SampleModule {
                 }
                 """,
        )

    companion object {
        internal fun assertGeneratedCode(
            results: List<CompileResult>,
            registryClassName: String,
            indexEntries: List<DeepLinkEntry>,
            generatedFiles: Map<String, String>,
        ) {
            results.forEach { result ->
                Assertions
                    .assertThat(result.result.exitCode)
                    .isEqualTo(KotlinCompilation.ExitCode.OK)
                Assertions
                    .assertThat(result.generatedFiles.keys)
                    .containsExactlyInAnyOrder(*generatedFiles.keys.toTypedArray())
                generatedFiles.keys.forEach { filename ->
                    Assertions
                        .assertThat(result.generatedFiles[filename]?.readText())
                        .isEqualTo(generatedFiles[filename])
                }
                // Ksp generated files do not compile in tests so do not try to load them.
                if (!result.useKsp) {
                    val generatedRegistryClazz =
                        result.result.classLoader.loadClass(registryClassName)
                    val baseRegistryClazz =
                        result.result.classLoader.loadClass("com.airbnb.deeplinkdispatch.BaseRegistry")
                    Assertions.assertThat(generatedRegistryClazz).hasDeclaredMethods("matchIndex0")
                    val registryInstance = generatedRegistryClazz.newInstance()
                    Assertions.assertThat(registryInstance).isNotNull
                    Assertions
                        .assertThat(
                            baseRegistryClazz
                                .getDeclaredMethod("getAllEntries")
                                .invoke(registryInstance) as List<DeepLinkEntry>,
                        ).isEqualTo(
                            indexEntries,
                        )
                }
            }
        }

        internal fun assertCompileError(
            results: List<CompileResult>,
            errorMessage: String,
        ) {
            results.forEach { result ->
                Assertions
                    .assertThat(result.result.exitCode)
                    .isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
                Assertions.assertThat(result.result.messages).contains(
                    errorMessage,
                )
            }
        }

        internal fun compile(
            sourceFiles: List<Source>,
            arguments: MutableMap<OptionName, OptionValue>? = null,
            useKsp: Boolean = false,
        ): CompileResult {
            val compilation =
                KotlinCompilation().apply {
                    val sourcesDir = workingDir.resolve("sources")
                    sources =
                        sourceFiles.map {
                            it.toKotlinSourceFile(sourcesDir)
                        }
                    if (useKsp) {
                        symbolProcessorProviders = listOf(DeepLinkProcessorProvider())
                        arguments?.let { kspArgs = arguments }
                    } else {
                        annotationProcessors = listOf(DeepLinkProcessor())
                        arguments?.let { kaptArgs = arguments }
                    }
                    inheritClassPath = true
                    messageOutputStream = System.out
                }
            val result = compilation.compile()
            val generatedSources =
                if (useKsp) {
                    compilation.kspSourcesDir
                        .walk()
                        .filter { it.isFile }
                        .toList()
                } else {
                    result.sourcesGeneratedByAnnotationProcessor
                }
            return CompileResult(result, generatedSources.map { it.name to it }.toMap(), useKsp)
        }

        internal fun compileIncremental(
            sourceFiles: List<Source>,
            customDeepLinks: List<String> = emptyList(),
            useKsp: Boolean = false,
            incrementalFlag: Boolean = true,
        ): CompileResult {
            val arguments: MutableMap<OptionName, OptionValue> = mutableMapOf()
            if (incrementalFlag) {
                arguments["deepLink.incremental"] = "true"
            }
            if (customDeepLinks.isNotEmpty()) {
                arguments["deepLink.customAnnotations"] = customDeepLinks.joinToString(separator = "|")
            }
            return compile(
                sourceFiles,
                arguments,
                useKsp,
            )
        }
    }

    class CompileResult(
        val result: KotlinCompilation.Result,
        val generatedFiles: Map<String, File>,
        val useKsp: Boolean,
    )
}
