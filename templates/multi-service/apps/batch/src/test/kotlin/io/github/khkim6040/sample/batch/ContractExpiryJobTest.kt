package io.github.khkim6040.sample.batch

import io.github.khkim6040.sample.domain.ContractRepository
import io.github.khkim6040.sample.domain.ContractStatus
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.job.Job
import org.springframework.batch.core.job.parameters.JobParameters
import org.springframework.batch.core.launch.JobOperator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext

@SpringBootTest(properties = ["spring.batch.job.enabled=false"])
class ContractExpiryJobTest {
    @Autowired lateinit var context: ApplicationContext
    @Autowired lateinit var jobOperator: JobOperator
    @Autowired lateinit var job: Job
    @Autowired lateinit var repository: ContractRepository

    @Test
    fun `has no servlet on the classpath and runs as a plain context`() {
        assertThatThrownBy { Class.forName("jakarta.servlet.Servlet") }.isInstanceOf(ClassNotFoundException::class.java)
        assertThat(context).isInstanceOf(AnnotationConfigApplicationContext::class.java)
    }

    @Test
    fun `expires due contracts and completes`() {
        val execution = jobOperator.start(job, JobParameters())
        assertThat(execution.exitStatus).isEqualTo(ExitStatus.COMPLETED)
        assertThat(execution.stepExecutions.single().writeCount).isEqualTo(1L)
        assertThat(repository.findById("C-1002").status).isEqualTo(ContractStatus.EXPIRED)
        assertThat(repository.findById("C-1001").status).isEqualTo(ContractStatus.ACTIVE)
    }
}
