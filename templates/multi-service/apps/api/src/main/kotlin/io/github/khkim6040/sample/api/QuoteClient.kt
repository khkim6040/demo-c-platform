package io.github.khkim6040.sample.api

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class QuoteClient(
    builder: RestClient.Builder,
    @Value("\${external.rating.base-url}") baseUrl: String,
) {
    private val client = builder.baseUrl(baseUrl).build()

    fun currentRate(): Double =
        client.get().uri("/rate").retrieve().body(RateResponse::class.java)?.rate
            ?: throw IllegalStateException("rating service returned no body")

    data class RateResponse(val rate: Double)
}
