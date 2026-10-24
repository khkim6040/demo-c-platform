plugins {
    id("platform.spring-boot-app")
}

dependencies {
    implementation(project(":libs:domain"))
    implementation(libs.platform.http.starter)

    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
}
