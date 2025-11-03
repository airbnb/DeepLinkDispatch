package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.test.Source
import com.squareup.kotlinpoet.javapoet.KotlinPoetJavaPoetPreview
import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.OptionName
import com.tschuchort.compiletesting.OptionValue
import com.tschuchort.compiletesting.kspProcessorOptions
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.assertj.core.api.Assertions
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import java.io.File

@ExperimentalCompilerApi
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

    @JvmField
    protected val fakeBaseDeeplinkDelegateJava =
        Source.JavaSource(
            "com.airbnb.deeplinkdispatch.BaseDeepLinkDelegate",
            """
                package com.airbnb.deeplinkdispatch;

                import com.airbnb.deeplinkdispatch.handler.TypeConverters;
                import java.lang.reflect.Type;
                import java.util.List;
                import java.util.Map;
                import java.util.Collections;
                import kotlin.jvm.functions.Function0;
                import kotlin.jvm.functions.Function3;

                public class BaseDeepLinkDelegate {
                    private final List<? extends BaseRegistry> registries;
                    private final Map<String, String> configurablePathSegmentReplacements;
                    private final Function0<? extends TypeConverters> typeConverters;
                    private final ErrorHandler errorHandler;
                    private final Function3<? super DeepLinkUri, ? super Type, ? super String, ? extends Integer> typeConversionErrorNullable;
                    private final Function3<? super DeepLinkUri, ? super Type, ? super String, ? extends Integer> typeConversionErrorNonNullable;

                    // Constructor with all parameters (mimics Kotlin @JvmOverloads)
                    public BaseDeepLinkDelegate(
                        List<? extends BaseRegistry> registries,
                        Map<String, String> configurablePathSegmentReplacements,
                        Function0<? extends TypeConverters> typeConverters,
                        ErrorHandler errorHandler,
                        Function3<? super DeepLinkUri, ? super Type, ? super String, ? extends Integer> typeConversionErrorNullable,
                        Function3<? super DeepLinkUri, ? super Type, ? super String, ? extends Integer> typeConversionErrorNonNullable
                    ) {
                        this.registries = registries;
                        this.configurablePathSegmentReplacements = configurablePathSegmentReplacements;
                        this.typeConverters = typeConverters;
                        this.errorHandler = errorHandler;
                        this.typeConversionErrorNullable = typeConversionErrorNullable;
                        this.typeConversionErrorNonNullable = typeConversionErrorNonNullable;
                    }

                    // Constructor with 5 parameters (mimics Kotlin @JvmOverloads)
                    public BaseDeepLinkDelegate(
                        List<? extends BaseRegistry> registries,
                        Map<String, String> configurablePathSegmentReplacements,
                        Function0<? extends TypeConverters> typeConverters,
                        ErrorHandler errorHandler,
                        Function3<? super DeepLinkUri, ? super Type, ? super String, ? extends Integer> typeConversionErrorNullable
                    ) {
                        this(registries, configurablePathSegmentReplacements, typeConverters, errorHandler,
                             typeConversionErrorNullable, (uri, type, s) -> 0);
                    }

                    // Constructor with 4 parameters (mimics Kotlin @JvmOverloads)
                    public BaseDeepLinkDelegate(
                        List<? extends BaseRegistry> registries,
                        Map<String, String> configurablePathSegmentReplacements,
                        Function0<? extends TypeConverters> typeConverters,
                        ErrorHandler errorHandler
                    ) {
                        this(registries, configurablePathSegmentReplacements, typeConverters, errorHandler,
                             (uri, type, s) -> null);
                    }

                    // Constructor with 3 parameters (mimics Kotlin @JvmOverloads)
                    public BaseDeepLinkDelegate(
                        List<? extends BaseRegistry> registries,
                        Map<String, String> configurablePathSegmentReplacements,
                        Function0<? extends TypeConverters> typeConverters
                    ) {
                        this(registries, configurablePathSegmentReplacements, typeConverters, null);
                    }

                    // Constructor with 2 parameters (mimics Kotlin @JvmOverloads)
                    public BaseDeepLinkDelegate(
                        List<? extends BaseRegistry> registries,
                        Map<String, String> configurablePathSegmentReplacements
                    ) {
                        this(registries, configurablePathSegmentReplacements, () -> new TypeConverters());
                    }

                    // Constructor with 1 parameter (mimics Kotlin @JvmOverloads)
                    public BaseDeepLinkDelegate(List<? extends BaseRegistry> registries) {
                        this(registries, Collections.emptyMap());
                    }
                }
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
                    val registryInstance = generatedRegistryClazz.getDeclaredConstructor().newInstance()
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

        @KotlinPoetJavaPoetPreview
        @kotlin.ExperimentalUnsignedTypes
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
                        symbolProcessorProviders = mutableListOf(DeepLinkProcessorProvider())
                        arguments?.let { kspProcessorOptions = arguments }
                        languageVersion = "1.9"
                        useKapt4 = false
                    } else {
                        annotationProcessors = listOf(DeepLinkProcessor())
                        arguments?.let { kaptArgs = arguments }
                        languageVersion = "1.9"
                        useKapt4 = true
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

        @KotlinPoetJavaPoetPreview
        @kotlin.ExperimentalUnsignedTypes
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
                sourceFiles = sourceFiles,
                arguments = arguments,
                useKsp = useKsp,
            )
        }
    }

    class CompileResult(
        val result: JvmCompilationResult,
        val generatedFiles: Map<String, File>,
        val useKsp: Boolean,
    )
}
