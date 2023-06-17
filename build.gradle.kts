import org.jetbrains.kotlin.com.intellij.openapi.vfs.StandardFileSystems.jar

plugins {
    kotlin("jvm") version "1.8.0"
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
    apply(plugin = "io.gitlab.arturbosch.detekt")

    dependencies {
        testImplementation(kotlin("test"))
        testImplementation("org.hamcrest:hamcrest:2.2")
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