import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.4.10"
}

group = "com.tsarev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.springframework.cloud", "spring-cloud-starter-openfeign", "2.2.5.RELEASE") {
        // Do not use lod balancing for this simple project.
        exclude("org.springframework.cloud", "spring-cloud-netflix-ribbon")
    }
    implementation("io.github.openfeign", "feign-jackson", "10.10.1")
    implementation("org.springframework.boot", "spring-boot-starter-web", "2.2.5.RELEASE")
    implementation("org.springframework.boot", "spring-boot-starter-cache", "2.2.5.RELEASE")
    implementation("com.fasterxml.jackson.dataformat", "jackson-dataformat-xml", "2.10.2")
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", "2.10.5")
    implementation("com.fasterxml.jackson.datatype", "jackson-datatype-jsr310", "2.10.5")
    implementation("com.github.ben-manes.caffeine", "caffeine", "2.8.4")

    testImplementation("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs += "-Xjvm-default=compatibility"
}