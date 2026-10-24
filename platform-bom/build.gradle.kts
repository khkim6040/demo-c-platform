plugins {
    `java-platform`
    `maven-publish`
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform(libs.spring.boot.bom))
    api(platform(libs.jackson.bom)) // security patch: overrides Boot's 3.1.7
    constraints {
        api("io.github.khkim6040.cplatform:platform-http-starter:${project.version}")
    }
}

publishing {
    publications {
        create<MavenPublication>("bom") {
            from(components["javaPlatform"])
        }
    }
}
