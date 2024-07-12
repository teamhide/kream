tasks.bootJar {
    enabled = true
}

tasks.jar {
    enabled = true
}

dependencies {
    implementation(project(":app-api"))
    implementation(project(":support"))
    implementation("org.springframework.boot:spring-boot-starter-batch")
    testImplementation("org.springframework.batch:spring-batch-test")
}
