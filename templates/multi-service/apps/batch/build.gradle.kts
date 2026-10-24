plugins {
    id("platform.spring-boot-app")
}

dependencies {
    implementation(project(":libs:domain"))
    implementation("org.springframework.boot:spring-boot-starter-batch")
}
