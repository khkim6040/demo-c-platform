package io.github.khkim6040.sample.consumer

import io.github.khkim6040.sample.domain.ContractEvent
import io.github.khkim6040.sample.domain.ContractEventType
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

/** Stand-in for a message broker. Replaced by a Kafka listener in experiment 10. */
@Component
class FakeEventSource(private val handler: ContractEventHandler) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val counter = AtomicLong()

    @Scheduled(fixedDelayString = "\${consumer.fake-source.interval}")
    fun emit() {
        val n = counter.incrementAndGet()
        val event = when {
            n % 3 == 0L -> ContractEvent("evt-$n", "unknown", ContractEventType.SUSPENDED)
            n % 2 == 0L -> ContractEvent("evt-1", "C-1001", ContractEventType.CREATED)
            else -> ContractEvent("evt-$n", "C-1001", ContractEventType.CREATED)
        }
        log.info("event {} -> {}", event.eventId, handler.handle(event))
    }
}
