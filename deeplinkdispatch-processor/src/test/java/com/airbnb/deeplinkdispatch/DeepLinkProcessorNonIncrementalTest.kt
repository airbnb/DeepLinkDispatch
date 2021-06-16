package com.airbnb.deeplinkdispatch

import com.tschuchort.compiletesting.SourceFile
import org.junit.Test

class DeepLinkProcessorNonIncrementalTest : BaseDeepLinkProcessorTest() {
    @Test
    fun testProcessor() {
        val sampleActivity = SourceFile.java(
            "SampleActivity.java",
            """
                     package com.example;
                     import com.airbnb.deeplinkdispatch.DeepLink;
                     import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                     import com.example.SampleModule;
                     @DeepLink("airbnb://example.com/deepLink")
                     @DeepLinkHandler({ SampleModule.class })
                     public class SampleActivity {
                     }
                     """
        )
        val result =
            compile(listOf(SIMPLE_DEEPLINK_MODULE, sampleActivity, fakeBaseDeeplinkDelegate))
        assertGeneratedCode(
            result = result,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = listOf(
                DeepLinkEntry(
                    "airbnb://example.com/deepLink",
                    "com.example.SampleActivity",
                    null
                )
            ),
            generatedFileNames = listOf("DeepLinkDelegate.java", "SampleModuleRegistry.java")
        )
    }

