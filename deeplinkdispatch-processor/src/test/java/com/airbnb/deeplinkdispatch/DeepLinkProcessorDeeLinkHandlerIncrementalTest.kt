package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.handler.DEEP_LINK_HANDLER_METHOD_NAME
import com.airbnb.deeplinkdispatch.test.Source
import org.junit.Test

class DeepLinkProcessorDeeLinkHandlerIncrementalTest : BaseDeepLinkProcessorTest() {
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
    private val sampleDeeplinkHandler = Source.KotlinSource(
        "SampleDeeplinkHandler.kt",
        """
            package com.example
            import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
            import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
            import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
            import java.time.LocalDate
            @PlaceholderDeepLink("pathSegment/{path_segment_variable_1}/{path_segment_variable_2}/{path_segment_variable_3}/{path_segment_variable_4}/{path_segment_variable_5}/{path_segment_variable_6}/{path_segment_variable_7}/{path_segment_variable_8}?queryParam1={query_param_1}&queryParam2={query_param_2}&queryParam3={query_param_3}&queryParam4={query_param_4}&queryParam5={query_param_5}&queryParam6={query_param_6}&queryParam7={query_param_7}&queryParam8={query_param_8}")
            object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs>() {
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

    private val sampleDeeplinkHandlerWithSingleParameterConstructor = Source.KotlinSource(
        "SampleDeeplinkHandler.kt",
        """
            package com.example
            
            import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
            import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
            import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
            
            @PlaceholderDeepLink("pathSegment/{one}")
            class TestDeepLinkHandler(val someValue: String) : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs>() {
                override fun handleDeepLink(parameters: TestDeepLinkHandlerDeepLinkArgs) {
                    TODO("Not yet implemented")
                }
            }
            data class TestDeepLinkHandlerDeepLinkArgs(
                @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: String,
            )
            """
    )

    private val sampleDeeplinkHandlerWithTwoMethods = Source.KotlinSource(
        "SampleDeeplinkHandler.kt",
        """
                package com.example
                
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}")
                object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs>() {
                    override fun handleDeepLink(parameters: TestDeepLinkHandlerDeepLinkArgs) {
                        TODO("Not yet implemented")
                    }
                    
