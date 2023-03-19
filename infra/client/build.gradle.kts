import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "2.7.9"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("jvm") version "1.6.21"
}

repositories {
    mavenCentral()
}

tasks.withType<BootJar> {
    enabled = false
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":app"))
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.10.5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4")
    testImplementation("com.squareup.retrofit2:retrofit-mock:2.9.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.3")
}