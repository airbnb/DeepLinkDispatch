//package com.airbnb.deeplinkdispatch
//
//import com.google.common.truth.Truth.assert_
//import com.google.testing.compile.JavaFileObjects
//import com.google.testing.compile.JavaSourcesSubjectFactory.javaSources
//import javax.tools.JavaFileObject
//import org.junit.Test
//
//class BaseDeepLinkDelegateTest {
//    private val complexDeepLinkModuleLoader: JavaFileObject = JavaFileObjects.forSourceLines(
//            "$packageName.ComplexDeepLinkModuleLoader",
//            """
//            package $packageName;
//
//            import com.airbnb.deeplinkdispatch.DeepLinkEntry;
//            import com.airbnb.deeplinkdispatch.Parser;
//            import java.util.Arrays;
//            import java.util.Collections;
//
//            public final class ComplexDeepLinkModuleLoader extends Parser {
//                public ComplexDeepLinkModuleLoader() {
//                    super(Collections.unmodifiableList(Arrays.<DeepLinkEntry>asList(
//                        new DeepLinkEntry("http://www.airbnb.at/reservations/create/{deep_link_listing_id}", DeepLinkEntry.Type.METHOD, P3DeepLinkIntents.class, "forWebLink")
//                    )));
//                }
//            }
//            """
//    )
//
//    @Test
//    fun pleaseWork() {
//        assert_()
//                .about(javaSources())
//                .that(listOf(complexDeepLinkModuleLoader))
//                .compilesWithoutError()
//
//    }
//
//    companion object {
//        const val packageName: String = "com.airbnb.deeplinkDispatch"
//    }
//}