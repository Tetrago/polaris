val okio_version: String by project

buildscript {
    extra.set("objectBoxVersion", "3.6.0")
}

plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("kapt") version "1.8.0" apply false
    kotlin("plugin.serialization") version "1.8.0" apply false
    id("com.google.devtools.ksp") version "1.8.0-1.0.9" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.0"
    id("org.jetbrains.compose") version "1.4.1" apply false
}

allprojects {
    group = "tetrago.polaris"
    version = "0.1"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
        implementation("com.squareup.okio:okio:$okio_version")
    }

    detekt {
        config.setFrom(file("$rootDir/config/detekt/detekt.yml"))
    }

    kotlin {
        jvmToolchain(11)
    }

    tasks.test {
        useJUnitPlatform()
    }
}

tasks.named("clean").also { clean ->
    subprojects.forEach {
        clean.get().dependsOn(it.tasks.named("clean"))
    }
}