package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.test.Source
import com.squareup.kotlinpoet.javapoet.KotlinPoetJavaPoetPreview
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.Test

@ExperimentalCompilerApi
@KotlinPoetJavaPoetPreview
class DeepLinkProcessorDeepLinkHandlerIncrementalTest : BaseDeepLinkProcessorTest() {
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
    private val customAnnotationPlaceholderInSchemeHostAppLink =
        Source.JavaSource(
            "com.example.PlaceholderDeepLink",
            """
                package com.example;
                import com.airbnb.deeplinkdispatch.DeepLinkSpec;
                @DeepLinkSpec(prefix = { "http{scheme}://{host}example.com/" })
                public @interface PlaceholderDeepLink {
                    String[] value();
                }
                """,
        )

    private val sampleDeeplinkHandler =
        Source.KotlinSource(
            "SampleDeeplinkHandler.kt",
            """
            package com.example
            import android.content.Context
            import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
            import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
            import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
            import java.time.LocalDate
            @PlaceholderDeepLink("pathSegment/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}/{path_segment_variable_4}/{path_segment_variable_5}/{path_segment_variable_6}/{path_segment_variable_7}/{path_segment_variable_8}?queryParam1={query_param_1}&queryParam2={query_param_2}&queryParam3={query_param_3}&queryParam4={query_param_4}&queryParam5={query_param_5}&queryParam6={query_param_6}&queryParam7={query_param_7}&queryParam8={query_param_8}")
            object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs> {
                override fun handleDeepLink(context: Context, parameters: TestDeepLinkHandlerDeepLinkArgs) {
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
            """,
        )

    private val sampleDeeplinkHandlerNoArgs =
        Source.KotlinSource(
            "SampleDeeplinkHandler.kt",
            """
            package com.example
            import android.content.Context
            import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
            import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
            import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
            import java.time.LocalDate
            @PlaceholderDeepLink("pathSegment/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}/{path_segment_variable_4}/{path_segment_variable_5}/{path_segment_variable_6}/{path_segment_variable_7}/{path_segment_variable_8}?queryParam1={query_param_1}&queryParam2={query_param_2}&queryParam3={query_param_3}&queryParam4={query_param_4}&queryParam5={query_param_5}&queryParam6={query_param_6}&queryParam7={query_param_7}&queryParam8={query_param_8}")
            object TestDeepLinkHandler : DeepLinkHandler<java.lang.Object> {
                override fun handleDeepLink(context: Context, parameters: java.lang.Object) {
                    TODO("Not yet implemented")
                }
            }
            """,
        )

    private val sampleDeeplinkHandlerWithSingleParameterConstructor =
        Source.KotlinSource(
            "SampleDeeplinkHandler.kt",
            """
            package com.example
            
            import android.content.Context
            import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
            import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
            import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
            
            @PlaceholderDeepLink("pathSegment/{one}")
            class TestDeepLinkHandler(val someValue: String) : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs> {
                override fun handleDeepLink(context: Context, parameters: TestDeepLinkHandlerDeepLinkArgs) {
                    TODO("Not yet implemented")
                }
            }
            data class TestDeepLinkHandlerDeepLinkArgs(
                @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: String,
            )
            """,
        )

    private val sampleDeeplinkHandlerWithTwoMethods =
        Source.KotlinSource(
            "SampleDeeplinkHandler.kt",
            """
                package com.example
                
                import android.content.Context
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}")
                object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs> {
                    override fun handleDeepLink(context: Context, parameters: TestDeepLinkHandlerDeepLinkArgs) {
                        TODO("Not yet implemented")
                    }
                    
                    fun handleDeepLink(context: Context, string: String) {
                        TODO("Not yet implemented")
                    }
                }
                data class TestDeepLinkHandlerDeepLinkArgs(
                    @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: String,
                )
            """,
        )

    private val sampleDeeplinkHandlerWithIntermediateClassAndTwoTypeParameters =
        Source.KotlinSource(
            "SampleDeeplinkHandler.kt",
            """
                package com.example
                
                import android.content.Context
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}")
                object TestDeepLinkHandler : InBetweenDeeplinkHandler<TestDeepLinkHandlerDeepLinkArgs, String>()
                
                open class InBetweenDeeplinkHandler<T, U>() : DeepLinkHandler<T> {
                    override fun handleDeepLink(context: Context, parameters: T) {
                        TODO("Not yet implemented")
                    }
                }
                
                data class TestDeepLinkHandlerDeepLinkArgs(
                    @DeeplinkParam(name = "scheme", type = DeepLinkParamType.Path ) val scheme: String,
                    @DeeplinkParam(name = "host", type = DeepLinkParamType.Path ) val host: String,
                    @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: String
                )
                """,
        )

