package com.airbnb.deeplinkdispatch

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.OptionName
import com.tschuchort.compiletesting.OptionValue
import com.tschuchort.compiletesting.SourceFile
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
            arguments: MutableMap<OptionName, OptionValue>? = null
        ) =
            KotlinCompilation().apply {
                sources = sourceFiles
                annotationProcessors = listOf(DeepLinkProcessor())
                inheritClassPath = true
                arguments?.let { kaptArgs = arguments }
            }.compile()

        internal fun compileIncremental(
            sourceFiles: List<SourceFile>,
            customDeeplink: String?
        ): KotlinCompilation.Result {
            val arguments: MutableMap<OptionName, OptionValue> = mutableMapOf(
                "deepLink.incremental" to "true"
            )
            if (customDeeplink != null) {
                arguments["deepLink.customAnnotations"] = customDeeplink
            }
            return compile(
                sourceFiles,
                arguments
            )
        }
    }

}