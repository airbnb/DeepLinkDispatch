package com.airbnb.deeplinkdispatch.gradleplugin

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.util.zip.ZipFile

/**
 * Integration tests verifying that the DeepLinkDispatch manifest is NOT included
 * in the AAR's classes.jar.
 *
 * This is the critical test that ensures the plugin achieves its main goal:
 * preventing the KSP-generated manifest from being bundled as a Java resource.
 */
class AarManifestExclusionTest {

    @get:Rule
    val testProjectDir = TemporaryFolder()

    private lateinit var buildFile: File
    private lateinit var settingsFile: File

    @Before
    fun setup() {
        settingsFile = testProjectDir.newFile("settings.gradle")
        buildFile = testProjectDir.newFile("build.gradle")

        val androidHome = System.getenv("ANDROID_HOME")
            ?: System.getenv("ANDROID_SDK_ROOT")
            ?: "${System.getProperty("user.home")}/Library/Android/sdk"
        testProjectDir.newFile("local.properties").writeText("sdk.dir=$androidHome\n")

        settingsFile.writeText("""
            pluginManagement {
                repositories {
                    google()
                    mavenCentral()
                    gradlePluginPortal()
                }
            }
            dependencyResolutionManagement {
                repositories {
                    google()
                    mavenCentral()
                }
            }
            rootProject.name = "test-project"
        """.trimIndent())

        val mainDir = testProjectDir.newFolder("src", "main")
        File(mainDir, "AndroidManifest.xml").writeText("""
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android">
                <application>
                    <activity android:name="com.test.library.TestActivity" />
                </application>
            </manifest>
        """.trimIndent())

        // Create a simple Kotlin source file so classes.jar has content
        val kotlinDir = File(mainDir, "java/com/test/library")
        kotlinDir.mkdirs()
        File(kotlinDir, "TestActivity.kt").writeText("""
            package com.test.library

            import android.app.Activity

            class TestActivity : Activity()
        """.trimIndent())
    }

    @Test
    fun `AAR classes jar does not contain deeplinkdispatch manifest`() {
        buildFile.writeText("""
            plugins {
                id 'com.android.library' version '8.2.0'
                id 'org.jetbrains.kotlin.android' version '1.9.22'
                id 'com.airbnb.deeplinkdispatch.manifest-generation'
            }

            android {
                namespace 'com.test.library'
                compileSdk 34
                defaultConfig {
                    minSdk 21
                }
                compileOptions {
                    sourceCompatibility JavaVersion.VERSION_11
                    targetCompatibility JavaVersion.VERSION_11
                }
                kotlinOptions {
                    jvmTarget = '11'
                }
            }
        """.trimIndent())

        // Simulate KSP-generated manifest by creating it in the expected location
        val kspResourcesDir = File(testProjectDir.root, "build/generated/ksp/debug/resources/deeplinkdispatch")
        kspResourcesDir.mkdirs()
        File(kspResourcesDir, "AndroidManifest.xml").writeText("""
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android">
                <application>
                    <activity android:name="com.test.library.TestActivity">
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <data android:scheme="test" android:host="example" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>
        """.trimIndent())

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments("assembleDebug", "--stacktrace")
            .withPluginClasspath()
            .build()

        assertThat(result.task(":assembleDebug")?.outcome).isEqualTo(TaskOutcome.SUCCESS)

        // Find the AAR file
        val aarFile = File(testProjectDir.root, "build/outputs/aar/test-project-debug.aar")
        assertThat(aarFile).exists()

        // Extract and check classes.jar
        ZipFile(aarFile).use { aar ->
            val classesJarEntry = aar.getEntry("classes.jar")
            assertThat(classesJarEntry).isNotNull

            // Extract classes.jar to temp file
            val tempClassesJar = File.createTempFile("classes", ".jar")
            tempClassesJar.deleteOnExit()

            aar.getInputStream(classesJarEntry).use { input ->
                tempClassesJar.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            // Check classes.jar contents
            ZipFile(tempClassesJar).use { classesJar ->
                val entries = classesJar.entries().toList().map { it.name }

                // Should NOT contain the deeplinkdispatch manifest
                assertThat(entries).noneMatch { it.contains("deeplinkdispatch") }
                assertThat(entries).noneMatch { it == "deeplinkdispatch/AndroidManifest.xml" }

                // Should contain actual class files
                assertThat(entries).anyMatch { it.endsWith("TestActivity.class") }
            }

            tempClassesJar.delete()
        }
    }

    @Test
    fun `AAR root manifest contains merged intent filters`() {
        buildFile.writeText("""
            plugins {
                id 'com.android.library' version '8.2.0'
                id 'org.jetbrains.kotlin.android' version '1.9.22'
                id 'com.airbnb.deeplinkdispatch.manifest-generation'
            }

            android {
                namespace 'com.test.library'
                compileSdk 34
                defaultConfig {
                    minSdk 21
                }
                compileOptions {
                    sourceCompatibility JavaVersion.VERSION_11
                    targetCompatibility JavaVersion.VERSION_11
                }
                kotlinOptions {
                    jvmTarget = '11'
                }
            }
        """.trimIndent())

        // Create a KSP-generated manifest with intent filters
        val kspResourcesDir = File(testProjectDir.root, "build/generated/ksp/debug/resources/deeplinkdispatch")
        kspResourcesDir.mkdirs()
        File(kspResourcesDir, "AndroidManifest.xml").writeText("""
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android">
                <application>
                    <activity android:name="com.test.library.TestActivity">
                        <intent-filter>
                            <action android:name="android.intent.action.VIEW" />
                            <category android:name="android.intent.category.DEFAULT" />
                            <category android:name="android.intent.category.BROWSABLE" />
                            <data android:scheme="myapp" android:host="deeplink" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>
        """.trimIndent())

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments("assembleDebug", "--stacktrace")
            .withPluginClasspath()
            .build()

        assertThat(result.task(":assembleDebug")?.outcome).isEqualTo(TaskOutcome.SUCCESS)

        // Check the AAR's root AndroidManifest.xml
        val aarFile = File(testProjectDir.root, "build/outputs/aar/test-project-debug.aar")
        ZipFile(aarFile).use { aar ->
            val manifestEntry = aar.getEntry("AndroidManifest.xml")
            assertThat(manifestEntry).isNotNull

            val manifestContent = aar.getInputStream(manifestEntry).bufferedReader().readText()

            // The merged manifest should contain the intent filter
            assertThat(manifestContent).contains("myapp")
            assertThat(manifestContent).contains("deeplink")
            assertThat(manifestContent).contains("android.intent.action.VIEW")
        }
    }
}