    private val sampleDeeplinkHandlerWithIntermediateInterface =
        Source.KotlinSource(
            "SampleDeeplinkHandler.kt",
            """
                package com.example
                
                import android.content.Context
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}")
                object TestDeepLinkHandler : InBetweenDeeplinkHandlerInterface<TestDeepLinkHandlerDeepLinkArgs> {
                    override fun handleDeepLink(context: Context, parameters: TestDeepLinkHandlerDeepLinkArgs) {
                        TODO("Not yet implemented")
                    }
                }
                
                interface InBetweenDeeplinkHandlerInterface<T> : DeepLinkHandler<T>
                
                data class TestDeepLinkHandlerDeepLinkArgs(
                    @DeeplinkParam(name = "scheme", type = DeepLinkParamType.Path ) val scheme: String,
                    @DeeplinkParam(name = "host", type = DeepLinkParamType.Path ) val host: String,
                    @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: String
                )
                """,
        )

    private val sampleDeeplinkHandlerWithArsWithMultipleConstructors =
        Source.KotlinSource(
            "SampleDeeplinkHandler.kt",
            """
        package com.example
        
        import android.content.Context
        import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
        import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
        import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
        
        @PlaceholderDeepLink("pathSegment/{one}/{two}")
        object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs> {
            override fun handleDeepLink(context: Context, parameters: TestDeepLinkHandlerDeepLinkArgs) {
                TODO("Not yet implemented")
            }
        }
        class TestDeepLinkHandlerDeepLinkArgs @JvmOverloads constructor(
            @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: String,
            @DeeplinkParam(name = "two", type = DeepLinkParamType.Path ) val two: String,
        ) {
        constructor(three: String) this("One", "two")
        }
        """,
        )

    private val sampleDeeplinkHandlerNotAllArgsAnnotated =
        Source.KotlinSource(
            "SampleDeeplinkHandler.kt",
            """
                package com.example
                
                import android.content.Context
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}/{two}?query={something}")
                object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs> {
                    override fun handleDeepLink(context: Context, parameters: TestDeepLinkHandlerDeepLinkArgs) {
                        TODO("Not yet implemented")
                    }
                }
                
                data class TestDeepLinkHandlerDeepLinkArgs (
                    @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: String,
                    @DeeplinkParam(name = "two", type = DeepLinkParamType.Path ) val two: Long,
                    val query: String,
                ) 
                """,
        )

    private val sampleDeeplinkHandlerFewerItemsInArgsThanInUrl =
        Source.KotlinSource(
            "SampleDeeplinkHandler.kt",
            """
                package com.example
                
                import android.content.Context
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}/{two}?query={something}")
                object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs> {
                    override fun handleDeepLink(context: Context, parameters: TestDeepLinkHandlerDeepLinkArgs) {
                        TODO("Not yet implemented")
                    }
                }
                
                data class TestDeepLinkHandlerDeepLinkArgs (
                    @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: String,
                    @DeeplinkParam(name = "two", type = DeepLinkParamType.Path ) val two: Long
                ) 
                """,
        )

    private val sampleDeeplinkHandlerMorePathItemsInArgsThanInUrl =
        Source.KotlinSource(
            "SampleDeeplinkHandler.kt",
            """
                package com.example
                
                import android.content.Context
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}/{two}?query={something}")
                object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs> {
                    override fun handleDeepLink(context: Context, parameters: TestDeepLinkHandlerDeepLinkArgs) {
                        TODO("Not yet implemented")
                    }
                }
                
                data class TestDeepLinkHandlerDeepLinkArgs (
                    @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: String,
                    @DeeplinkParam(name = "two", type = DeepLinkParamType.Path ) val two: Long,
                    @DeeplinkParam(name = "not_existing", type = DeepLinkParamType.Path ) val notExisting: Long
                ) 
                """,
        )

    private val sampleDeeplinkHandlerMoreQueryItemsInArgsThanInUrl =
        Source.KotlinSource(
            "SampleDeeplinkHandler.kt",
            """
                package com.example
                
                import android.content.Context
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}/{two}?query={something}")
                object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs> {
                    override fun handleDeepLink(context: Context, parameters: TestDeepLinkHandlerDeepLinkArgs) {
                        TODO("Not yet implemented")
                    }
                }
                
                data class TestDeepLinkHandlerDeepLinkArgs (
                    @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: String,
                    @DeeplinkParam(name = "two", type = DeepLinkParamType.Path ) val two: Long,
                    @DeeplinkParam(name = "not_existing", type = DeepLinkParamType.Query ) val notExisting: Long
                ) 
                """,
        )

