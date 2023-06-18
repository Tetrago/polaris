plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.serialization") version "1.8.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.0"
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
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

        testImplementation(kotlin("test"))
        testImplementation("org.hamcrest:hamcrest:2.2")
        testImplementation("io.mockk:mockk:1.13.5")
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