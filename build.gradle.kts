// Shared publishing setup. Each module declares its own publication; this block
// gives every module the same coordinates and the GitHub Packages repository.
subprojects {
    group = "io.github.khkim6040.cplatform"
    version = providers.gradleProperty("platformVersion").orElse("0.0.0-LOCAL").get()

    repositories {
        mavenCentral()
    }

    plugins.withId("maven-publish") {
        configure<PublishingExtension> {
            repositories {
                maven {
                    name = "GitHubPackages"
                    setUrl("https://maven.pkg.github.com/khkim6040/demo-c-platform")
                    credentials {
                        username = providers.gradleProperty("gpr.user")
                            .orElse(providers.environmentVariable("GITHUB_ACTOR")).orNull
                        password = providers.gradleProperty("gpr.key")
                            .orElse(providers.environmentVariable("GITHUB_TOKEN")).orNull
                    }
                }
            }
        }
    }
}
