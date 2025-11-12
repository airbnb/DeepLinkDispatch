package com.airbnb.deeplinkdispatch.metadata.writers

import androidx.room.compiler.codegen.toJavaPoet
import androidx.room.compiler.processing.XMessager
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XTypeElement
import com.airbnb.deeplinkdispatch.DeepLinkAnnotatedElement
import com.squareup.javapoet.ClassName
import com.squareup.kotlinpoet.javapoet.KotlinPoetJavaPoetPreview
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.PrintWriter
import java.io.StringWriter
import javax.tools.Diagnostic

class ManifestWriterTest {
    private val messager = mockk<XMessager>(relaxed = true)
    private val processingEnv = mockk<XProcessingEnv>()

    init {
        every { processingEnv.messager } returns messager
    }

    /**
     * Test simple URL without any placeholders
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testSimpleUrlWithoutPlaceholders() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements = listOf(
            createActivityElement(
                uri = "https://example.com/path",
                activityClassFqn = "com.example.TestActivity"
            )
        )

        writer.write(processingEnv, printWriter, elements)

        val expected = """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.TestActivity" android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/path" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

        """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test URL with scheme placeholder that expands to http and https
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testUrlWithSchemePlaceholder() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements = listOf(
            createActivityElement(
                uri = "http{scheme(|s)}://example.com/path",
                activityClassFqn = "com.example.TestActivity"
            )
        )

        writer.write(processingEnv, printWriter, elements)

        val expected = """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.TestActivity" android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="http" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/path" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

        """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test URL with host prefix placeholder
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testUrlWithHostPrefixPlaceholder() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements = listOf(
            createActivityElement(
                uri = "https://{host_prefix(|de.|ro.|www.)}example.com/guest",
                activityClassFqn = "com.example.GuestActivity"
            )
        )

        writer.write(processingEnv, printWriter, elements)

        val expected = """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.GuestActivity" android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:host="de.example.com" />
                            <data android:host="ro.example.com" />
                            <data android:host="www.example.com" />
                            <data android:path="/guest" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

        """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test URL with path parameter placeholder that has no allowed values (should become .*)
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testUrlWithPathParameterPlaceholder() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements = listOf(
            createActivityElement(
                uri = "https://example.com/user/{id}",
                activityClassFqn = "com.example.UserActivity"
            )
        )

        writer.write(processingEnv, printWriter, elements)

        val expected = """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.UserActivity" android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:pathPattern="/user/.*" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

        """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test URL with multiple path segments and parameters
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testUrlWithMultiplePathParameters() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements = listOf(
            createActivityElement(
                uri = "https://example.com/user/{id}/profile/{section}",
                activityClassFqn = "com.example.ProfileActivity"
            )
        )

        writer.write(processingEnv, printWriter, elements)

        val expected = """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.ProfileActivity" android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:pathPattern="/user/.*/profile/.*" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

        """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test URL with scheme and host placeholders combined
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testUrlWithMultiplePlaceholders() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements = listOf(
            createActivityElement(
                uri = "http{scheme(|s)}://{prefix(www.|m.)}example.com/path",
                activityClassFqn = "com.example.TestActivity"
            )
        )

        writer.write(processingEnv, printWriter, elements)

        val expected = """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.TestActivity" android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="http" />
                            <data android:scheme="https" />
                            <data android:host="www.example.com" />
                            <data android:host="m.example.com" />
                            <data android:path="/path" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

        """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test path without parameters should use android:path, not android:pathPattern
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testStaticPathUsesPathNotPathPattern() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements = listOf(
            createActivityElement(
                uri = "https://example.com/static/path",
                activityClassFqn = "com.example.StaticActivity"
            )
        )

        writer.write(processingEnv, printWriter, elements)

        val expected = """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.StaticActivity" android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/static/path" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

        """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test multiple activities with same scheme/host but different paths
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testMultipleActivitiesSameSchemeHostDifferentPaths() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements = listOf(
            createActivityElement(
                uri = "https://example.com/path1",
                activityClassFqn = "com.example.Activity1"
            ),
            createActivityElement(
                uri = "https://example.com/path2",
                activityClassFqn = "com.example.Activity2"
            )
        )

        writer.write(processingEnv, printWriter, elements)

        val expected = """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.Activity1" android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/path1" />
                        </intent-filter>
                    </activity>
                    <activity
                        android:name="com.example.Activity2" android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/path2" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

