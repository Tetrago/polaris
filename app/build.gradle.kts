val exposed_version: String by rootProject
val hoplite_version: String by rootProject
val koin_version: String by rootProject
val koin_ksp_version: String by rootProject
val kotlin_faker_version: String by rootProject
val okio_version: String by rootProject
val slf4j_version: String by rootProject

plugins {
    application
    id("com.google.devtools.ksp") version "1.8.0-1.0.9"
    id("org.openjfx.javafxplugin") version "0.0.14"
}

dependencies {
    implementation(project(":annotations"))

    implementation("com.sksamuel.hoplite:hoplite-core:$hoplite_version")
    implementation("com.sksamuel.hoplite:hoplite-toml:$hoplite_version")
    implementation("com.squareup.okio:okio:$okio_version")
    implementation("io.github.serpro69:kotlin-faker:$kotlin_faker_version")
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-annotations:$koin_ksp_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0")
    implementation("org.slf4j:slf4j-api:$slf4j_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.xerial:sqlite-jdbc:3.30.1")

    ksp("io.insert-koin:koin-ksp-compiler:$koin_ksp_version")
}

application {
    mainClass.set("tetrago.polaris.app.MainKt")
}

javafx {
    modules("javafx.controls", "javafx.graphics", "javafx.fxml")
}

tasks.register("copyModules") {
    project(":modules").subprojects.forEach {
        dependsOn(it.tasks.named("copyModule"))
    }
}

tasks.named("run") {
    dependsOn(tasks.named("copyModules"))
}