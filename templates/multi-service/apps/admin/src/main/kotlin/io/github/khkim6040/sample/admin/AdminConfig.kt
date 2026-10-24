package io.github.khkim6040.sample.admin

import io.github.khkim6040.sample.domain.ContractRepository
import io.github.khkim6040.sample.domain.InMemoryContractRepository
import io.github.khkim6040.sample.domain.SampleContracts
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class AdminConfig {
    @Bean
    fun contractRepository(): ContractRepository =
        InMemoryContractRepository().apply { SampleContracts.all().forEach(::save) }
}