    private val sampleDeeplinkHandlerWrongTypePath =
        Source.KotlinSource(
            "SampleDeeplinkHandler.kt",
            """
                package com.example
                
                import android.content.Context
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                import java.time.LocalDate
                
                @PlaceholderDeepLink("pathSegment/{one}/{two}?query={something}")
                object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs> {
                    override fun handleDeepLink(context: Context, parameters: TestDeepLinkHandlerDeepLinkArgs) {
                        TODO("Not yet implemented")
                    }
                }
                
                data class TestDeepLinkHandlerDeepLinkArgs (
                    @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: LocalDate,
                    @DeeplinkParam(name = "two", type = DeepLinkParamType.Path ) val two: Long,
                    @DeeplinkParam(name = "query", type = DeepLinkParamType.Query ) val query: String,
                ) 
                """,
        )

    private val sampleDeeplinkHandlerWrongTypeQuery =
        Source.KotlinSource(
            "SampleDeeplinkHandler.kt",
            """
                package com.example
                
                import android.content.Context
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}/{two}?query={something}")
                object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs> {
                    override fun handleDeepLink(context: Context, parameters: TestDeepLinkHandlerDeepLinkArgs) {
                        TODO("Not yet implemented")
                    }
                }
                
                data class TestDeepLinkHandlerDeepLinkArgs (
                    @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: String,
                    @DeeplinkParam(name = "two", type = DeepLinkParamType.Path ) val two: Long,
                    @DeeplinkParam(name = "query", type = DeepLinkParamType.Query ) val query: Long,
                ) 
                """,
        )

    private val sampleDeeplinkHandlerExtraAnnotatedItem =
        Source.KotlinSource(
            "SampleDeeplinkHandler.kt",
            """
                package com.example
                
                import android.content.Context
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}?query={something}")
                object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs> {
                    override fun handleDeepLink(context: Context, parameters: TestDeepLinkHandlerDeepLinkArgs) {
                        TODO("Not yet implemented")
                    }
                }
                
                data class TestDeepLinkHandlerDeepLinkArgs (
                    @DeeplinkParam(name = "scheme", type = DeepLinkParamType.Path ) val scheme: String,
                    @DeeplinkParam(name = "host", type = DeepLinkParamType.Path ) val host: String,
                    @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: String,
                    @DeeplinkParam(name = "two", type = DeepLinkParamType.Path ) val two: Long,
                    @DeeplinkParam(name = "query", type = DeepLinkParamType.Query ) val query: String,
                )
                """,
        )

    private val sampleDeeplinkHandlerExtraTemplateItem =
        Source.KotlinSource(
            "SampleDeeplinkHandler.kt",
            """
                package com.example
                
                import android.content.Context
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}/{two}/{three}?query={something}")
                object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs> {
                    override fun handleDeepLink(context: Context, parameters: TestDeepLinkHandlerDeepLinkArgs) {
                        TODO("Not yet implemented")
                    }
                }
                
                data class TestDeepLinkHandlerDeepLinkArgs (
                    @DeeplinkParam(name = "scheme", type = DeepLinkParamType.Path ) val scheme: String,
                    @DeeplinkParam(name = "host", type = DeepLinkParamType.Path ) val host: String,
                    @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: String,
                    @DeeplinkParam(name = "two", type = DeepLinkParamType.Path ) val two: Long,
                    @DeeplinkParam(name = "query", type = DeepLinkParamType.Query ) val query: String,
                )
                """,
        )

    private val sampleDeeplinkHandlerSameNumButDifferentNames =
        Source.KotlinSource(
            "SampleDeeplinkHandler.kt",
            """
                package com.example
                
                import android.content.Context
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}/{two}/{three}?query={something}")
                object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs> {
                    override fun handleDeepLink(context: Context, parameters: TestDeepLinkHandlerDeepLinkArgs) {
                        TODO("Not yet implemented")
                    }
                }
                
                data class TestDeepLinkHandlerDeepLinkArgs (
                    @DeeplinkParam(name = "scheme", type = DeepLinkParamType.Path ) val scheme: String,
                    @DeeplinkParam(name = "host", type = DeepLinkParamType.Path ) val host: String,
                    @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: String,
                    @DeeplinkParam(name = "two", type = DeepLinkParamType.Path ) val two: Long,
                    @DeeplinkParam(name = "four", type = DeepLinkParamType.Path ) val four: Long,
                    @DeeplinkParam(name = "query", type = DeepLinkParamType.Query ) val query: String,
                )
                """,
        )

