package io.github.khkim6040.platform.resilience

import org.springframework.boot.EnvironmentPostProcessor
import org.springframework.boot.SpringApplication
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.io.ClassPathResource

/** Registers platform-resilience-defaults.yml with the lowest precedence so every app property wins. */
class PlatformResilienceEnvironmentPostProcessor : EnvironmentPostProcessor {
    override fun postProcessEnvironment(environment: ConfigurableEnvironment, application: SpringApplication) {
        YamlPropertySourceLoader()
            .load("platform-resilience-defaults", ClassPathResource("platform-resilience-defaults.yml"))
            .forEach { environment.propertySources.addLast(it) }
    }
}
