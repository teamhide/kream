import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("kapt") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20" apply false
    kotlin("plugin.jpa") version "1.9.20" apply false
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.20" apply false
    id("jacoco")
    id("org.springframework.boot") version "3.2.0" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0" apply false
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

allprojects {
    group = "com.teamhide.kream"
    version = "0.0.1"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "jacoco")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
        implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
        kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
        kapt("jakarta.annotation:jakarta.annotation-api")
        kapt("jakarta.persistence:jakarta.persistence-api")
        runtimeOnly("com.mysql:mysql-connector-j")
        runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
        runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
        testImplementation("io.kotest:kotest-runner-junit5-jvm:5.6.2")
        testImplementation("io.kotest:kotest-framework-datatest:5.6.2")
        testImplementation("io.kotest:kotest-assertions-core:5.6.2")
        testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
        testImplementation("io.mockk:mockk:1.13.3")
        testImplementation("com.ninja-squad:springmockk:4.0.0")
        testImplementation("com.squareup.okhttp3:okhttp:4.11.0")
        testImplementation("com.squareup.okhttp3:mockwebserver:4.11.0")
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
                    "com.teamhide.kream.client.FeignClientConfig",
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
}
