package io.github.khkim6040.sample.domain

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class ContractTest {
    private val today = LocalDate.of(2026, 10, 24)
    private fun contract(end: LocalDate = today.plusYears(1), amount: Long = 10_000_000) =
        Contract("C-1", "Kim", amount, today.minusDays(30), end)

    @Test
    fun `rejects non-positive insured amount`() {
        assertFailsWith<IllegalArgumentException> { contract(amount = 0) }
    }

    @Test
    fun `rejects end date not after start date`() {
        assertFailsWith<IllegalArgumentException> { contract(end = today.minusDays(30)) }
    }

    @Test
    fun `suspend only from active`() {
        val suspended = contract().suspend()
        assertEquals(ContractStatus.SUSPENDED, suspended.status)
        assertFailsWith<IllegalStateException> { suspended.suspend() }
    }

    @Test
    fun `expires when end date reached`() {
        val due = contract(end = today)
        assertEquals(ContractStatus.EXPIRED, due.expireIfDue(today).status)
        val notDue = contract()
        assertSame(notDue, notDue.expireIfDue(today))
    }

    @Test
    fun `monthly premium is one per mille with floor`() {
        assertEquals(10_000, PremiumCalculator.monthlyPremium(contract()))
        assertEquals(1_000, PremiumCalculator.monthlyPremium(contract(amount = 500)))
    }

    @Test
    fun `in-memory repository finds and fails`() {
        val repo = InMemoryContractRepository()
        repo.save(contract())
        assertEquals("C-1", repo.findById("C-1").id)
        assertFailsWith<NoSuchElementException> { repo.findById("nope") }
    }

    @Test
    fun `sample contracts contain exactly one due for expiry`() {
        val due = SampleContracts.all().count { it.expireIfDue(LocalDate.now()).status == ContractStatus.EXPIRED }
        assertEquals(1, due)
    }
}
