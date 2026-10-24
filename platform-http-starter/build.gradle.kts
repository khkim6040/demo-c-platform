plugins {
    id("platform.kotlin-library")
    `maven-publish`
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-webmvc")
    api("org.springframework.boot:spring-boot-starter-restclient")
    api("tools.jackson.module:jackson-module-kotlin")
    api("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
}

publishing {
    publications {
        create<MavenPublication>("starter") {
            from(components["java"])
        }
    }
}
