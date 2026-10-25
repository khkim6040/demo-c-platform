plugins {
    id("platform.kotlin-library")
    `maven-publish`
}

dependencies {
    api("org.springframework.boot:spring-boot-starter")
    api(libs.resilience4j.spring.boot4)
    api("org.aspectj:aspectjweaver")
    api("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

publishing {
    publications {
        create<MavenPublication>("starter") {
            from(components["java"])
        }
    }
}
