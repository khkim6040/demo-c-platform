// Reads platformVersion from the root gradle.properties so the number lives in one place.
val rootProperties = java.util.Properties().apply {
    settingsDir.resolve("../gradle.properties").inputStream().use(::load)
}
val platformVersion: String = rootProperties.getProperty("platformVersion")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven {
            name = "GitHubPackages"
            setUrl("https://maven.pkg.github.com/khkim6040/demo-c-platform")
            credentials {
                username = providers.gradleProperty("gpr.user")
                    .orElse(providers.environmentVariable("GITHUB_ACTOR")).orNull
                password = providers.gradleProperty("gpr.key")
                    .orElse(providers.environmentVariable("GITHUB_TOKEN")).orNull
            }
            content { includeGroup("io.github.khkim6040.cplatform") }
        }
    }
    versionCatalogs {
        create("libs") {
            from("io.github.khkim6040.cplatform:platform-catalog:$platformVersion")
        }
    }
}

rootProject.name = "build-logic"
