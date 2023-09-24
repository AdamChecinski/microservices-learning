import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
}

dependencies {
    // https://mvnrepository.com/artifact/org.springframework.kafka/spring-kafka
    implementation("org.springframework.kafka:spring-kafka:3.0.11")

    implementation("org.springframework.boot:spring-boot-starter")
    // https://mvnrepository.com/artifact/org.springframework.retry/spring-retry
    implementation("org.springframework.retry:spring-retry:2.0.3")
// https://mvnrepository.com/artifact/org.springframework/spring-aop
    implementation("org.springframework:spring-aop:6.0.12")
    implementation(project(mapOf("path" to ":app-config-data")))
    implementation("org.jetbrains.kotlin:kotlin-reflect")



    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

group = "com.microservices.demo"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}