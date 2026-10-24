package io.github.khkim6040.sample.api

import io.github.khkim6040.sample.domain.Contract
import io.github.khkim6040.sample.domain.ContractRepository
import io.github.khkim6040.sample.domain.PremiumCalculator
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.UUID

data class CreateContractRequest(
    val holderName: String,
    val insuredAmount: Long,
    val startDate: LocalDate,
    val endDate: LocalDate,
)

data class QuoteResponse(val contractId: String, val monthlyPremium: Long, val quotedPremium: Long)

@RestController
@RequestMapping("/contracts")
class ContractController(
    private val repository: ContractRepository,
    private val quoteClient: QuoteClient,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CreateContractRequest): Contract =
        repository.save(
            Contract(
                id = UUID.randomUUID().toString(),
                holderName = request.holderName,
                insuredAmount = request.insuredAmount,
                startDate = request.startDate,
                endDate = request.endDate,
            ),
        )

    @GetMapping("/{id}")
    fun get(@PathVariable id: String): Contract = repository.findById(id)

    @GetMapping("/{id}/quote")
    fun quote(@PathVariable id: String): QuoteResponse {
        val contract = repository.findById(id)
        val premium = PremiumCalculator.monthlyPremium(contract)
        val rate = quoteClient.currentRate()
        return QuoteResponse(contract.id, premium, (premium * rate).toLong())
    }
}
