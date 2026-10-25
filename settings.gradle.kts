pluginManagement {
    includeBuild("build-logic")
}

rootProject.name = "demo-c-platform"

include(
    "platform-bom",
    "platform-catalog",
    "platform-http-starter",
    "platform-observability-starter",
    "platform-resilience-starter",
)
