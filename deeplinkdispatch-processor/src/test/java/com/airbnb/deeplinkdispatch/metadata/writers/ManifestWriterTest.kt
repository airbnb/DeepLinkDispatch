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
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.PrintWriter
import java.io.StringWriter

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

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/path",
                    activityClassFqn = "com.example.TestActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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

        val elements =
            listOf(
                createActivityElement(
                    uri = "http{scheme(|s)}://example.com/path",
                    activityClassFqn = "com.example.TestActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://{host_prefix(|de.|ro.|www.)}example.com/guest",
                    activityClassFqn = "com.example.GuestActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/user/{id}",
                    activityClassFqn = "com.example.UserActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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
                            <data android:pathPattern="/user/..*" />
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

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/user/{id}/profile/{section}",
                    activityClassFqn = "com.example.ProfileActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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
                            <data android:pathPattern="/user/..*/profile/..*" />
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

        val elements =
            listOf(
                createActivityElement(
                    uri = "http{scheme(|s)}://{prefix(www.|m.)}example.com/path",
                    activityClassFqn = "com.example.TestActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/static/path",
                    activityClassFqn = "com.example.StaticActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/path1",
                    activityClassFqn = "com.example.Activity1",
                ),
                createActivityElement(
                    uri = "https://example.com/path2",
                    activityClassFqn = "com.example.Activity2",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/path1",
                    activityClassFqn = "com.example.TestActivity",
                ),
                createActivityElement(
                    uri = "https://example.com/path2",
                    activityClassFqn = "com.example.TestActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/path",
                    activityClassFqn = "com.example.TestActivity",
                ),
                createActivityElement(
                    uri = "myapp://custom/path",
                    activityClassFqn = "com.example.TestActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/path1",
                    activityClassFqn = null, // No activity class specified
                ),
                createActivityElement(
                    uri = "https://example.com/path2",
                    activityClassFqn = "com.example.TestActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/{section(home|profile|settings)}",
                    activityClassFqn = "com.example.TestActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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

        val elements =
            listOf(
                createActivityElement(
                    uri = "http{scheme(|s)}://{prefix(|www.|m.)}myapp.com/items/{id}",
                    activityClassFqn = "com.example.ItemActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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
                            <data android:pathPattern="/items/..*" />
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

        val expected =
            """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                </application>
            </manifest>

            """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test configurable path segment in the middle of the path
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testConfigurablePathSegmentInMiddle() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/<configurable-path-segment>/bar",
                    activityClassFqn = "com.example.TestActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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
                            <data android:path="${'$'}{configurable-path-segment}/bar" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

            """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test configurable path segment at the beginning of the path
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testConfigurablePathSegmentAtBeginning() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/<start>/middle/end",
                    activityClassFqn = "com.example.TestActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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
                            <data android:path="${'$'}{start}/middle/end" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

            """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test configurable path segment at the end of the path
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testConfigurablePathSegmentAtEnd() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/path/<end>",
                    activityClassFqn = "com.example.TestActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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
                            <data android:path="/path${'$'}{end}" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

            """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test multiple configurable path segments in one URL
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testMultipleConfigurablePathSegments() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/<first>/<second>/bar",
                    activityClassFqn = "com.example.TestActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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
                            <data android:path="${'$'}{first}${'$'}{second}/bar" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

            """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test mix of configurable path segments and dynamic parameters
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testConfigurablePathSegmentWithDynamicParameter() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/<config>/{id}/bar",
                    activityClassFqn = "com.example.TestActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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
                            <data android:pathPattern="${'$'}{config}/..*/bar" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

            """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test configurable path segment with hyphens and underscores
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testConfigurablePathSegmentWithComplexName() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/<my-config_segment>/bar",
                    activityClassFqn = "com.example.TestActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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
                            <data android:path="${'$'}{my-config_segment}/bar" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

            """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test with single intentFilterAttribute (android:autoVerify="true")
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testIntentFilterWithAutoVerifyAttribute() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/path",
                    activityClassFqn = "com.example.TestActivity",
                    intentFilterAttributes = setOf("android:autoVerify=\"true\""),
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.TestActivity" android:exported="true">
                        <intent-filter android:autoVerify="true">
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
     * Test with multiple intentFilterAttributes
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testIntentFilterWithMultipleAttributes() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/path",
                    activityClassFqn = "com.example.TestActivity",
                    intentFilterAttributes = setOf("android:autoVerify=\"true\"", "android:label=\"@string/app_name\""),
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.TestActivity" android:exported="true">
                        <intent-filter android:autoVerify="true" android:label="@string/app_name">
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
     * Test that different intentFilterAttributes create separate activity blocks
     * (this is the current behavior - different intentFilterAttributes result in separate groupings)
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testDifferentIntentFilterAttributesCreateSeparateActivityBlocks() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/path1",
                    activityClassFqn = "com.example.TestActivity",
                    intentFilterAttributes = setOf("android:autoVerify=\"true\""),
                ),
                createActivityElement(
                    uri = "https://example.com/path2",
                    activityClassFqn = "com.example.TestActivity",
                    intentFilterAttributes = emptySet(),
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.TestActivity" android:exported="true">
                        <intent-filter android:autoVerify="true">
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/path1" />
                        </intent-filter>
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
     * Test that elements with the same IntentFilterGroup (same actions, categories, and intentFilterAttributes)
     * but different scheme/host are grouped together in one activity block with multiple intent-filters
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testSameIntentFilterGroupDifferentSchemeHostCreatesMultipleIntentFilters() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/path",
                    activityClassFqn = "com.example.TestActivity",
                ),
                createActivityElement(
                    uri = "myapp://custom/path",
                    activityClassFqn = "com.example.TestActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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
     * Test that elements with different actions create separate activity blocks
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testDifferentActionsCreateSeparateActivityBlocks() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/path1",
                    activityClassFqn = "com.example.TestActivity",
                    actions = setOf("android.intent.action.VIEW"),
                ),
                createActivityElement(
                    uri = "https://example.com/path2",
                    activityClassFqn = "com.example.TestActivity",
                    actions = setOf("android.intent.action.SEND"),
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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
                        </intent-filter>
                        <intent-filter>
                            <action android:name="android.intent.action.SEND" />
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
     * Test that elements with different categories create separate activity blocks
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testDifferentCategoriesCreateSeparateActivityBlocks() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/path1",
                    activityClassFqn = "com.example.TestActivity",
                    categories = setOf("android.intent.category.DEFAULT", "android.intent.category.BROWSABLE"),
                ),
                createActivityElement(
                    uri = "https://example.com/path2",
                    activityClassFqn = "com.example.TestActivity",
                    categories = setOf("android.intent.category.DEFAULT"),
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
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
                        </intent-filter>
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
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
     * Test that elements with identical IntentFilterGroup (same actions, categories, intentFilterAttributes)
     * and same scheme/host but different paths are grouped in one intent-filter
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testSameIntentFilterGroupSameSchemeHostDifferentPathsGroupedInOneIntentFilter() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/path1",
                    activityClassFqn = "com.example.TestActivity",
                    intentFilterAttributes = setOf("android:autoVerify=\"true\""),
                    actions = setOf("android.intent.action.VIEW"),
                    categories = setOf("android.intent.category.DEFAULT", "android.intent.category.BROWSABLE"),
                ),
                createActivityElement(
                    uri = "https://example.com/path2",
                    activityClassFqn = "com.example.TestActivity",
                    intentFilterAttributes = setOf("android:autoVerify=\"true\""),
                    actions = setOf("android.intent.action.VIEW"),
                    categories = setOf("android.intent.category.DEFAULT", "android.intent.category.BROWSABLE"),
                ),
                createActivityElement(
                    uri = "https://example.com/path3",
                    activityClassFqn = "com.example.TestActivity",
                    intentFilterAttributes = setOf("android:autoVerify=\"true\""),
                    actions = setOf("android.intent.action.VIEW"),
                    categories = setOf("android.intent.category.DEFAULT", "android.intent.category.BROWSABLE"),
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.TestActivity" android:exported="true">
                        <intent-filter android:autoVerify="true">
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/path1" />
                            <data android:path="/path2" />
                            <data android:path="/path3" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

            """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test complex grouping scenario with multiple IntentFilterGroups
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testComplexIntentFilterGrouping() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                // Group 1: autoVerify=true, VIEW action, DEFAULT+BROWSABLE categories
                createActivityElement(
                    uri = "https://example.com/verified1",
                    activityClassFqn = "com.example.TestActivity",
                    intentFilterAttributes = setOf("android:autoVerify=\"true\""),
                    actions = setOf("android.intent.action.VIEW"),
                    categories = setOf("android.intent.category.DEFAULT", "android.intent.category.BROWSABLE"),
                ),
                createActivityElement(
                    uri = "https://example.com/verified2",
                    activityClassFqn = "com.example.TestActivity",
                    intentFilterAttributes = setOf("android:autoVerify=\"true\""),
                    actions = setOf("android.intent.action.VIEW"),
                    categories = setOf("android.intent.category.DEFAULT", "android.intent.category.BROWSABLE"),
                ),
                // Group 2: no autoVerify, VIEW action, DEFAULT+BROWSABLE categories
                createActivityElement(
                    uri = "https://example.com/unverified1",
                    activityClassFqn = "com.example.TestActivity",
                    intentFilterAttributes = emptySet(),
                    actions = setOf("android.intent.action.VIEW"),
                    categories = setOf("android.intent.category.DEFAULT", "android.intent.category.BROWSABLE"),
                ),
                createActivityElement(
                    uri = "https://example.com/unverified2",
                    activityClassFqn = "com.example.TestActivity",
                    intentFilterAttributes = emptySet(),
                    actions = setOf("android.intent.action.VIEW"),
                    categories = setOf("android.intent.category.DEFAULT", "android.intent.category.BROWSABLE"),
                ),
                // Group 3: no autoVerify, SEND action, DEFAULT category only
                createActivityElement(
                    uri = "https://example.com/send",
                    activityClassFqn = "com.example.TestActivity",
                    intentFilterAttributes = emptySet(),
                    actions = setOf("android.intent.action.SEND"),
                    categories = setOf("android.intent.category.DEFAULT"),
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.TestActivity" android:exported="true">
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
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/unverified1" />
                            <data android:path="/unverified2" />
                        </intent-filter>
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

            """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test that multiple hosts with the same scheme/actions/categories/paths are consolidated
     * into a single intent-filter with multiple host entries
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testMultipleHostsConsolidatedIntoSingleIntentFilter() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://airbnb.com/luxury/messages",
                    activityClassFqn = "com.example.TestActivity",
                    intentFilterAttributes = setOf("android:autoVerify=\"true\""),
                ),
                createActivityElement(
                    uri = "https://www.airbnb.com/luxury/messages",
                    activityClassFqn = "com.example.TestActivity",
                    intentFilterAttributes = setOf("android:autoVerify=\"true\""),
                ),
                createActivityElement(
                    uri = "https://airbnb.fr/luxury/messages",
                    activityClassFqn = "com.example.TestActivity",
                    intentFilterAttributes = setOf("android:autoVerify=\"true\""),
                ),
                createActivityElement(
                    uri = "https://www.airbnb.fr/luxury/messages",
                    activityClassFqn = "com.example.TestActivity",
                    intentFilterAttributes = setOf("android:autoVerify=\"true\""),
                ),
                createActivityElement(
                    uri = "https://airbnb.it/luxury/messages",
                    activityClassFqn = "com.example.TestActivity",
                    intentFilterAttributes = setOf("android:autoVerify=\"true\""),
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.TestActivity" android:exported="true">
                        <intent-filter android:autoVerify="true">
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="airbnb.com" />
                            <data android:host="www.airbnb.com" />
                            <data android:host="airbnb.fr" />
                            <data android:host="www.airbnb.fr" />
                            <data android:host="airbnb.it" />
                            <data android:path="/luxury/messages" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

            """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test with custom actions and categories
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testIntentFilterWithCustomActionsAndCategories() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                createActivityElement(
                    uri = "https://example.com/path",
                    activityClassFqn = "com.example.TestActivity",
                    intentFilterAttributes = emptySet(),
                    actions = setOf("android.intent.action.VIEW", "android.intent.action.SEND"),
                    categories = setOf("android.intent.category.DEFAULT"),
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        val expected =
            """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.TestActivity" android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <action android:name="android.intent.action.SEND" />
                            <category android:name="android.intent.category.DEFAULT" />
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

    @OptIn(KotlinPoetJavaPoetPreview::class)
    private fun createActivityElement(
        uri: String,
        activityClassFqn: String?,
        intentFilterAttributes: Set<String> = emptySet(),
        actions: Set<String> = setOf("android.intent.action.VIEW"),
        categories: Set<String> = setOf("android.intent.category.DEFAULT", "android.intent.category.BROWSABLE"),
    ): DeepLinkAnnotatedElement.ActivityAnnotatedElement {
        val typeElement = mockk<XTypeElement>()
        every { typeElement.asClassName().toJavaPoet() } returns ClassName.get("", "TestClass")
        every { typeElement.docComment } returns null

        return DeepLinkAnnotatedElement.ActivityAnnotatedElement(
            uri = uri,
            activityClassFqn = activityClassFqn,
            intentFilterAttributes = intentFilterAttributes,
            actions = actions,
            categories = categories,
            element = typeElement,
        )
    }
}
