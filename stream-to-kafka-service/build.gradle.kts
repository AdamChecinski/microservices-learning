import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
}

group = "com.microservices.demo"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven {
        url = URI( "https://packages.confluent.io/maven")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.+")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    implementation(project(mapOf("path" to ":common-config")))
    implementation(project(mapOf("path" to ":app-config-data")))
    implementation(project(mapOf("path" to ":kafka:kafka-admin")))
    implementation(project(mapOf("path" to ":kafka:kafka-producer")))
    implementation(project(mapOf("path" to ":kafka:kafka-model")))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
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