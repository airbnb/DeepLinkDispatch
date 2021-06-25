package com.airbnb.deeplinkdispatch

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.OptionName
import com.tschuchort.compiletesting.OptionValue
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspArgs
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.assertj.core.api.Assertions
import java.io.File

open class BaseDeepLinkProcessorTest {
    @JvmField
    protected val fakeBaseDeeplinkDelegate = SourceFile.java(
        "BaseDeepLinkDelegate.java",
        """
                package com.airbnb.deeplinkdispatch;
                
                import java.util.List;
                import java.util.Map;
                
                public class BaseDeepLinkDelegate {  public BaseDeepLinkDelegate(List<? extends BaseRegistry> registries) {
                  }
                  public BaseDeepLinkDelegate(
                    List<? extends BaseRegistry> registries,
                    Map<String, String> configurablePathSegmentReplacements
                  ) {}
                }
                """
    )

    companion object {
        internal fun assertGeneratedCode(
            results: List<CompileResult>,
            registryClassName: String,
            indexEntries: List<DeepLinkEntry>,
            generatedFiles: Map<String, String>
        ) {
            results.forEach { result ->
                Assertions.assertThat(result.result.exitCode)
                    .isEqualTo(KotlinCompilation.ExitCode.OK)
                Assertions.assertThat(result.generatedFiles.keys)
                    .containsExactlyInAnyOrder(*generatedFiles.keys.toTypedArray())
                generatedFiles.keys.forEach { filename ->
                    Assertions.assertThat(result.generatedFiles[filename]?.readText())
                        .isEqualTo(generatedFiles[filename])
                }
                // Ksp generated files do not compile in tests so so not try to load them.
                if (!result.useKsp) {
                    val generatedRegistryClazz =
                        result.result.classLoader.loadClass(registryClassName)
                    val baseRegistryClazz =
                        result.result.classLoader.loadClass("com.airbnb.deeplinkdispatch.BaseRegistry")
                    Assertions.assertThat(generatedRegistryClazz).hasDeclaredMethods("matchIndex0")
                    val registryInstance = generatedRegistryClazz.newInstance()
                    Assertions.assertThat(registryInstance).isNotNull
                    Assertions.assertThat(
                        baseRegistryClazz.getDeclaredMethod("getAllEntries")
                            .invoke(registryInstance) as List<DeepLinkEntry>
                    ).isEqualTo(
                        indexEntries
                    )
                }
            }
        }

        internal fun assertCompileError(
            results: List<CompileResult>,
            errorMessage: String
        ) {
            results.forEach { result ->
                Assertions.assertThat(result.result.exitCode)
                    .isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
                Assertions.assertThat(result.result.messages).contains(
                    errorMessage
                )
            }
        }

        internal fun compile(
            sourceFiles: List<SourceFile>,
            arguments: MutableMap<OptionName, OptionValue>? = null,
            useKsp: Boolean = false
        ): CompileResult {
            val compilation = KotlinCompilation().apply {
                sources = sourceFiles
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
            val generatedSources = if (useKsp) {
                compilation.kspSourcesDir.walk().filter { it.isFile }.toList()
            } else {
                result.sourcesGeneratedByAnnotationProcessor
            }
            return CompileResult(result, generatedSources.map { it.name to it }.toMap(), useKsp)
        }

        internal fun compileIncremental(
            sourceFiles: List<SourceFile>,
            customDeepLinks: List<String>?,
            useKsp: Boolean = false
        ): CompileResult {
            val arguments: MutableMap<OptionName, OptionValue> = mutableMapOf(
                "deepLink.incremental" to "true"
            )
            if (customDeepLinks != null) {
                arguments["deepLink.customAnnotations"] = customDeepLinks.joinToString(separator = "|")
            }
            return compile(
                sourceFiles,
                arguments,
                useKsp
            )
        }
    }

    class CompileResult(val result: KotlinCompilation.Result, val generatedFiles: Map<String, File>, val useKsp: Boolean)
}
