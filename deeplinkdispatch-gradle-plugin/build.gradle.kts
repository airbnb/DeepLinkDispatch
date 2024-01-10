apply(from = "$rootDir/dependencies.gradle")
apply(from = "$rootDir/publishing.gradle")


val deps: Map<String, String> by project

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
    maven( url ="https://oss.sonatype.org/service/local/repositories/snapshots/content/" )
}

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

gradlePlugin {
    plugins {
        create("deeplinkdispatch-manifest-generation") {
            id = "com.airbnb.deeplinkdispatch.manifest-generation"
            implementationClass = "com.airbnb.deeplinkdispatch.gradleplugin.ManifestGenerationPlugin"
        }
    }
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

dependencies {
    implementation("com.android.tools:common:30.4.1")
    implementation(dep("kspGradlePlugin"))
    implementation(dep("androidPlugin"))
    implementation(dep("kotlinGradlePlugin"))
}