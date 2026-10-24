plugins {
    `java-platform`
    `maven-publish`
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform(libs.spring.boot.bom))
    constraints {
        api("io.github.khkim6040.platform:platform-http-starter:${project.version}")
    }
}

publishing {
    publications {
        create<MavenPublication>("bom") {
            from(components["javaPlatform"])
        }
    }
}
