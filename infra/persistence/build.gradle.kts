import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "2.7.9"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("plugin.spring") version "1.6.21"
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
    implementation("com.h2database:h2")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}