    @Test
    fun testProcessorWithDefaultAndCustomAnnotations() {
        val customAnnotationWebLink = SourceFile.java(
            "WebDeepLink.java", """
            package com.example;
            import com.airbnb.deeplinkdispatch.DeepLinkSpec;
            @DeepLinkSpec(prefix = { "http://", "https://"})
            public @interface WebDeepLink {
                String[] value();
            }
            """
        )
        val customAnnotationAppLink = SourceFile.java(
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
        val sampleActivity = SourceFile.java(
            "SampleActivity.java",
            """
                 package com.example;import com.airbnb.deeplinkdispatch.DeepLink;
                 import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                 import com.example.SampleModule;
                 @DeepLink("airbnb://example.com/deepLink")
                 @AppDeepLink({"example.com/deepLink","example.com/another"})
                 @WebDeepLink({"example.com/deepLink","example.com/another"})
                 @DeepLinkHandler({ SampleModule.class })
                 public class SampleActivity {
                 }
                 """
        )
        val result = compile(
            listOf(
                customAnnotationAppLink, customAnnotationWebLink,
                SIMPLE_DEEPLINK_MODULE, sampleActivity, fakeBaseDeeplinkDelegate
            )
        )
        assertGeneratedCode(
            result = result,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = listOf(
                DeepLinkEntry(
                    uriTemplate = "airbnb://example.com/deepLink",
                    className = "com.example.SampleActivity",
                    method = null
                ),
                DeepLinkEntry(
                    uriTemplate = "example://example.com/another",
                    className = "com.example.SampleActivity",
                    method = null
                ),
                DeepLinkEntry(
                    uriTemplate = "example://example.com/deepLink",
                    className = "com.example.SampleActivity",
                    method = null
                ),
                DeepLinkEntry(
                    uriTemplate = "http://example.com/another",
                    className = "com.example.SampleActivity",
                    method = null
                ),
                DeepLinkEntry(
                    uriTemplate = "http://example.com/deepLink",
                    className = "com.example.SampleActivity",
                    method = null
                ),
                DeepLinkEntry(
                    uriTemplate = "https://example.com/another",
                    className = "com.example.SampleActivity",
                    method = null
                ),
                DeepLinkEntry(
                    uriTemplate = "https://example.com/deepLink",
                    className = "com.example.SampleActivity",
                    method = null
                )
            ),
            generatedFileNames = listOf("DeepLinkDelegate.java", "SampleModuleRegistry.java")
        )
    }

    @Test
    fun testDuplicatedUriMatch() {
        val sampleActivity = SourceFile.java(
            "SampleActivity.java",
            """
                    package com.example;import android.content.Context;
                    import android.content.Intent;
                    import com.airbnb.deeplinkdispatch.DeepLink;
                    import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                    import com.example.SampleModule;
                    @DeepLinkHandler({ SampleModule.class })
                    public class SampleActivity { 
                        @DeepLink("airbnb://host/{var}")  
                        public static Intent intentFromOnePathWithOneParam(Context context){
                            return new Intent();  
                        }
                        @DeepLink("airbnb://host/path")  
                        public static Intent intentFromOnePath(Context context){
                            return new Intent(); 
                        }
                        @DeepLink("airbnb://host/path1/path2")  
                        public static Intent intentFromTwoPath(Context context){  
                            return new Intent();
                        }
                        @DeepLink("airbnb://host/path1/path2?q={q}")  
                        public static Intent intentFromTwoPathWithQuery(Context context){
                            return new Intent();
                        }
                        @DeepLink("airbnb://host/{var1}/{var2}")  
                        public static Intent intentFromTwoPathWithTwoParams(Context context){
                            return new Intent();
                        }
                    }
                    """
        )
        val module = SourceFile.java(
            "SampleModule.java",
            """
                     package com.example;import com.airbnb.deeplinkdispatch.DeepLinkModule;
                     @DeepLinkModule
                     public class SampleModule { }
                     """
        )
        val result = compile(
            listOf(
                module, sampleActivity
            )
        )
        assertCompileError(
            result,
            "Internal error during annotation processing: java.lang.IllegalStateException: " +
                    "Ambiguous URI. Same match for two URIs (UriMatch(uriTemplate=" +
                    "airbnb://host/path1/path2?q={q}, annotatedClassFullyQualifiedName=com." +
                    "example.SampleActivity, annotatedMethod=intentFromTwoPathWithQuery) vs " +
                    "UriMatch(uriTemplate=airbnb://host/path1/path2, " +
                    "annotatedClassFullyQualifiedName=com.example.SampleActivity, " +
                    "annotatedMethod=intentFromTwoPath))"
        )
    }

    @Test
    fun uppercasePackage() {
        val activityWithUppercasePackage = SourceFile.java(
            "SampleActivity.java",
            """
                     package com.Example;
                     import com.airbnb.deeplinkdispatch.DeepLink;
                     import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                     import com.Example.SampleModule;
                     @DeepLink("airbnb://example.com/deepLink")@DeepLinkHandler({ SampleModule.class })
                     public class SampleActivity {
                     }
                     """
        )
        val result = compile(
            listOf(
                SIMPLE_DEEPLINK_MODULE_UPPERCASE_PACKAGE,
                activityWithUppercasePackage,
                fakeBaseDeeplinkDelegate
            )
        )
        assertGeneratedCode(
            result = result,
            registryClassName = "com.Example.SampleModuleRegistry",
            indexEntries = listOf(
                DeepLinkEntry(
                    uriTemplate = "airbnb://example.com/deepLink",
                    className = "com.Example.SampleActivity",
                    method = null
                )
            ),
            generatedFileNames = listOf("DeepLinkDelegate.java", "SampleModuleRegistry.java")
        )
    }

    @Test
    fun testNonStaticMethodCompileFail() {
        val sampleActivity = SourceFile.java(
            "SampleActivity.java",
            """
                    package com.example;
                    import com.airbnb.deeplinkdispatch.DeepLink;
                    public class SampleActivity {
                        @DeepLink("airbnb://host/{arbitraryNumber}")
                        public Intent intentFromNoStatic(Context context) {
                            return new Intent();
                        }
                    }
                    """
        )
        val result = compile(listOf(sampleActivity))
        assertCompileError(result, "Only static methods can be annotated")
    }

    @Test
    fun testProcessorWithEmptyCustomPrefixFail() {
        val emptyPrefixLink = SourceFile.java(
            "MyDeepLink.java",
            """
                    package com.example;
                    import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                    @DeepLinkSpec(prefix = { "http://", "" })
                    public @interface MyDeepLink {
                        String[] value();
                    }
                    """
        )
        val sourceFiles = listOf(emptyPrefixLink, SIMPLE_DEEPLINK_ACTIVITY, SIMPLE_DEEPLINK_MODULE)

        val result = compile(sourceFiles)
        assertCompileError(result, "Prefix property cannot have null or empty strings")
    }

    @Test
    fun testProcessorWithEmptyDeepLinkSpecPrefixesFail() {
        val emptyPrefixArrayLink = SourceFile.java(
            "MyDeepLink.java",
            """
                    package com.example;
                    import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                    @DeepLinkSpec(prefix = { })
                    public @interface MyDeepLink {
                        String[] value();
                    }
                    """
        )
        val result = compile(
            listOf(
                emptyPrefixArrayLink, SIMPLE_DEEPLINK_ACTIVITY,
                SIMPLE_DEEPLINK_MODULE
            )
        )
        assertCompileError(result, "Prefix property cannot be empty")
    }

    @Test
    fun testProcessorWithDeepLinkSortRule() {
        val sampleActivity = SourceFile.java(
            "SampleActivity.java",
            """
                    package com.example;
                    import android.content.Context;
                    import android.content.Intent;
                    import com.airbnb.deeplinkdispatch.DeepLink;
                    import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                    import com.example.SampleModule;
                    @DeepLinkHandler({ SampleModule.class })
                    public class SampleActivity {
                        @DeepLink("airbnb://host/{var}")
                        public static Intent intentFromOnePathWithOneParam(Context context){
                          return new Intent();
                        }
                        @DeepLink("airbnb://host/path")
                        public static Intent intentFromOnePath(Context context){
                          return new Intent();
                        }  
                        @DeepLink("airbnb://host/path1/path2")
                        public static Intent intentFromTwoPath(Context context){
                          return new Intent();
                        }
                        @DeepLink("airbnb://host/path1/path3?q={q}")
                        public static Intent intentFromTwoPathWithQuery(Context context){
                          return new Intent();
                        }
                        @DeepLink("airbnb://host/{var1}/{var2}")
                        public static Intent intentFromTwoPathWithTwoParams(Context context){
                          return new Intent();  
                        }
                    }
                     """
        )
        val result =
            compile(listOf(SIMPLE_DEEPLINK_MODULE, sampleActivity, fakeBaseDeeplinkDelegate))
        assertGeneratedCode(
            result = result,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = listOf(
                DeepLinkEntry(
                    uriTemplate = "airbnb://host/path",
                    className = "com.example.SampleActivity",
                    method = "intentFromOnePath"
                ),
                DeepLinkEntry(
                    uriTemplate = "airbnb://host/path1/path2",
                    className = "com.example.SampleActivity",
                    method = "intentFromTwoPath"
                ),
                DeepLinkEntry(
                    uriTemplate = "airbnb://host/path1/path3?q={q}",
                    className = "com.example.SampleActivity",
                    method = "intentFromTwoPathWithQuery"
                ),
                DeepLinkEntry(
                    uriTemplate = "airbnb://host/{var1}/{var2}",
                    className = "com.example.SampleActivity",
                    method = "intentFromTwoPathWithTwoParams"
                ),
                DeepLinkEntry(
                    uriTemplate = "airbnb://host/{var}",
                    className = "com.example.SampleActivity",
                    method = "intentFromOnePathWithOneParam"
                ),
            ),
            generatedFileNames = listOf("DeepLinkDelegate.java", "SampleModuleRegistry.java")
        )
    }

    @Test
    fun testNonAppCompatTaskStackBuilderClassErrorMessage() {
        val sampleActivity = SourceFile.java(
            "SampleActivity.java",
            """
                    package com.example;
                    import com.airbnb.deeplinkdispatch.DeepLink;
                    import android.content.Context;
                    import android.app.TaskStackBuilder;
                    import android.content.Intent;
                    public class SampleActivity {  
                        @DeepLink("airbnb://host/{arbitraryNumber}")
                        public static TaskStackBuilder intentFromNoStatic(Context context) {
                            return TaskStackBuilder.create(context);
                        }
                    }
                    """
        )
        val result = compile(listOf(sampleActivity))
        assertCompileError(
            result,
            "Only `Intent`, `androidx.core.app.TaskStackBuilder` or "
                    + "'com.airbnb.deeplinkdispatch.DeepLinkMethodResult' are supported. Please double check "
                    + "your imports and try again."
        )
    }

    @Test
    fun testInvalidComponentParamInSchemeErrorMessage() {
        val sampleActivity = SourceFile.java(
            "SampleActivity.java",
            """
                    package com.example;import com.airbnb.deeplinkdispatch.DeepLink;
                    import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                    
                    import com.example.SampleModule;
                    
                    @DeepLink("airbnb://example.com/d{eepLink")
                    @DeepLinkHandler({ SampleModule.class })
                    public class SampleActivity { }
                    """
        )
        val result = compile(listOf(SIMPLE_DEEPLINK_MODULE, sampleActivity))
        assertCompileError(result, "Invalid URI component: d{eepLink. { must come before }.")
    }

    @Test
    fun testInvalidComponentParamPathSegmentErrorMessage() {
        val sampleActivity = SourceFile.java(
            "SampleActivity.java",
            """
             package com.example;import com.airbnb.deeplinkdispatch.DeepLink;
             import com.airbnb.deeplinkdispatch.DeepLinkHandler;
        
             import com.example.SampleModule;
        
             @DeepLink("airbnb://example.com/de}{epLink")
             @DeepLinkHandler({ SampleModule.class })
             public class SampleActivity {
             }
             """
        )
        val result = compile(listOf(SIMPLE_DEEPLINK_MODULE, sampleActivity))
        assertCompileError(result, "Invalid URI component: de}{epLink. { must come before }.")
    }

    @Test
    fun malformedConfigurablePathSegmentFailsWithErrorMessage() {
        val simpleActivity = SourceFile.java(
            "SampleActivity.java",
            """
                    package com.example;import com.airbnb.deeplinkdispatch.DeepLink;
                    import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                    
                    import com.Example.SampleModule;
                    
                    @DeepLink("airbnb://example.com/deepL<ink")@DeepLinkHandler({ SampleModule.class })
                    public class SampleActivity {
                    }
                    """
        )
        val result = compile(listOf(SIMPLE_DEEPLINK_MODULE, simpleActivity))
        assertCompileError(
            result,
            "Malformed path segment: deepL<ink! If it contains < or >, it must start"
                    + " with < and end with >."
        )
    }

    companion object {
        private val SIMPLE_DEEPLINK_ACTIVITY = SourceFile.java(
            "SampleActivity.java",
            """
                     package com.example;import com.airbnb.deeplinkdispatch.DeepLink;
                     import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                
                     import com.example.SampleModule;
                
                     @MyDeepLink({"example.com/deepLink","example.com/another"})
                     public class SampleActivity {
                     }
                     """
        )

        private val SIMPLE_DEEPLINK_MODULE = SourceFile.java(
            "SampleModule.java",
            """
                     package com.example;import com.airbnb.deeplinkdispatch.DeepLinkModule;
                
                     @DeepLinkModule
                     public class SampleModule {
                     }
                     """
        )
        private val SIMPLE_DEEPLINK_MODULE_UPPERCASE_PACKAGE = SourceFile.java(
            "SampleModule.java",
            """
                     package com.Example;import com.airbnb.deeplinkdispatch.DeepLinkModule;
                
                     @DeepLinkModule
                     public class SampleModule {
                     }
                     """
        )
    }
}