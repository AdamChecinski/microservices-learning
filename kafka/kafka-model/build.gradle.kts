import com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    id("com.github.davidmc24.gradle.plugin.avro-base") version "1.8.0"

}

group = "com.microservices.demo"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.avro:avro:1.11.2")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
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
val generateAvro = tasks.register<GenerateAvroJavaTask>("generateAvro") {
    source("src/main/avro")
    setOutputDir(file("src/main/java"))
}

tasks.named<JavaCompile>("compileJava").configure {
    source(generateAvro)
}