package io.github.khkim6040.sample.domain

object PremiumCalculator {
    private const val MINIMUM_PREMIUM = 1_000L

    fun monthlyPremium(contract: Contract): Long = maxOf(contract.insuredAmount / 1_000, MINIMUM_PREMIUM)
}
