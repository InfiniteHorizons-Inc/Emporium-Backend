import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    java
    id("nu.studer.jooq") version "9.0"
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

val springCloudVersion by extra("2023.0.3")

val versionPrefix = "SNAPSHOT"

val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.M.d.HHmm"))!!
val versionNumber = "${currentDateTime}-${versionPrefix}"

group = "com.infinitehorizons"
version = versionNumber

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {

    // Spring Boot Dependencies: Spring Framework
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-configuration-processor")

    // Springdoc OpenApi Swagger-UI: API Documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
    implementation("org.springframework.cloud:spring-cloud-function-context")

    // Spring Boot DevTools: Development Only
    "developmentOnly"("org.springframework.boot:spring-boot-devtools")
    
    // Lombok Dependency: Java Annotation Processor
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    
    // JANSI Dependency: ANSI Color support
    implementation("org.fusesource.jansi:jansi:2.4.1")

    // Apache Commons Lang3 Dependency: String Utilities
    implementation("org.apache.commons:commons-lang3:3.17.0")

    // Test Dependencies
    // Spring Boot Test Dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // JUnit 5 Dependencies
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")

    // Mockito for Mocking
    testImplementation("org.mockito:mockito-core:5.14.2")

    // Kafka Test Dependency
    testImplementation("org.springframework.kafka:spring-kafka-test")

}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    mainClass.set("com.infinitehorizons.user.service.UserServiceApplication")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}
