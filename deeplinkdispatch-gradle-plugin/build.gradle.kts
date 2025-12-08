apply(from = "$rootDir/dependencies.gradle")
apply(from = "$rootDir/publishing.gradle")

val deps: Map<String, Any> by project

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

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
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