package io.github.khkim6040.sample.domain

import java.time.LocalDate

object SampleContracts {
    fun all(): List<Contract> {
        val today = LocalDate.now()
        return listOf(
            Contract("C-1001", "Kim Gwanho", 50_000_000, today.minusMonths(6), today.plusYears(1)),
            Contract("C-1002", "Lee Minji", 20_000_000, today.minusYears(1), today.minusDays(1)),
        )
    }
}
