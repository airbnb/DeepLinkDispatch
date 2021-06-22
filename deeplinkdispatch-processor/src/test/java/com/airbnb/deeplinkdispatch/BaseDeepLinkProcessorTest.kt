package com.airbnb.deeplinkdispatch

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.OptionName
import com.tschuchort.compiletesting.OptionValue
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspArgs
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.assertj.core.api.Assertions

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
            result: KotlinCompilation.Result,
            registryClassName: String,
            indexEntries: List<DeepLinkEntry>,
            generatedFileNames: List<String>
        ) {
            Assertions.assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
            Assertions.assertThat(result.sourcesGeneratedByAnnotationProcessor.map { it.name }
                .toList())
                .containsExactlyInAnyOrder(*generatedFileNames.toTypedArray())
            val generatedRegistryClazz =
                result.classLoader.loadClass(registryClassName)
            val baseRegistryClazz =
                result.classLoader.loadClass("com.airbnb.deeplinkdispatch.BaseRegistry")
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

        internal fun assertCompileError(
            result: KotlinCompilation.Result,
            errorMessage: String
        ) {
            Assertions.assertThat(result.exitCode)
                .isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
            Assertions.assertThat(result.messages).contains(
                errorMessage
            )
        }

        internal fun compile(
            sourceFiles: List<SourceFile>,
            arguments: MutableMap<OptionName, OptionValue>? = null,
            useKsp: Boolean = false
        ) =
            KotlinCompilation().apply {
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
            }.compile()

        internal fun compileIncremental(
            sourceFiles: List<SourceFile>,
            customDeepLinks: List<String>?,
            useKsp: Boolean = false
        ): KotlinCompilation.Result {
            val arguments: MutableMap<OptionName, OptionValue> = mutableMapOf(
                "deepLink.incremental" to "true"
            )
            if (customDeepLinks != null) {
                arguments["deepLink.customAnnotations"] = customDeepLinks.joinToString(separator = ",")
            }
            return compile(
                sourceFiles,
                arguments,
                useKsp
            )
        }
    }

}