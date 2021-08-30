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
            import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
            import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
            import java.time.LocalDate
            @PlaceholderDeepLink("pathSegment/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}/{path_segment_variable_4}/{path_segment_variable_5}/{path_segment_variable_6}/{path_segment_variable_7}/{path_segment_variable_8}?queryParam1={query_param_1}&queryParam2={query_param_2}&queryParam3={query_param_3}&queryParam4={query_param_4}&queryParam5={query_param_5}&queryParam6={query_param_6}&queryParam7={query_param_7}&queryParam8={query_param_8}")
            class TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs>() {
                override fun handleDeepLink(parameters: TestDeepLinkHandlerDeepLinkArgs) {
                    TODO("Not yet implemented")
                }
            }
            data class TestDeepLinkHandlerDeepLinkArgs(
                // path params can be non null
                @DeeplinkParam(name = "host", type = DeepLinkParamType.Path ) val host: String,
                @DeeplinkParam(name = "scheme", type = DeepLinkParamType.Path) val scheme: String,
                @DeeplinkParam(name = "path_segment_variable_1", type = DeepLinkParamType.Path ) val byte: Byte,
                @DeeplinkParam(name = "path_segment_variable_2", type = DeepLinkParamType.Path) val short: Short,
                @DeeplinkParam(name = "path_segment_variable_3", type = DeepLinkParamType.Path) val int: Int,
                @DeeplinkParam(name = "path_segment_variable_4", type = DeepLinkParamType.Path ) val long: Long,
                @DeeplinkParam(name = "path_segment_variable_5", type = DeepLinkParamType.Path) val float: Float,
                @DeeplinkParam(name = "path_segment_variable_6", type = DeepLinkParamType.Path) val double: Double,
                @DeeplinkParam(name = "path_segment_variable_7", type = DeepLinkParamType.Path) val boolean: Boolean,
                @DeeplinkParam(name = "path_segment_variable_8", type = DeepLinkParamType.Path) val String: String,
                @DeeplinkParam(name = "queryParam1", type = DeepLinkParamType.Query ) val byteQuery: Byte?,
                @DeeplinkParam(name = "queryParam2", type = DeepLinkParamType.Query) val shortQuery: Short?,
                @DeeplinkParam(name = "queryParam3", type = DeepLinkParamType.Query) val intQuery: Int?,
                @DeeplinkParam(name = "queryParam4", type = DeepLinkParamType.Query ) val longQuery: Long?,
                @DeeplinkParam(name = "queryParam5", type = DeepLinkParamType.Query) val floatQuery: Float?,
                @DeeplinkParam(name = "queryParam6", type = DeepLinkParamType.Query) val doubleQuery: Double?,
                @DeeplinkParam(name = "queryParam7", type = DeepLinkParamType.Query) val booleanQuery: Boolean?,
                @DeeplinkParam(name = "queryParam8", type = DeepLinkParamType.Query) val StringQuery: String?
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
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "example://example.com/deepLink",
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
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "http{scheme}://{host}example.com/deepLink",
                    className = "com.example.SampleActivity",
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
                DeepLinkEntry.MethodDeeplinkEntry(
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
                DeepLinkEntry.MethodDeeplinkEntry(
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
                DeepLinkEntry.HandlerDeepLinkEntry(
                    uriTemplate = "http{scheme}://{host}example.com/pathSegment/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}/{path_segment_variable_4}/{path_segment_variable_5}/{path_segment_variable_6}/{path_segment_variable_7}/{path_segment_variable_8}?queryParam1={query_param_1}&queryParam2={query_param_2}&queryParam3={query_param_3}&queryParam4={query_param_4}&queryParam5={query_param_5}&queryParam6={query_param_6}&queryParam7={query_param_7}&queryParam8={query_param_8}",
                    className = "com.example.TestDeepLinkHandler"
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
                        return "\u0001\u0001\u0000\u0000\u0000\u0000\u0003Ir\u0012\f\u0000\u0000\u0000\u0000\u00035http{scheme}\u0014\u0011\u0000\u0000\u0000\u0000\u0003\u001c{host}example.com\b\u000b\u0000\u0000\u0000\u0000\u0003\tpathSegment\u0018\u0019\u0000\u0000\u0000\u0000\u0002è{path_segment_variable_1}\u0018\u0019\u0000\u0000\u0000\u0000\u0002Ç{path_segment_variable_2}\u0018\u0019\u0000\u0000\u0000\u0000\u0002¦{path_segment_variable_3}\u0018\u0019\u0000\u0000\u0000\u0000\u0002\u0085{path_segment_variable_4}\u0018\u0019\u0000\u0000\u0000\u0000\u0002d{path_segment_variable_5}\u0018\u0019\u0000\u0000\u0000\u0000\u0002C{path_segment_variable_6}\u0018\u0019\u0000\u0000\u0000\u0000\u0002\"{path_segment_variable_7}\u0018\u0019\u0002\u0001\u0000\u0000\u0000\u0000{path_segment_variable_8}\u0002\u0001Ühttp{scheme}://{host}example.com/pathSegment/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}/{path_segment_variable_4}/{path_segment_variable_5}/{path_segment_variable_6}/{path_segment_variable_7}/{path_segment_variable_8}?queryParam1={query_param_1}&queryParam2={query_param_2}&queryParam3={query_param_3}&queryParam4={query_param_4}&queryParam5={query_param_5}&queryParam6={query_param_6}&queryParam7={query_param_7}&queryParam8={query_param_8}\u0000\u001fcom.example.TestDeepLinkHandler\u0000";
                      }
                    }

                    """.trimIndent()
            )
        )
    }
}
