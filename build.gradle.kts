import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("jacoco")
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    kotlin("kapt") version "1.9.20"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("plugin.jpa") version "1.9.20"
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

group = "com.sample"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.security:spring-security-messaging:6.2.0")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")
    implementation("org.hibernate:hibernate-core:6.1.7.Final")
    implementation("org.hibernate:hibernate-spatial:6.1.7.Final")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.6.2")
    testImplementation("io.kotest:kotest-framework-datatest:5.6.2")
    testImplementation("io.kotest:kotest-assertions-core:5.6.2")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
    testImplementation("io.mockk:mockk:1.13.3")
    testImplementation("com.ninja-squad:springmockk:4.0.0")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.awaitility:awaitility-kotlin:4.2.0")
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

val testAll by tasks.registering {
    dependsOn("test", "jacocoTestReport", "jacocoTestCoverageVerification")
    tasks["test"].mustRunAfter(tasks["ktlintCheck"])
    tasks["jacocoTestReport"].mustRunAfter(tasks["test"])
    tasks["jacocoTestCoverageVerification"].mustRunAfter(tasks["jacocoTestReport"])
}

val snippetsDir by extra { file("build/generated-snippets") }

tasks.test {
    useJUnitPlatform()
    systemProperties["spring.profiles.active"] = "test"
    outputs.dir(snippetsDir)
}

tasks.register("testUnit", Test::class) {
    useJUnitPlatform()
    systemProperties["spring.profiles.active"] = "test"
    exclude("**/*ControllerTest*")
}

tasks.register("teste2e", Test::class) {
    useJUnitPlatform()
    systemProperties["spring.profiles.active"] = "test"
    include("**/*ControllerTest*")
}

tasks.asciidoctor {
    inputs.dir(snippetsDir)
    doFirst {
        delete {
            file("src/main/resources/static/docs")
        }
    }
}

val copyHTML by tasks.registering(Copy::class) {
    dependsOn(tasks.asciidoctor)
    from(file("build/docs/asciidoc"))
    into(file("src/main/resources/static/docs"))
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        html.required.set(true)
        xml.required.set(false)
        csv.required.set(false)
    }
    finalizedBy(tasks.jacocoTestCoverageVerification)
    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it) {
                    exclude(
                        "**/*Application*",
                        "**/Q*Entity*",
                        "**/healthcheck**",
                        "**/*logger*",
                        "**/*Logger*",
                        "**/**Logger**.class",
                        "**/**logger**.class",
                        "**logger*",
                    )
                }
            }
        )
    )
}

tasks.jacocoTestCoverageVerification {
    val queryDslClasses = ('A'..'Z').map { "*.Q$it*" }
    violationRules {
        rule {
            element = "CLASS"
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "1.00".toBigDecimal()
            }
            classDirectories.setFrom(sourceSets.main.get().output.asFileTree)
            excludes = listOf(
                "com.teamhide.kream.KreamApplicationKt",
                "com.teamhide.kream.common.healthcheck*",
                "**/*logger*",
                "**/*Logger*",
                "**/**Logger**.class",
                "**/**logger**.class",
                "**logger*",
            ) + queryDslClasses
        }
    }
}

val installHooks by tasks.registering(Copy::class) {
    from(file("$rootDir/hooks/pre-commit"))
    into(file("$rootDir/.git/hooks"))
    eachFile {
        fileMode = 0b111101101 // 755
    }
}
