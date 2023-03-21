plugins {
    id("org.springframework.boot") version "2.7.9"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

dependencies {
    implementation(project(":domain"))
    implementation("io.github.resilience4j:resilience4j-spring-boot2:1.7.1")
    // compile "io.github.resilience4j:resilience4j-spring-boot3:${resilience4jVersion}"
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-aop")
}