import org.jetbrains.kotlin.gradle.dsl.JvmTarget

apply(from = "$rootDir/dependencies.gradle")
apply(from = "$rootDir/publishing.gradle")

val deps: Map<String, Any> by project
val jvmToolchainVersion: Int by rootProject.extra
val jvmTargetVersion: Int by rootProject.extra

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
    maven(url = "https://oss.sonatype.org/service/local/repositories/snapshots/content/")
}

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
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
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}
