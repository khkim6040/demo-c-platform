plugins {
    `version-catalog`
    `maven-publish`
}

catalog {
    versionCatalog {
        from(files("../gradle/libs.versions.toml"))
        version("platform", project.version.toString())
        library("platform-bom", "io.github.khkim6040.platform", "platform-bom").versionRef("platform")
        library("platform-http-starter", "io.github.khkim6040.platform", "platform-http-starter").versionRef("platform")
    }
}

publishing {
    publications {
        create<MavenPublication>("catalog") {
            from(components["versionCatalog"])
        }
    }
}
