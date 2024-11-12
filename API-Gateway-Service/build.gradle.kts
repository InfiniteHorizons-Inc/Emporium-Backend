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

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
	}
}
dependencies {
	
	// Spring Boot Dependencies: Spring Framework
	implementation("org.springframework.boot:spring-boot-configuration-processor")
	implementation("org.springframework.cloud:spring-cloud-starter-gateway-mvc")
	implementation("org.springframework.cloud:spring-cloud-function-context")
	
	// Lombok Dependency: Java Annotation Processor
	implementation("org.projectlombok:lombok")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

	// DevTools Dependency: Development Only
	"developmentOnly"("org.springframework.boot:spring-boot-devtools")

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

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
	mainClass.set("com.infinitehorizons.user.service.UserServiceApplication")
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
}


tasks.withType<Test> {
	useJUnitPlatform()
}
