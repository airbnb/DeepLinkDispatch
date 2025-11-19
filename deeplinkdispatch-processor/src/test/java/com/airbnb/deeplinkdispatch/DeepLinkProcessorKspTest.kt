package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.test.Source
import com.squareup.kotlinpoet.javapoet.KotlinPoetJavaPoetPreview
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.Test
import java.io.File

@ExperimentalCompilerApi
@KotlinPoetJavaPoetPreview
class DeepLinkProcessorKspTest : BaseDeepLinkProcessorTest() {
    private val customWebDeepLinkJava =
        Source.JavaSource(
            "com.example.WebDeepLink",
            """
                package com.example;
                import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                @DeepLinkSpec(prefix = { "http://", "https://"})
                public @interface WebDeepLink {
                    String[] value();
                }
                """,
        )

    private val customManifestGenWebDeepLinkJava =
        Source.JavaSource(
            "com.example.ManifestGenWebDeepLink",
            """
                package com.example;
                import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                @DeepLinkSpec(prefix = { "http{scheme_suffix(|s)}://example.com/" }, activityClassFqn = "com.example.SampleActivity")
                public @interface ManifestGenWebDeepLink {
                    String[] value();
                }
                """,
        )

    private val customAppDeepLinkJava =
        Source.JavaSource(
            "com.example.AppDeepLink",
            """
                        package com.example;
                        import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                        @DeepLinkSpec(prefix = { "example://" })
                        public @interface AppDeepLink {
                            String[] value();
                        }
                        """,
        )

    private val customManifestGenAppDeepLinkJava =
        Source.JavaSource(
            "com.example.ManifestGenAppDeepLink",
            """
                        package com.example;
                        import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                        @DeepLinkSpec(prefix = { "example://" }, activityClassFqn = "com.example.SampleActivity")
                        public @interface ManifestGenAppDeepLink {
                            String[] value();
                        }
                        """,
        )

    // Need to define here as the original one is define an in android lib we cannot depend on here.
    private val deeplinkHandlerInterface =
        Source.KotlinSource(
            "DeepLinkHandler.kt",
            """
            package com.airbnb.deeplinkdispatch.handler
            import android.content.Context
            interface DeepLinkHandler<T> {
                fun handleDeepLink(context: Context, parameters: T)
            }
            """.trimIndent(),
        )

