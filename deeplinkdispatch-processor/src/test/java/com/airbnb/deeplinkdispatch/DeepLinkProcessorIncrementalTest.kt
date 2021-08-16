package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.test.Source
import org.junit.Test

class DeepLinkProcessorIncrementalTest : BaseDeepLinkProcessorTest() {
    private val customAnnotationAppLink = Source.JavaSource(
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
    private val customAnnotationPlaceholderInSchemeHostAppLink = Source.JavaSource(
        "com.example.PlaceholderDeepLink",
        """
                package com.example;
                import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                @DeepLinkSpec(prefix = { "http{scheme}://{host}example.com/" })
                public @interface PlaceholderDeepLink {
                    String[] value();
                }
                """
    )
    private val customAnnotationPlaceholderWithValidAllowedElementsInSchemeHostAppLink =
        Source.JavaSource(
            "com.example.PlaceholderDeepLink",
            """
                package com.example;
                import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                @DeepLinkSpec(prefix = { "http{scheme(|s)}://{host(|www.)}example.com/" })
                public @interface PlaceholderDeepLink {
                    String[] value();
                }
                """
        )
    private val customAnnotationPlaceholderWithDuplicateAllowedElementsInSchemeHostAppLink =
        Source.JavaSource(
            "com.example.PlaceholderDeepLink",
            """
                package com.example;
                import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                @DeepLinkSpec(prefix = { "http{scheme(|s)(|test)}://{host(|www.)}example.com/" })
                public @interface PlaceholderDeepLink {
                    String[] value();
                }
                """
        )
    private val customAnnotationPlaceholderWithValidElementsNotAtEndInSchemeHostAppLink =
        Source.JavaSource(
            "com.example.PlaceholderDeepLink",
            """
                package com.example;
                import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                @DeepLinkSpec(prefix = { "http{scheme(|s)}://{(|www.)host}example.com/" })
                public @interface PlaceholderDeepLink {
                    String[] value();
                }
                """
        )
    private val sampleActivityWithStandardAndCustomDeepLink = Source.JavaSource(
        "com.example.SampleActivity",
        """
                 package com.example;
                 import com.airbnb.deeplinkdispatch.DeepLink;
                 @DeepLink("airbnb://example.com/deepLink")
                 @AppDeepLink({"example.com/deepLink","example.com/another"})
                 public class SampleActivity {
                 }
                 """
    )
    private val sampleActivityWithInnerClassDeeplinkJava = Source.JavaSource(
        "com.example.SampleActivity",
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
    private val sampleActivityWithInnerClassDeeplinkKotlin = Source.KotlinSource(
        "SampleActivity.kt",
        """
                 package com.example
                 import com.airbnb.deeplinkdispatch.DeepLink
                 import com.airbnb.deeplinkdispatch.DeepLinkHandler
                 import android.content.Intent
                 import android.content.Context

                 @DeepLinkHandler(SampleModule::class)
                 class SampleActivity {
                       class InnerClass { 
                                object DeepLinks {
                                    @DeepLink("airbnb://example.com/innerClassDeeplink")
                                    @JvmStatic
                                    fun intentForDeepLinkMethod(context: Context) = Intent()
                                }
                        }
                 }
                 """
    )
    private val sampleActivityWithOnlyCustomDeepLink = Source.JavaSource(
        "com.example.SampleActivity",
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
    private val sampleActivityWithOnlyCustomPlaceholderDeepLink = Source.JavaSource(
        "com.example.SampleActivity",
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
                useKsp = false,
            ),
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = listOf("com.example.AppDeepLink"),
                useKsp = true,
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
                    return "\u0001\u0001\u0000\u0000\u0000\u0000\u0000or\u0002\u0007\u0000\u0000\u0000\u0000\u0000`example\u0004\u000b\u0000\u0000\u0000\u0000\u0000Mexample.com\b\b\u0000=\u0000\u0000\u0000\u0000deepLink\u0000\u001eexample://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000";
                  }
                }
            
                    """.trimIndent()
            )
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
                useKsp = false,
            ),
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                useKsp = true,
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
                    return "\u0001\u0001\u0000\u0000\u0000\u0000\u0000\u0085r\u0012\f\u0000\u0000\u0000\u0000\u0000qhttp{scheme}\u0014\u0011\u0000\u0000\u0000\u0000\u0000X{host}example.com\b\b\u0000H\u0000\u0000\u0000\u0000deepLink\u0000)http{scheme}://{host}example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000";
                  }
                }
            
                    """.trimIndent()
            )
        )
    }

    @Test
    fun testIncrementalProcessorWithCustomDeepLinkWithPlaceholdersAndValidAllowedValuesRegistration() {
        val sourceFiles = listOf(
            customAnnotationPlaceholderWithValidAllowedElementsInSchemeHostAppLink,
            module,
            sampleActivityWithOnlyCustomPlaceholderDeepLink,
            fakeBaseDeeplinkDelegate
        )
        val results = listOf(
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                useKsp = false,
            ),
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                useKsp = true,
            )
        )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = listOf(
                DeepLinkEntry(
                    uriTemplate = "http{scheme(|s)}://{host(|www.)}example.com/deepLink",
                    className = "com.example.SampleActivity",
                    method = null
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
                    return "\u0001\u0001\u0000\u0000\u0000\u0000\u0000\u009br\u0012\u0010\u0000\u0000\u0000\u0000\u0000\u0083http{scheme(|s)}\u0014\u0018\u0000\u0000\u0000\u0000\u0000c{host(|www.)}example.com\b\b\u0000S\u0000\u0000\u0000\u0000deepLink\u00004http{scheme(|s)}://{host(|www.)}example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000";
                  }
                }
            
                    """.trimIndent()
            )
        )
    }

    @Test
    fun testIncrementalProcessorWithCustomDeepLinkWithPlaceholdersAndDupliateAllowedValuesRegistration() {
        val sourceFiles = listOf(
            customAnnotationPlaceholderWithDuplicateAllowedElementsInSchemeHostAppLink,
            module,
            sampleActivityWithOnlyCustomPlaceholderDeepLink,
            fakeBaseDeeplinkDelegate
        )
        val results = listOf(
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                useKsp = false,
            ),
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                useKsp = true,
            )
        )
        assertCompileError(
            results,
            "Only one allowed placeholder values section allowed per placeholder."
        )
    }

    @Test
    fun testIncrementalProcessorWithCustomDeepLinkWithPlaceholdersAndAllowedValuesNotAtEndRegistration() {
        val sourceFiles = listOf(
            customAnnotationPlaceholderWithValidElementsNotAtEndInSchemeHostAppLink,
            module,
            sampleActivityWithOnlyCustomPlaceholderDeepLink,
            fakeBaseDeeplinkDelegate
        )
        val results = listOf(
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                useKsp = false,
            ),
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                useKsp = true,
            )
        )
        assertCompileError(
            results,
            "Allowed placeholder values must be last in placeholder."
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
                useKsp = false,
            ),
            compileIncremental(
                sourceFiles = sourceFiles,
                useKsp = true,
            )
        )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = emptyList(),
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
                    return "\u0001\u0001\u0000\u0000\u0000\u0000\u0000\u0000r";
                  }
                }
                
                    """.trimIndent()
            )
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
                useKsp = false,
            ),
            compileIncremental(
                sourceFiles = sourceFiles,
                useKsp = true,
            )
        )
        assertCompileError(
            results,
            "Unable to find annotation 'com.example.AppDeepLink' you must update " +
                "'deepLink.customAnnotations' within the build.gradle"
        )
    }

    @Test
    fun testInnerClassJavaDeeplink() {
        val sourceFiles = listOf(
            customAnnotationAppLink,
            sampleActivityWithInnerClassDeeplinkJava,
            module,
            fakeBaseDeeplinkDelegate,
        )
        val results = listOf(
            compileIncremental(
                sourceFiles = sourceFiles,
                useKsp = false,
            ),
            // Need to disable for now because of bug in compile testing lib
            // https://github.com/tschuchortdev/kotlin-compile-testing/issues/105
//            compileIncremental(
//                sourceFiles = sourceFiles,
//                customDeepLinks = null,
//                useKsp = true,
//            )
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
                        return "\u0001\u0001\u0000\u0000\u0000\u0000\u0000£r\u0002\u0006\u0000\u0000\u0000\u0000\u0000\u0095airbnb\u0004\u000b\u0000\u0000\u0000\u0000\u0000\u0082example.com\b\u0012\u0000h\u0000\u0000\u0000\u0000innerClassDeeplink\u0000'airbnb://example.com/innerClassDeeplink\u0000%com.example.SampleActivity${"\$"}InnerClass\u0017intentForDeepLinkMethod";
                      }
                    }

                    """.trimIndent()
            )
        )
    }

    @Test
    fun testInnerClassKotlinDeeplink() {
        val sourceFiles = listOf(
            customAnnotationAppLink,
            sampleActivityWithInnerClassDeeplinkKotlin,
            module,
            fakeBaseDeeplinkDelegate,
        )
        val results = listOf(
            compileIncremental(
                sourceFiles = sourceFiles,
                useKsp = false,
            ),
            compileIncremental(
                sourceFiles = sourceFiles,
                useKsp = true,
            )
        )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = listOf(
                DeepLinkEntry(
                    uriTemplate = "airbnb://example.com/innerClassDeeplink",
                    className = "com.example.SampleActivity\$InnerClass\$DeepLinks",
                    method = "intentForDeepLinkMethod"
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
                        return "\u0001\u0001\u0000\u0000\u0000\u0000\u0000­r\u0002\u0006\u0000\u0000\u0000\u0000\u0000\u009fairbnb\u0004\u000b\u0000\u0000\u0000\u0000\u0000\u008cexample.com\b\u0012\u0000r\u0000\u0000\u0000\u0000innerClassDeeplink\u0000'airbnb://example.com/innerClassDeeplink\u0000/com.example.SampleActivity${"$"}InnerClass${"$"}DeepLinks\u0017intentForDeepLinkMethod";
                      }
                    }

                    """.trimIndent()
            )
        )
    }
}
