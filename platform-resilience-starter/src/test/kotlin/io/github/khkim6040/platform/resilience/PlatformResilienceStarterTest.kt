package io.github.khkim6040.platform.resilience

import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment

@SpringBootTest
class PlatformResilienceStarterTest {
    @Autowired lateinit var flaky: FlakyService
    @Autowired lateinit var broken: BrokenService
    @Autowired lateinit var registry: CircuitBreakerRegistry
    @Autowired lateinit var env: Environment

    @Test
    fun `retries three times by default`() {
        assertThat(flaky.call()).isEqualTo("ok")
        assertThat(flaky.calls).isEqualTo(3)
    }

    @Test
    fun `opens the circuit after five failing calls by default`() {
        repeat(5) { assertThatThrownBy { broken.call() }.isInstanceOf(IllegalStateException::class.java) }
        assertThat(registry.circuitBreaker("probe-cb").state).isEqualTo(CircuitBreaker.State.OPEN)
        assertThatThrownBy { broken.call() }.isInstanceOf(CallNotPermittedException::class.java)
    }

    @Test
    fun `platform defaults apply with lowest precedence`() {
        assertThat(env.getProperty("resilience4j.retry.configs.default.max-attempts")).isEqualTo("3")
        assertThat(env.getProperty("resilience4j.circuitbreaker.configs.default.minimum-number-of-calls")).isEqualTo("5")
    }
}