    @Test
    fun testWithNoClass() {
        val sampleDeeplinkMethod =
            Source.KotlinSource(
                "SampleNoClassFile.kt",
                """
                package com.example
                import android.content.Context
                import android.content.Intent
                import com.airbnb.deeplinkdispatch.DeepLink
                import com.airbnb.deeplinkdispatch.DeepLinkHandler
                
                @DeepLinkHandler( SampleModule::class )
                class Activity {}

                @DeepLink("airbnb://example.com/noClassTest")
                fun intentForInquiryDetailFragment(context: Context) = Intent()
                """.trimIndent(),
            )
        val sourceFiles =
            listOf(
                module,
                sampleDeeplinkMethod,
                fakeBaseDeeplinkDelegateJava,
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    useKsp = false,
                ),
                compileIncremental(
                    sourceFiles = sourceFiles,
                    useKsp = true,
                    incrementalFlag = false,
                ),
            )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries =
                listOf(
                    DeepLinkEntry.MethodDeeplinkEntry(
                        uriTemplate = "airbnb://example.com/noClassTest",
                        className = "com.example.SampleNoClassFileKt",
                        method = "intentForInquiryDetailFragment",
                    ),
                ),
            generatedSourceFiles =
                mapOf(
                    "DeepLinkDelegate.java" to
                        """
                        package com.example;

                        import com.airbnb.deeplinkdispatch.BaseDeepLinkDelegate;
                        import com.airbnb.deeplinkdispatch.DeepLinkUri;
                        import com.airbnb.deeplinkdispatch.handler.TypeConverters;
                        import java.lang.Integer;
                        import java.lang.String;
                        import java.lang.reflect.Type;
                        import java.util.Arrays;
                        import java.util.Map;
                        import kotlin.jvm.functions.Function0;
                        import kotlin.jvm.functions.Function3;
                        import org.jetbrains.annotations.NotNull;
                        
                        public final class DeepLinkDelegate extends BaseDeepLinkDelegate {
                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry) {
                            super(Arrays.asList(
                              sampleModuleRegistry)
                            );
                          }
                        
                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry,
                              @NotNull Map<String, String> configurablePathSegmentReplacements) {
                            super(Arrays.asList(
                              sampleModuleRegistry),
                              configurablePathSegmentReplacements
                            );
                          }
                        
                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry,
                              @NotNull Map<String, String> configurablePathSegmentReplacements,
                              @NotNull Function0<TypeConverters> typeConverters) {
                            super(Arrays.asList(
                              sampleModuleRegistry),
                              configurablePathSegmentReplacements,
                              typeConverters
                            );
                          }
                        
                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry,
                              @NotNull Map<String, String> configurablePathSegmentReplacements,
                              @NotNull Function0<TypeConverters> typeConverters,
                              @NotNull Function3<DeepLinkUri, Type, ? super String, Integer> typeConversionErrorNullable,
                              @NotNull Function3<DeepLinkUri, Type, ? super String, Integer> typeConversionErrorNonNullable) {
                            super(Arrays.asList(
                              sampleModuleRegistry),
                              configurablePathSegmentReplacements,
                              typeConverters,
                              null,
                              typeConversionErrorNullable,
                              typeConversionErrorNonNullable
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
                            return "\u0001\u0000\u0001\u0000\u0000\u0000\u0000\u0000\u009ar\u0002\u0000\u0006\u0000\u0000\u0000\u0000\u0000\u008bairbnb\u0004\u0000\u000b\u0000\u0000\u0000\u0000\u0000wexample.com\b\u0000\u000b\u0000c\u0000\u0000\u0000\u0000noClassTest\u0001\u0000 airbnb://example.com/noClassTest\u0000\u001fcom.example.SampleNoClassFileKt\u001eintentForInquiryDetailFragment";
                          }
                        }
                        
                        """.trimIndent(),
                ),
        )
    }

    @Test
    fun testWithKotlinSource() {
        val sampleActivityKotlin =
            Source.KotlinSource(
                "SampleActivity.kt",
                """
                 package com.example
                 import com.airbnb.deeplinkdispatch.DeepLink
                 import com.airbnb.deeplinkdispatch.DeepLinkHandler
                 import android.content.Context
                 import androidx.core.app.TaskStackBuilder
                 import android.content.Intent
                 import com.example.SampleModule
                 @DeepLink("airbnb://example.com/deepLink")
                 @AppDeepLink( "example.com/deepLink","example.com/another")
                 @WebDeepLink(value = arrayOf("example.com/deepLink","example.com/another"))
                 @DeepLinkHandler( SampleModule::class )
                 class Activity : android.app.Activity() {
                        object DeepLinks {
                            @DeepLink("airbnb://intentMethod/{var1}/{var2}")
                            @JvmStatic
                            fun intentFromTwoPathWithTwoParams(context: Context) = Intent()
                            
                            @DeepLink("airbnb://taskStackBuilderMethod/{arbitraryNumber}")
                            @JvmStatic
                            fun deeplinkOneParameter(context: Context) = TaskStackBuilder.create(context);
                            
                            
                            @WebDeepLink( "example.com/method1","example.com/method2")
                            @JvmStatic
                            fun webLinkMethod(context: Context) = TaskStackBuilder.create(context)
                        }
                 }
                 """,
            )
        val sourceFiles =
            listOf(
                customAppDeepLinkJava,
                customWebDeepLinkJava,
                module,
                sampleActivityKotlin,
                fakeBaseDeeplinkDelegateJava,
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.AppDeepLink", "com.example.WebDeepLink"),
                    useKsp = false,
                ),
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.AppDeepLink", "com.example.WebDeepLink"),
                    useKsp = true,
                    incrementalFlag = false,
                ),
            )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries =
                listOf(
                    DeepLinkEntry.ActivityDeeplinkEntry(
                        uriTemplate = "airbnb://example.com/deepLink",
                        className = "com.example.Activity",
                    ),
                    DeepLinkEntry.MethodDeeplinkEntry(
                        uriTemplate = "airbnb://intentMethod/{var1}/{var2}",
                        className = "com.example.Activity\$DeepLinks",
                        method = "intentFromTwoPathWithTwoParams",
                    ),
                    DeepLinkEntry.MethodDeeplinkEntry(
                        uriTemplate = "airbnb://taskStackBuilderMethod/{arbitraryNumber}",
                        className = "com.example.Activity\$DeepLinks",
                        method = "deeplinkOneParameter",
                    ),
                    DeepLinkEntry.ActivityDeeplinkEntry(
                        uriTemplate = "example://example.com/another",
                        className = "com.example.Activity",
                    ),
                    DeepLinkEntry.ActivityDeeplinkEntry(
                        uriTemplate = "example://example.com/deepLink",
                        className = "com.example.Activity",
                    ),
                    DeepLinkEntry.ActivityDeeplinkEntry(
                        uriTemplate = "http://example.com/another",
                        className = "com.example.Activity",
                    ),
                    DeepLinkEntry.ActivityDeeplinkEntry(
                        uriTemplate = "http://example.com/deepLink",
                        className = "com.example.Activity",
                    ),
                    DeepLinkEntry.MethodDeeplinkEntry(
                        uriTemplate = "http://example.com/method1",
                        className = "com.example.Activity\$DeepLinks",
                        method = "webLinkMethod",
                    ),
                    DeepLinkEntry.MethodDeeplinkEntry(
                        uriTemplate = "http://example.com/method2",
                        className = "com.example.Activity\$DeepLinks",
                        method = "webLinkMethod",
                    ),
                    DeepLinkEntry.ActivityDeeplinkEntry(
                        uriTemplate = "https://example.com/another",
                        className = "com.example.Activity",
                    ),
                    DeepLinkEntry.ActivityDeeplinkEntry(
                        uriTemplate = "https://example.com/deepLink",
                        className = "com.example.Activity",
                    ),
                    DeepLinkEntry.MethodDeeplinkEntry(
                        uriTemplate = "https://example.com/method1",
                        className = "com.example.Activity\$DeepLinks",
                        method = "webLinkMethod",
                    ),
                    DeepLinkEntry.MethodDeeplinkEntry(
                        uriTemplate = "https://example.com/method2",
                        className = "com.example.Activity\$DeepLinks",
                        method = "webLinkMethod",
                    ),
                ),
            generatedSourceFiles =
                mapOf(
                    "DeepLinkDelegate.java" to
                        """
                        package com.example;

                        import com.airbnb.deeplinkdispatch.BaseDeepLinkDelegate;
                        import com.airbnb.deeplinkdispatch.DeepLinkUri;
                        import com.airbnb.deeplinkdispatch.handler.TypeConverters;
                        import java.lang.Integer;
                        import java.lang.String;
                        import java.lang.reflect.Type;
                        import java.util.Arrays;
                        import java.util.Map;
                        import kotlin.jvm.functions.Function0;
                        import kotlin.jvm.functions.Function3;
                        import org.jetbrains.annotations.NotNull;
                        
                        public final class DeepLinkDelegate extends BaseDeepLinkDelegate {
                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry) {
                            super(Arrays.asList(
                              sampleModuleRegistry)
                            );
                          }
                        
                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry,
                              @NotNull Map<String, String> configurablePathSegmentReplacements) {
                            super(Arrays.asList(
                              sampleModuleRegistry),
                              configurablePathSegmentReplacements
                            );
                          }
                        
                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry,
                              @NotNull Map<String, String> configurablePathSegmentReplacements,
                              @NotNull Function0<TypeConverters> typeConverters) {
                            super(Arrays.asList(
                              sampleModuleRegistry),
                              configurablePathSegmentReplacements,
                              typeConverters
                            );
                          }
                        
                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry,
                              @NotNull Map<String, String> configurablePathSegmentReplacements,
                              @NotNull Function0<TypeConverters> typeConverters,
                              @NotNull Function3<DeepLinkUri, Type, ? super String, Integer> typeConversionErrorNullable,
                              @NotNull Function3<DeepLinkUri, Type, ? super String, Integer> typeConversionErrorNonNullable) {
                            super(Arrays.asList(
                              sampleModuleRegistry),
                              configurablePathSegmentReplacements,
                              typeConverters,
                              null,
                              typeConversionErrorNullable,
                              typeConversionErrorNonNullable
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
                            return "\u0001\u0000\u0001\u0000\u0000\u0000\u0000\u0005 r\u0002\u0000\u0006\u0000\u0000\u0000\u0000\u0001\u0096airbnb\u0004\u0000\u000b\u0000\u0000\u0000\u0000\u0000Hexample.com\b\u0000\b\u00007\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001dairbnb://example.com/deepLink\u0000\u0014com.example.Activity\u0000\u0004\u0000\f\u0000\u0000\u0000\u0000\u0000\u0083intentmethod\u0018\u0000\u0006\u0000\u0000\u0000\u0000\u0000t{var1}\u0018\u0000\u0006\u0000e\u0000\u0000\u0000\u0000{var2}\u0001\u0000#airbnb://intentMethod/{var1}/{var2}\u0000\u001ecom.example.Activity${"\$"}DeepLinks\u001eintentFromTwoPathWithTwoParams\u0004\u0000\u0016\u0000\u0000\u0000\u0000\u0000\u0083taskstackbuildermethod\u0018\u0000\u0011\u0000i\u0000\u0000\u0000\u0000{arbitraryNumber}\u0001\u00001airbnb://taskStackBuilderMethod/{arbitraryNumber}\u0000\u001ecom.example.Activity${"\$"}DeepLinks\u0014deeplinkOneParameter\u0002\u0000\u0007\u0000\u0000\u0000\u0000\u0000¤example\u0004\u0000\u000b\u0000\u0000\u0000\u0000\u0000\u0090example.com\b\u0000\u0007\u00007\u0000\u0000\u0000\u0000another\u0000\u0000\u001dexample://example.com/another\u0000\u0014com.example.Activity\u0000\b\u0000\b\u00008\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001eexample://example.com/deepLink\u0000\u0014com.example.Activity\u0000\u0002\u0000\u0004\u0000\u0000\u0000\u0000\u0001Thttp\u0004\u0000\u000b\u0000\u0000\u0000\u0000\u0001@example.com\b\u0000\u0007\u00004\u0000\u0000\u0000\u0000another\u0000\u0000\u001ahttp://example.com/another\u0000\u0014com.example.Activity\u0000\b\u0000\b\u00005\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001bhttp://example.com/deepLink\u0000\u0014com.example.Activity\u0000\b\u0000\u0007\u0000K\u0000\u0000\u0000\u0000method1\u0001\u0000\u001ahttp://example.com/method1\u0000\u001ecom.example.Activity${"\$"}DeepLinks\rwebLinkMethod\b\u0000\u0007\u0000K\u0000\u0000\u0000\u0000method2\u0001\u0000\u001ahttp://example.com/method2\u0000\u001ecom.example.Activity${"\$"}DeepLinks\rwebLinkMethod\u0002\u0000\u0005\u0000\u0000\u0000\u0000\u0001Xhttps\u0004\u0000\u000b\u0000\u0000\u0000\u0000\u0001Dexample.com\b\u0000\u0007\u00005\u0000\u0000\u0000\u0000another\u0000\u0000\u001bhttps://example.com/another\u0000\u0014com.example.Activity\u0000\b\u0000\b\u00006\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001chttps://example.com/deepLink\u0000\u0014com.example.Activity\u0000\b\u0000\u0007\u0000L\u0000\u0000\u0000\u0000method1\u0001\u0000\u001bhttps://example.com/method1\u0000\u001ecom.example.Activity${"\$"}DeepLinks\rwebLinkMethod\b\u0000\u0007\u0000L\u0000\u0000\u0000\u0000method2\u0001\u0000\u001bhttps://example.com/method2\u0000\u001ecom.example.Activity${"\$"}DeepLinks\rwebLinkMethod";
                          }
                        }
                        
                        """.trimIndent(),
                ),
        )
    }

    @Test
    fun testWithKotlinSourceAndManifestGeneration() {
        val sampleActivityKotlin =
            Source.KotlinSource(
                "SampleActivity.kt",
                """
                 package com.example
                 import com.airbnb.deeplinkdispatch.DeepLink
                 import com.airbnb.deeplinkdispatch.DeepLinkHandler
                 import android.content.Context
                 import androidx.core.app.TaskStackBuilder
                 import android.content.Intent
                 import com.example.SampleModule
                 @ManifestGenAppDeepLink( "host/deepLink","host/another")
                 @ManifestGenWebDeepLink(value = arrayOf("deepLink","another"))
                 @DeepLink(value = "http{scheme_suffix(|s)}://example.com/direct", activityClassFqn = "com.example.SampleActivity")
                 @DeepLinkHandler( SampleModule::class )
                 class SampleActivity : android.app.Activity() {
                        object DeepLinks {
                            @ManifestGenWebDeepLink( "method1/test","method2/test")
                            @JvmStatic
                            fun webLinkMethod(context: Context) = TaskStackBuilder.create(context)
                        }
                 }
                 """,
            )
        val sourceFiles =
            listOf(
                customManifestGenAppDeepLinkJava,
                customManifestGenWebDeepLinkJava,
                module,
                sampleActivityKotlin,
                fakeBaseDeeplinkDelegateJava,
            )
        // This is not great but we do not have access to the path that kotlin compile testing
        // be using either from here nor inside the compiler.g
        val generateManifestFile =
            File(
                System.getProperty("java.io.tmpdir"),
                "GeneratedAndroidManifest_${System.currentTimeMillis()}.xml",
            )
        val results =
            // Manifest generation only works with KSP so only test this with KSP
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.ManifestGenAppDeepLink", "com.example.ManifestGenWebDeepLink"),
                    useKsp = true,
                    incrementalFlag = false,
                    additionalArguments =
                        mutableMapOf(
                            "deepLinkManifestGenMetadata.output" to generateManifestFile.canonicalPath,
                        ),
                ),
            )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = emptyList(), // Irrelevant for KSP only tets
            generatedSourceFiles =
                mapOf(
                    "DeepLinkDelegate.java" to
                        """
                        package com.example;

                        import com.airbnb.deeplinkdispatch.BaseDeepLinkDelegate;
                        import com.airbnb.deeplinkdispatch.DeepLinkUri;
                        import com.airbnb.deeplinkdispatch.handler.TypeConverters;
                        import java.lang.Integer;
                        import java.lang.String;
                        import java.lang.reflect.Type;
                        import java.util.Arrays;
                        import java.util.Map;
                        import kotlin.jvm.functions.Function0;
                        import kotlin.jvm.functions.Function3;
                        import org.jetbrains.annotations.NotNull;
                        
                        public final class DeepLinkDelegate extends BaseDeepLinkDelegate {
                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry) {
                            super(Arrays.asList(
                              sampleModuleRegistry)
                            );
                          }
                        
                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry,
                              @NotNull Map<String, String> configurablePathSegmentReplacements) {
                            super(Arrays.asList(
                              sampleModuleRegistry),
                              configurablePathSegmentReplacements
                            );
                          }
                        
                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry,
                              @NotNull Map<String, String> configurablePathSegmentReplacements,
                              @NotNull Function0<TypeConverters> typeConverters) {
                            super(Arrays.asList(
                              sampleModuleRegistry),
                              configurablePathSegmentReplacements,
                              typeConverters
                            );
                          }
                        
                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry,
                              @NotNull Map<String, String> configurablePathSegmentReplacements,
                              @NotNull Function0<TypeConverters> typeConverters,
                              @NotNull Function3<DeepLinkUri, Type, ? super String, Integer> typeConversionErrorNullable,
                              @NotNull Function3<DeepLinkUri, Type, ? super String, Integer> typeConversionErrorNonNullable) {
                            super(Arrays.asList(
                              sampleModuleRegistry),
                              configurablePathSegmentReplacements,
                              typeConverters,
                              null,
                              typeConversionErrorNullable,
                              typeConversionErrorNonNullable
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
                            return "\u0001\u0000\u0001\u0000\u0000\u0000\u0000\u0003\u0002r\u0002\u0000\u0007\u0000\u0000\u0000\u0000\u0000\u009bexample\u0004\u0000\u0004\u0000\u0000\u0000\u0000\u0000\u008ehost\b\u0000\u0007\u00006\u0000\u0000\u0000\u0000another\u0000\u0000\u0016example://host/another\u0000\u001acom.example.SampleActivity\u0000\b\u0000\b\u00007\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u0017example://host/deepLink\u0000\u001acom.example.SampleActivity\u0000\u0012\u0000\u0017\u0000\u0000\u0000\u0000\u00027http{scheme_suffix(|s)}\u0004\u0000\u000b\u0000\u0000\u0000\u0000\u0002#example.com\b\u0000\u0007\u0000M\u0000\u0000\u0000\u0000another\u0000\u0000-http{scheme_suffix(|s)}://example.com/another\u0000\u001acom.example.SampleActivity\u0000\b\u0000\b\u0000N\u0000\u0000\u0000\u0000deepLink\u0000\u0000.http{scheme_suffix(|s)}://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\b\u0000\u0006\u0000L\u0000\u0000\u0000\u0000direct\u0000\u0000,http{scheme_suffix(|s)}://example.com/direct\u0000\u001acom.example.SampleActivity\u0000\b\u0000\u0007\u0000\u0000\u0000\u0000\u0000vmethod1\b\u0000\u0004\u0000i\u0000\u0000\u0000\u0000test\u0001\u00002http{scheme_suffix(|s)}://example.com/method1/test\u0000${"\$"}com.example.SampleActivity${"\$"}DeepLinks\rwebLinkMethod\b\u0000\u0007\u0000\u0000\u0000\u0000\u0000vmethod2\b\u0000\u0004\u0000i\u0000\u0000\u0000\u0000test\u0001\u00002http{scheme_suffix(|s)}://example.com/method2/test\u0000${"\$"}com.example.SampleActivity${"\$"}DeepLinks\rwebLinkMethod";
                          }
                        }
                        
                        """.trimIndent(),
                ),
            generatedFiles =
                mapOf(
                    generateManifestFile to
                        """
                        <?xml version="1.0" encoding="utf-8"?>
                        <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                            <application>
                                <activity
                                    android:name="com.example.SampleActivity" android:exported="true">
                                    <intent-filter>
                                        <action android:name="android.intent.action.VIEW" />
                                        <category android:name="android.intent.category.DEFAULT" />
                                        <category android:name="android.intent.category.BROWSABLE" />
                                        <data android:scheme="example" />
                                        <data android:host="host" />
                                        <data android:path="/deepLink" />
                                        <data android:path="/another" />
                                    </intent-filter>
                                    <intent-filter>
                                        <action android:name="android.intent.action.VIEW" />
                                        <category android:name="android.intent.category.DEFAULT" />
                                        <category android:name="android.intent.category.BROWSABLE" />
                                        <data android:scheme="http" />
                                        <data android:scheme="https" />
                                        <data android:host="example.com" />
                                        <data android:path="/deepLink" />
                                        <data android:path="/another" />
                                        <data android:path="/direct" />
                                        <data android:path="/method1/test" />
                                        <data android:path="/method2/test" />
                                    </intent-filter>
                                </activity>
                            </application>
                        </manifest>

                        """.trimIndent(),
                ),
        )
    }

    @Test
    fun testWithKotlinSourceMethodAndManifestGeneration() {
        val sampleActivityKotlin =
            Source.KotlinSource(
                "SampleActivity.kt",
                """
                 package com.example
                 import com.airbnb.deeplinkdispatch.DeepLink
                 import com.airbnb.deeplinkdispatch.DeepLinkHandler
                 import android.content.Context
                 import android.content.Intent
                 import com.example.SampleModule
                 @DeepLinkHandler( SampleModule::class )
                 class SampleActivity : android.app.Activity() {
                        @ManifestGenWebDeepLink( "method1","method2")
                        @ManifestGenAppDeepLink( "host/method1","host/method2")
                        @JvmStatic
                        fun webLinkMethod(context: Context) = Intent()
                 }
                 """,
            )
        val sourceFiles =
            listOf(
                customManifestGenAppDeepLinkJava,
                customManifestGenWebDeepLinkJava,
                module,
                sampleActivityKotlin,
                fakeBaseDeeplinkDelegateJava,
            )
        val generateManifestFile =
            File(
                System.getProperty("java.io.tmpdir"),
                "GeneratedAndroidManifest_${System.currentTimeMillis()}.xml",
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.ManifestGenAppDeepLink", "com.example.ManifestGenWebDeepLink"),
                    useKsp = true,
                    incrementalFlag = false,
                    additionalArguments =
                        mutableMapOf(
                            "deepLinkManifestGenMetadata.output" to generateManifestFile.canonicalPath,
                        ),
                ),
            )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = emptyList(),
            generatedSourceFiles =
                mapOf(
                    "DeepLinkDelegate.java" to
                        """
                        package com.example;

                        import com.airbnb.deeplinkdispatch.BaseDeepLinkDelegate;
                        import com.airbnb.deeplinkdispatch.DeepLinkUri;
                        import com.airbnb.deeplinkdispatch.handler.TypeConverters;
                        import java.lang.Integer;
                        import java.lang.String;
                        import java.lang.reflect.Type;
                        import java.util.Arrays;
                        import java.util.Map;
                        import kotlin.jvm.functions.Function0;
                        import kotlin.jvm.functions.Function3;
                        import org.jetbrains.annotations.NotNull;

                        public final class DeepLinkDelegate extends BaseDeepLinkDelegate {
                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry) {
                            super(Arrays.asList(
                              sampleModuleRegistry)
                            );
                          }

                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry,
                              @NotNull Map<String, String> configurablePathSegmentReplacements) {
                            super(Arrays.asList(
                              sampleModuleRegistry),
                              configurablePathSegmentReplacements
                            );
                          }

                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry,
                              @NotNull Map<String, String> configurablePathSegmentReplacements,
                              @NotNull Function0<TypeConverters> typeConverters) {
                            super(Arrays.asList(
                              sampleModuleRegistry),
                              configurablePathSegmentReplacements,
                              typeConverters
                            );
                          }

                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry,
                              @NotNull Map<String, String> configurablePathSegmentReplacements,
                              @NotNull Function0<TypeConverters> typeConverters,
                              @NotNull Function3<DeepLinkUri, Type, ? super String, Integer> typeConversionErrorNullable,
                              @NotNull Function3<DeepLinkUri, Type, ? super String, Integer> typeConversionErrorNonNullable) {
                            super(Arrays.asList(
                              sampleModuleRegistry),
                              configurablePathSegmentReplacements,
                              typeConverters,
                              null,
                              typeConversionErrorNullable,
                              typeConversionErrorNonNullable
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
                            return "\u0001\u0000\u0001\u0000\u0000\u0000\u0000\u0001Ër\u0002\u0000\u0007\u0000\u0000\u0000\u0000\u0000³example\u0004\u0000\u0004\u0000\u0000\u0000\u0000\u0000¦host\b\u0000\u0007\u0000C\u0000\u0000\u0000\u0000method1\u0001\u0000\u0016example://host/method1\u0000\u001acom.example.SampleActivity\rwebLinkMethod\b\u0000\u0007\u0000C\u0000\u0000\u0000\u0000method2\u0001\u0000\u0016example://host/method2\u0000\u001acom.example.SampleActivity\rwebLinkMethod\u0012\u0000\u0017\u0000\u0000\u0000\u0000\u0000èhttp{scheme_suffix(|s)}\u0004\u0000\u000b\u0000\u0000\u0000\u0000\u0000Ôexample.com\b\u0000\u0007\u0000Z\u0000\u0000\u0000\u0000method1\u0001\u0000-http{scheme_suffix(|s)}://example.com/method1\u0000\u001acom.example.SampleActivity\rwebLinkMethod\b\u0000\u0007\u0000Z\u0000\u0000\u0000\u0000method2\u0001\u0000-http{scheme_suffix(|s)}://example.com/method2\u0000\u001acom.example.SampleActivity\rwebLinkMethod";
                          }
                        }

                        """.trimIndent(),
                ),
            generatedFiles =
                mapOf(
                    generateManifestFile to
                        """
                        <?xml version="1.0" encoding="utf-8"?>
                        <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                            <application>
                                <activity
                                    android:name="com.example.SampleActivity" android:exported="true">
                                    <intent-filter>
                                        <action android:name="android.intent.action.VIEW" />
                                        <category android:name="android.intent.category.DEFAULT" />
                                        <category android:name="android.intent.category.BROWSABLE" />
                                        <data android:scheme="http" />
                                        <data android:scheme="https" />
                                        <data android:host="example.com" />
                                        <data android:path="/method1" />
                                        <data android:path="/method2" />
                                    </intent-filter>
                                    <intent-filter>
                                        <action android:name="android.intent.action.VIEW" />
                                        <category android:name="android.intent.category.DEFAULT" />
                                        <category android:name="android.intent.category.BROWSABLE" />
                                        <data android:scheme="example" />
                                        <data android:host="host" />
                                        <data android:path="/method1" />
                                        <data android:path="/method2" />
                                    </intent-filter>
                                </activity>
                            </application>
                        </manifest>

                        """.trimIndent(),
                ),
        )
    }

    @Test
    fun testWithKotlinSourceObjectHandlerAndManifestGeneration() {
        val sampleDeeplinkHandler =
            Source.KotlinSource(
                "SampleDeeplinkHandler.kt",
                """
                 package com.example
                 import com.airbnb.deeplinkdispatch.DeepLink
                 import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                 import android.content.Context
                 import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                 import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                 import com.example.SampleModule
                 @ManifestGenWebDeepLink(value = arrayOf("handler1/{value1}/{value2}","handler2/{value1}/{value2}"))
                 @ManifestGenAppDeepLink( "host/handler1/{value1}/{value2}","host/handler2/{value1}/{value2}")
                 object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs> {
                     override fun handleDeepLink(context: Context, parameters: TestDeepLinkHandlerDeepLinkArgs) {
                         TODO("Not yet implemented")
                     }
                 }
                 data class TestDeepLinkHandlerDeepLinkArgs(
                     @DeeplinkParam(name = "value1", type = DeepLinkParamType.Path ) val value1: String,
                     @DeeplinkParam(name = "value2", type = DeepLinkParamType.Path ) val value2: String,
                 )
                 @DeepLinkHandler( SampleModule::class )
                 class Activity : android.app.Activity() {}
                 """,
            )
        val sourceFiles =
            listOf(
                deeplinkHandlerInterface,
                customManifestGenAppDeepLinkJava,
                customManifestGenWebDeepLinkJava,
                module,
                sampleDeeplinkHandler,
                fakeBaseDeeplinkDelegateJava,
            )
        val generateManifestFile =
            File(
                System.getProperty("java.io.tmpdir"),
                "GeneratedAndroidManifest_${System.currentTimeMillis()}.xml",
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.ManifestGenAppDeepLink", "com.example.ManifestGenWebDeepLink"),
                    useKsp = true,
                    incrementalFlag = false,
                    additionalArguments =
                        mutableMapOf(
                            "deepLinkManifestGenMetadata.output" to generateManifestFile.canonicalPath,
                        ),
                ),
            )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = emptyList(),
            generatedSourceFiles =
                mapOf(
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
                            return "\u0001\u0000\u0001\u0000\u0000\u0000\u0000\u0002\u0083r\u0002\u0000\u0007\u0000\u0000\u0000\u0000\u0001\u000fexample\u0004\u0000\u0004\u0000\u0000\u0000\u0000\u0001\u0002host\b\u0000\b\u0000\u0000\u0000\u0000\u0000phandler1\u0018\u0000\b\u0000\u0000\u0000\u0000\u0000_{value1}\u0018\u0000\b\u0000N\u0000\u0000\u0000\u0000{value2}\u0002\u0000)example://host/handler1/{value1}/{value2}\u0000\u001fcom.example.TestDeepLinkHandler\u0000\b\u0000\b\u0000\u0000\u0000\u0000\u0000phandler2\u0018\u0000\b\u0000\u0000\u0000\u0000\u0000_{value1}\u0018\u0000\b\u0000N\u0000\u0000\u0000\u0000{value2}\u0002\u0000)example://host/handler2/{value1}/{value2}\u0000\u001fcom.example.TestDeepLinkHandler\u0000\u0012\u0000\u0017\u0000\u0000\u0000\u0000\u0001Dhttp{scheme_suffix(|s)}\u0004\u0000\u000b\u0000\u0000\u0000\u0000\u00010example.com\b\u0000\b\u0000\u0000\u0000\u0000\u0000\u0087handler1\u0018\u0000\b\u0000\u0000\u0000\u0000\u0000v{value1}\u0018\u0000\b\u0000e\u0000\u0000\u0000\u0000{value2}\u0002\u0000@http{scheme_suffix(|s)}://example.com/handler1/{value1}/{value2}\u0000\u001fcom.example.TestDeepLinkHandler\u0000\b\u0000\b\u0000\u0000\u0000\u0000\u0000\u0087handler2\u0018\u0000\b\u0000\u0000\u0000\u0000\u0000v{value1}\u0018\u0000\b\u0000e\u0000\u0000\u0000\u0000{value2}\u0002\u0000@http{scheme_suffix(|s)}://example.com/handler2/{value1}/{value2}\u0000\u001fcom.example.TestDeepLinkHandler\u0000";
                          }
                        }

                        """.trimIndent(),
                ),
            generatedFiles =
                mapOf(
                    generateManifestFile to
                        """
                        <?xml version="1.0" encoding="utf-8"?>
                        <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                            <application>
                                <activity
                                    android:name="com.example.SampleActivity" android:exported="true">
                                    <intent-filter>
                                        <action android:name="android.intent.action.VIEW" />
                                        <category android:name="android.intent.category.DEFAULT" />
                                        <category android:name="android.intent.category.BROWSABLE" />
                                        <data android:scheme="http" />
                                        <data android:scheme="https" />
                                        <data android:host="example.com" />
                                        <data android:pathPattern="/handler1/.*/.*" />
                                        <data android:pathPattern="/handler2/.*/.*" />
                                    </intent-filter>
                                    <intent-filter>
                                        <action android:name="android.intent.action.VIEW" />
                                        <category android:name="android.intent.category.DEFAULT" />
                                        <category android:name="android.intent.category.BROWSABLE" />
                                        <data android:scheme="example" />
                                        <data android:host="host" />
                                        <data android:pathPattern="/handler1/.*/.*" />
                                        <data android:pathPattern="/handler2/.*/.*" />
                                    </intent-filter>
                                </activity>
                            </application>
                        </manifest>

                        """.trimIndent(),
                ),
        )
    }

    @Test
    fun testKsp() {
        val sampleActivityJava =
            Source.JavaSource(
                "com.example.SampleActivity",
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
                 public class SampleActivity extends android.app.Activity {
                 
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
                 """,
            )
        val sourceFiles =
            listOf(
                customAppDeepLinkJava,
                customWebDeepLinkJava,
                module,
                sampleActivityJava,
                fakeBaseDeeplinkDelegateJava,
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.AppDeepLink", "com.example.WebDeepLink"),
                    useKsp = false,
                ),
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.AppDeepLink", "com.example.WebDeepLink"),
                    useKsp = true,
                    incrementalFlag = false,
                ),
            )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries =
                listOf(
                    DeepLinkEntry.ActivityDeeplinkEntry(
                        uriTemplate = "airbnb://example.com/deepLink",
                        className = "com.example.SampleActivity",
                    ),
                    DeepLinkEntry.MethodDeeplinkEntry(
                        uriTemplate = "airbnb://intentMethod/{var1}/{var2}",
                        className = "com.example.SampleActivity",
                        method = "intentFromTwoPathWithTwoParams",
                    ),
                    DeepLinkEntry.MethodDeeplinkEntry(
                        uriTemplate = "airbnb://taskStackBuilderMethod/{arbitraryNumber}",
                        className = "com.example.SampleActivity",
                        method = "deeplinkOneParameter",
                    ),
                    DeepLinkEntry.ActivityDeeplinkEntry(
                        uriTemplate = "example://example.com/another",
                        className = "com.example.SampleActivity",
                    ),
                    DeepLinkEntry.ActivityDeeplinkEntry(
                        uriTemplate = "example://example.com/deepLink",
                        className = "com.example.SampleActivity",
                    ),
                    DeepLinkEntry.ActivityDeeplinkEntry(
                        uriTemplate = "http://example.com/another",
                        className = "com.example.SampleActivity",
                    ),
                    DeepLinkEntry.ActivityDeeplinkEntry(
                        uriTemplate = "http://example.com/deepLink",
                        className = "com.example.SampleActivity",
                    ),
                    DeepLinkEntry.MethodDeeplinkEntry(
                        uriTemplate = "http://example.com/method1",
                        className = "com.example.SampleActivity",
                        method = "webLinkMethod",
                    ),
                    DeepLinkEntry.MethodDeeplinkEntry(
                        uriTemplate = "http://example.com/method2",
                        className = "com.example.SampleActivity",
                        method = "webLinkMethod",
                    ),
                    DeepLinkEntry.ActivityDeeplinkEntry(
                        uriTemplate = "https://example.com/another",
                        className = "com.example.SampleActivity",
                    ),
                    DeepLinkEntry.ActivityDeeplinkEntry(
                        uriTemplate = "https://example.com/deepLink",
                        className = "com.example.SampleActivity",
                    ),
                    DeepLinkEntry.MethodDeeplinkEntry(
                        uriTemplate = "https://example.com/method1",
                        className = "com.example.SampleActivity",
                        method = "webLinkMethod",
                    ),
                    DeepLinkEntry.MethodDeeplinkEntry(
                        uriTemplate = "https://example.com/method2",
                        className = "com.example.SampleActivity",
                        method = "webLinkMethod",
                    ),
                ),
            generatedSourceFiles =
                mapOf(
                    "DeepLinkDelegate.java" to
                        """
                        package com.example;

                        import com.airbnb.deeplinkdispatch.BaseDeepLinkDelegate;
                        import com.airbnb.deeplinkdispatch.DeepLinkUri;
                        import com.airbnb.deeplinkdispatch.handler.TypeConverters;
                        import java.lang.Integer;
                        import java.lang.String;
                        import java.lang.reflect.Type;
                        import java.util.Arrays;
                        import java.util.Map;
                        import kotlin.jvm.functions.Function0;
                        import kotlin.jvm.functions.Function3;
                        import org.jetbrains.annotations.NotNull;
                        
                        public final class DeepLinkDelegate extends BaseDeepLinkDelegate {
                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry) {
                            super(Arrays.asList(
                              sampleModuleRegistry)
                            );
                          }
                        
                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry,
                              @NotNull Map<String, String> configurablePathSegmentReplacements) {
                            super(Arrays.asList(
                              sampleModuleRegistry),
                              configurablePathSegmentReplacements
                            );
                          }
                        
                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry,
                              @NotNull Map<String, String> configurablePathSegmentReplacements,
                              @NotNull Function0<TypeConverters> typeConverters) {
                            super(Arrays.asList(
                              sampleModuleRegistry),
                              configurablePathSegmentReplacements,
                              typeConverters
                            );
                          }
                        
                          public DeepLinkDelegate(@NotNull SampleModuleRegistry sampleModuleRegistry,
                              @NotNull Map<String, String> configurablePathSegmentReplacements,
                              @NotNull Function0<TypeConverters> typeConverters,
                              @NotNull Function3<DeepLinkUri, Type, ? super String, Integer> typeConversionErrorNullable,
                              @NotNull Function3<DeepLinkUri, Type, ? super String, Integer> typeConversionErrorNonNullable) {
                            super(Arrays.asList(
                              sampleModuleRegistry),
                              configurablePathSegmentReplacements,
                              typeConverters,
                              null,
                              typeConversionErrorNullable,
                              typeConversionErrorNonNullable
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
                            return "\u0001\u0000\u0001\u0000\u0000\u0000\u0000\u00052r\u0002\u0000\u0006\u0000\u0000\u0000\u0000\u0001\u0094airbnb\u0004\u0000\u000b\u0000\u0000\u0000\u0000\u0000Nexample.com\b\u0000\b\u0000=\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001dairbnb://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\u0004\u0000\f\u0000\u0000\u0000\u0000\u0000\u007fintentmethod\u0018\u0000\u0006\u0000\u0000\u0000\u0000\u0000p{var1}\u0018\u0000\u0006\u0000a\u0000\u0000\u0000\u0000{var2}\u0001\u0000#airbnb://intentMethod/{var1}/{var2}\u0000\u001acom.example.SampleActivity\u001eintentFromTwoPathWithTwoParams\u0004\u0000\u0016\u0000\u0000\u0000\u0000\u0000\u007ftaskstackbuildermethod\u0018\u0000\u0011\u0000e\u0000\u0000\u0000\u0000{arbitraryNumber}\u0001\u00001airbnb://taskStackBuilderMethod/{arbitraryNumber}\u0000\u001acom.example.SampleActivity\u0014deeplinkOneParameter\u0002\u0000\u0007\u0000\u0000\u0000\u0000\u0000°example\u0004\u0000\u000b\u0000\u0000\u0000\u0000\u0000\u009cexample.com\b\u0000\u0007\u0000=\u0000\u0000\u0000\u0000another\u0000\u0000\u001dexample://example.com/another\u0000\u001acom.example.SampleActivity\u0000\b\u0000\b\u0000>\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001eexample://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\u0002\u0000\u0004\u0000\u0000\u0000\u0000\u0001Xhttp\u0004\u0000\u000b\u0000\u0000\u0000\u0000\u0001Dexample.com\b\u0000\u0007\u0000:\u0000\u0000\u0000\u0000another\u0000\u0000\u001ahttp://example.com/another\u0000\u001acom.example.SampleActivity\u0000\b\u0000\b\u0000;\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001bhttp://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\b\u0000\u0007\u0000G\u0000\u0000\u0000\u0000method1\u0001\u0000\u001ahttp://example.com/method1\u0000\u001acom.example.SampleActivity\rwebLinkMethod\b\u0000\u0007\u0000G\u0000\u0000\u0000\u0000method2\u0001\u0000\u001ahttp://example.com/method2\u0000\u001acom.example.SampleActivity\rwebLinkMethod\u0002\u0000\u0005\u0000\u0000\u0000\u0000\u0001\\https\u0004\u0000\u000b\u0000\u0000\u0000\u0000\u0001Hexample.com\b\u0000\u0007\u0000;\u0000\u0000\u0000\u0000another\u0000\u0000\u001bhttps://example.com/another\u0000\u001acom.example.SampleActivity\u0000\b\u0000\b\u0000<\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001chttps://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\b\u0000\u0007\u0000H\u0000\u0000\u0000\u0000method1\u0001\u0000\u001bhttps://example.com/method1\u0000\u001acom.example.SampleActivity\rwebLinkMethod\b\u0000\u0007\u0000H\u0000\u0000\u0000\u0000method2\u0001\u0000\u001bhttps://example.com/method2\u0000\u001acom.example.SampleActivity\rwebLinkMethod";
                          }
                        }
                        
                        """.trimIndent(),
                ),
        )
    }

    @Test
    fun testDeepLinkWithCustomIntentFilterAttributes() {
        val sampleActivityKotlin =
            Source.KotlinSource(
                "SampleActivity.kt",
                """
                 package com.example
                 import com.airbnb.deeplinkdispatch.DeepLink
                 import com.airbnb.deeplinkdispatch.DeepLinkHandler
                 import com.example.SampleModule
                 @DeepLink(
                     value = "https://example.com/verified",
                     activityClassFqn = "com.example.SampleActivity",
                     intentFilterAttributes = ["android:autoVerify=\"true\""]
                 )
                 @DeepLinkHandler( SampleModule::class )
                 class SampleActivity : android.app.Activity()
                 """,
            )
        val sourceFiles =
            listOf(
                module,
                sampleActivityKotlin,
                fakeBaseDeeplinkDelegateJava,
            )
        val generateManifestFile =
            File(
                System.getProperty("java.io.tmpdir"),
                "GeneratedAndroidManifest_${System.currentTimeMillis()}.xml",
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = emptyList(),
                    useKsp = true,
                    incrementalFlag = false,
                    additionalArguments = mutableMapOf("deepLinkManifestGenMetadata.output" to generateManifestFile.canonicalPath),
                ),
            )
        // Just verify the manifest was generated correctly - skip source file verification
        assertThat(generateManifestFile.exists()).isTrue()
        assertThat(generateManifestFile.readText()).isEqualTo(
            """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.SampleActivity" android:exported="true">
                        <intent-filter android:autoVerify="true">
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/verified" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

            """.trimIndent(),
        )
    }

    @Test
    fun testDeepLinkWithCustomActionsAndCategories() {
        val sampleActivityKotlin =
            Source.KotlinSource(
                "SampleActivity.kt",
                """
                 package com.example
                 import com.airbnb.deeplinkdispatch.DeepLink
                 import com.airbnb.deeplinkdispatch.DeepLinkHandler
                 import com.example.SampleModule
                 @DeepLink(
                     value = "https://example.com/send",
                     activityClassFqn = "com.example.SampleActivity",
                     actions = ["android.intent.action.SEND"],
                     categories = ["android.intent.category.DEFAULT"]
                 )
                 @DeepLinkHandler( SampleModule::class )
                 class SampleActivity : android.app.Activity()
                 """,
            )
        val sourceFiles =
            listOf(
                module,
                sampleActivityKotlin,
                fakeBaseDeeplinkDelegateJava,
            )
        val generateManifestFile =
            File(
                System.getProperty("java.io.tmpdir"),
                "GeneratedAndroidManifest_${System.currentTimeMillis()}.xml",
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = emptyList(),
                    useKsp = true,
                    incrementalFlag = false,
                    additionalArguments = mutableMapOf("deepLinkManifestGenMetadata.output" to generateManifestFile.canonicalPath),
                ),
            )
        // Just verify the manifest was generated correctly - skip source file verification
        assertThat(generateManifestFile.exists()).isTrue()
        assertThat(generateManifestFile.readText()).isEqualTo(
            """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.SampleActivity" android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.SEND" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/send" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

            """.trimIndent(),
        )
    }

    @Test
    fun testDeepLinkSpecWithCustomIntentFilterAttributesActionsAndCategories() {
        val customVerifiedWebDeepLinkJava =
            Source.JavaSource(
                "com.example.VerifiedWebDeepLink",
                """
                package com.example;
                import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                @DeepLinkSpec(
                    prefix = { "https://example.com/" },
                    activityClassFqn = "com.example.SampleActivity",
                    intentFilterAttributes = { "android:autoVerify=\"true\"", "android:label=\"@string/app_name\"" },
                    actions = { "android.intent.action.VIEW", "android.intent.action.SEND" },
                    categories = { "android.intent.category.DEFAULT" }
                )
                public @interface VerifiedWebDeepLink {
                    String[] value();
                }
                """,
            )

        val sampleActivityKotlin =
            Source.KotlinSource(
                "SampleActivity.kt",
                """
                 package com.example
                 import com.airbnb.deeplinkdispatch.DeepLinkHandler
                 import com.example.SampleModule
                 @VerifiedWebDeepLink("path1", "path2")
                 @DeepLinkHandler( SampleModule::class )
                 class SampleActivity : android.app.Activity()
                 """,
            )
        val sourceFiles =
            listOf(
                customVerifiedWebDeepLinkJava,
                module,
                sampleActivityKotlin,
                fakeBaseDeeplinkDelegateJava,
            )
        val generateManifestFile =
            File(
                System.getProperty("java.io.tmpdir"),
                "GeneratedAndroidManifest_${System.currentTimeMillis()}.xml",
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.VerifiedWebDeepLink"),
                    useKsp = true,
                    incrementalFlag = false,
                    additionalArguments = mutableMapOf("deepLinkManifestGenMetadata.output" to generateManifestFile.canonicalPath),
                ),
            )
        // Just verify the manifest was generated correctly - skip source file verification
        assertThat(generateManifestFile.exists()).isTrue()
        assertThat(generateManifestFile.readText()).isEqualTo(
            """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.SampleActivity" android:exported="true">
                        <intent-filter android:autoVerify="true" android:label="@string/app_name">
                            <action android:name="android.intent.action.VIEW" />
                            <action android:name="android.intent.action.SEND" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/path1" />
                            <data android:path="/path2" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

            """.trimIndent(),
        )
    }

    @Test
    fun testMultipleDeepLinksWithDifferentIntentFilterGroupings() {
        val customAutoVerifyDeepLinkJava =
            Source.JavaSource(
                "com.example.AutoVerifyDeepLink",
                """
                package com.example;
                import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                @DeepLinkSpec(
                    prefix = { "https://example.com/" },
                    activityClassFqn = "com.example.SampleActivity",
                    intentFilterAttributes = { "android:autoVerify=\"true\"" }
                )
                public @interface AutoVerifyDeepLink {
                    String[] value();
                }
                """,
            )

        val customSendDeepLinkJava =
            Source.JavaSource(
                "com.example.SendDeepLink",
                """
                package com.example;
                import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                @DeepLinkSpec(
                    prefix = { "https://example.com/" },
                    activityClassFqn = "com.example.SampleActivity",
                    actions = { "android.intent.action.SEND" },
                    categories = { "android.intent.category.DEFAULT" }
                )
                public @interface SendDeepLink {
                    String[] value();
                }
                """,
            )

        val sampleActivityKotlin =
            Source.KotlinSource(
                "SampleActivity.kt",
                """
                 package com.example
                 import com.airbnb.deeplinkdispatch.DeepLink
                 import com.airbnb.deeplinkdispatch.DeepLinkHandler
                 import com.example.SampleModule
                 @AutoVerifyDeepLink("verified1", "verified2")
                 @DeepLink(
                     value = "https://example.com/unverified",
                     activityClassFqn = "com.example.SampleActivity"
                 )
                 @SendDeepLink("send")
                 @DeepLinkHandler( SampleModule::class )
                 class SampleActivity : android.app.Activity()
                 """,
            )
        val sourceFiles =
            listOf(
                customAutoVerifyDeepLinkJava,
                customSendDeepLinkJava,
                module,
                sampleActivityKotlin,
                fakeBaseDeeplinkDelegateJava,
            )
        val generateManifestFile =
            File(
                System.getProperty("java.io.tmpdir"),
                "GeneratedAndroidManifest_${System.currentTimeMillis()}.xml",
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.AutoVerifyDeepLink", "com.example.SendDeepLink"),
                    useKsp = true,
                    incrementalFlag = false,
                    additionalArguments = mutableMapOf("deepLinkManifestGenMetadata.output" to generateManifestFile.canonicalPath),
                ),
            )
        // Just verify the manifest was generated correctly - skip source file verification
        assertThat(generateManifestFile.exists()).isTrue()
        assertThat(generateManifestFile.readText()).isEqualTo(
            """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.SampleActivity" android:exported="true">
                        <intent-filter android:autoVerify="true">
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/verified1" />
                            <data android:path="/verified2" />
                        </intent-filter>
                        <intent-filter>
                            <action android:name="android.intent.action.SEND" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/send" />
                        </intent-filter>
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/unverified" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

            """.trimIndent(),
        )
    }

    @Test
    fun testDeepLinkSpecVsDeepLinkDifferentIntentFilterAttributes() {
        val customAutoVerifyWebDeepLinkJava =
            Source.JavaSource(
                "com.example.AutoVerifyWebDeepLink",
                """
                package com.example;
                import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                @DeepLinkSpec(
                    prefix = { "https://example.com/" },
                    activityClassFqn = "com.example.SampleActivity",
                    intentFilterAttributes = { "android:autoVerify=\"true\"" }
                )
                public @interface AutoVerifyWebDeepLink {
                    String[] value();
                }
                """,
            )

        val sampleActivityKotlin =
            Source.KotlinSource(
                "SampleActivity.kt",
                """
                 package com.example
                 import com.airbnb.deeplinkdispatch.DeepLink
                 import com.airbnb.deeplinkdispatch.DeepLinkHandler
                 import com.example.SampleModule
                 @AutoVerifyWebDeepLink("spec1", "spec2")
                 @DeepLink(
                     value = "https://example.com/direct",
                     activityClassFqn = "com.example.SampleActivity"
                 )
                 @DeepLinkHandler( SampleModule::class )
                 class SampleActivity : android.app.Activity()
                 """,
            )
        val sourceFiles =
            listOf(
                customAutoVerifyWebDeepLinkJava,
                module,
                sampleActivityKotlin,
                fakeBaseDeeplinkDelegateJava,
            )
        val generateManifestFile =
            File(
                System.getProperty("java.io.tmpdir"),
                "GeneratedAndroidManifest_${System.currentTimeMillis()}.xml",
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.AutoVerifyWebDeepLink"),
                    useKsp = true,
                    incrementalFlag = false,
                    additionalArguments = mutableMapOf("deepLinkManifestGenMetadata.output" to generateManifestFile.canonicalPath),
                ),
            )
        // Just verify the manifest was generated correctly - skip source file verification
        assertThat(generateManifestFile.exists()).isTrue()
        assertThat(generateManifestFile.readText()).isEqualTo(
            """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.SampleActivity" android:exported="true">
                        <intent-filter android:autoVerify="true">
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/spec1" />
                            <data android:path="/spec2" />
                        </intent-filter>
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/direct" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

            """.trimIndent(),
        )
    }
}
