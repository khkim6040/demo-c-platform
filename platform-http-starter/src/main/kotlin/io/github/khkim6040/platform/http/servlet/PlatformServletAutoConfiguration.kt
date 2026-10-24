package io.github.khkim6040.platform.http.servlet

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
class PlatformServletAutoConfiguration {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun correlationIdFilter() = CorrelationIdFilter()

    @Bean
    fun platformExceptionHandler() = PlatformExceptionHandler()
}
