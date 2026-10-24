import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("platform.kotlin-library")
    id("org.springframework.boot")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.named<BootJar>("bootJar") {
    archiveFileName.set("app.jar")
}

tasks.named<Jar>("jar") {
    enabled = false
}
