apply(from = "$rootDir/../dependencies.gradle")

val deps: Map<String, String> by project

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
    maven( url ="https://oss.sonatype.org/service/local/repositories/snapshots/content/" )
}

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

gradlePlugin {
    plugins {
        create("buildsrc-manifest-generation-plugin") {
            id = "com.airbnb.deeplinkdispatch.buildsrc.manifest-generation"
            implementationClass = "com.airbnb.deeplinkdispatch.buildsrc.ManifestGenerationPlugin"
        }
    }
}

dependencies {
    implementation("com.android.tools:common:31.7.3")
    implementation(dep("androidPlugin"))
    implementation(dep("kotlinGradlePlugin"))
    implementation(dep("kspGradlePlugin"))

    // Test dependencies
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation(gradleTestKit())
}

fun dep(vararg names: String, map: Map<String, Any> = deps): String {
    val firstName = names.firstOrNull() ?: error("Dependency name is empty")

    return when (val dependency = map[firstName] ?: error("Dependency not found for name $firstName")) {
        is String -> return dependency
        is org.codehaus.groovy.runtime.GStringImpl -> return dependency.toString()
        is Map<*, *> -> {
            check(names.size > 1) { "Expected nested dependency names for ${names.toList()}" }
            dep(*names.drop(1).toTypedArray(), map = dependency as Map<String, Any>)
        }
        else -> {
            error("Unknown dependency type ${dependency::class} $dependency for $firstName")
        }
    }
}


