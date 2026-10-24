package io.github.khkim6040.platform.http

import org.springframework.boot.EnvironmentPostProcessor
import org.springframework.boot.SpringApplication
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.io.ClassPathResource

/** Registers platform-defaults.yml with the lowest precedence so every app property wins. */
class PlatformDefaultsEnvironmentPostProcessor : EnvironmentPostProcessor {
    override fun postProcessEnvironment(environment: ConfigurableEnvironment, application: SpringApplication) {
        YamlPropertySourceLoader()
            .load("platform-defaults", ClassPathResource("platform-defaults.yml"))
            .forEach { environment.propertySources.addLast(it) }
    }
}
