package io.github.khkim6040.platform.http.servlet

import io.github.khkim6040.platform.http.CorrelationId
import org.slf4j.MDC
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class PlatformExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    fun badRequest(e: IllegalArgumentException) = problem(HttpStatus.BAD_REQUEST, e)

    @ExceptionHandler(NoSuchElementException::class)
    fun notFound(e: NoSuchElementException) = problem(HttpStatus.NOT_FOUND, e)

    @ExceptionHandler(IllegalStateException::class)
    fun conflict(e: IllegalStateException) = problem(HttpStatus.CONFLICT, e)

    private fun problem(status: HttpStatus, e: Exception): ProblemDetail =
        ProblemDetail.forStatusAndDetail(status, e.message ?: status.reasonPhrase).apply {
            setProperty("correlationId", MDC.get(CorrelationId.MDC_KEY))
        }
}
