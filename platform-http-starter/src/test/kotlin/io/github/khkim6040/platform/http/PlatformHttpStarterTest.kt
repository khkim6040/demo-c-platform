package io.github.khkim6040.platform.http

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.assertj.MockMvcTester

@SpringBootTest(properties = ["spring.http.clients.read-timeout=9s"])
@AutoConfigureMockMvc
class PlatformHttpStarterTest {
    @Autowired lateinit var mvc: MockMvcTester
    @Autowired lateinit var env: Environment

    @Test
    fun `generates correlation id when absent`() {
        val result = mvc.get().uri("/ok").exchange()
        assertThat(result).hasStatusOk()
        assertThat(result.response.getHeader(CorrelationId.HEADER)).isNotBlank()
    }

    @Test
    fun `echoes incoming correlation id`() {
        val result = mvc.get().uri("/ok").header(CorrelationId.HEADER, "abc-123").exchange()
        assertThat(result.response.getHeader(CorrelationId.HEADER)).isEqualTo("abc-123")
    }

    @Test
    fun `maps IllegalArgumentException to 400 problem detail with correlation id`() {
        val result = mvc.get().uri("/bad").header(CorrelationId.HEADER, "corr-400").exchange()
        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST)
        assertThat(result).bodyJson().extractingPath("$.correlationId").isEqualTo("corr-400")
        assertThat(result).bodyJson().extractingPath("$.detail").isEqualTo("bad input")
    }

    @Test
    fun `maps NoSuchElementException to 404 and IllegalStateException to 409`() {
        assertThat(mvc.get().uri("/missing").exchange()).hasStatus(HttpStatus.NOT_FOUND)
        assertThat(mvc.get().uri("/conflict").exchange()).hasStatus(HttpStatus.CONFLICT)
    }

    @Test
    fun `platform defaults apply with lowest precedence`() {
        assertThat(env.getProperty("spring.threads.virtual.enabled")).isEqualTo("true")
        assertThat(env.getProperty("spring.mvc.problemdetails.enabled")).isEqualTo("true")
        assertThat(env.getProperty("spring.http.clients.connect-timeout")).isEqualTo("2s")
        assertThat(env.getProperty("spring.http.clients.read-timeout")).isEqualTo("9s")
    }
}