    private val sampleAnnotatedObjectNotExtendingDeepLinkHandler =
        Source.KotlinSource(
            "SampleDeeplinkHandler.kt",
            """
                package com.example
                
                import android.content.Context
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}/{two}/{three}?query={something}")
                object TestDeepLinkHandler {
                    fun handleDeepLink(context:Context, parameters: TestDeepLinkHandlerDeepLinkArgs) {
                        TODO("Not yet implemented")
                    }
                }
                
                data class TestDeepLinkHandlerDeepLinkArgs (
                    @DeeplinkParam(name = "scheme", type = DeepLinkParamType.Path ) val scheme: String,
                    @DeeplinkParam(name = "host", type = DeepLinkParamType.Path ) val host: String,
                    @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: String,
                    @DeeplinkParam(name = "two", type = DeepLinkParamType.Path ) val two: Long,
                    @DeeplinkParam(name = "four", type = DeepLinkParamType.Path ) val four: Long,
                    @DeeplinkParam(name = "query", type = DeepLinkParamType.Query ) val query: String,
                )
                """,
        )

    private val sampleJavaTestDeepLinkArgs =
        Source.JavaSource(
            "com.example.TestJavaDeepLinkHandlerDeepLinkArgs",
            """
            package com.example;
            import android.content.Context;
            import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType;
            import com.airbnb.deeplinkdispatch.handler.DeeplinkParam;
            public class TestJavaDeepLinkHandlerDeepLinkArgs {
              private final long uuid;
              public TestJavaDeepLinkHandlerDeepLinkArgs(
                // path params can be non null
                @DeeplinkParam(name = "scheme", type = DeepLinkParamType.Path) String scheme,
                @DeeplinkParam(name = "host", type = DeepLinkParamType.Path) String host,
                @DeeplinkParam(name = "path_segment", type = DeepLinkParamType.Path) long uuid
              ) {
                this.uuid = uuid;
              }
            }
            """.trimIndent(),
        )

    private val sampleJavaDeepLinkHandler =
        Source.JavaSource(
            "com.example.SampleJavaDeeplinkHandler",
            """
            package com.example;
            import android.content.Context;
            import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler;
            @PlaceholderDeepLink({"java/{path_segment}"})
            public class SampleJavaDeeplinkHandler implements DeepLinkHandler<TestJavaDeepLinkHandlerDeepLinkArgs> {
              @Override
              public void handleDeepLink(Context context, TestJavaDeepLinkHandlerDeepLinkArgs parameters) {
                /** Do nothing **/
              }
            }    
            """.trimIndent(),
        )

    private val sampleNonPublicJavaDeepLinkHandler =
        Source.JavaSource(
            "com.example.SampleJavaDeeplinkHandler",
            """
            package com.example;
            import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler;
            @PlaceholderDeepLink({"java/{path_segment}"})
            class SampleJavaDeeplinkHandler implements DeepLinkHandler<TestJavaDeepLinkHandlerDeepLinkArgs> {
              @Override
              public void handleDeepLink(Context context, TestJavaDeepLinkHandlerDeepLinkArgs parameters) {
                /** Do nothing **/
              }
            }    
            """.trimIndent(),
        )

    private val sampleNonPublicJavaTestDeepLinkArgs =
        Source.JavaSource(
            "com.example.TestJavaDeepLinkHandlerDeepLinkArgs",
            """
            package com.example;
            import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType;
            import com.airbnb.deeplinkdispatch.handler.DeeplinkParam;
            class TestJavaDeepLinkHandlerDeepLinkArgs {
              private final long uuid;
              public TestJavaDeepLinkHandlerDeepLinkArgs(
                // path params can be non null
                @DeeplinkParam(name = "scheme", type = DeepLinkParamType.Path) String scheme,
                @DeeplinkParam(name = "host", type = DeepLinkParamType.Path) String host,
                @DeeplinkParam(name = "path_segment", type = DeepLinkParamType.Path) long uuid
              ) {
                this.uuid = uuid;
              }
            }
            """.trimIndent(),
        )

