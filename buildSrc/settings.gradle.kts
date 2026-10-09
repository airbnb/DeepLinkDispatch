pluginManagement {
    apply(from = "../dependencies.gradle")
    @Suppress("UNCHECKED_CAST")
    val versions = extra["versions"] as Map<String, String>
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        id("org.jetbrains.kotlin.jvm") version versions.getValue("kotlinVersion")
        id("org.jetbrains.kotlin.plugin.sam.with.receiver") version versions.getValue("kotlinVersion")
    }
}
