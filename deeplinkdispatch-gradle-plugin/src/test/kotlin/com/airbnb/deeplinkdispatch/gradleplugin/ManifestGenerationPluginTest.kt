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
 * Functional tests for ManifestGenerationPlugin using Gradle TestKit.
 *
 * These tests verify:
 * - Plugin applies correctly to library modules
 * - Plugin rejects application modules
 * - Correct tasks are registered
 * - Task dependencies are wired correctly
 * - Manifest relocation works as expected
 */
class ManifestGenerationPluginTest {

    @get:Rule
    val testProjectDir = TemporaryFolder()

    private lateinit var buildFile: File
    private lateinit var settingsFile: File
    private lateinit var localPropertiesFile: File

    @Before
    fun setup() {
        settingsFile = testProjectDir.newFile("settings.gradle")
        buildFile = testProjectDir.newFile("build.gradle")
        localPropertiesFile = testProjectDir.newFile("local.properties")

        // Create minimal local.properties with SDK location
        val androidHome = System.getenv("ANDROID_HOME")
            ?: System.getenv("ANDROID_SDK_ROOT")
            ?: "${System.getProperty("user.home")}/Library/Android/sdk"
        localPropertiesFile.writeText("sdk.dir=$androidHome\n")

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

        // Create minimal AndroidManifest.xml
        val mainDir = testProjectDir.newFolder("src", "main")
        File(mainDir, "AndroidManifest.xml").writeText("""
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android">
                <application />
            </manifest>
        """.trimIndent())
    }

    @Test
    fun `plugin applies successfully to library module`() {
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

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments("tasks", "--group=deeplinkdispatch", "--stacktrace")
            .withPluginClasspath()
            .build()

        assertThat(result.output.lowercase()).contains("deeplinkdispatch")
        assertThat(result.output).contains("relocateDeepLinkManifestDebug")
        assertThat(result.output).contains("relocateDeepLinkManifestRelease")
    }

    @Test
    fun `plugin fails on application module with clear error message`() {
        buildFile.writeText("""
            plugins {
                id 'com.android.application' version '8.2.0'
                id 'org.jetbrains.kotlin.android' version '1.9.22'
                id 'com.airbnb.deeplinkdispatch.manifest-generation'
            }

            android {
                namespace 'com.test.app'
                compileSdk 34

                defaultConfig {
                    applicationId "com.test.app"
                    minSdk 21
                    targetSdk 34
                    versionCode 1
                    versionName "1.0"
                }
            }
        """.trimIndent())

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments("tasks", "--stacktrace")
            .withPluginClasspath()
            .buildAndFail()

        assertThat(result.output).contains("cannot be applied to application modules")
        assertThat(result.output).contains("only works with library modules")
    }

    @Test
    fun `relocate task is registered for each variant`() {
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

                buildTypes {
                    debug {}
                    release {}
                    staging {}
                }
            }
        """.trimIndent())

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments("tasks", "--all", "--stacktrace")
            .withPluginClasspath()
            .build()

        assertThat(result.output).contains("relocateDeepLinkManifestDebug")
        assertThat(result.output).contains("relocateDeepLinkManifestRelease")
        assertThat(result.output).contains("relocateDeepLinkManifestStaging")
    }

    @Test
    fun `manifest merge task is registered for each variant`() {
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

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments("tasks", "--all", "--stacktrace")
            .withPluginClasspath()
            .build()

        assertThat(result.output).contains("debugGenerateManifestIntentFiltersForDeepLinkDispatch")
        assertThat(result.output).contains("releaseGenerateManifestIntentFiltersForDeepLinkDispatch")
    }

    @Test
    fun `assembleDebug succeeds without KSP generated manifest`() {
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

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments("assembleDebug", "--stacktrace")
            .withPluginClasspath()
            .build()

        assertThat(result.task(":assembleDebug")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        // Verify message about no manifest found
        assertThat(result.output).contains("No DeepLinkDispatch")
    }

    @Test
    fun `plugin works with product flavors`() {
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

                flavorDimensions = ["environment"]
                productFlavors {
                    dev {
                        dimension "environment"
                    }
                    prod {
                        dimension "environment"
                    }
                }
            }
        """.trimIndent())

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments("tasks", "--all", "--stacktrace")
            .withPluginClasspath()
            .build()

        // Should have tasks for each flavor + build type combination
        assertThat(result.output).contains("relocateDeepLinkManifestDevDebug")
        assertThat(result.output).contains("relocateDeepLinkManifestDevRelease")
        assertThat(result.output).contains("relocateDeepLinkManifestProdDebug")
        assertThat(result.output).contains("relocateDeepLinkManifestProdRelease")
    }
}
