package io.github.khkim6040.platform.http.servlet

import io.github.khkim6040.platform.http.CorrelationId
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

class CorrelationIdFilter : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val id = request.getHeader(CorrelationId.HEADER)?.takeIf { it.isNotBlank() } ?: UUID.randomUUID().toString()
        MDC.put(CorrelationId.MDC_KEY, id)
        response.setHeader(CorrelationId.HEADER, id)
        try {
            chain.doFilter(request, response)
        } finally {
            MDC.remove(CorrelationId.MDC_KEY)
        }
    }
}
