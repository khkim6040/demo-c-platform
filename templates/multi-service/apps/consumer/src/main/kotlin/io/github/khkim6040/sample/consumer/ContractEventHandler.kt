package io.github.khkim6040.sample.consumer

import io.github.khkim6040.sample.domain.ContractEvent
import io.github.khkim6040.sample.domain.ContractEventType
import io.github.khkim6040.sample.domain.ContractRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

enum class HandleResult { PROCESSED, DUPLICATE, DEAD_LETTERED }

@Component
class ContractEventHandler(
    private val repository: ContractRepository,
    @Value("\${consumer.max-attempts}") private val maxAttempts: Int,
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val attempts = ConcurrentHashMap<String, Int>()

    fun handle(event: ContractEvent): HandleResult {
        if (attempts.putIfAbsent(event.eventId, 0) != null) return HandleResult.DUPLICATE
        repeat(maxAttempts) { attempt ->
            attempts[event.eventId] = attempt + 1
            try {
                process(event)
                return HandleResult.PROCESSED
            } catch (e: Exception) {
                log.warn("event {} attempt {}/{} failed: {}", event.eventId, attempt + 1, maxAttempts, e.message)
            }
        }
        log.error("event {} dead-lettered after {} attempts", event.eventId, maxAttempts)
        return HandleResult.DEAD_LETTERED
    }

    fun attemptsFor(eventId: String): Int = attempts[eventId] ?: 0

    private fun process(event: ContractEvent) {
        when (event.type) {
            ContractEventType.CREATED -> repository.findById(event.contractId)
            ContractEventType.SUSPENDED -> repository.save(repository.findById(event.contractId).suspend())
            ContractEventType.EXPIRED -> repository.save(repository.findById(event.contractId).expireIfDue(LocalDate.now()))
        }
    }
}
