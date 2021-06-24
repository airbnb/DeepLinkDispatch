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
                 package com.example;
                 import com.airbnb.deeplinkdispatch.DeepLink;
                 @DeepLink("airbnb://example.com/deepLink")
                 @AppDeepLink({"example.com/deepLink","example.com/another"})
                 public class SampleActivity {
                 }
                 """
    )
    private val sampleActivityWithInnerClassDeeplink = SourceFile.java(
        "SampleActivity.java",
        """
                 package com.example;
                 import com.airbnb.deeplinkdispatch.DeepLink;
                 import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                 import android.content.Intent;
                 import android.content.Context;

                 @DeepLinkHandler({ SampleModule.class })
                 public class SampleActivity {
                       public static class InnerClass { 
                              @DeepLink("airbnb://example.com/innerClassDeeplink")
                              public static Intent intentForDeepLinkMethod(Context context) {
                                return new Intent();
                              }
                        }
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
        val sourceFiles = listOf(
            customAnnotationAppLink,
            module,
            sampleActivityWithOnlyCustomDeepLink,
            fakeBaseDeeplinkDelegate
        )
        val results = listOf(
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = listOf("com.example.AppDeepLink"),
                useKsp = false
            ),
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = listOf("com.example.AppDeepLink"),
                useKsp = true
            )
        )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = listOf(
                DeepLinkEntry(
                    uriTemplate = "example://example.com/deepLink",
                    className = "com.example.SampleActivity",
                    method = null
                )
            ),
            generatedFiles = mapOf("DeepLinkDelegate.java" to
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
            
                """.trimIndent(), "SampleModuleRegistry.java" to
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
                    return "\u0001\u0001\u0000\u0000\u0000\u0000\u0000or\u0002\u0007\u0000\u0000\u0000\u0000\u0000`example\u0004\u000b\u0000\u0000\u0000\u0000\u0000Mexample.com\b\b\u0000=\u0000\u0000\u0000\u0000deepLink\u0000\u001eexample://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000";
                  }
                }
            
               """.trimIndent())
        )
    }

    @Test
    fun testIncrementalProcessorWithCustomDeepLinkWithPlaceholdersRegistration() {
        val sourceFiles = listOf(
            customAnnotationPlaceholderInSchemeHostAppLink,
            module,
            sampleActivityWithOnlyCustomPlaceholderDeepLink,
            fakeBaseDeeplinkDelegate
        )
        val results = listOf(
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                useKsp = false
            ),
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                useKsp = true
            )
        )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = listOf(
                DeepLinkEntry(
                    uriTemplate = "http{scheme}://{host}example.com/deepLink",
                    className = "com.example.SampleActivity",
                    method = null
                )
            ),
            generatedFiles = mapOf("DeepLinkDelegate.java" to
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
                
                """.trimIndent(), "SampleModuleRegistry.java" to
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
                    return "\u0001\u0001\u0000\u0000\u0000\u0000\u0000\u0085r\u0012\f\u0000\u0000\u0000\u0000\u0000qhttp{scheme}\u0014\u0011\u0000\u0000\u0000\u0000\u0000X{host}example.com\b\b\u0000H\u0000\u0000\u0000\u0000deepLink\u0000)http{scheme}://{host}example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000";
                  }
                }
            
                """.trimIndent())
        )
    }

    @Test
    fun testIncrementalProcessorWithoutCustomDeepLinkRegistration() {
        val sourceFiles = listOf(
            customAnnotationAppLink,
            module,
            sampleActivityWithOnlyCustomDeepLink,
            fakeBaseDeeplinkDelegate
        )
        val results = listOf(
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = null,
                useKsp = false
            ),
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = null,
                useKsp = true
            )
        )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = emptyList(),
            generatedFiles = mapOf("DeepLinkDelegate.java" to
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
                
                """.trimIndent(), "SampleModuleRegistry.java" to
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
                    return "\u0001\u0001\u0000\u0000\u0000\u0000\u0000\u0000r";
                  }
                }
                
                """.trimIndent())
        )

    }

    @Test
    fun testCustomAnnotationMissingFromCompilerOptionsErrorMessage() {
        val sourceFiles = listOf(
            customAnnotationAppLink,
            sampleActivityWithStandardAndCustomDeepLink
        )
        val results = listOf(
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = null,
                useKsp = false
            ),
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = null,
                useKsp = true
            )
        )
        assertCompileError(
            results, "Unable to find annotation 'com.example.AppDeepLink' you must update "
                    + "'deepLink.customAnnotations' within the build.gradle"
        )
    }

    @Test
    fun testInnerClassDeeplink() {
        val sourceFiles = listOf(
            customAnnotationAppLink,
            sampleActivityWithInnerClassDeeplink,
            module,
            fakeBaseDeeplinkDelegate
        )
        val results = listOf(
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = null,
                useKsp = false
            ),
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = null,
                useKsp = true
            )
        )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = listOf(
                DeepLinkEntry(
                    uriTemplate = "airbnb://example.com/innerClassDeeplink",
                    className = "com.example.SampleActivity\$InnerClass",
                    method = "intentForDeepLinkMethod"
                )
            ),
            generatedFiles = mapOf("DeepLinkDelegate.java" to
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
                    
                    """.trimIndent(), "SampleModuleRegistry.java" to
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
                        return "\u0001\u0001\u0000\u0000\u0000\u0000\u0000£r\u0002\u0006\u0000\u0000\u0000\u0000\u0000\u0095airbnb\u0004\u000b\u0000\u0000\u0000\u0000\u0000\u0082example.com\b\u0012\u0000h\u0000\u0000\u0000\u0000innerClassDeeplink\u0000'airbnb://example.com/innerClassDeeplink\u0000%com.example.SampleActivity${"\$"}InnerClass\u0017intentForDeepLinkMethod";
                      }
                    }

                    """.trimIndent()
            )
        )
    }

    @Test
    fun testKsp() {
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
                 package com.example;
                 import com.airbnb.deeplinkdispatch.DeepLink;
                 import com.airbnb.deeplinkdispatch.DeepLinkHandler;
                 import android.content.Context;
                 import androidx.core.app.TaskStackBuilder;
                 import android.content.Intent;
                 import com.example.SampleModule;
                 @DeepLink("airbnb://example.com/deepLink")
                 @AppDeepLink({"example.com/deepLink","example.com/another"})
                 @WebDeepLink({"example.com/deepLink","example.com/another"})
                 @DeepLinkHandler({ SampleModule.class })
                 public class SampleActivity {
                 
                        @DeepLink("airbnb://intentMethod/{var1}/{var2}")
                        public static Intent intentFromTwoPathWithTwoParams(Context context){
                            return new Intent();  
                        }
                        
                        @DeepLink("airbnb://taskStackBuilderMethod/{arbitraryNumber}")
                        public static TaskStackBuilder deeplinkOneParameter(Context context) {
                            return TaskStackBuilder.create(context);
                        }
                        
                        @WebDeepLink({"example.com/method1","example.com/method2"})
                        public static TaskStackBuilder webLinkMethod(Context context) {
                            return TaskStackBuilder.create(context);
                        }
                 }
                 """
        )
        val sourceFiles = listOf(
            customAnnotationAppLink, customAnnotationWebLink,
            module, sampleActivity, fakeBaseDeeplinkDelegate
        )
        val results = listOf(
            compileIncremental(
                sourceFiles = sourceFiles,
                useKsp = false,
                customDeepLinks = listOf("com.example.AppDeepLink", "com.example.WebDeepLink")
            ),
            compileIncremental(
                sourceFiles = sourceFiles,
                useKsp = true,
                customDeepLinks = listOf("com.example.AppDeepLink", "com.example.WebDeepLink")
            )
        )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = listOf(
                    DeepLinkEntry(uriTemplate= "airbnb://example.com/deepLink" , className= "com.example.SampleActivity" ,method= null),
                    DeepLinkEntry(uriTemplate= "airbnb://intentMethod/{var1}/{var2}" , className= "com.example.SampleActivity" ,method= "intentFromTwoPathWithTwoParams"),
                    DeepLinkEntry(uriTemplate= "airbnb://taskStackBuilderMethod/{arbitraryNumber}" , className= "com.example.SampleActivity" ,method= "deeplinkOneParameter"),
                    DeepLinkEntry(uriTemplate= "example://example.com/another" , className= "com.example.SampleActivity" ,method= null),
                    DeepLinkEntry(uriTemplate= "example://example.com/deepLink" , className= "com.example.SampleActivity" ,method= null),
                    DeepLinkEntry(uriTemplate= "http://example.com/another" , className= "com.example.SampleActivity" ,method= null),
                    DeepLinkEntry(uriTemplate= "http://example.com/deepLink" , className= "com.example.SampleActivity" ,method= null),
                    DeepLinkEntry(uriTemplate= "http://example.com/method1" , className= "com.example.SampleActivity" ,method= "webLinkMethod"),
                    DeepLinkEntry(uriTemplate= "http://example.com/method2" , className= "com.example.SampleActivity" ,method= "webLinkMethod"),
                    DeepLinkEntry(uriTemplate= "https://example.com/another" , className= "com.example.SampleActivity" ,method= null),
                    DeepLinkEntry(uriTemplate= "https://example.com/deepLink" , className= "com.example.SampleActivity" ,method= null),
                    DeepLinkEntry(uriTemplate= "https://example.com/method1" , className= "com.example.SampleActivity" ,method= "webLinkMethod"),
                    DeepLinkEntry(uriTemplate= "https://example.com/method2" , className= "com.example.SampleActivity" ,method= "webLinkMethod"),
            ),
            generatedFiles = mapOf("DeepLinkDelegate.java" to
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
                
                """.trimIndent(), "SampleModuleRegistry.java" to
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
                    return "\u0001\u0001\u0000\u0000\u0000\u0000\u0005\rr\u0002\u0006\u0000\u0000\u0000\u0000\u0001\u008aairbnb\u0004\u000b\u0000\u0000\u0000\u0000\u0000Lexample.com\b\b\u0000<\u0000\u0000\u0000\u0000deepLink\u0000\u001dairbnb://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\u0004\f\u0000\u0000\u0000\u0000\u0000|intentmethod\u0018\u0006\u0000\u0000\u0000\u0000\u0000n{var1}\u0018\u0006\u0000`\u0000\u0000\u0000\u0000{var2}\u0000#airbnb://intentMethod/{var1}/{var2}\u0000\u001acom.example.SampleActivity\u001eintentFromTwoPathWithTwoParams\u0004\u0016\u0000\u0000\u0000\u0000\u0000}taskstackbuildermethod\u0018\u0011\u0000d\u0000\u0000\u0000\u0000{arbitraryNumber}\u00001airbnb://taskStackBuilderMethod/{arbitraryNumber}\u0000\u001acom.example.SampleActivity\u0014deeplinkOneParameter\u0002\u0007\u0000\u0000\u0000\u0000\u0000«example\u0004\u000b\u0000\u0000\u0000\u0000\u0000\u0098example.com\b\u0007\u0000<\u0000\u0000\u0000\u0000another\u0000\u001dexample://example.com/another\u0000\u001acom.example.SampleActivity\u0000\b\b\u0000=\u0000\u0000\u0000\u0000deepLink\u0000\u001eexample://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\u0002\u0004\u0000\u0000\u0000\u0000\u0001Ohttp\u0004\u000b\u0000\u0000\u0000\u0000\u0001<example.com\b\u0007\u00009\u0000\u0000\u0000\u0000another\u0000\u001ahttp://example.com/another\u0000\u001acom.example.SampleActivity\u0000\b\b\u0000:\u0000\u0000\u0000\u0000deepLink\u0000\u001bhttp://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\b\u0007\u0000F\u0000\u0000\u0000\u0000method1\u0000\u001ahttp://example.com/method1\u0000\u001acom.example.SampleActivity\rwebLinkMethod\b\u0007\u0000F\u0000\u0000\u0000\u0000method2\u0000\u001ahttp://example.com/method2\u0000\u001acom.example.SampleActivity\rwebLinkMethod\u0002\u0005\u0000\u0000\u0000\u0000\u0001Shttps\u0004\u000b\u0000\u0000\u0000\u0000\u0001@example.com\b\u0007\u0000:\u0000\u0000\u0000\u0000another\u0000\u001bhttps://example.com/another\u0000\u001acom.example.SampleActivity\u0000\b\b\u0000;\u0000\u0000\u0000\u0000deepLink\u0000\u001chttps://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\b\u0007\u0000G\u0000\u0000\u0000\u0000method1\u0000\u001bhttps://example.com/method1\u0000\u001acom.example.SampleActivity\rwebLinkMethod\b\u0007\u0000G\u0000\u0000\u0000\u0000method2\u0000\u001bhttps://example.com/method2\u0000\u001acom.example.SampleActivity\rwebLinkMethod";
                  }
                }
                
                """.trimIndent())
        )
    }
}