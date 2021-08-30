package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.test.Source
import com.tschuchort.compiletesting.KotlinCompilation
import org.assertj.core.api.Assertions
import org.junit.Test

class DeepLinkProcessorNonIncrementalTest : BaseDeepLinkProcessorTest() {
    @Test
    fun testProcessor() {
        val sampleActivity = Source.JavaSource(
            "com.example.SampleActivity",
            """
                     package com.example;
                     import com.airbnb.deeplinkdispatch.DeepLink;
                     import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                     import com.example.SampleModule;
                     @DeepLink("airbnb://example.com/deepLink")
                     @DeepLinkHandler({ SampleModule.class })
                     public class SampleActivity extends android.app.Activity {
                     }
                     """
        )
        val results = listOf(
            compile(
                sourceFiles = listOf(
                    SAMPLE_DEEPLINK_MODULE,
                    sampleActivity,
                    fakeBaseDeeplinkDelegate
                ),
                useKsp = false
            ),
            compile(
                sourceFiles = listOf(
                    SAMPLE_DEEPLINK_MODULE,
                    sampleActivity,
                    fakeBaseDeeplinkDelegate
                ),
                useKsp = true
            )
        )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = listOf(
                DeepLinkEntry.ActivityDeeplinkEntry(
                    "airbnb://example.com/deepLink",
                    "com.example.SampleActivity"
                )
            ),
            generatedFiles = mapOf(
                "DeepLinkDelegate.java" to
                    """
                package com.example;

                import com.airbnb.deeplinkdispatch.BaseDeepLinkDelegate;
                import java.lang.String;
                import java.util.Arrays;
                import java.util.Map;

                public final class DeepLinkDelegate extends BaseDeepLinkDelegate {
                  public DeepLinkDelegate(SampleModuleRegistry sampleModuleRegistry) {
                    super(Arrays.asList(
                      sampleModuleRegistry
                    ));
                  }

                  public DeepLinkDelegate(SampleModuleRegistry sampleModuleRegistry,
                      Map<String, String> configurablePathSegmentReplacements) {
                    super(Arrays.asList(
                      sampleModuleRegistry),
                      configurablePathSegmentReplacements
                    );
                  }
                }

                    """.trimIndent(),
                "SampleModuleRegistry.java" to
                    """
                package com.example;

                import com.airbnb.deeplinkdispatch.BaseRegistry;
                import com.airbnb.deeplinkdispatch.base.Utils;
                import java.lang.String;

                public final class SampleModuleRegistry extends BaseRegistry {
                  public SampleModuleRegistry() {
                    super(Utils.readMatchIndexFromStrings( new String[] {matchIndex0(), }),
                    new String[]{});
                  }

                  private static String matchIndex0() {
                    return "\u0001\u0001\u0000\u0000\u0000\u0000\u0000nr\u0002\u0006\u0000\u0000\u0000\u0000\u0000`airbnb\u0004\u000b\u0000\u0000\u0000\u0000\u0000Mexample.com\b\b\u0000=\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001dairbnb://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000";
                  }
                }

                    """.trimIndent()
            )
        )
    }

    @Test
    fun testProcessorWithDefaultAndCustomAnnotations() {
        val customAnnotationWebLink = Source.JavaSource(
            "com.example.WebDeepLink",
            """
            package com.example;
            import com.airbnb.deeplinkdispatch.DeepLinkSpec;
            @DeepLinkSpec(prefix = { "http://", "https://"})
            public @interface WebDeepLink {
                String[] value();
            }
            """
        )
        val customAnnotationAppLink = Source.JavaSource(
            "com.example.AppDeepLink",
            """
                    package com.example;
                    import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                    @DeepLinkSpec(prefix = { "example://" })
                    public @interface AppDeepLink {
                        String[] value();
                    }
                    """
        )
        val sampleActivity = Source.JavaSource(
            "com.example.SampleActivity",
            """
                 package com.example;
                 import com.airbnb.deeplinkdispatch.DeepLink;
                 import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                 import com.example.SampleModule;
                 @DeepLink("airbnb://example.com/deepLink")
                 @AppDeepLink({"example.com/deepLink","example.com/another"})
                 @WebDeepLink({"example.com/deepLink","example.com/another"})
                 @DeepLinkHandler({ SampleModule.class })
                 public class SampleActivity extends android.app.Activity {
                 }
                 """
        )
        val resultsKapt = listOf(
            compile(
                sourceFiles = listOf(
                    customAnnotationAppLink, customAnnotationWebLink,
                    SAMPLE_DEEPLINK_MODULE, sampleActivity, fakeBaseDeeplinkDelegate
                ),
                useKsp = false
            )
        )
        assertGeneratedCode(
            results = resultsKapt,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = listOf(
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "airbnb://example.com/deepLink",
                    className = "com.example.SampleActivity"
                ),
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "example://example.com/another",
                    className = "com.example.SampleActivity"
                ),
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "example://example.com/deepLink",
                    className = "com.example.SampleActivity"
                ),
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "http://example.com/another",
                    className = "com.example.SampleActivity"
                ),
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "http://example.com/deepLink",
                    className = "com.example.SampleActivity"
                ),
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "https://example.com/another",
                    className = "com.example.SampleActivity"
                ),
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "https://example.com/deepLink",
                    className = "com.example.SampleActivity"
                )
            ),
            generatedFiles = mapOf(
                "DeepLinkDelegate.java" to
                    """
                package com.example;

                import com.airbnb.deeplinkdispatch.BaseDeepLinkDelegate;
                import java.lang.String;
                import java.util.Arrays;
                import java.util.Map;

                public final class DeepLinkDelegate extends BaseDeepLinkDelegate {
                  public DeepLinkDelegate(SampleModuleRegistry sampleModuleRegistry) {
                    super(Arrays.asList(
                      sampleModuleRegistry
                    ));
                  }

                  public DeepLinkDelegate(SampleModuleRegistry sampleModuleRegistry,
                      Map<String, String> configurablePathSegmentReplacements) {
                    super(Arrays.asList(
                      sampleModuleRegistry),
                      configurablePathSegmentReplacements
                    );
                  }
                }
                
                    """.trimIndent(),
                "SampleModuleRegistry.java" to
                    """
                package com.example;

                import com.airbnb.deeplinkdispatch.BaseRegistry;
                import com.airbnb.deeplinkdispatch.base.Utils;
                import java.lang.String;

                public final class SampleModuleRegistry extends BaseRegistry {
                  public SampleModuleRegistry() {
                    super(Utils.readMatchIndexFromStrings( new String[] {matchIndex0(), }),
                    new String[]{});
                  }

                  private static String matchIndex0() {
                    return "\u0001\u0001\u0000\u0000\u0000\u0000\u0002\u0093r\u0002\u0006\u0000\u0000\u0000\u0000\u0000`airbnb\u0004\u000b\u0000\u0000\u0000\u0000\u0000Mexample.com\b\b\u0000=\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001dairbnb://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\u0002\u0007\u0000\u0000\u0000\u0000\u0000­example\u0004\u000b\u0000\u0000\u0000\u0000\u0000\u009aexample.com\b\u0007\u0000=\u0000\u0000\u0000\u0000another\u0000\u0000\u001dexample://example.com/another\u0000\u001acom.example.SampleActivity\u0000\b\b\u0000>\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001eexample://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\u0002\u0004\u0000\u0000\u0000\u0000\u0000§http\u0004\u000b\u0000\u0000\u0000\u0000\u0000\u0094example.com\b\u0007\u0000:\u0000\u0000\u0000\u0000another\u0000\u0000\u001ahttp://example.com/another\u0000\u001acom.example.SampleActivity\u0000\b\b\u0000;\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001bhttp://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\u0002\u0005\u0000\u0000\u0000\u0000\u0000©https\u0004\u000b\u0000\u0000\u0000\u0000\u0000\u0096example.com\b\u0007\u0000;\u0000\u0000\u0000\u0000another\u0000\u0000\u001bhttps://example.com/another\u0000\u001acom.example.SampleActivity\u0000\b\b\u0000<\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001chttps://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000";
                  }
                }
               
                    """.trimIndent()
            )
        )
        // KSP does not support custom annotations it does not know before processing. When using KSP they always
        // have to be defined via the deepLink.customAnnotations gradle config.
        val resultsKsp = listOf(
            compile(
                sourceFiles = listOf(
                    customAnnotationAppLink, customAnnotationWebLink,
                    SAMPLE_DEEPLINK_MODULE, sampleActivity, fakeBaseDeeplinkDelegate
                ),
                useKsp = true
            )
        )
        assertCompileError(resultsKsp, "[ksp] Unable to find annotation 'com.example.AppDeepLink' you must update 'deepLink.customAnnotations' within the build.gradle")
    }

    @Test
    fun testDuplicatedUriMatch() {
        val sampleActivity = Source.JavaSource(
            "com.example.SampleActivity",
            """
                    package com.example;
                    import android.content.Context;
                    import android.content.Intent;
                    import com.airbnb.deeplinkdispatch.DeepLink;
                    import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                    import com.example.SampleModule;
                    @DeepLinkHandler({ SampleModule.class })
                    public class SampleActivity extends android.app.Activity { 
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
        val module = Source.JavaSource(
            "com.example.SampleModule",
            """
                     package com.example;
                     import com.airbnb.deeplinkdispatch.DeepLinkModule;
                     @DeepLinkModule
                     public class SampleModule { }
                     """
        )
        val results = listOf(
            compile(
                sourceFiles = listOf(
                    module, sampleActivity, fakeBaseDeeplinkDelegate
                ),
                useKsp = false
            ),
            compile(
                sourceFiles = listOf(
                    module, sampleActivity, fakeBaseDeeplinkDelegate
                ),
                useKsp = true
            )
        )
        assertCompileError(
            results = results,
            errorMessage = "Internal error during annotation processing: java.lang.IllegalStateException: " +
                "Ambiguous URI. Same match for two URIs"
        )
    }

    @Test
    fun uppercasePackage() {
        val activityWithUppercasePackage = Source.JavaSource(
            "com.example.SampleActivity",
            """
                     package com.Example;
                     import com.airbnb.deeplinkdispatch.DeepLink;
                     import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                     import com.Example.SampleModule;
                     @DeepLink("airbnb://example.com/deepLink")@DeepLinkHandler({ SampleModule.class })
                     public class SampleActivity extends android.app.Activity {
                     }
                     """
        )
        val results = listOf(
            compile(
                listOf(
                    SIMPLE_DEEPLINK_MODULE_UPPERCASE_PACKAGE,
                    activityWithUppercasePackage,
                    fakeBaseDeeplinkDelegate
                ),
                useKsp = false
            ),
            compile(
                listOf(
                    SIMPLE_DEEPLINK_MODULE_UPPERCASE_PACKAGE,
                    activityWithUppercasePackage,
                    fakeBaseDeeplinkDelegate
                ),
                useKsp = true
            )
        )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.Example.SampleModuleRegistry",
            indexEntries = listOf(
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "airbnb://example.com/deepLink",
                    className = "com.Example.SampleActivity"
                )
            ),
            generatedFiles = mapOf(
                "DeepLinkDelegate.java" to
                    """
                package com.Example;

                import com.airbnb.deeplinkdispatch.BaseDeepLinkDelegate;
                import java.lang.String;
                import java.util.Arrays;
                import java.util.Map;

                public final class DeepLinkDelegate extends BaseDeepLinkDelegate {
                  public DeepLinkDelegate(SampleModuleRegistry sampleModuleRegistry) {
                    super(Arrays.asList(
                      sampleModuleRegistry
                    ));
                  }

                  public DeepLinkDelegate(SampleModuleRegistry sampleModuleRegistry,
                      Map<String, String> configurablePathSegmentReplacements) {
                    super(Arrays.asList(
                      sampleModuleRegistry),
                      configurablePathSegmentReplacements
                    );
                  }
                }
                
                    """.trimIndent(),
                "SampleModuleRegistry.java" to
                    """
                package com.Example;

                import com.airbnb.deeplinkdispatch.BaseRegistry;
                import com.airbnb.deeplinkdispatch.base.Utils;
                import java.lang.String;

                public final class SampleModuleRegistry extends BaseRegistry {
                  public SampleModuleRegistry() {
                    super(Utils.readMatchIndexFromStrings( new String[] {matchIndex0(), }),
                    new String[]{});
                  }

                  private static String matchIndex0() {
                    return "\u0001\u0001\u0000\u0000\u0000\u0000\u0000nr\u0002\u0006\u0000\u0000\u0000\u0000\u0000`airbnb\u0004\u000b\u0000\u0000\u0000\u0000\u0000Mexample.com\b\b\u0000=\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001dairbnb://example.com/deepLink\u0000\u001acom.Example.SampleActivity\u0000";
                  }
                }
                
                    """.trimIndent()
            )
        )
    }

    @Test
    fun testNonStaticMethodCompileFail() {
        val sampleActivity = Source.JavaSource(
            "com.example.SampleActivity",
            """
                    package com.example;
                    import com.airbnb.deeplinkdispatch.DeepLink;
                    public class SampleActivity extends android.app.Activity {
                        @DeepLink("airbnb://host/{arbitraryNumber}")
                        public Intent intentFromNoStatic(Context context) {
                            return new Intent();
                        }
                    }
                    """
        )
        val results = listOf(
            compile(
                sourceFiles = listOf(sampleActivity),
                useKsp = false
            ),
            compile(
                sourceFiles = listOf(sampleActivity),
                useKsp = true
            )
        )
        assertCompileError(
            results = results,
            errorMessage = "Only static methods can be annotated"
        )
    }

    @Test
    fun testProcessorWithEmptyCustomPrefixFail() {
        val emptyPrefixLink = Source.JavaSource(
            "com.example.MyDeepLink",
            """
                    package com.example;
                    import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                    @DeepLinkSpec(prefix = { "http://", "" })
                    public @interface MyDeepLink {
                        String[] value();
                    }
                    """
        )
        val sourceFiles = listOf(emptyPrefixLink, SIMPLE_DEEPLINK_ACTIVITY, SAMPLE_DEEPLINK_MODULE)

        val resultsKapt = listOf(
            compile(
                sourceFiles = sourceFiles,
                useKsp = false
            )
        )
        assertCompileError(
            results = resultsKapt,
            errorMessage = "Prefix property cannot have null or empty strings"
        )
        // KSP does not support custom annotations it does not know before processing.
        // When using KSP they always have to be defined via the deepLink.customAnnotations
        // gradle config.
        // Because of this it does not see the error in the Spec.
        val resultsKsp = listOf(
            compile(
                sourceFiles = sourceFiles,
                useKsp = true
            )
        )
        Assertions.assertThat(resultsKsp[0].result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
    }

    @Test
    fun testProcessorWithEmptyDeepLinkSpecPrefixesFail() {
        val emptyPrefixArrayLink = Source.JavaSource(
            "com.example.MyDeepLink",
            """
                    package com.example;
                    import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                    @DeepLinkSpec(prefix = { })
                    public @interface MyDeepLink {
                        String[] value();
                    }
                    """
        )
        val sourceFiles = listOf(
            emptyPrefixArrayLink, SIMPLE_DEEPLINK_ACTIVITY,
            SAMPLE_DEEPLINK_MODULE
        )
        val results = listOf(
            compile(
                sourceFiles = sourceFiles,
                useKsp = false
            )
        )
        assertCompileError(results, "Prefix property cannot be empty")
        // KSP does not support custom annotations it does not know before processing.
        // When using KSP they always have to be defined via the deepLink.customAnnotations
        // gradle config.
        // Because of this it does not see the error in the Spec.
        val resultsKsp = listOf(
            compile(
                sourceFiles = sourceFiles,
                useKsp = true
            )
        )
        Assertions.assertThat(resultsKsp[0].result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
    }

    @Test
    fun testProcessorWithDeepLinkSortRule() {
        val sampleActivity = Source.JavaSource(
            "com.example.SampleActivity",
            """
                    package com.example;
                    import android.content.Context;
                    import android.content.Intent;
                    import com.airbnb.deeplinkdispatch.DeepLink;
                    import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                    import com.example.SampleModule;
                    @DeepLinkHandler({ SampleModule.class })
                    public class SampleActivity extends android.app.Activity {
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
        val results = listOf(
            compile(
                sourceFiles = listOf(
                    SAMPLE_DEEPLINK_MODULE,
                    sampleActivity,
                    fakeBaseDeeplinkDelegate
                ),
                useKsp = false
            ),
            compile(
                sourceFiles = listOf(
                    SAMPLE_DEEPLINK_MODULE,
                    sampleActivity,
                    fakeBaseDeeplinkDelegate
                ),
                useKsp = true
            )
        )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = listOf(
                DeepLinkEntry.MethodDeeplinkEntry(
                    uriTemplate = "airbnb://host/path",
                    className = "com.example.SampleActivity",
                    method = "intentFromOnePath"
                ),
                DeepLinkEntry.MethodDeeplinkEntry(
                    uriTemplate = "airbnb://host/path1/path2",
                    className = "com.example.SampleActivity",
                    method = "intentFromTwoPath"
                ),
                DeepLinkEntry.MethodDeeplinkEntry(
                    uriTemplate = "airbnb://host/path1/path3?q={q}",
                    className = "com.example.SampleActivity",
                    method = "intentFromTwoPathWithQuery"
                ),
                DeepLinkEntry.MethodDeeplinkEntry(
                    uriTemplate = "airbnb://host/{var1}/{var2}",
                    className = "com.example.SampleActivity",
                    method = "intentFromTwoPathWithTwoParams"
                ),
                DeepLinkEntry.MethodDeeplinkEntry(
                    uriTemplate = "airbnb://host/{var}",
                    className = "com.example.SampleActivity",
                    method = "intentFromOnePathWithOneParam"
                ),
            ),
            generatedFiles = mapOf(
                "DeepLinkDelegate.java" to
                    """
                package com.example;

                import com.airbnb.deeplinkdispatch.BaseDeepLinkDelegate;
                import java.lang.String;
                import java.util.Arrays;
                import java.util.Map;

                public final class DeepLinkDelegate extends BaseDeepLinkDelegate {
                  public DeepLinkDelegate(SampleModuleRegistry sampleModuleRegistry) {
                    super(Arrays.asList(
                      sampleModuleRegistry
                    ));
                  }

                  public DeepLinkDelegate(SampleModuleRegistry sampleModuleRegistry,
                      Map<String, String> configurablePathSegmentReplacements) {
                    super(Arrays.asList(
                      sampleModuleRegistry),
                      configurablePathSegmentReplacements
                    );
                  }
                }
                
                    """.trimIndent(),
                "SampleModuleRegistry.java" to
                    """
                package com.example;

                import com.airbnb.deeplinkdispatch.BaseRegistry;
                import com.airbnb.deeplinkdispatch.base.Utils;
                import java.lang.String;

                public final class SampleModuleRegistry extends BaseRegistry {
                  public SampleModuleRegistry() {
                    super(Utils.readMatchIndexFromStrings( new String[] {matchIndex0(), }),
                    new String[]{});
                  }

                  private static String matchIndex0() {
                    return "\u0001\u0001\u0000\u0000\u0000\u0000\u0002\u0005r\u0002\u0006\u0000\u0000\u0000\u0000\u0001÷airbnb\u0004\u0004\u0000\u0000\u0000\u0000\u0001ëhost\b\u0004\u0000C\u0000\u0000\u0000\u0000path\u0001\u0000\u0012airbnb://host/path\u0000\u001acom.example.SampleActivity\u0011intentFromOnePath\b\u0005\u0000\u0000\u0000\u0000\u0000½path1\b\u0005\u0000J\u0000\u0000\u0000\u0000path2\u0001\u0000\u0019airbnb://host/path1/path2\u0000\u001acom.example.SampleActivity\u0011intentFromTwoPath\b\u0005\u0000Y\u0000\u0000\u0000\u0000path3\u0001\u0000\u001fairbnb://host/path1/path3?q={q}\u0000\u001acom.example.SampleActivity\u001aintentFromTwoPathWithQuery\u0018\u0006\u0000\u0000\u0000\u0000\u0000g{var1}\u0018\u0006\u0000Y\u0000\u0000\u0000\u0000{var2}\u0001\u0000\u001bairbnb://host/{var1}/{var2}\u0000\u001acom.example.SampleActivity\u001eintentFromTwoPathWithTwoParams\u0018\u0005\u0000P\u0000\u0000\u0000\u0000{var}\u0001\u0000\u0013airbnb://host/{var}\u0000\u001acom.example.SampleActivity\u001dintentFromOnePathWithOneParam";
                  }
                }
            
                    """.trimIndent()
            )
        )
    }

    @Test
    fun testNonAppCompatTaskStackBuilderClassErrorMessage() {
        val sampleActivity = Source.JavaSource(
            "com.example.SampleActivity",
            """
                    package com.example;
                    import com.airbnb.deeplinkdispatch.DeepLink;
                    import android.content.Context;
                    import android.app.TaskStackBuilder;
                    import android.content.Intent;
                    public class SampleActivity extends android.app.Activity {  
                        @DeepLink("airbnb://host/{arbitraryNumber}")
                        public static TaskStackBuilder intentFromNoStatic(Context context) {
                            return TaskStackBuilder.create(context);
                        }
                    }
                    """
        )
        val results = listOf(
            compile(
                sourceFiles = listOf(sampleActivity),
                useKsp = false
            ),
            compile(
                sourceFiles = listOf(sampleActivity),
                useKsp = true
            )
        )
        assertCompileError(
            results,
            "Only `Intent`, `androidx.core.app.TaskStackBuilder` or " +
                "'com.airbnb.deeplinkdispatch.DeepLinkMethodResult' are supported. Please double check " +
                "your imports and try again."
        )
    }

    @Test
    fun testInvalidComponentParamInSchemeErrorMessage() {
        val sampleActivity = Source.JavaSource(
            "com.example.SampleActivity",
            """
                    package com.example;
                    import com.airbnb.deeplinkdispatch.DeepLink;
                    import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                    
                    import com.example.SampleModule;
                    
                    @DeepLink("airbnb://example.com/d{eepLink")
                    @DeepLinkHandler({ SampleModule.class })
                    public class SampleActivity extends android.app.Activity { }
                    """
        )
        val results = listOf(
            compile(
                listOf(SAMPLE_DEEPLINK_MODULE, sampleActivity),
                useKsp = false
            ),
            compile(
                listOf(SAMPLE_DEEPLINK_MODULE, sampleActivity),
                useKsp = true
            )
        )
        assertCompileError(results, "Invalid URI component: d{eepLink. { must come before }.")
    }

    @Test
    fun testInvalidComponentParamPathSegmentErrorMessage() {
        val sampleActivity = Source.JavaSource(
            "com.example.SampleActivity",
            """
             package com.example;
             import com.airbnb.deeplinkdispatch.DeepLink;
             import com.airbnb.deeplinkdispatch.DeepLinkHandler;
        
             import com.example.SampleModule;
        
             @DeepLink("airbnb://example.com/de}{epLink")
             @DeepLinkHandler({ SampleModule.class })
             public class SampleActivity extends android.app.Activity {
             }
             """
        )
        val results = listOf(
            compile(
                listOf(SAMPLE_DEEPLINK_MODULE, sampleActivity),
                useKsp = false
            ),
            compile(
                listOf(SAMPLE_DEEPLINK_MODULE, sampleActivity),
                useKsp = true
            )
        )
        assertCompileError(results, "Invalid URI component: de}{epLink. { must come before }.")
    }

    @Test
    fun malformedConfigurablePathSegmentFailsWithErrorMessage() {
        val simpleActivity = Source.JavaSource(
            "com.example.SampleActivity",
            """
                    package com.example;
                    import com.airbnb.deeplinkdispatch.DeepLink;
                    import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                    
                    import com.Example.SampleModule;
                    
                    @DeepLink("airbnb://example.com/deepL<ink")@DeepLinkHandler({ SampleModule.class })
                    public class SampleActivity extends android.app.Activity {
                    }
                    """
        )
        val results = listOf(
            compile(
                sourceFiles = listOf(SAMPLE_DEEPLINK_MODULE, simpleActivity),
                useKsp = false
            ),
            compile(
                sourceFiles = listOf(SAMPLE_DEEPLINK_MODULE, simpleActivity),
                useKsp = true
            )
        )
        assertCompileError(
            results,
            "Malformed path segment: deepL<ink! If it contains < or >, it must start" +
                " with < and end with >."
        )
    }

    companion object {
        private val SIMPLE_DEEPLINK_ACTIVITY = Source.JavaSource(
            "com.example.SampleActivity",
            """
                     package com.example;
                     import com.airbnb.deeplinkdispatch.DeepLink;
                     import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                
                     import com.example.SampleModule;
                
                     @MyDeepLink({"example.com/deepLink","example.com/another"})
                     public class SampleActivity extends android.app.Activity {
                     }
                     """
        )

        private val SAMPLE_DEEPLINK_MODULE = Source.JavaSource(
            "com.example.SampleModule",
            """
                     package com.example;
                     import com.airbnb.deeplinkdispatch.DeepLinkModule;
                
                     @DeepLinkModule
                     public class SampleModule {
                     }
                     """
        )
        private val SIMPLE_DEEPLINK_MODULE_UPPERCASE_PACKAGE = Source.JavaSource(
            "com.Example.SampleModule",
            """
                     package com.Example;
                     import com.airbnb.deeplinkdispatch.DeepLinkModule;
                
                     @DeepLinkModule
                     public class SampleModule {
                     }
                     """
        )
    }
}
