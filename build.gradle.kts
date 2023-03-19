import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("org.jmailen.kotlinter") version "3.10.0"
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "org.jmailen.kotlinter")

    group = "com.example.blogsearcher"
    version = "0.0.1-SNAPSHOT"
    java.sourceCompatibility = JavaVersion.VERSION_11

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
        testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
        testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}