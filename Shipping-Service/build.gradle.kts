import java.nio.file.Files
import java.nio.file.Paths
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

tasks.register("replaceVersion") {
    doLast {
        val templatePath = Paths.get("${project.projectDir}/src/main/resources/application.properties.template")
        val outputPath = Paths.get("${project.projectDir}/src/main/resources/application.properties")

        if (Files.exists(outputPath)) {
            Files.delete(outputPath)
        }

        var content = Files.readString(templatePath)

        content = content.replace("__version__", project.version.toString())

        Files.createDirectories(outputPath.parent)

        Files.writeString(outputPath, content)
    }
}

tasks.processResources {
    dependsOn("replaceVersion")
}

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
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-configuration-processor")

    // Springdoc OpenApi Swagger-UI: API Documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Spring Boot DevTools: Development Only
    "developmentOnly"("org.springframework.boot:spring-boot-devtools")


    // Lombok Dependency: Java Annotation Processor
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Database Dependencies: MySQL
    runtimeOnly("com.mysql:mysql-connector-j")
    compileOnly("com.mysql:mysql-connector-j")
    jooqGenerator("com.mysql:mysql-connector-j")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // JANSI Dependency: ANSI Color support
    implementation("org.fusesource.jansi:jansi:2.4.1")

    // Apache Commons Lang3 Dependency: String Utilities
    implementation("org.apache.commons:commons-lang3:3.17.0")

    // JOOQ Dependencies: Code Generation
    implementation("org.springframework.boot:spring-boot-starter-jooq")

    //JOOQ Generator
    implementation("org.jooq:jooq:3.19.15")

    // JOOQ Codegen
    implementation("org.jooq:jooq-codegen:3.19.15")

    // JOOQ Meta
    implementation("org.jooq:jooq-meta:3.19.15")

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
    mainClass.set("com.infinitehorizons.shipping.service.ShippingServiceApplication")
}

fun configureDatabase(): Pair<String, String> {
    val serviceName = "emporium_${project.name.replace("-", "_").lowercase()}"

    val baseUri: String = project.findProperty("emporium.db.baseUri") as String? ?: System.getenv("EMPORIUM_BASE_URI")
    val suffix: String = project.findProperty("emporium.db.suffix") as String? ?: System.getenv("EMPORIUM_DB_SUFFIX")

    val databaseURL = "$baseUri/$serviceName$suffix"

    fun logWithColor(message: String, url: String) {
        val green = "\u001B[32m"
        val red = "\u001B[31m"
        val reset = "\u001B[0m"

        println("${green}$message${reset} ${red}$url${reset}")
    }

    logWithColor("Setting database URL dynamically:", databaseURL)

    return Pair(databaseURL, serviceName)
}

val (databaseURL, schemaName) = configureDatabase()

jooq {
    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(true)

            jooqConfiguration.apply {
                jdbc.apply {
                    driver = "com.mysql.cj.jdbc.Driver"
                    url = databaseURL
                    user = project.findProperty("emporium.db.user") as String? ?: System.getenv("EMPORIUM_DB_USER")
                    password = project.findProperty("emporium.db.password") as String? ?: System.getenv("EMPORIUM_DB_PASSWORD")
                }

                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.mysql.MySQLDatabase"
                        inputSchema = schemaName
                        includes = ".*"
                        excludes = ""
                    }

                    target.apply {
                        packageName = "com.infinitehorizons.database"
                        directory = "build/generated-sources/jooq-classes"
                    }
                }
            }
        }
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}
