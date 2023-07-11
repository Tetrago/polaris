val aurora_version: String by rootProject
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

subprojects {
    apply(plugin = "com.google.devtools.ksp")
    apply(plugin = "io.objectbox")
    apply(plugin = "org.jetbrains.compose")

    dependencies {
        implementation(project(":annotations"))
        implementation(project(":app"))

        implementation(compose.desktop.currentOs)
        implementation("io.insert-koin:koin-core:$koin_version")
        implementation("io.insert-koin:koin-annotations:$koin_ksp_version")
        implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
        implementation("org.pushing-pixels:aurora-component:$aurora_version")
        implementation("org.pushing-pixels:aurora-theming:$aurora_version")
        implementation("org.pushing-pixels:aurora-window:$aurora_version")
        implementation("org.slf4j:slf4j-api:$slf4j_version")

        ksp(project(":ksp"))
        ksp("io.insert-koin:koin-ksp-compiler:$koin_ksp_version")

        testImplementation("io.insert-koin:koin-test:$koin_version")
        testImplementation("io.insert-koin:koin-test-junit5:$koin_version")
        testImplementation("org.hamcrest:hamcrest:2.2")
        testImplementation("io.mockk:mockk:1.13.5")
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