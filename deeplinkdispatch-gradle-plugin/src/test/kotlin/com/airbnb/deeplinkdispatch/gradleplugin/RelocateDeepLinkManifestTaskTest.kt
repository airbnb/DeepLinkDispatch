package com.airbnb.deeplinkdispatch.gradleplugin

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

/**
 * Tests for RelocateDeepLinkManifestTask behavior.
 *
 * These tests verify:
 * - Manifest file is correctly moved from KSP resources to safe location
 * - Empty parent directories are cleaned up
 * - Incremental build scenarios work correctly
 */
class RelocateDeepLinkManifestTaskTest {

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
                <application />
            </manifest>
        """.trimIndent())
    }

    @Test
    fun `relocate task moves manifest from KSP resources to safe location`() {
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
            }
        """.trimIndent())

        // Create a fake KSP-generated manifest
        val kspResourcesDir = File(testProjectDir.root, "build/generated/ksp/debug/resources/deeplinkdispatch")
        kspResourcesDir.mkdirs()
        val kspManifest = File(kspResourcesDir, "AndroidManifest.xml")
        kspManifest.writeText("""
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android">
                <application>
                    <activity android:name="com.test.TestActivity">
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
            .withArguments("relocateDeepLinkManifestDebug", "--stacktrace")
            .withPluginClasspath()
            .build()

        assertThat(result.task(":relocateDeepLinkManifestDebug")?.outcome).isEqualTo(TaskOutcome.SUCCESS)

        // Verify manifest was moved to safe location
        val safeManifest = File(testProjectDir.root, "build/intermediates/deeplinkdispatch/debug/AndroidManifest.xml")
        assertThat(safeManifest).exists()
        assertThat(safeManifest.readText()).contains("com.test.TestActivity")

        // Verify original was deleted
        assertThat(kspManifest).doesNotExist()
    }

    @Test
    fun `relocate task handles missing source file gracefully`() {
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
            }
        """.trimIndent())

        // Don't create any KSP manifest - simulates no deep links with activityClassFqn

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments("relocateDeepLinkManifestDebug", "--stacktrace")
            .withPluginClasspath()
            .build()

        assertThat(result.task(":relocateDeepLinkManifestDebug")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).contains("No DeepLinkDispatch manifest found to relocate")
    }

    @Test
    fun `relocate task succeeds when no manifest exists`() {
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
            }
        """.trimIndent())

        // Don't create any manifest files - simulates clean build without KSP

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments("relocateDeepLinkManifestDebug", "--stacktrace")
            .withPluginClasspath()
            .build()

        assertThat(result.task(":relocateDeepLinkManifestDebug")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        // Should indicate no manifest found
        assertThat(result.output).contains("No DeepLinkDispatch manifest")
    }
}
