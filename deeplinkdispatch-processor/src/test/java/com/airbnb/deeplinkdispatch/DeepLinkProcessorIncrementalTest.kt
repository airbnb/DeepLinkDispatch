package com.airbnb.deeplinkdispatch

import com.tschuchort.compiletesting.SourceFile
import org.junit.Test

class DeepLinkProcessorIncrementalTest : BaseDeepLinkProcessorTest() {
    private val customAnnotationAppLink = SourceFile.java(
        "AppDeepLink.java",
        """
                package com.example;
                import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                @DeepLinkSpec(prefix = { "example://" })
                public @interface AppDeepLink {
                    String[] value();
                }
                """
    )
    private val customAnnotationPlaceholderInSchemeHostAppLink = SourceFile.java(
        "PlaceholderDeepLink.java",
        """
                package com.example;
                import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                @DeepLinkSpec(prefix = { "http{scheme}://{host}example.com/" })
                public @interface PlaceholderDeepLink {
                    String[] value();
                }
                """
    )
    private val sampleActivityWithStandardAndCustomDeepLink = SourceFile.java(
        "SampleActivity.java",
        """
                 package com.example;import com.airbnb.deeplinkdispatch.DeepLink;
                 @DeepLink("airbnb://example.com/deepLink")
                 @AppDeepLink({"example.com/deepLink","example.com/another"})
                 public class SampleActivity {
                 }
                 """
    )
    private val sampleActivityWithOnlyCustomDeepLink = SourceFile.java(
        "SampleActivity.java",
        """
                 package com.example;import com.airbnb.deeplinkdispatch.DeepLink;
                 import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                 
                 import com.example.SampleModule;
                 
                 @AppDeepLink({"example.com/deepLink"})
                 @DeepLinkHandler({ SampleModule.class })
                 public class SampleActivity {
                 }
                 """
    )
    private val sampleActivityWithOnlyCustomPlaceholderDeepLink = SourceFile.java(
        "SampleActivity.java",
        """
                 package com.example;import com.airbnb.deeplinkdispatch.DeepLink;
                 import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                 
                 import com.example.SampleModule;
                 
                 @PlaceholderDeepLink({"deepLink"})
                 @DeepLinkHandler({ SampleModule.class })
                 public class SampleActivity {
                 }
                 """
    )
    private val module = SourceFile.java(
        "SampleModule.java",
        """
                 package com.example;import com.airbnb.deeplinkdispatch.DeepLinkModule;
                 
                 @DeepLinkModule
                 public class SampleModule {
                 }
                 """
    )

    @Test
    fun testIncrementalProcessorWithCustomDeepLinkRegistration() {
        val result = compileIncremental(
            listOf(
                customAnnotationAppLink,
                module,
                sampleActivityWithOnlyCustomDeepLink,
                fakeBaseDeeplinkDelegate
            ), "com.example.AppDeepLink"
        )
        assertGeneratedCode(
            result = result,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = listOf(
                DeepLinkEntry(
                    uriTemplate = "example://example.com/deepLink",
                    className = "com.example.SampleActivity",
                    method = null
                )
            ),
            generatedFileNames = listOf("DeepLinkDelegate.java", "SampleModuleRegistry.java")
        )
    }

    @Test
    fun testIncrementalProcessorWithCustomDeepLinkWithPlaceholdersRegistration() {
        val result = compileIncremental(
            listOf(
                customAnnotationPlaceholderInSchemeHostAppLink,
                module,
                sampleActivityWithOnlyCustomPlaceholderDeepLink,
                fakeBaseDeeplinkDelegate
            ), "com.example.PlaceholderDeepLink"
        )
        assertGeneratedCode(
            result = result,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = listOf(
                DeepLinkEntry(
                    uriTemplate = "http{scheme}://{host}example.com/deepLink",
                    className = "com.example.SampleActivity",
                    method = null
                )
            ),
            generatedFileNames = listOf("DeepLinkDelegate.java", "SampleModuleRegistry.java")
        )
    }

    @Test
    fun testIncrementalProcessorWithoutCustomDeepLinkRegistration() {
        val result = compileIncremental(
            listOf(
                customAnnotationAppLink,
                module,
                sampleActivityWithOnlyCustomDeepLink,
                fakeBaseDeeplinkDelegate
            ), null
        )
        assertGeneratedCode(
            result = result,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = emptyList(),
            generatedFileNames = listOf("DeepLinkDelegate.java", "SampleModuleRegistry.java")
        )

    }

    @Test
    fun testCustomAnnotationMissingFromCompilerOptionsErrorMessage() {
        val result = compileIncremental(
            listOf(
                customAnnotationAppLink,
                sampleActivityWithStandardAndCustomDeepLink
            ), null
        )
        assertCompileError(
            result, "Unable to find annotation 'com.example.AppDeepLink' you must update "
                    + "'deepLink.customAnnotations' within the build.gradle"
        )
    }
}