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
    private val sampleActivityWithStandardAndCustomDeepLink = Source.JavaSource(
        "com.example.SampleActivity",
        """
                 package com.example;
                 import com.airbnb.deeplinkdispatch.DeepLink;
                 @DeepLink("airbnb://example.com/deepLink")
                 @AppDeepLink({"example.com/deepLink","example.com/another"})
                 public class SampleActivity extends android.app.Activity {
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
                 public class SampleActivity extends android.app.Activity {
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
                 class SampleActivity : android.app.Activity() {
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

    private val sampleDeeplinkHandler = Source.KotlinSource(
        "SampleDeeplinkHandler.kt",
        """
            package com.example
            import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
            import com.airbnb.deeplinkdispatch.handler.PathParam
            import com.airbnb.deeplinkdispatch.handler.QueryParam
            import java.time.LocalDate
            @PlaceholderDeepLink("pathSegment/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}?queryParam={query_param_1}")
            class TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs>() {
                override fun handleDeepLink(parameters: TestDeepLinkHandlerDeepLinkArgs) {
                    TODO("Not yet implemented")
                }
            }
            data class TestDeepLinkHandlerDeepLinkArgs(
                // path params can be non null
                @PathParam("path_segment_variable_1") val uuid: Long,
                @PathParam("path_segment_variable_2") val name: String,
                @PathParam("path_segment_variable_3") val date: LocalDate,
                @QueryParam("show_taxes") val boolean: Boolean?
            )
            """
    )

    private val sampleWrongClassTypeAnnotated = Source.KotlinSource(
        "SampleDeeplinkHandler.kt",
        """
            package com.example
            import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
            import com.airbnb.deeplinkdispatch.handler.PathParam
            import com.airbnb.deeplinkdispatch.handler.QueryParam
            import java.time.LocalDate
            @PlaceholderDeepLink("pathSegment/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}?queryParam={query_param_1}")
            class TestDeepLinkHandler {
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
                 public class SampleActivity extends android.app.Activity {
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
                 public class SampleActivity extends android.app.Activity {
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
                    type = MatchType.Activity,
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
                    return "\u0001\u0001\u0000\u0000\u0000\u0000\u0000pr\u0002\u0007\u0000\u0000\u0000\u0000\u0000aexample\u0004\u000b\u0000\u0000\u0000\u0000\u0000Nexample.com\b\b\u0000>\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001eexample://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000";
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
                    type = MatchType.Activity,
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
                    return "\u0001\u0001\u0000\u0000\u0000\u0000\u0000\u0086r\u0012\f\u0000\u0000\u0000\u0000\u0000rhttp{scheme}\u0014\u0011\u0000\u0000\u0000\u0000\u0000Y{host}example.com\b\b\u0000I\u0000\u0000\u0000\u0000deepLink\u0000\u0000)http{scheme}://{host}example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000";
                  }
                }
            
                    """.trimIndent()
            )
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
                    type = MatchType.Method,
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
                        return "\u0001\u0001\u0000\u0000\u0000\u0000\u0000¤r\u0002\u0006\u0000\u0000\u0000\u0000\u0000\u0096airbnb\u0004\u000b\u0000\u0000\u0000\u0000\u0000\u0083example.com\b\u0012\u0000i\u0000\u0000\u0000\u0000innerClassDeeplink\u0001\u0000'airbnb://example.com/innerClassDeeplink\u0000%com.example.SampleActivity${"\$"}InnerClass\u0017intentForDeepLinkMethod";
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
                    type = MatchType.Method,
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
                        return "\u0001\u0001\u0000\u0000\u0000\u0000\u0000®r\u0002\u0006\u0000\u0000\u0000\u0000\u0000 airbnb\u0004\u000b\u0000\u0000\u0000\u0000\u0000\u008dexample.com\b\u0012\u0000s\u0000\u0000\u0000\u0000innerClassDeeplink\u0001\u0000'airbnb://example.com/innerClassDeeplink\u0000/com.example.SampleActivity${"\$"}InnerClass${"\$"}DeepLinks\u0017intentForDeepLinkMethod";
                      }
                    }

                    """.trimIndent()
            )
        )
    }

    @Test
    fun testWrongClassTypeAnnotated() {
        val sourceFiles = listOf(
            customAnnotationPlaceholderInSchemeHostAppLink,
            sampleWrongClassTypeAnnotated,
            module,
            fakeBaseDeeplinkDelegate,
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
            "Only classes inheriting from either 'android.app.Activity' or " +
                "'com.airbnb.deeplinkdispatch.handler.DeepLinkHandler' can be annotated with" +
                " @DeepLink or another custom deep link annotation."
        )
    }

    @Test
    fun testDeeplinkHandler() {
        val sourceFiles = listOf(
            customAnnotationPlaceholderInSchemeHostAppLink,
            sampleDeeplinkHandler,
            module,
            fakeBaseDeeplinkDelegate,
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
                    MatchType.Handler,
                    uriTemplate = "http{scheme}://{host}example.com/pathSegment/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}?queryParam={query_param_1}",
                    className = "com.example.TestDeepLinkHandler",
                    method = null
                )
            ),
            generatedFiles = mapOf(
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
                        return "\u0001\u0001\u0000\u0000\u0000\u0000\u0001]r\u0012\f\u0000\u0000\u0000\u0000\u0001Ihttp{scheme}\u0014\u0011\u0000\u0000\u0000\u0000\u00010{host}example.com\b\u000b\u0000\u0000\u0000\u0000\u0001\u001dpathSegment\u0018\u0019\u0000\u0000\u0000\u0000\u0000ü{path_segment_variable_1}\u0018\u0019\u0000\u0000\u0000\u0000\u0000Û{path_segment_variable_2}\u0018\u0019\u0000º\u0000\u0000\u0000\u0000{path_segment_variable_3}\u0002\u0000\u0095http{scheme}://{host}example.com/pathSegment/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}?queryParam={query_param_1}\u0000\u001fcom.example.TestDeepLinkHandler\u0000";
                      }
                    }

                    """.trimIndent()
            )
        )
    }
}
