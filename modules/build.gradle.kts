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
        implementation(project(":app"))
        implementation(project(":annotations"))

        implementation("org.slf4j:slf4j-api:$slf4j_version")

        ksp(project(":ksp"))
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