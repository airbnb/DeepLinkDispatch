package com.airbnb.deeplinkdispatch

import com.tschuchort.compiletesting.SourceFile
import org.junit.Test

class DeepLinkProcessorKspTest : BaseDeepLinkProcessorTest() {

    private val customWebDeepLinkJava: SourceFile
        get() {
            val customAnnotationWebLink = SourceFile.java(
                "WebDeepLink.java",
                """
                package com.example;
                import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                @DeepLinkSpec(prefix = { "http://", "https://"})
                public @interface WebDeepLink {
                    String[] value();
                }
                """
            )
            return customAnnotationWebLink
        }

    private val customAppDeepLinkJava: SourceFile
        get() {
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
            return customAnnotationAppLink
        }

    @Test
    fun testWithKotlinSource() {
        val sampleActivityKotlin = SourceFile.kotlin(
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
                 class Activity {
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
                DeepLinkEntry(uriTemplate = "airbnb://example.com/deepLink", className = "com.example.Activity", method = null),
                DeepLinkEntry(uriTemplate = "airbnb://intentMethod/{var1}/{var2}", className = "com.example.Activity\$DeepLinks", method = "intentFromTwoPathWithTwoParams"),
                DeepLinkEntry(uriTemplate = "airbnb://taskStackBuilderMethod/{arbitraryNumber}", className = "com.example.Activity\$DeepLinks", method = "deeplinkOneParameter"),
                DeepLinkEntry(uriTemplate = "example://example.com/another", className = "com.example.Activity", method = null),
                DeepLinkEntry(uriTemplate = "example://example.com/deepLink", className = "com.example.Activity", method = null),
                DeepLinkEntry(uriTemplate = "http://example.com/another", className = "com.example.Activity", method = null),
                DeepLinkEntry(uriTemplate = "http://example.com/deepLink", className = "com.example.Activity", method = null),
                DeepLinkEntry(uriTemplate = "http://example.com/method1", className = "com.example.Activity\$DeepLinks", method = "webLinkMethod"),
                DeepLinkEntry(uriTemplate = "http://example.com/method2", className = "com.example.Activity\$DeepLinks", method = "webLinkMethod"),
                DeepLinkEntry(uriTemplate = "https://example.com/another", className = "com.example.Activity", method = null),
                DeepLinkEntry(uriTemplate = "https://example.com/deepLink", className = "com.example.Activity", method = null),
                DeepLinkEntry(uriTemplate = "https://example.com/method1", className = "com.example.Activity\$DeepLinks", method = "webLinkMethod"),
                DeepLinkEntry(uriTemplate = "https://example.com/method2", className = "com.example.Activity\$DeepLinks", method = "webLinkMethod")
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
                    return "\u0001\u0001\u0000\u0000\u0000\u0000\u0004ûr\u0002\u0006\u0000\u0000\u0000\u0000\u0001\u008cairbnb\u0004\u000b\u0000\u0000\u0000\u0000\u0000Fexample.com\b\b\u00006\u0000\u0000\u0000\u0000deepLink\u0000\u001dairbnb://example.com/deepLink\u0000\u0014com.example.Activity\u0000\u0004\f\u0000\u0000\u0000\u0000\u0000\u0080intentmethod\u0018\u0006\u0000\u0000\u0000\u0000\u0000r{var1}\u0018\u0006\u0000d\u0000\u0000\u0000\u0000{var2}\u0000#airbnb://intentMethod/{var1}/{var2}\u0000\u001ecom.example.Activity${"$"}DeepLinks\u001eintentFromTwoPathWithTwoParams\u0004\u0016\u0000\u0000\u0000\u0000\u0000\u0081taskstackbuildermethod\u0018\u0011\u0000h\u0000\u0000\u0000\u0000{arbitraryNumber}\u00001airbnb://taskStackBuilderMethod/{arbitraryNumber}\u0000\u001ecom.example.Activity${"$"}DeepLinks\u0014deeplinkOneParameter\u0002\u0007\u0000\u0000\u0000\u0000\u0000\u009fexample\u0004\u000b\u0000\u0000\u0000\u0000\u0000\u008cexample.com\b\u0007\u00006\u0000\u0000\u0000\u0000another\u0000\u001dexample://example.com/another\u0000\u0014com.example.Activity\u0000\b\b\u00007\u0000\u0000\u0000\u0000deepLink\u0000\u001eexample://example.com/deepLink\u0000\u0014com.example.Activity\u0000\u0002\u0004\u0000\u0000\u0000\u0000\u0001Khttp\u0004\u000b\u0000\u0000\u0000\u0000\u00018example.com\b\u0007\u00003\u0000\u0000\u0000\u0000another\u0000\u001ahttp://example.com/another\u0000\u0014com.example.Activity\u0000\b\b\u00004\u0000\u0000\u0000\u0000deepLink\u0000\u001bhttp://example.com/deepLink\u0000\u0014com.example.Activity\u0000\b\u0007\u0000J\u0000\u0000\u0000\u0000method1\u0000\u001ahttp://example.com/method1\u0000\u001ecom.example.Activity${"$"}DeepLinks\rwebLinkMethod\b\u0007\u0000J\u0000\u0000\u0000\u0000method2\u0000\u001ahttp://example.com/method2\u0000\u001ecom.example.Activity${"$"}DeepLinks\rwebLinkMethod\u0002\u0005\u0000\u0000\u0000\u0000\u0001Ohttps\u0004\u000b\u0000\u0000\u0000\u0000\u0001<example.com\b\u0007\u00004\u0000\u0000\u0000\u0000another\u0000\u001bhttps://example.com/another\u0000\u0014com.example.Activity\u0000\b\b\u00005\u0000\u0000\u0000\u0000deepLink\u0000\u001chttps://example.com/deepLink\u0000\u0014com.example.Activity\u0000\b\u0007\u0000K\u0000\u0000\u0000\u0000method1\u0000\u001bhttps://example.com/method1\u0000\u001ecom.example.Activity${"$"}DeepLinks\rwebLinkMethod\b\u0007\u0000K\u0000\u0000\u0000\u0000method2\u0000\u001bhttps://example.com/method2\u0000\u001ecom.example.Activity${"$"}DeepLinks\rwebLinkMethod";
                  }
                }
                
                    """.trimIndent()
            )
        )
    }

    @Test
    fun testKsp() {
        val sampleActivityJava = SourceFile.java(
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
                DeepLinkEntry(uriTemplate = "airbnb://example.com/deepLink", className = "com.example.SampleActivity", method = null),
                DeepLinkEntry(uriTemplate = "airbnb://intentMethod/{var1}/{var2}", className = "com.example.SampleActivity", method = "intentFromTwoPathWithTwoParams"),
                DeepLinkEntry(uriTemplate = "airbnb://taskStackBuilderMethod/{arbitraryNumber}", className = "com.example.SampleActivity", method = "deeplinkOneParameter"),
                DeepLinkEntry(uriTemplate = "example://example.com/another", className = "com.example.SampleActivity", method = null),
                DeepLinkEntry(uriTemplate = "example://example.com/deepLink", className = "com.example.SampleActivity", method = null),
                DeepLinkEntry(uriTemplate = "http://example.com/another", className = "com.example.SampleActivity", method = null),
                DeepLinkEntry(uriTemplate = "http://example.com/deepLink", className = "com.example.SampleActivity", method = null),
                DeepLinkEntry(uriTemplate = "http://example.com/method1", className = "com.example.SampleActivity", method = "webLinkMethod"),
                DeepLinkEntry(uriTemplate = "http://example.com/method2", className = "com.example.SampleActivity", method = "webLinkMethod"),
                DeepLinkEntry(uriTemplate = "https://example.com/another", className = "com.example.SampleActivity", method = null),
                DeepLinkEntry(uriTemplate = "https://example.com/deepLink", className = "com.example.SampleActivity", method = null),
                DeepLinkEntry(uriTemplate = "https://example.com/method1", className = "com.example.SampleActivity", method = "webLinkMethod"),
                DeepLinkEntry(uriTemplate = "https://example.com/method2", className = "com.example.SampleActivity", method = "webLinkMethod"),
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
                    return "\u0001\u0001\u0000\u0000\u0000\u0000\u0005\rr\u0002\u0006\u0000\u0000\u0000\u0000\u0001\u008aairbnb\u0004\u000b\u0000\u0000\u0000\u0000\u0000Lexample.com\b\b\u0000<\u0000\u0000\u0000\u0000deepLink\u0000\u001dairbnb://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\u0004\f\u0000\u0000\u0000\u0000\u0000|intentmethod\u0018\u0006\u0000\u0000\u0000\u0000\u0000n{var1}\u0018\u0006\u0000`\u0000\u0000\u0000\u0000{var2}\u0000#airbnb://intentMethod/{var1}/{var2}\u0000\u001acom.example.SampleActivity\u001eintentFromTwoPathWithTwoParams\u0004\u0016\u0000\u0000\u0000\u0000\u0000}taskstackbuildermethod\u0018\u0011\u0000d\u0000\u0000\u0000\u0000{arbitraryNumber}\u00001airbnb://taskStackBuilderMethod/{arbitraryNumber}\u0000\u001acom.example.SampleActivity\u0014deeplinkOneParameter\u0002\u0007\u0000\u0000\u0000\u0000\u0000«example\u0004\u000b\u0000\u0000\u0000\u0000\u0000\u0098example.com\b\u0007\u0000<\u0000\u0000\u0000\u0000another\u0000\u001dexample://example.com/another\u0000\u001acom.example.SampleActivity\u0000\b\b\u0000=\u0000\u0000\u0000\u0000deepLink\u0000\u001eexample://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\u0002\u0004\u0000\u0000\u0000\u0000\u0001Ohttp\u0004\u000b\u0000\u0000\u0000\u0000\u0001<example.com\b\u0007\u00009\u0000\u0000\u0000\u0000another\u0000\u001ahttp://example.com/another\u0000\u001acom.example.SampleActivity\u0000\b\b\u0000:\u0000\u0000\u0000\u0000deepLink\u0000\u001bhttp://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\b\u0007\u0000F\u0000\u0000\u0000\u0000method1\u0000\u001ahttp://example.com/method1\u0000\u001acom.example.SampleActivity\rwebLinkMethod\b\u0007\u0000F\u0000\u0000\u0000\u0000method2\u0000\u001ahttp://example.com/method2\u0000\u001acom.example.SampleActivity\rwebLinkMethod\u0002\u0005\u0000\u0000\u0000\u0000\u0001Shttps\u0004\u000b\u0000\u0000\u0000\u0000\u0001@example.com\b\u0007\u0000:\u0000\u0000\u0000\u0000another\u0000\u001bhttps://example.com/another\u0000\u001acom.example.SampleActivity\u0000\b\b\u0000;\u0000\u0000\u0000\u0000deepLink\u0000\u001chttps://example.com/deepLink\u0000\u001acom.example.SampleActivity\u0000\b\u0007\u0000G\u0000\u0000\u0000\u0000method1\u0000\u001bhttps://example.com/method1\u0000\u001acom.example.SampleActivity\rwebLinkMethod\b\u0007\u0000G\u0000\u0000\u0000\u0000method2\u0000\u001bhttps://example.com/method2\u0000\u001acom.example.SampleActivity\rwebLinkMethod";
                  }
                }
                
                    """.trimIndent()
            )
        )
    }
}