    @Test
    fun testJavaDeeplinkHandler() {
        val sourceFiles =
            listOf(
                deeplinkHandlerInterface,
                customAnnotationPlaceholderInSchemeHostAppLink,
                sampleJavaDeepLinkHandler,
                sampleJavaTestDeepLinkArgs,
                module,
                fakeBaseDeeplinkDelegate,
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = false,
                ),
                // TODO Need to figure out why this fails
//            compileIncremental(
//                sourceFiles = sourceFiles,
//                customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
//                useKsp = true,
//            )
            )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries =
                listOf(
                    DeepLinkEntry.HandlerDeepLinkEntry(
                        uriTemplate = "http{scheme}://{host}example.com/java/{path_segment}",
                        className = "com.example.SampleJavaDeeplinkHandler",
                    ),
                ),
            generatedFiles =
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
                            return "\u0001\u0000\u0001\u0000\u0000\u0000\u0000\u0000²r\u0012\u0000\f\u0000\u0000\u0000\u0000\u0000\u009dhttp{scheme}\u0014\u0000\u0011\u0000\u0000\u0000\u0000\u0000\u0083{host}example.com\b\u0000\u0004\u0000\u0000\u0000\u0000\u0000vjava\u0018\u0000\u000e\u0000_\u0000\u0000\u0000\u0000{path_segment}\u0002\u00004http{scheme}://{host}example.com/java/{path_segment}\u0000%com.example.SampleJavaDeeplinkHandler\u0000";
                          }
                        }

                        """.trimIndent(),
                ),
        )
    }

    @Test
    fun testJavaDeeplinkHandlerWithNonPublicArgs() {
        val sourceFiles =
            listOf(
                deeplinkHandlerInterface,
                customAnnotationPlaceholderInSchemeHostAppLink,
                sampleJavaDeepLinkHandler,
                sampleNonPublicJavaTestDeepLinkArgs,
                module,
                fakeBaseDeeplinkDelegate,
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = false,
                ),
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = true,
                ),
            )
        assertCompileError(
            results,
            "Argument class must be public.",
        )
    }

    @Test
    fun testJavaDeeplinkHandlerWithNonPublicHandler() {
        val sourceFiles =
            listOf(
                deeplinkHandlerInterface,
                customAnnotationPlaceholderInSchemeHostAppLink,
                sampleNonPublicJavaDeepLinkHandler,
                sampleJavaTestDeepLinkArgs,
                module,
                fakeBaseDeeplinkDelegate,
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = false,
                ),
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = true,
                ),
            )
        assertCompileError(
            results,
            "Only classes inheriting from either 'android.app.Activity' or public" +
                " classes implementing the 'com.airbnb.deeplinkdispatch.handler" +
                ".DeepLinkHandler' interface can be annotated with @DeepLink or another custom" +
                " deep link annotation.",
        )
    }

    @Test
    fun testDeeplinkHandler() {
        val sourceFiles =
            listOf(
                deeplinkHandlerInterface,
                customAnnotationPlaceholderInSchemeHostAppLink,
                sampleDeeplinkHandler,
                module,
                fakeBaseDeeplinkDelegate,
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = false,
                ),
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = true,
                ),
            )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries =
                listOf(
                    DeepLinkEntry.HandlerDeepLinkEntry(
                        uriTemplate =
                            "http{scheme}://{host}example.com/pathSegment/{path_segment_variable_1}/" +
                                "{path_segment_variable_2}/{path_segment_variable_3}/{path_segment_variable_4}/" +
                                "{path_segment_variable_5}/{path_segment_variable_6}/{path_segment_variable_7}/" +
                                "{path_segment_variable_8}?queryParam1={query_param_1}&queryParam2={query_param_2}" +
                                "&queryParam3={query_param_3}&queryParam4={query_param_4}&queryParam5={query_param_5}" +
                                "&queryParam6={query_param_6}&queryParam7={query_param_7}&queryParam8={query_param_8}",
                        className = "com.example.TestDeepLinkHandler",
                    ),
                ),
            generatedFiles =
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
                            return "\u0001\u0000\u0001\u0000\u0000\u0000\u0000\u0003Tr\u0012\u0000\f\u0000\u0000\u0000\u0000\u0003?http{scheme}\u0014\u0000\u0011\u0000\u0000\u0000\u0000\u0003%{host}example.com\b\u0000\u000b\u0000\u0000\u0000\u0000\u0003\u0011pathSegment\u0018\u0000\u0019\u0000\u0000\u0000\u0000\u0002ï{path_segment_variable_1}\u0018\u0000\u0019\u0000\u0000\u0000\u0000\u0002Í{path_segment_variable_2}\u0018\u0000\u0019\u0000\u0000\u0000\u0000\u0002«{path_segment_variable_3}\u0018\u0000\u0019\u0000\u0000\u0000\u0000\u0002\u0089{path_segment_variable_4}\u0018\u0000\u0019\u0000\u0000\u0000\u0000\u0002g{path_segment_variable_5}\u0018\u0000\u0019\u0000\u0000\u0000\u0000\u0002E{path_segment_variable_6}\u0018\u0000\u0019\u0000\u0000\u0000\u0000\u0002#{path_segment_variable_7}\u0018\u0000\u0019\u0002\u0001\u0000\u0000\u0000\u0000{path_segment_variable_8}\u0002\u0001Ühttp{scheme}://{host}example.com/pathSegment/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}/{path_segment_variable_4}/{path_segment_variable_5}/{path_segment_variable_6}/{path_segment_variable_7}/{path_segment_variable_8}?queryParam1={query_param_1}&queryParam2={query_param_2}&queryParam3={query_param_3}&queryParam4={query_param_4}&queryParam5={query_param_5}&queryParam6={query_param_6}&queryParam7={query_param_7}&queryParam8={query_param_8}\u0000\u001fcom.example.TestDeepLinkHandler\u0000";
                          }
                        }

                        """.trimIndent(),
                ),
        )
    }

    @Test
    fun testDeeplinkHandlerWithoutArgs() {
        val sourceFiles =
            listOf(
                deeplinkHandlerInterface,
                customAnnotationPlaceholderInSchemeHostAppLink,
                sampleDeeplinkHandlerNoArgs,
                module,
                fakeBaseDeeplinkDelegate,
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = false,
                ),
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = true,
                ),
            )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries =
                listOf(
                    DeepLinkEntry.HandlerDeepLinkEntry(
                        uriTemplate =
                            "http{scheme}://{host}example.com/pathSegment/{path_segment_variable_1}/" +
                                "{path_segment_variable_2}/{path_segment_variable_3}/{path_segment_variable_4}/" +
                                "{path_segment_variable_5}/{path_segment_variable_6}/{path_segment_variable_7}/" +
                                "{path_segment_variable_8}?queryParam1={query_param_1}&queryParam2={query_param_2}" +
                                "&queryParam3={query_param_3}&queryParam4={query_param_4}&queryParam5={query_param_5}" +
                                "&queryParam6={query_param_6}&queryParam7={query_param_7}&queryParam8={query_param_8}",
                        className = "com.example.TestDeepLinkHandler",
                    ),
                ),
            generatedFiles =
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
                            return "\u0001\u0000\u0001\u0000\u0000\u0000\u0000\u0003Tr\u0012\u0000\f\u0000\u0000\u0000\u0000\u0003?http{scheme}\u0014\u0000\u0011\u0000\u0000\u0000\u0000\u0003%{host}example.com\b\u0000\u000b\u0000\u0000\u0000\u0000\u0003\u0011pathSegment\u0018\u0000\u0019\u0000\u0000\u0000\u0000\u0002ï{path_segment_variable_1}\u0018\u0000\u0019\u0000\u0000\u0000\u0000\u0002Í{path_segment_variable_2}\u0018\u0000\u0019\u0000\u0000\u0000\u0000\u0002«{path_segment_variable_3}\u0018\u0000\u0019\u0000\u0000\u0000\u0000\u0002\u0089{path_segment_variable_4}\u0018\u0000\u0019\u0000\u0000\u0000\u0000\u0002g{path_segment_variable_5}\u0018\u0000\u0019\u0000\u0000\u0000\u0000\u0002E{path_segment_variable_6}\u0018\u0000\u0019\u0000\u0000\u0000\u0000\u0002#{path_segment_variable_7}\u0018\u0000\u0019\u0002\u0001\u0000\u0000\u0000\u0000{path_segment_variable_8}\u0002\u0001Ühttp{scheme}://{host}example.com/pathSegment/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}/{path_segment_variable_4}/{path_segment_variable_5}/{path_segment_variable_6}/{path_segment_variable_7}/{path_segment_variable_8}?queryParam1={query_param_1}&queryParam2={query_param_2}&queryParam3={query_param_3}&queryParam4={query_param_4}&queryParam5={query_param_5}&queryParam6={query_param_6}&queryParam7={query_param_7}&queryParam8={query_param_8}\u0000\u001fcom.example.TestDeepLinkHandler\u0000";
                          }
                        }

                        """.trimIndent(),
                ),
        )
    }

    @Test
    fun testDeeplinkHandlerWithTwoParameterMethodsWithCorrectName() {
        val sourceFiles =
            listOf(
                deeplinkHandlerInterface,
                customAnnotationPlaceholderInSchemeHostAppLink,
                sampleDeeplinkHandlerWithTwoMethods,
                module,
                fakeBaseDeeplinkDelegate,
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = false,
                ),
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = true,
                ),
            )
        assertCompileError(
            results,
            "More than one method with two parameters and handleDeepLink name found in handler class.",
        )
    }

    @Test
    fun testDeeplinkHandlerWithIntermediateInterface() {
        val sourceFiles =
            listOf(
                deeplinkHandlerInterface,
                customAnnotationPlaceholderInSchemeHostAppLink,
                sampleDeeplinkHandlerWithIntermediateInterface,
                module,
                fakeBaseDeeplinkDelegate,
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = false,
                ),
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = true,
                ),
            )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries =
                listOf(
                    DeepLinkEntry.HandlerDeepLinkEntry(
                        uriTemplate = "http{scheme}://{host}example.com/pathSegment/{one}",
                        className = "com.example.TestDeepLinkHandler",
                    ),
                ),
            generatedFiles =
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
                            return "\u0001\u0000\u0001\u0000\u0000\u0000\u0000\u0000¨r\u0012\u0000\f\u0000\u0000\u0000\u0000\u0000\u0093http{scheme}\u0014\u0000\u0011\u0000\u0000\u0000\u0000\u0000y{host}example.com\b\u0000\u000b\u0000\u0000\u0000\u0000\u0000epathSegment\u0018\u0000\u0005\u0000W\u0000\u0000\u0000\u0000{one}\u0002\u00002http{scheme}://{host}example.com/pathSegment/{one}\u0000\u001fcom.example.TestDeepLinkHandler\u0000";
                          }
                        }

                        """.trimIndent(),
                ),
        )
    }

    @Test
    fun testDeeplinkHandlerWithTwoTypeParameters() {
        val sourceFiles =
            listOf(
                deeplinkHandlerInterface,
                customAnnotationPlaceholderInSchemeHostAppLink,
                sampleDeeplinkHandlerWithIntermediateClassAndTwoTypeParameters,
                module,
                fakeBaseDeeplinkDelegate,
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = false,
                ),
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = true,
                ),
            )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries =
                listOf(
                    DeepLinkEntry.HandlerDeepLinkEntry(
                        uriTemplate = "http{scheme}://{host}example.com/pathSegment/{one}",
                        className = "com.example.TestDeepLinkHandler",
                    ),
                ),
            generatedFiles =
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
                            return "\u0001\u0000\u0001\u0000\u0000\u0000\u0000\u0000¨r\u0012\u0000\f\u0000\u0000\u0000\u0000\u0000\u0093http{scheme}\u0014\u0000\u0011\u0000\u0000\u0000\u0000\u0000y{host}example.com\b\u0000\u000b\u0000\u0000\u0000\u0000\u0000epathSegment\u0018\u0000\u0005\u0000W\u0000\u0000\u0000\u0000{one}\u0002\u00002http{scheme}://{host}example.com/pathSegment/{one}\u0000\u001fcom.example.TestDeepLinkHandler\u0000";
                          }
                        }

                        """.trimIndent(),
                ),
        )
    }

    @Test
    fun testDeeplinkHandlerWithArgsWithMultipleConstructors() {
        val sourceFiles =
            listOf(
                deeplinkHandlerInterface,
                customAnnotationPlaceholderInSchemeHostAppLink,
                sampleDeeplinkHandlerWithArsWithMultipleConstructors,
                module,
                fakeBaseDeeplinkDelegate,
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = false,
                ),
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = true,
                ),
            )
        assertCompileError(
            results,
            "Argument class for deeplink handler can only have a single constructor",
        )
    }

    @Test
    fun testDeeplinkHandlerNotAllArgsAnnotated() {
        val sourceFiles =
            listOf(
                deeplinkHandlerInterface,
                customAnnotationPlaceholderInSchemeHostAppLink,
                sampleDeeplinkHandlerNotAllArgsAnnotated,
                module,
                fakeBaseDeeplinkDelegate,
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = false,
                ),
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = true,
                ),
            )
        assertCompileError(
            results,
            "Parameters: one, two, query Annotated parameters: one, two",
        )
    }

    @Test
    fun testDeeplinkHandlerFewerItemsInArgsThanUrlButAllAnnotated() {
        val sourceFiles =
            listOf(
                deeplinkHandlerInterface,
                customAnnotationPlaceholderInSchemeHostAppLink,
                sampleDeeplinkHandlerFewerItemsInArgsThanInUrl,
                module,
                fakeBaseDeeplinkDelegate,
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = false,
                ),
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = true,
                ),
            )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries =
                listOf(
                    DeepLinkEntry.HandlerDeepLinkEntry(
                        uriTemplate = "http{scheme}://{host}example.com/pathSegment/{one}/{two}?query={something}",
                        className = "com.example.TestDeepLinkHandler",
                    ),
                ),
            generatedFiles =
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
                            return "\u0001\u0000\u0001\u0000\u0000\u0000\u0000\u0000Îr\u0012\u0000\f\u0000\u0000\u0000\u0000\u0000¹http{scheme}\u0014\u0000\u0011\u0000\u0000\u0000\u0000\u0000\u009f{host}example.com\b\u0000\u000b\u0000\u0000\u0000\u0000\u0000\u008bpathSegment\u0018\u0000\u0005\u0000\u0000\u0000\u0000\u0000}{one}\u0018\u0000\u0005\u0000o\u0000\u0000\u0000\u0000{two}\u0002\u0000Jhttp{scheme}://{host}example.com/pathSegment/{one}/{two}?query={something}\u0000\u001fcom.example.TestDeepLinkHandler\u0000";
                          }
                        }

                        """.trimIndent(),
                ),
        )
    }

    @Test
    fun testDeeplinkHandlerMorePathItemsInArgsThanUrlButAllAnnotated() {
        val sourceFiles =
            listOf(
                deeplinkHandlerInterface,
                customAnnotationPlaceholderInSchemeHostAppLink,
                sampleDeeplinkHandlerMorePathItemsInArgsThanInUrl,
                module,
                fakeBaseDeeplinkDelegate,
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = false,
                ),
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = true,
                ),
            )
        assertCompileError(
            results,
            "The annotated path arguments in the arguments class must be a subset of the path placeholders contained in the url. Annotated in args class but not in uri template: not_existing",
        )
    }

    @Test
    fun testDeeplinkHandlerMoreQueryItemsInArgsThanUrlButAllAnnotated() {
        val sourceFiles =
            listOf(
                deeplinkHandlerInterface,
                customAnnotationPlaceholderInSchemeHostAppLink,
                sampleDeeplinkHandlerMoreQueryItemsInArgsThanInUrl,
                module,
                fakeBaseDeeplinkDelegate,
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = false,
                ),
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = true,
                ),
            )
        assertGeneratedCode(
            results = results,
            registryClassName = "com.example.SampleModuleRegistry",
            indexEntries =
                listOf(
                    DeepLinkEntry.HandlerDeepLinkEntry(
                        uriTemplate = "http{scheme}://{host}example.com/pathSegment/{one}/{two}?query={something}",
                        className = "com.example.TestDeepLinkHandler",
                    ),
                ),
            generatedFiles =
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
                            return "\u0001\u0000\u0001\u0000\u0000\u0000\u0000\u0000Îr\u0012\u0000\f\u0000\u0000\u0000\u0000\u0000¹http{scheme}\u0014\u0000\u0011\u0000\u0000\u0000\u0000\u0000\u009f{host}example.com\b\u0000\u000b\u0000\u0000\u0000\u0000\u0000\u008bpathSegment\u0018\u0000\u0005\u0000\u0000\u0000\u0000\u0000}{one}\u0018\u0000\u0005\u0000o\u0000\u0000\u0000\u0000{two}\u0002\u0000Jhttp{scheme}://{host}example.com/pathSegment/{one}/{two}?query={something}\u0000\u001fcom.example.TestDeepLinkHandler\u0000";
                          }
                        }

                        """.trimIndent(),
                ),
        )
    }

    @Test
    fun testDeeplinkHandlerExtraAnnotatedItem() {
        val sourceFiles =
            listOf(
                deeplinkHandlerInterface,
                customAnnotationPlaceholderInSchemeHostAppLink,
                sampleDeeplinkHandlerExtraAnnotatedItem,
                module,
                fakeBaseDeeplinkDelegate,
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = false,
                ),
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = true,
                ),
            )
        assertCompileError(
            results,
            "The annotated path arguments in the arguments class must be a subset of the path placeholders contained in the url. Annotated in args class but not in uri template: two",
        )
    }

    @Test
    fun testDeeplinkHandlerSameNumArgumentButDifferent() {
        val sourceFiles =
            listOf(
                deeplinkHandlerInterface,
                deeplinkHandlerInterface,
                customAnnotationPlaceholderInSchemeHostAppLink,
                sampleDeeplinkHandlerSameNumButDifferentNames,
                module,
                fakeBaseDeeplinkDelegate,
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = false,
                ),
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = true,
                ),
            )
        assertCompileError(
            results,
            "The annotated path arguments in the arguments class must be a subset of the path placeholders contained in the url. Annotated in args class but not in uri template: four",
        )
    }

    @Test
    fun testAnnotatedObjectNotExtendingDeepLinkHandler() {
        val sourceFiles =
            listOf(
                deeplinkHandlerInterface,
                customAnnotationPlaceholderInSchemeHostAppLink,
                sampleAnnotatedObjectNotExtendingDeepLinkHandler,
                module,
                fakeBaseDeeplinkDelegate,
            )
        val results =
            listOf(
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = false,
                ),
                compileIncremental(
                    sourceFiles = sourceFiles,
                    customDeepLinks = listOf("com.example.PlaceholderDeepLink"),
                    useKsp = true,
                ),
            )
        assertCompileError(
            results,
            "Only public objects implementing com.airbnb.deeplinkdispatch.handler.DeepLinkHandler can be annotated with @DeepLink or any custom deep link annotation",
        )
    }
}
