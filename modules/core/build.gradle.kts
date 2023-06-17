val exposed_version: String by rootProject
val koin_version: String by rootProject
val koin_ksp_version: String by rootProject
val sqlite_version: String by rootProject

dependencies {
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-annotations:$koin_ksp_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    implementation("org.xerial:sqlite-jdbc:$sqlite_version")

    ksp("io.insert-koin:koin-ksp-compiler:$koin_ksp_version")
}