                    fun handleDeepLink(string: String) {
                        TODO("Not yet implemented")
                    }
                }
            """
    )

    private val sampleDeeplinkHandlerWithTwoTypeParameters = Source.KotlinSource(
        "SampleDeeplinkHandler.kt",
        """
                package com.example
                
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}")
                object TestDeepLinkHandler() : InBetweenDeeplinkHandler<TestDeepLinkHandlerDeepLinkArgs, String>() {
                    override fun handleDeepLink(parameters: TestDeepLinkHandlerDeepLinkArgs) {
                        TODO("Not yet implemented")
                    }
                }
                
                open class InBetweenDeeplinkHandler<T, U>() : DeepLinkHandler<T>() {
                    override fun handleDeepLink(parameters: T) {
                        TODO("Not yet implemented")
                    }
                }
                
                data class TestDeepLinkHandlerDeepLinkArgs(
                    @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: String,
                )
                """
    )

    private val sampleDeeplinkHandlerWithArsWithMultipleConstructors = Source.KotlinSource(
        "SampleDeeplinkHandler.kt",
        """
        package com.example
        
        import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
        import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
        import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
        
        @PlaceholderDeepLink("pathSegment/{one}")
        object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs>() {
            override fun handleDeepLink(parameters: TestDeepLinkHandlerDeepLinkArgs) {
                TODO("Not yet implemented")
            }
        }
        class TestDeepLinkHandlerDeepLinkArgs @JvmOverloads constructor(
            @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: String,
            @DeeplinkParam(name = "two", type = DeepLinkParamType.Path ) val two: String = "test",
        ) {
        constructor(three: String) this("One", "two")
        }
        """
    )

    private val sampleDeeplinkHandlerNotAllArgsAnnotated = Source.KotlinSource(
        "SampleDeeplinkHandler.kt",
        """
                package com.example
                
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}/{two}?query={something}")
                object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs>() {
                    override fun handleDeepLink(parameters: TestDeepLinkHandlerDeepLinkArgs) {
                        TODO("Not yet implemented")
                    }
                }
                
                data class TestDeepLinkHandlerDeepLinkArgs (
                    @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: String,
                    @DeeplinkParam(name = "two", type = DeepLinkParamType.Path ) val two: Long,
                    val query: String,
                ) 
                """
    )

    private val sampleDeeplinkHandlerWrongTypePath = Source.KotlinSource(
        "SampleDeeplinkHandler.kt",
        """
                package com.example
                
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                import java.time.LocalDate
                
                @PlaceholderDeepLink("pathSegment/{one}/{two}?query={something}")
                object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs>() {
                    override fun handleDeepLink(parameters: TestDeepLinkHandlerDeepLinkArgs) {
                        TODO("Not yet implemented")
                    }
                }
                
                data class TestDeepLinkHandlerDeepLinkArgs (
                    @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: LocalDate,
                    @DeeplinkParam(name = "two", type = DeepLinkParamType.Path ) val two: Long,
                    @DeeplinkParam(name = "query", type = DeepLinkParamType.Query ) val query: String,
                ) 
                """
    )

    private val sampleDeeplinkHandlerWrongTypeQuery = Source.KotlinSource(
        "SampleDeeplinkHandler.kt",
        """
                package com.example
                
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}/{two}?query={something}")
                object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs>() {
                    override fun handleDeepLink(parameters: TestDeepLinkHandlerDeepLinkArgs) {
                        TODO("Not yet implemented")
                    }
                }
                
                data class TestDeepLinkHandlerDeepLinkArgs (
                    @DeeplinkParam(name = "one", type = DeepLinkParamType.Path ) val one: String,
                    @DeeplinkParam(name = "two", type = DeepLinkParamType.Path ) val two: Long,
                    @DeeplinkParam(name = "query", type = DeepLinkParamType.Query ) val query: Long,
                ) 
                """
    )

    private val sampleDeeplinkHandlerExtraAnnotatedItem = Source.KotlinSource(
        "SampleDeeplinkHandler.kt",
        """
                package com.example
                
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}?query={something}")
                object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs>() {
                    override fun handleDeepLink(parameters: TestDeepLinkHandlerDeepLinkArgs) {
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
                """
    )

    private val sampleDeeplinkHandlerExtraTemplateItem = Source.KotlinSource(
        "SampleDeeplinkHandler.kt",
        """
                package com.example
                
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}/{two}/{three}?query={something}")
                object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs>() {
                    override fun handleDeepLink(parameters: TestDeepLinkHandlerDeepLinkArgs) {
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
                """
    )

    private val sampleDeeplinkHandlerSameNumButDifferentNames = Source.KotlinSource(
        "SampleDeeplinkHandler.kt",
        """
                package com.example
                
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}/{two}/{three}?query={something}")
                object TestDeepLinkHandler : DeepLinkHandler<TestDeepLinkHandlerDeepLinkArgs>() {
                    override fun handleDeepLink(parameters: TestDeepLinkHandlerDeepLinkArgs) {
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
                """
    )

    private val sampleAnnotatedObjectNotExtendingDeepLinkHandler = Source.KotlinSource(
        "SampleDeeplinkHandler.kt",
        """
                package com.example
                
                import com.airbnb.deeplinkdispatch.handler.DeepLinkHandler
                import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType
                import com.airbnb.deeplinkdispatch.handler.DeeplinkParam
                
                @PlaceholderDeepLink("pathSegment/{one}/{two}/{three}?query={something}")
                object TestDeepLinkHandler {
                    fun handleDeepLink(parameters: TestDeepLinkHandlerDeepLinkArgs) {
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
                """
    )

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

    @Test
    fun testDeeplinkHandlerWithTwoSingleParameterMethodsWithCorrectName() {
        val sourceFiles = listOf(
            customAnnotationPlaceholderInSchemeHostAppLink,
            sampleDeeplinkHandlerWithTwoMethods,
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
            "More than one method with a single parameter and $DEEP_LINK_HANDLER_METHOD_NAME name found in handler class."
        )
    }

    @Test
    fun testDeeplinkHandlerWithTwoTypeParameters() {
        val sourceFiles = listOf(
            customAnnotationPlaceholderInSchemeHostAppLink,
            sampleDeeplinkHandlerWithTwoTypeParameters,
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
            "Only one type argument allowed for DeepLinkHandler objects"
        )
    }

    @Test
    fun testDeeplinkHandlerWithArgsWithMultipleConstructors() {
        val sourceFiles = listOf(
            customAnnotationPlaceholderInSchemeHostAppLink,
            sampleDeeplinkHandlerWithArsWithMultipleConstructors,
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
            "Argument class can only have one constructor"
        )
    }

    @Test
    fun testDeeplinkHandlerNotAllArgsAnnotated() {
        val sourceFiles = listOf(
            customAnnotationPlaceholderInSchemeHostAppLink,
            sampleDeeplinkHandlerNotAllArgsAnnotated,
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
            "Parameters: one, two, query Annotated parameters: one, two"
        )
    }

    @Test
    fun testDeeplinkHandlerWrongTypePath() {
        val sourceFiles = listOf(
            customAnnotationPlaceholderInSchemeHostAppLink,
            sampleDeeplinkHandlerWrongTypePath,
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
            "For args constructor elements of type Path only the following simple types are allowed: byte, short, int, long, float, double, boolean, java.lang.String"
        )
    }

    @Test
    fun testDeeplinkHandlerWrongTypeQuery() {
        val sourceFiles = listOf(
            customAnnotationPlaceholderInSchemeHostAppLink,
            sampleDeeplinkHandlerWrongTypeQuery,
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
            "For args constructor elements of type Query only the following simple types are allowed: java.lang.Byte, java.lang.Short, java.lang.Integer, java.lang.Long, java.lang.Float, java.lang.Double, java.lang.Boolean, java.lang.String"
        )
    }

    @Test
    fun testDeeplinkHandlerExtraAnnotatedItem() {
        val sourceFiles = listOf(
            customAnnotationPlaceholderInSchemeHostAppLink,
            sampleDeeplinkHandlerExtraAnnotatedItem,
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
            "All scheme/host/path placeholders in the uri template must be annotated in the argument class constructor. Present in urlTemplate: scheme, host, one Present in constructor: scheme, host, one, two"
        )
    }

    @Test
    fun testDeeplinkHandlerExtraTemplateItem() {
        val sourceFiles = listOf(
            customAnnotationPlaceholderInSchemeHostAppLink,
            sampleDeeplinkHandlerExtraTemplateItem,
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
            "All scheme/host/path placeholders in the uri template must be annotated in the argument class constructor. Present in urlTemplate: host, scheme, two, three, one Present in constructor: scheme, host, one, two"
        )
    }

    @Test
    fun testDeeplinkHandlerSameNumArgumentButDifferent() {
        val sourceFiles = listOf(
            customAnnotationPlaceholderInSchemeHostAppLink,
            sampleDeeplinkHandlerSameNumButDifferentNames,
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
            "All scheme/host/path placeholders in the uri template must be annotated in the argument class constructor. Present in urlTemplate: host, scheme, two, three, one Present in constructor: scheme, host, one, two, four"
        )
    }

    @Test
    fun testAnnotatedObjectNotExtendingDeepLinkHandler() {
        val sourceFiles = listOf(
            customAnnotationPlaceholderInSchemeHostAppLink,
            sampleAnnotatedObjectNotExtendingDeepLinkHandler,
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
            "Only objects extending com.airbnb.deeplinkdispatch.handler.DeepLinkHandler can be annotated with @DeepLink"
        )
    }
}
