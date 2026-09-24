import org.jetbrains.kotlin.gradle.dsl.JvmTarget

apply(from = "$rootDir/dependencies.gradle")
apply(from = "$rootDir/publishing.gradle")

val deps: Map<String, Any> by project
val jvmToolchainVersion: Int by rootProject.extra
val jvmTargetVersion = 17 // Matches the minimum JVM required by Gradle and AGP.
val androidConfig: Map<String, Any> by project

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
    maven(url = "https://central.sonatype.com/repository/maven-snapshots/")
}

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.sam.with.receiver")
    `java-gradle-plugin`
}

samWithReceiver {
    annotation("org.gradle.api.HasImplicitReceiver")
}

// JVM toolchain and target - uses central versions from dependencies.gradle
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(jvmToolchainVersion))
    }
    sourceCompatibility = JavaVersion.toVersion(jvmTargetVersion)
    targetCompatibility = JavaVersion.toVersion(jvmTargetVersion)
}

kotlin.compilerOptions {
    jvmTarget = JvmTarget.fromTarget(jvmTargetVersion.toString())
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
    implementation(project(":deeplinkdispatch-base"))
    implementation(deps["androidCommonTools"].toString())
    implementation(deps["kspGradlePlugin"].toString())
    implementation(deps["androidPlugin"].toString())
    implementation(deps["kotlinGradlePlugin"].toString())

    testImplementation(gradleTestKit())
    testImplementation(deps["junit"].toString())
    testImplementation(deps["assertJ"].toString())
}

tasks.test {
    useJUnit()
    systemProperty("test.compileSdk", androidConfig.getValue("compileSdkVersion").toString())
    systemProperty("test.compileSdkMinor", androidConfig.getValue("compileSdkMinorVersion").toString())
    systemProperty("test.buildTools", androidConfig.getValue("buildToolsVersion").toString())
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}
