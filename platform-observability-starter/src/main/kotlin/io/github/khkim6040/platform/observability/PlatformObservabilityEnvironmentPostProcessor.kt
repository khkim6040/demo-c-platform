package io.github.khkim6040.platform.observability

import org.springframework.boot.EnvironmentPostProcessor
import org.springframework.boot.SpringApplication
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.io.ClassPathResource

/** Registers platform-observability-defaults.yml with the lowest precedence so every app property wins. */
class PlatformObservabilityEnvironmentPostProcessor : EnvironmentPostProcessor {
    override fun postProcessEnvironment(environment: ConfigurableEnvironment, application: SpringApplication) {
        YamlPropertySourceLoader()
            .load("platform-observability-defaults", ClassPathResource("platform-observability-defaults.yml"))
            .forEach { environment.propertySources.addLast(it) }
    }
}
