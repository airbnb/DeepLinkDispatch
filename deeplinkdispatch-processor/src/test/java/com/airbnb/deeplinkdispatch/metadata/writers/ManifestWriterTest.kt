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
     * Test simple url without a path
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testSimpleUrlWithoutPath() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                createActivityElement(
                    uri = "scheme://host",
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
                            <data android:scheme="scheme" />
                            <data android:host="host" />
                            <data android:path="/" />
                            <data android:path="" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

            """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test simple url without a path
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testSimpleUrlWithEmptyPath() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                createActivityElement(
                    uri = "scheme://host/",
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
                            <data android:scheme="scheme" />
                            <data android:host="host" />
                            <data android:path="/" />
                            <data android:path="" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

            """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
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
     * Test URL with path parameter placeholder that has no allowed values (should become ..*)
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
     * Test multiple URLs for same activity with same scheme/host are merged (Cartesian product rule).
     *
     * URLs: https://example.com/path1, https://example.com/path2
     * Cartesian product: {https} × {example.com} × {/path1, /path2} = 2 combinations
     * We have 2 URLs. They form a complete Cartesian product, so they CAN be merged.
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
     * Test that URLs with different schemes AND different hosts CANNOT be merged.
     *
     * Merging https://example.com/path and myapp://custom/path would create:
     * - Cartesian product: {https, myapp} × {example.com, custom} × {/path} = 4 combinations
     * - But we only have 2 URLs, so it would incorrectly match:
     *   - https://custom/path (not defined!)
     *   - myapp://example.com/path (not defined!)
     *
     * @see <a href="https://en.wikipedia.org/wiki/Cartesian_product">Cartesian product</a>
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

    /**
     * Comprehensive test for URL grouping with different schemes, hosts, and paths.
     *
     * This test verifies that URLs are grouped correctly based on the IntentFilterGroup
     * (activity, path, actions, categories, intentFilterAttributes).
     *
     * IMPORTANT: The grouping must be by PATH to avoid incorrect matches.
     * Android's intent-filter matching works as: scheme matches ANY listed scheme,
     * host matches ANY listed host, path matches ANY listed path. This means if we
     * incorrectly merge different paths with different hosts, we'd match unintended URLs.
     *
     * For example, if we have:
     * - deeplink://app/section1
     * - deeplink://app/section2
     * - deeplink://nav/page
     *
     * And we incorrectly merge them as:
     *   <data android:host="app" />
     *   <data android:host="nav" />
     *   <data android:path="/section1" />
     *   <data android:path="/section2" />
     *   <data android:path="/page" />
     *
     * This would INCORRECTLY match deeplink://app/page (not defined!)
     *
     * Correct grouping rules:
     * 1. First level grouping: by activity class
     * 2. Second level grouping: by IntentFilterGroup (path + actions + categories + intentFilterAttributes)
     * 3. Within each IntentFilterGroup: schemes and hosts are merged/consolidated
     *
     * This allows URLs with the same path but different schemes/hosts to be grouped together,
     * while keeping different paths separate.
     *
     * This test covers:
     * - Multiple activities with different URLs
     * - Same activity with same path but different schemes/hosts (merged into one intent-filter)
     * - Same activity with different paths (creates separate intent-filters per path)
     * - Custom schemes (myapp://, deeplink://)
     * - Different actions/categories causing separate intent-filters
     * - intentFilterAttributes causing separate intent-filters
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testComprehensiveUrlGroupingWithDifferentSchemesHostsAndPaths() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                // ========== Activity 1: com.example.HomeActivity ==========
                // Each unique path gets its own intent-filter
                // URLs with the same path but different schemes/hosts are merged together
                // Path: /home -> schemes: https, http; hosts: example.com, www.example.com
                createActivityElement(
                    uri = "https://example.com/home",
                    activityClassFqn = "com.example.HomeActivity",
                ),
                createActivityElement(
                    uri = "https://www.example.com/home",
                    activityClassFqn = "com.example.HomeActivity",
                ),
                createActivityElement(
                    uri = "http://example.com/home",
                    activityClassFqn = "com.example.HomeActivity",
                ),
                // Path: /dashboard -> schemes: https, http; hosts: example.com, m.example.com, legacy.example.com
                createActivityElement(
                    uri = "https://example.com/dashboard",
                    activityClassFqn = "com.example.HomeActivity",
                ),
                createActivityElement(
                    uri = "https://m.example.com/dashboard",
                    activityClassFqn = "com.example.HomeActivity",
                ),
                createActivityElement(
                    uri = "http://legacy.example.com/dashboard",
                    activityClassFqn = "com.example.HomeActivity",
                ),
                // Path: /main -> schemes: https, myapp; hosts: www.example.com, home
                createActivityElement(
                    uri = "https://www.example.com/main",
                    activityClassFqn = "com.example.HomeActivity",
                ),
                createActivityElement(
                    uri = "myapp://home/main",
                    activityClassFqn = "com.example.HomeActivity",
                ),
                // ========== Activity 2: com.example.ProfileActivity ==========
                // Path: /profile with autoVerify -> hosts: example.com, www.example.com
                createActivityElement(
                    uri = "https://example.com/profile",
                    activityClassFqn = "com.example.ProfileActivity",
                    intentFilterAttributes = setOf("android:autoVerify=\"true\""),
                ),
                createActivityElement(
                    uri = "https://www.example.com/profile",
                    activityClassFqn = "com.example.ProfileActivity",
                    intentFilterAttributes = setOf("android:autoVerify=\"true\""),
                ),
                // Path: /user/settings with autoVerify
                createActivityElement(
                    uri = "https://example.com/user/settings",
                    activityClassFqn = "com.example.ProfileActivity",
                    intentFilterAttributes = setOf("android:autoVerify=\"true\""),
                ),
                // Path: /public-profile without autoVerify (different intentFilterAttributes = separate from above)
                createActivityElement(
                    uri = "https://example.com/public-profile",
                    activityClassFqn = "com.example.ProfileActivity",
                ),
                // Path: /share-profile with SEND action (different action = separate intent-filter)
                createActivityElement(
                    uri = "https://example.com/share-profile",
                    activityClassFqn = "com.example.ProfileActivity",
                    actions = setOf("android.intent.action.SEND"),
                ),
                // ========== Activity 3: com.example.DeepLinkActivity ==========
                // Path: /deep -> multiple hosts with same path are merged
                createActivityElement(
                    uri = "https://example.com/deep",
                    activityClassFqn = "com.example.DeepLinkActivity",
                ),
                createActivityElement(
                    uri = "https://example.org/deep",
                    activityClassFqn = "com.example.DeepLinkActivity",
                ),
                createActivityElement(
                    uri = "https://example.net/deep",
                    activityClassFqn = "com.example.DeepLinkActivity",
                ),
                createActivityElement(
                    uri = "https://example.co.uk/deep",
                    activityClassFqn = "com.example.DeepLinkActivity",
                ),
                createActivityElement(
                    uri = "https://other-domain.com/deep",
                    activityClassFqn = "com.example.DeepLinkActivity",
                ),
                // Path: /section1 -> deeplink://app (separate from /section2 and /page)
                createActivityElement(
                    uri = "deeplink://app/section1",
                    activityClassFqn = "com.example.DeepLinkActivity",
                ),
                // Path: /section2 -> deeplink://app (separate from /section1 and /page)
                createActivityElement(
                    uri = "deeplink://app/section2",
                    activityClassFqn = "com.example.DeepLinkActivity",
                ),
                // Path: /page -> deeplink://nav (separate from /section1 and /section2)
                createActivityElement(
                    uri = "deeplink://nav/page",
                    activityClassFqn = "com.example.DeepLinkActivity",
                ),
            )

        writer.write(processingEnv, printWriter, elements)

        // Expected grouping based on Cartesian product rule:
        // - HomeActivity /home: https can merge 2 hosts, but http://example.com is separate (3 URLs ≠ 2×2×1=4)
        // - HomeActivity /dashboard: https can merge 2 hosts, but http://legacy is separate (3 URLs ≠ 2×3×1=6)
        // - HomeActivity /main: https://www.example.com and myapp://home CANNOT merge (2 URLs ≠ 2×2×1=4)
        // - DeepLinkActivity /deep: all https with same scheme CAN merge (5 URLs = 1×5×1=5)
        // - deeplink://app/section1 and deeplink://app/section2 CAN merge (same scheme, same host, 2 paths: 1×1×2=2)
        val expected =
            """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android" >
                <application>
                    <activity
                        android:name="com.example.HomeActivity" android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:host="www.example.com" />
                            <data android:path="/home" />
                        </intent-filter>
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="http" />
                            <data android:host="example.com" />
                            <data android:path="/home" />
                        </intent-filter>
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:host="m.example.com" />
                            <data android:path="/dashboard" />
                        </intent-filter>
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="http" />
                            <data android:host="legacy.example.com" />
                            <data android:path="/dashboard" />
                        </intent-filter>
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="www.example.com" />
                            <data android:path="/main" />
                        </intent-filter>
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="myapp" />
                            <data android:host="home" />
                            <data android:path="/main" />
                        </intent-filter>
                    </activity>
                    <activity
                        android:name="com.example.ProfileActivity" android:exported="true">
                        <intent-filter android:autoVerify="true">
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:host="www.example.com" />
                            <data android:path="/profile" />
                        </intent-filter>
                        <intent-filter android:autoVerify="true">
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/user/settings" />
                        </intent-filter>
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/public-profile" />
                        </intent-filter>
                        <intent-filter>
                            <action android:name="android.intent.action.SEND" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:path="/share-profile" />
                        </intent-filter>
                    </activity>
                    <activity
                        android:name="com.example.DeepLinkActivity" android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:host="example.org" />
                            <data android:host="example.net" />
                            <data android:host="example.co.uk" />
                            <data android:host="other-domain.com" />
                            <data android:path="/deep" />
                        </intent-filter>
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="deeplink" />
                            <data android:host="app" />
                            <data android:path="/section1" />
                            <data android:path="/section2" />
                        </intent-filter>
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="deeplink" />
                            <data android:host="nav" />
                            <data android:path="/page" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

            """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    /**
     * Test that URLs with scheme and host placeholders are correctly grouped together.
     *
     * When using placeholders like http{scheme(|s)}://{prefix(|www.)}example.com/path,
     * this expands to multiple (scheme, host) combinations but since they share the same
     * path, they should all be in the same intent-filter.
     *
     * URLs with the same path (even with different placeholder patterns) will be grouped,
     * with all their schemes and hosts merged together.
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testUrlWithSchemeAndHostPlaceholdersGroupedCorrectly() {
        val writer = ManifestWriter()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        val elements =
            listOf(
                // Both URLs have the same path /home, so they should be grouped
                // even though they have different placeholder patterns
                createActivityElement(
                    uri = "http{scheme(|s)}://{prefix(|www.)}example.com/home",
                    activityClassFqn = "com.example.TestActivity",
                ),
                createActivityElement(
                    uri = "http{scheme(|s)}://{prefix(|m.)}example.com/home",
                    activityClassFqn = "com.example.TestActivity",
                ),
                // This URL has a different path, so it should be in a separate intent-filter
                createActivityElement(
                    uri = "http{scheme(|s)}://{prefix(|www.)}example.com/dashboard",
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
                            <data android:host="www.example.com" />
                            <data android:host="m.example.com" />
                            <data android:path="/home" />
                        </intent-filter>
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="http" />
                            <data android:scheme="https" />
                            <data android:host="example.com" />
                            <data android:host="www.example.com" />
                            <data android:path="/dashboard" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>

            """.trimIndent()

        assertThat(stringWriter.toString()).isEqualTo(expected)
    }

    // ==================== findMergeableGroups Unit Tests ====================

    /**
     * Test that a single URL returns a single group.
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testFindMergeableGroups_singleElement() {
        val writer = ManifestWriter()
        val elements =
            listOf(
                createActivityElement(uri = "https://example.com/path", activityClassFqn = "Activity"),
            )

        val groups = writer.findMergeableGroups(elements)

        assertThat(groups).hasSize(1)
        assertThat(groups[0]).hasSize(1)
    }

    /**
     * Test that URLs with same scheme and host but different paths CAN be merged.
     * Cartesian: 1×1×2 = 2, Actual: 2 ✓
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testFindMergeableGroups_sameSchemeHost_differentPaths_canMerge() {
        val writer = ManifestWriter()
        val elements =
            listOf(
                createActivityElement(uri = "https://example.com/path1", activityClassFqn = "Activity"),
                createActivityElement(uri = "https://example.com/path2", activityClassFqn = "Activity"),
            )

        val groups = writer.findMergeableGroups(elements)

        // Should merge into one group
        assertThat(groups).hasSize(1)
        assertThat(groups[0]).hasSize(2)
    }

    /**
     * Test that URLs with different schemes AND different hosts CANNOT be merged.
     * Cartesian: 2×2×1 = 4, Actual: 2 ✗
     * Merging would incorrectly match https://custom/path and myapp://example.com/path
     *
     * @see <a href="https://en.wikipedia.org/wiki/Cartesian_product">Cartesian product</a>
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testFindMergeableGroups_differentSchemeAndHost_cannotMerge() {
        val writer = ManifestWriter()
        val elements =
            listOf(
                createActivityElement(uri = "https://example.com/path", activityClassFqn = "Activity"),
                createActivityElement(uri = "myapp://custom/path", activityClassFqn = "Activity"),
            )

        val groups = writer.findMergeableGroups(elements)

        // Should NOT merge - need 2 separate groups
        assertThat(groups).hasSize(2)
        assertThat(groups[0]).hasSize(1)
        assertThat(groups[1]).hasSize(1)
    }

    /**
     * Test that URLs forming a complete Cartesian product CAN be merged.
     * {http, https} × {example.com} × {/path} = 2 combinations
     * We have 2 URLs, so they CAN merge.
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testFindMergeableGroups_completeCartesianProduct_canMerge() {
        val writer = ManifestWriter()
        val elements =
            listOf(
                createActivityElement(uri = "http://example.com/path", activityClassFqn = "Activity"),
                createActivityElement(uri = "https://example.com/path", activityClassFqn = "Activity"),
            )

        val groups = writer.findMergeableGroups(elements)

        // Should merge into one group (complete Cartesian product)
        assertThat(groups).hasSize(1)
        assertThat(groups[0]).hasSize(2)
    }

    /**
     * Test that URLs NOT forming a complete Cartesian product CANNOT be merged.
     * {http, https} × {example.com, www.example.com} × {/path} = 4 combinations
     * We have 3 URLs (missing http://www.example.com/path), so they CANNOT all merge.
     *
     * Expected behavior: http and https with example.com merge, but we also have
     * https://www.example.com which can only merge with other same-scheme URLs.
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testFindMergeableGroups_incompleteCartesianProduct_partialMerge() {
        val writer = ManifestWriter()
        val elements =
            listOf(
                createActivityElement(uri = "http://example.com/path", activityClassFqn = "Activity"),
                createActivityElement(uri = "https://example.com/path", activityClassFqn = "Activity"),
                createActivityElement(uri = "https://www.example.com/path", activityClassFqn = "Activity"),
            )

        val groups = writer.findMergeableGroups(elements)

        // The greedy algorithm will:
        // 1. http://example.com -> group 1
        // 2. https://example.com can merge with group 1 (2×1×1=2, actual=2) ✓
        // 3. https://www.example.com cannot merge with group 1 (2×2×1=4, actual=3) ✗
        // So we expect 2 groups
        assertThat(groups).hasSize(2)
    }

    /**
     * Test that same host with multiple schemes and multiple paths CAN merge.
     * {http, https} × {example.com} × {/path1, /path2} = 4 combinations
     * We have 4 URLs, so they CAN merge.
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testFindMergeableGroups_fullCartesianProduct_canMerge() {
        val writer = ManifestWriter()
        val elements =
            listOf(
                createActivityElement(uri = "http://example.com/path1", activityClassFqn = "Activity"),
                createActivityElement(uri = "http://example.com/path2", activityClassFqn = "Activity"),
                createActivityElement(uri = "https://example.com/path1", activityClassFqn = "Activity"),
                createActivityElement(uri = "https://example.com/path2", activityClassFqn = "Activity"),
            )

        val groups = writer.findMergeableGroups(elements)

        // Should merge into one group (complete 2×1×2 = 4 Cartesian product)
        assertThat(groups).hasSize(1)
        assertThat(groups[0]).hasSize(4)
    }

    // ==================== canMerge Unit Tests ====================

    /**
     * Test canMerge returns true for complete Cartesian product.
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testCanMerge_completeCartesianProduct_returnsTrue() {
        val writer = ManifestWriter()
        val urls =
            listOf(
                ExpandedUrl(
                    setOf("http"),
                    setOf("example.com"),
                    setOf("/path"),
                    createActivityElement(uri = "http://example.com/path", activityClassFqn = "Activity"),
                ),
                ExpandedUrl(
                    setOf("https"),
                    setOf("example.com"),
                    setOf("/path"),
                    createActivityElement(uri = "https://example.com/path", activityClassFqn = "Activity"),
                ),
            )

        // {http, https} × {example.com} × {/path} = 2, actual = 2
        assertThat(writer.canMerge(urls)).isTrue()
    }

    /**
     * Test canMerge returns false for incomplete Cartesian product.
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testCanMerge_incompleteCartesianProduct_returnsFalse() {
        val writer = ManifestWriter()
        val urls =
            listOf(
                ExpandedUrl(
                    setOf("https"),
                    setOf("example.com"),
                    setOf("/path"),
                    createActivityElement(uri = "https://example.com/path", activityClassFqn = "Activity"),
                ),
                ExpandedUrl(
                    setOf("myapp"),
                    setOf("custom"),
                    setOf("/path"),
                    createActivityElement(uri = "myapp://custom/path", activityClassFqn = "Activity"),
                ),
            )

        // {https, myapp} × {example.com, custom} × {/path} = 4, actual = 2
        assertThat(writer.canMerge(urls)).isFalse()
    }

    /**
     * Test canMerge with single URL returns true.
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testCanMerge_singleUrl_returnsTrue() {
        val writer = ManifestWriter()
        val urls =
            listOf(
                ExpandedUrl(
                    setOf("https"),
                    setOf("example.com"),
                    setOf("/path"),
                    createActivityElement(uri = "https://example.com/path", activityClassFqn = "Activity"),
                ),
            )

        // {https} × {example.com} × {/path} = 1, actual = 1
        assertThat(writer.canMerge(urls)).isTrue()
    }

    /**
     * Test canMerge with URLs that have multiple paths each.
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testCanMerge_multiplePaths_complete() {
        val writer = ManifestWriter()
        val urls =
            listOf(
                ExpandedUrl(
                    setOf("https"),
                    setOf("example.com"),
                    setOf("/path1", "/path2"),
                    createActivityElement(uri = "https://example.com/path1", activityClassFqn = "Activity"),
                ),
            )

        // {https} × {example.com} × {/path1, /path2} = 2, actual = 2 (from expansion)
        assertThat(writer.canMerge(urls)).isTrue()
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
