subprojects {
    dependencies {
        implementation(project(":annotations"))

        implementation("com.google.devtools.ksp:symbol-processing-api:1.5.30-1.0.0-beta09")
        implementation("com.squareup:kotlinpoet:1.14.2")
        implementation("com.squareup:kotlinpoet-ksp:1.14.2")
    }
}