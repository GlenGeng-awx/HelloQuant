plugins {
    kotlin("jvm") version "1.9.0"
    application
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("io.ktor:ktor-client-core:1.6.7")
    implementation("io.ktor:ktor-client-cio:1.6.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}