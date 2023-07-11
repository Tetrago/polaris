val aurora_version: String by rootProject
val hoplite_version: String by rootProject
val koin_version: String by rootProject
val koin_ksp_version: String by rootProject
val slf4j_version: String by rootProject

buildscript {
    val objectBoxVersion: String by rootProject.extra

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("io.objectbox:objectbox-gradle-plugin:$objectBoxVersion")
    }
}

plugins {
    id("com.google.devtools.ksp")
    id("org.jetbrains.compose")
}

apply(plugin = "io.objectbox")

dependencies {
    implementation(project(":annotations"))

    implementation(compose.desktop.currentOs)
    implementation("com.sksamuel.hoplite:hoplite-core:$hoplite_version")
    implementation("com.sksamuel.hoplite:hoplite-toml:$hoplite_version")
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-annotations:$koin_ksp_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0")
    implementation("org.pushing-pixels:aurora-component:$aurora_version")
    implementation("org.pushing-pixels:aurora-theming:$aurora_version")
    implementation("org.pushing-pixels:aurora-window:$aurora_version")
    implementation("org.slf4j:slf4j-api:$slf4j_version")

    ksp("io.insert-koin:koin-ksp-compiler:$koin_ksp_version")
}

compose.desktop {
    application {
        mainClass = "tetrago.polaris.app.MainKt"
    }
}

tasks.register("copyModules") {
    project(":modules").subprojects.forEach {
        dependsOn(it.tasks.named("copyModule"))
    }
}

tasks.named("assemble") {
    dependsOn(tasks.named("copyModules"))
}