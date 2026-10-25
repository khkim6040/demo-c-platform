plugins {
    id("platform.spring-boot-app")
}

dependencies {
    implementation(project(":libs:domain"))
    implementation(libs.platform.http.starter)
    implementation(libs.platform.observability.starter)
    implementation(libs.platform.resilience.starter)

    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
}
