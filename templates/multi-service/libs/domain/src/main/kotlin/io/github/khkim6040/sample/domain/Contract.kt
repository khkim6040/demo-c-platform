package io.github.khkim6040.sample.domain

import java.time.LocalDate

enum class ContractStatus { ACTIVE, SUSPENDED, EXPIRED }

data class Contract(
    val id: String,
    val holderName: String,
    val insuredAmount: Long,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val status: ContractStatus = ContractStatus.ACTIVE,
) {
    init {
        require(holderName.isNotBlank()) { "holderName must not be blank" }
        require(insuredAmount > 0) { "insuredAmount must be positive" }
        require(endDate.isAfter(startDate)) { "endDate must be after startDate" }
    }

    fun suspend(): Contract {
        check(status == ContractStatus.ACTIVE) { "contract $id is $status, only ACTIVE can be suspended" }
        return copy(status = ContractStatus.SUSPENDED)
    }

    fun expireIfDue(today: LocalDate): Contract =
        if (status != ContractStatus.EXPIRED && !endDate.isAfter(today)) copy(status = ContractStatus.EXPIRED) else this
}
