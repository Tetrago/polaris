val koin_version: String by rootProject
val koin_ksp_version: String by rootProject
val slf4j_version: String by rootProject

plugins {
    id("com.google.devtools.ksp") version "1.8.0-1.0.9"
    id("org.openjfx.javafxplugin") version "0.0.14"
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "com.google.devtools.ksp")
    apply(plugin = "org.openjfx.javafxplugin")

    dependencies {
        implementation(project(":annotations"))
        implementation(project(":app"))

        implementation("io.insert-koin:koin-core:$koin_version")
        implementation("io.insert-koin:koin-annotations:$koin_ksp_version")
        implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
        implementation("org.slf4j:slf4j-api:$slf4j_version")

        ksp(project(":ksp"))
        ksp("io.insert-koin:koin-ksp-compiler:$koin_ksp_version")

        testImplementation("io.insert-koin:koin-test:$koin_version")
        testImplementation("io.insert-koin:koin-test-junit5:$koin_version")
        testImplementation("org.hamcrest:hamcrest:2.2")
        testImplementation("io.mockk:mockk:1.13.5")
    }

    javafx {
        modules("javafx.controls", "javafx.graphics", "javafx.fxml")
    }

    tasks.register<Copy>("copyModule") {
        from("${project.buildDir}/libs/${project.name}-${project.version}.jar")
        into("${rootProject.project(":app").projectDir}/modules")

        dependsOn("assemble")
    }
}

tasks.named("clean").also { clean ->
    subprojects.forEach {
        clean.get().dependsOn(it.tasks.named("clean"))
    }
}