package io.github.khkim6040.platform.observability

import io.micrometer.tracing.Tracer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.core.env.Environment
import org.springframework.test.web.servlet.assertj.MockMvcTester

@SpringBootTest(properties = ["spring.application.name=probe"])
@AutoConfigureMockMvc
class PlatformObservabilityStarterTest {
    @Autowired lateinit var mvc: MockMvcTester
    @Autowired lateinit var env: Environment
    @Autowired lateinit var tracer: Tracer

    @Test
    fun `exposes health and prometheus endpoints by default`() {
        val health = mvc.get().uri("/actuator/health").exchange()
        assertThat(health).hasStatusOk()
        assertThat(health).bodyJson().extractingPath("$.status").isEqualTo("UP")

        val prometheus = mvc.get().uri("/actuator/prometheus").exchange()
        assertThat(prometheus).hasStatusOk()
        assertThat(prometheus.response.contentAsString).contains("jvm_memory_used_bytes")
    }

    @Test
    fun `tags observations with the application name and traces every request`() {
        assertThat(env.getProperty("management.observations.key-values.application")).isEqualTo("probe")
        assertThat(env.getProperty("management.tracing.sampling.probability")).isEqualTo("1.0")
        assertThat(env.getProperty("management.otlp.metrics.export.enabled")).isEqualTo("false")
        assertThat(tracer).isNotNull()
    }
}