        """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test multiple URLs for same activity with same scheme/host are grouped in one intent filter
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testMultipleUrlsSameActivityGrouped() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements = listOf(
            createActivityElement(
                uri = "https://example.com/path1",
                activityClassFqn = "com.example.TestActivity"
            ),
            createActivityElement(
                uri = "https://example.com/path2",
                activityClassFqn = "com.example.TestActivity"
            )
        )

        writer.write(processingEnv, printWriter, elements)

        val expected = """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.TestActivity" android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/path1" />
                            <data android:path="/path2" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

        """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test multiple URLs with different schemes/hosts for same activity creates separate intent filters
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testMultipleUrlsDifferentSchemeHostSameActivity() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements = listOf(
            createActivityElement(
                uri = "https://example.com/path",
                activityClassFqn = "com.example.TestActivity"
            ),
            createActivityElement(
                uri = "myapp://custom/path",
                activityClassFqn = "com.example.TestActivity"
            )
        )

        writer.write(processingEnv, printWriter, elements)

        val expected = """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.TestActivity" android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/path" />
                        </intent-filter>
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="myapp" />
                            <data android:host="custom" />
                            <data android:path="/path" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

        """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test elements without activityClassFqn are filtered out
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testElementsWithoutActivityClassFqnFiltered() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements = listOf(
            createActivityElement(
                uri = "https://example.com/path1",
                activityClassFqn = null // No activity class specified
            ),
            createActivityElement(
                uri = "https://example.com/path2",
                activityClassFqn = "com.example.TestActivity"
            )
        )

        writer.write(processingEnv, printWriter, elements)

        val expected = """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.TestActivity" android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/path2" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

        """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test path with allowed values creates multiple path entries
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testPathWithAllowedValues() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements = listOf(
            createActivityElement(
                uri = "https://example.com/{section(home|profile|settings)}",
                activityClassFqn = "com.example.TestActivity"
            )
        )

        writer.write(processingEnv, printWriter, elements)

        val expected = """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.TestActivity" android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/home" />
                            <data android:path="/profile" />
                            <data android:path="/settings" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

        """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test complex real-world URL with multiple placeholders
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testComplexRealWorldUrl() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements = listOf(
            createActivityElement(
                uri = "http{scheme(|s)}://{prefix(|www.|m.)}myapp.com/items/{id}",
                activityClassFqn = "com.example.ItemActivity"
            )
        )

        writer.write(processingEnv, printWriter, elements)

        val expected = """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.ItemActivity" android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="http" />
                            <data android:scheme="https" />
                            <data android:host="myapp.com" />
                            <data android:host="www.myapp.com" />
                            <data android:host="m.myapp.com" />
                            <data android:pathPattern="/items/.*" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

        """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test empty list produces minimal manifest
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testEmptyElementsList() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        writer.write(processingEnv, printWriter, emptyList())

        val expected = """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                </application>
            </manifest>

        """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test that configurable path segments with activityClassFqn produce compilation error
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testConfigurablePathSegmentWithActivityClassFqnProducesError() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val element = createActivityElement(
            uri = "https://example.com/<configurable-path-segment>/bar",
            activityClassFqn = "com.example.TestActivity"
        )

        val elements = listOf(element)

        writer.write(processingEnv, printWriter, elements)

        // Verify that an error was printed
        verify {
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                match { message ->
                    message.contains("Manifest generation is not supported for deep links using configurable path segments") &&
                    message.contains("https://example.com/<configurable-path-segment>/bar") &&
                    message.contains("Please remove the activityClassFqn parameter from @DeepLink annotation")
                },
                element.element
            )
        }
    }

    /**
     * Test that configurable path segments without activityClassFqn do not produce error
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testConfigurablePathSegmentWithoutActivityClassFqnNoError() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements = listOf(
            createActivityElement(
                uri = "https://example.com/<configurable-path-segment>/bar",
                activityClassFqn = null // No activity class specified
            )
        )

        writer.write(processingEnv, printWriter, elements)

        // Verify that no error was printed
        verify(exactly = 0) {
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                any(),
                any()
            )
        }

        // Should produce minimal manifest since element is filtered out
        val expected = """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                </application>
            </manifest>

        """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    @OptIn(KotlinPoetJavaPoetPreview::class)
    private fun createActivityElement(
        uri: String,
        activityClassFqn: String?
    ): DeepLinkAnnotatedElement.ActivityAnnotatedElement {
        val typeElement = mockk<XTypeElement>()
        every { typeElement.asClassName().toJavaPoet() } returns ClassName.get("", "TestClass")
        every { typeElement.docComment } returns null

        return DeepLinkAnnotatedElement.ActivityAnnotatedElement(
            uri = uri,
            activityClassFqn = activityClassFqn,
            element = typeElement
        )
    }
}
