import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.sam.with.receiver")
    `java-gradle-plugin`
}

// Bootstrap the local plugin before configuring the main build. Keep its published project
// in the main build so its existing test and publishing tasks continue to work.
apply(from = "../dependencies.gradle")

val deps: Map<String, Any> by project
val jvmToolchainVersion: Int by extra
val jvmTargetVersion = 17 // Gradle and AGP require Java 17 or newer.

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

samWithReceiver {
    annotation("org.gradle.api.HasImplicitReceiver")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(jvmToolchainVersion))
    sourceCompatibility = JavaVersion.toVersion(jvmTargetVersion)
    targetCompatibility = JavaVersion.toVersion(jvmTargetVersion)
}

kotlin.compilerOptions {
    jvmTarget = JvmTarget.fromTarget(jvmTargetVersion.toString())
    allWarningsAsErrors = false
}

// Compile the original sources, including the plugin's base-library dependency, without
// resolving any published DeepLinkDispatch artifacts or sharing main-build output directories.
val localSourceDirectories = listOf(
    "../deeplinkdispatch-base/src/main/java",
    "../deeplinkdispatch-gradle-plugin/src/main/java"
)
sourceSets.main {
    java.setSrcDirs(localSourceDirectories)
    resources.srcDir("../deeplinkdispatch-gradle-plugin/src/main/resources")
}
kotlin.sourceSets.main {
    kotlin.setSrcDirs(localSourceDirectories)
}

gradlePlugin {
    plugins {
        create("deeplinkdispatch-manifest-generation") {
            id = "com.airbnb.deeplinkdispatch.manifest-generation"
            implementationClass = "com.airbnb.deeplinkdispatch.gradleplugin.ManifestGenerationPlugin"
        }
    }
}

dependencies {
    implementation(deps["okio"].toString())
    implementation(deps["jsr305"].toString())
    implementation(deps["androidXAnnotations"].toString())
    implementation(deps["androidCommonTools"].toString())
    implementation(deps["kspGradlePlugin"].toString())
    implementation(deps["androidPlugin"].toString())
    implementation(deps["kotlinGradlePlugin"].toString())
}
