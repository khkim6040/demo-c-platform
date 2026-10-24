package io.github.khkim6040.sample.batch

import io.github.khkim6040.sample.domain.ContractRepository
import io.github.khkim6040.sample.domain.InMemoryContractRepository
import io.github.khkim6040.sample.domain.SampleContracts
import org.slf4j.LoggerFactory
import org.springframework.batch.core.job.Job
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.Step
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.infrastructure.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate

@Configuration(proxyBeanMethods = false)
class ContractExpiryJobConfig {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun contractRepository(): ContractRepository =
        InMemoryContractRepository().apply { SampleContracts.all().forEach(::save) }

    @Bean
    fun expireContractsStep(jobRepository: JobRepository, repository: ContractRepository): Step {
        val tasklet = Tasklet { contribution, _ ->
            val today = LocalDate.now()
            var expired = 0L
            repository.findAll().forEach { contract ->
                val updated = contract.expireIfDue(today)
                if (updated !== contract) {
                    repository.save(updated)
                    expired++
                }
            }
            contribution.incrementWriteCount(expired)
            log.info("expired {} contract(s) as of {}", expired, today)
            RepeatStatus.FINISHED
        }
        return StepBuilder("expireContracts", jobRepository).tasklet(tasklet).build()
    }

    @Bean
    fun contractExpiryJob(jobRepository: JobRepository, expireContractsStep: Step): Job =
        JobBuilder("contractExpiryJob", jobRepository).start(expireContractsStep).build()
}
