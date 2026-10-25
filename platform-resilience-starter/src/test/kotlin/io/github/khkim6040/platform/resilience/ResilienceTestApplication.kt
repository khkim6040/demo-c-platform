package io.github.khkim6040.platform.resilience

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.stereotype.Component

@SpringBootApplication
class ResilienceTestApplication

@Component
class FlakyService {
    var calls = 0

    @Retry(name = "probe-retry")
    fun call(): String {
        calls++
        if (calls < 3) throw IllegalStateException("flaky")
        return "ok"
    }
}

@Component
class BrokenService {
    @CircuitBreaker(name = "probe-cb")
    fun call(): String = throw IllegalStateException("always broken")
}
