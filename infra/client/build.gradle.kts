plugins {
    kotlin("jvm") version "1.6.21"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.10.5")
    testImplementation("com.squareup.retrofit2:retrofit-mock:2.9.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.3")
}