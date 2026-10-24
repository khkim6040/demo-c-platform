package io.github.khkim6040.sample.domain

enum class ContractEventType { CREATED, SUSPENDED, EXPIRED }

data class ContractEvent(
    val eventId: String,
    val contractId: String,
    val type: ContractEventType,
)
