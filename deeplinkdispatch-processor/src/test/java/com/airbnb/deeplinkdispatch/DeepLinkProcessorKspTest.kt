package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.test.Source
import org.junit.Test

class DeepLinkProcessorKspTest : BaseDeepLinkProcessorTest() {

    private val customWebDeepLinkJava = Source.JavaSource(
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

    private val customAppDeepLinkJava = Source.JavaSource(
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

    @Test
    fun testWithNoClass() {
        val sampleDeeplinkMethod = Source.KotlinSource(
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
            """.trimIndent()
        )
        val sourceFiles = listOf(
            module, sampleDeeplinkMethod, fakeBaseDeeplinkDelegate
        )
        val results = listOf(
            compileIncremental(
                sourceFiles = sourceFiles,
                useKsp = false,
            ),
            compileIncremental(
                sourceFiles = sourceFiles,
                useKsp = true,
                incrementalFlag = false
            )
        )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = listOf(
                DeepLinkEntry.MethodDeeplinkEntry(
                    uriTemplate = "airbnb://example.com/noClassTest",
                    className = "com.example.SampleNoClassFileKt",
                    method = "intentForInquiryDetailFragment"
                )
            ),
            generatedFiles = mapOf(
                "DeepLinkDelegate.java" to
                    """
                    package com.example;

                    import com.airbnb.deeplinkdispatch.BaseDeepLinkDelegate;
                    import com.airbnb.deeplinkdispatch.handler.TypeConverters;
                    import java.lang.Integer;
                    import java.lang.String;
                    import java.util.Arrays;
                    import java.util.Map;
                    import kotlin.jvm.functions.Function0;
                    import kotlin.jvm.functions.Function1;
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
                          @NotNull Function1<? super String, Integer> typeConversionErrorNullable,
                          @NotNull Function1<? super String, Integer> typeConversionErrorNonNullable) {
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
                        return "\u0001\u0001\u0000\u0000\u0000\u0000\u0000\u0097r\u0002\u0006\u0000\u0000\u0000\u0000\u0000\u0089airbnb\u0004\u000b\u0000\u0000\u0000\u0000\u0000vexample.com\b\u000b\u0000c\u0000\u0000\u0000\u0000noClassTest\u0001\u0000 airbnb://example.com/noClassTest\u0000\u001fcom.example.SampleNoClassFileKt\u001eintentForInquiryDetailFragment";
                      }
                    }
                    
                    """.trimIndent()
            )
        )
    }

    @Test
    fun testWithKotlinSource() {
        val sampleActivityKotlin = Source.KotlinSource(
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
                 """
        )
        val sourceFiles = listOf(
            customAppDeepLinkJava, customWebDeepLinkJava,
            module, sampleActivityKotlin, fakeBaseDeeplinkDelegate
        )
        val results = listOf(
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = listOf("com.example.AppDeepLink", "com.example.WebDeepLink"),
                useKsp = false,
            ),
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = listOf("com.example.AppDeepLink", "com.example.WebDeepLink"),
                useKsp = true,
                incrementalFlag = false
            )
        )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = listOf(
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "airbnb://example.com/deepLink",
                    className = "com.example.Activity"
                ),
                DeepLinkEntry.MethodDeeplinkEntry(
                    uriTemplate = "airbnb://intentMethod/{var1}/{var2}",
                    className = "com.example.Activity\$DeepLinks",
                    method = "intentFromTwoPathWithTwoParams"
                ),
                DeepLinkEntry.MethodDeeplinkEntry(
                    uriTemplate = "airbnb://taskStackBuilderMethod/{arbitraryNumber}",
                    className = "com.example.Activity\$DeepLinks",
                    method = "deeplinkOneParameter"
                ),
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "example://example.com/another",
                    className = "com.example.Activity"
                ),
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "example://example.com/deepLink",
                    className = "com.example.Activity"
                ),
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "http://example.com/another",
                    className = "com.example.Activity"
                ),
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "http://example.com/deepLink",
                    className = "com.example.Activity"
                ),
                DeepLinkEntry.MethodDeeplinkEntry(
                    uriTemplate = "http://example.com/method1",
                    className = "com.example.Activity\$DeepLinks",
                    method = "webLinkMethod"
                ),
                DeepLinkEntry.MethodDeeplinkEntry(
                    uriTemplate = "http://example.com/method2",
                    className = "com.example.Activity\$DeepLinks",
                    method = "webLinkMethod"
                ),
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "https://example.com/another",
                    className = "com.example.Activity"
                ),
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "https://example.com/deepLink",
                    className = "com.example.Activity"
                ),
                DeepLinkEntry.MethodDeeplinkEntry(
                    uriTemplate = "https://example.com/method1",
                    className = "com.example.Activity\$DeepLinks",
                    method = "webLinkMethod"
                ),
                DeepLinkEntry.MethodDeeplinkEntry(
                    uriTemplate = "https://example.com/method2",
                    className = "com.example.Activity\$DeepLinks",
                    method = "webLinkMethod"
                )
            ),
            generatedFiles = mapOf(
                "DeepLinkDelegate.java" to
                    """
                    package com.example;

                    import com.airbnb.deeplinkdispatch.BaseDeepLinkDelegate;
                    import com.airbnb.deeplinkdispatch.handler.TypeConverters;
                    import java.lang.Integer;
                    import java.lang.String;
                    import java.util.Arrays;
                    import java.util.Map;
                    import kotlin.jvm.functions.Function0;
                    import kotlin.jvm.functions.Function1;
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
                          @NotNull Function1<? super String, Integer> typeConversionErrorNullable,
                          @NotNull Function1<? super String, Integer> typeConversionErrorNonNullable) {
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
                        return "\u0001\u0001\u0000\u0000\u0000\u0000\u0005\br\u0002\u0006\u0000\u0000\u0000\u0000\u0001\u008fairbnb\u0004\u000b\u0000\u0000\u0000\u0000\u0000Gexample.com\b\b\u00007\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001dairbnb://example.com/deepLink\u0000\u0014com.example.Activity\u0000\u0004\f\u0000\u0000\u0000\u0000\u0000\u0081intentmethod\u0018\u0006\u0000\u0000\u0000\u0000\u0000s{var1}\u0018\u0006\u0000e\u0000\u0000\u0000\u0000{var2}\u0001\u0000#airbnb://intentMethod/{var1}/{var2}\u0000\u001ecom.example.Activity${"\$"}DeepLinks\u001eintentFromTwoPathWithTwoParams\u0004\u0016\u0000\u0000\u0000\u0000\u0000\u0082taskstackbuildermethod\u0018\u0011\u0000i\u0000\u0000\u0000\u0000{arbitraryNumber}\u0001\u00001airbnb://taskStackBuilderMethod/{arbitraryNumber}\u0000\u001ecom.example.Activity${"\$"}DeepLinks\u0014deeplinkOneParameter\u0002\u0007\u0000\u0000\u0000\u0000\u0000¡example\u0004\u000b\u0000\u0000\u0000\u0000\u0000\u008eexample.com\b\u0007\u00007\u0000\u0000\u0000\u0000another\u0000\u0000\u001dexample://example.com/another\u0000\u0014com.example.Activity\u0000\b\b\u00008\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001eexample://example.com/deepLink\u0000\u0014com.example.Activity\u0000\u0002\u0004\u0000\u0000\u0000\u0000\u0001Ohttp\u0004\u000b\u0000\u0000\u0000\u0000\u0001<example.com\b\u0007\u00004\u0000\u0000\u0000\u0000another\u0000\u0000\u001ahttp://example.com/another\u0000\u0014com.example.Activity\u0000\b\b\u00005\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001bhttp://example.com/deepLink\u0000\u0014com.example.Activity\u0000\b\u0007\u0000K\u0000\u0000\u0000\u0000method1\u0001\u0000\u001ahttp://example.com/method1\u0000\u001ecom.example.Activity${"\$"}DeepLinks\rwebLinkMethod\b\u0007\u0000K\u0000\u0000\u0000\u0000method2\u0001\u0000\u001ahttp://example.com/method2\u0000\u001ecom.example.Activity${"\$"}DeepLinks\rwebLinkMethod\u0002\u0005\u0000\u0000\u0000\u0000\u0001Shttps\u0004\u000b\u0000\u0000\u0000\u0000\u0001@example.com\b\u0007\u00005\u0000\u0000\u0000\u0000another\u0000\u0000\u001bhttps://example.com/another\u0000\u0014com.example.Activity\u0000\b\b\u00006\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001chttps://example.com/deepLink\u0000\u0014com.example.Activity\u0000\b\u0007\u0000L\u0000\u0000\u0000\u0000method1\u0001\u0000\u001bhttps://example.com/method1\u0000\u001ecom.example.Activity${"\$"}DeepLinks\rwebLinkMethod\b\u0007\u0000L\u0000\u0000\u0000\u0000method2\u0001\u0000\u001bhttps://example.com/method2\u0000\u001ecom.example.Activity${"\$"}DeepLinks\rwebLinkMethod";
                      }
                    }
                    
                    """.trimIndent()
            )
        )
    }

    @Test
    fun testKsp() {
        val sampleActivityJava = Source.JavaSource(
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
                 """
        )
        val sourceFiles = listOf(
            customAppDeepLinkJava, customWebDeepLinkJava,
            module, sampleActivityJava, fakeBaseDeeplinkDelegate
        )
        val results = listOf(
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = listOf("com.example.AppDeepLink", "com.example.WebDeepLink"),
                useKsp = false,
            ),
            compileIncremental(
                sourceFiles = sourceFiles,
                customDeepLinks = listOf("com.example.AppDeepLink", "com.example.WebDeepLink"),
                useKsp = true,
                incrementalFlag = false
            )
        )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries = listOf(
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "airbnb://example.com/deepLink",
                    className = "com.example.SampleActivity"
                ),
                DeepLinkEntry.MethodDeeplinkEntry(
                    uriTemplate = "airbnb://intentMethod/{var1}/{var2}",
                    className = "com.example.SampleActivity",
                    method = "intentFromTwoPathWithTwoParams"
                ),
                DeepLinkEntry.MethodDeeplinkEntry(
                    uriTemplate = "airbnb://taskStackBuilderMethod/{arbitraryNumber}",
                    className = "com.example.SampleActivity",
                    method = "deeplinkOneParameter"
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
                DeepLinkEntry.MethodDeeplinkEntry(
                    uriTemplate = "http://example.com/method1",
                    className = "com.example.SampleActivity",
                    method = "webLinkMethod"
                ),
                DeepLinkEntry.MethodDeeplinkEntry(
                    uriTemplate = "http://example.com/method2",
                    className = "com.example.SampleActivity",
                    method = "webLinkMethod"
                ),
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "https://example.com/another",
                    className = "com.example.SampleActivity"
                ),
                DeepLinkEntry.ActivityDeeplinkEntry(
                    uriTemplate = "https://example.com/deepLink",
                    className = "com.example.SampleActivity"
                ),
                DeepLinkEntry.MethodDeeplinkEntry(
                    uriTemplate = "https://example.com/method1",
                    className = "com.example.SampleActivity",
                    method = "webLinkMethod"
                ),
                DeepLinkEntry.MethodDeeplinkEntry(
                    uriTemplate = "https://example.com/method2",
                    className = "com.example.SampleActivity",
                    method = "webLinkMethod"
                ),
            ),
            generatedFiles = mapOf(
                "DeepLinkDelegate.java" to
                    """
                    package com.example;

                    import com.airbnb.deeplinkdispatch.BaseDeepLinkDelegate;
                    import com.airbnb.deeplinkdispatch.handler.TypeConverters;
                    import java.lang.Integer;
                    import java.lang.String;
                    import java.util.Arrays;
                    import java.util.Map;
                    import kotlin.jvm.functions.Function0;
                    import kotlin.jvm.functions.Function1;
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
                          @NotNull Function1<? super String, Integer> typeConversionErrorNullable,
                          @NotNull Function1<? super String, Integer> typeConversionErrorNonNullable) {
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
                        return "\u0001\u0001\u0000\u0000\u0000\u0000\u0005\u001ar\u0002\u0006\u0000\u0000\u0000\u0000\u0001\u008dairbnb\u0004\u000b\u0000\u0000\u0000\u0000\u0000Mexample.com\b\b\u0000=\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001dairbnb://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\u0004\f\u0000\u0000\u0000\u0000\u0000}intentmethod\u0018\u0006\u0000\u0000\u0000\u0000\u0000o{var1}\u0018\u0006\u0000a\u0000\u0000\u0000\u0000{var2}\u0001\u0000#airbnb://intentMethod/{var1}/{var2}\u0000\u001acom.example.SampleActivity\u001eintentFromTwoPathWithTwoParams\u0004\u0016\u0000\u0000\u0000\u0000\u0000~taskstackbuildermethod\u0018\u0011\u0000e\u0000\u0000\u0000\u0000{arbitraryNumber}\u0001\u00001airbnb://taskStackBuilderMethod/{arbitraryNumber}\u0000\u001acom.example.SampleActivity\u0014deeplinkOneParameter\u0002\u0007\u0000\u0000\u0000\u0000\u0000­example\u0004\u000b\u0000\u0000\u0000\u0000\u0000\u009aexample.com\b\u0007\u0000=\u0000\u0000\u0000\u0000another\u0000\u0000\u001dexample://example.com/another\u0000\u001acom.example.SampleActivity\u0000\b\b\u0000>\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001eexample://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\u0002\u0004\u0000\u0000\u0000\u0000\u0001Shttp\u0004\u000b\u0000\u0000\u0000\u0000\u0001@example.com\b\u0007\u0000:\u0000\u0000\u0000\u0000another\u0000\u0000\u001ahttp://example.com/another\u0000\u001acom.example.SampleActivity\u0000\b\b\u0000;\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001bhttp://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\b\u0007\u0000G\u0000\u0000\u0000\u0000method1\u0001\u0000\u001ahttp://example.com/method1\u0000\u001acom.example.SampleActivity\rwebLinkMethod\b\u0007\u0000G\u0000\u0000\u0000\u0000method2\u0001\u0000\u001ahttp://example.com/method2\u0000\u001acom.example.SampleActivity\rwebLinkMethod\u0002\u0005\u0000\u0000\u0000\u0000\u0001Whttps\u0004\u000b\u0000\u0000\u0000\u0000\u0001Dexample.com\b\u0007\u0000;\u0000\u0000\u0000\u0000another\u0000\u0000\u001bhttps://example.com/another\u0000\u001acom.example.SampleActivity\u0000\b\b\u0000<\u0000\u0000\u0000\u0000deepLink\u0000\u0000\u001chttps://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\b\u0007\u0000H\u0000\u0000\u0000\u0000method1\u0001\u0000\u001bhttps://example.com/method1\u0000\u001acom.example.SampleActivity\rwebLinkMethod\b\u0007\u0000H\u0000\u0000\u0000\u0000method2\u0001\u0000\u001bhttps://example.com/method2\u0000\u001acom.example.SampleActivity\rwebLinkMethod";
                      }
                    }
                    
                    """.trimIndent()
            )
        )
    }
}
