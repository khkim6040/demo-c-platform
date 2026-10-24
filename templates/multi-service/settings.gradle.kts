pluginManagement {
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
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
            from("io.github.khkim6040.cplatform:platform-catalog:${providers.gradleProperty("platformVersion").get()}")
        }
    }
}

rootProject.name = "demo-c-template"

include(
    "libs:domain",
    "apps:api",
    "apps:admin",
    "apps:consumer",
    "apps:batch",
)
