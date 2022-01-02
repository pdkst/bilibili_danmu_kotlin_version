import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
}
group = "io.github.pdkst"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://maven.aliyun.com/repository/central")
    mavenCentral()
}
dependencies {
    testImplementation(kotlin("test-junit"))
    implementation("org.java-websocket", "Java-WebSocket", "1.5.1")
    implementation("com.google.code.gson", "gson", "2.8.6")
    implementation("com.squareup.okhttp3", "okhttp", "4.8.1")
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}