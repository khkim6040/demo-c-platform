package io.github.khkim6040.sample.consumer

import io.github.khkim6040.sample.domain.ContractEvent
import io.github.khkim6040.sample.domain.ContractEventType
import io.github.khkim6040.sample.domain.ContractRepository
import io.github.khkim6040.sample.domain.ContractStatus
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext

@SpringBootTest(properties = ["consumer.fake-source.interval=PT1H"])
class ConsumerApplicationTest {
    @Autowired lateinit var context: ApplicationContext
    @Autowired lateinit var handler: ContractEventHandler
    @Autowired lateinit var repository: ContractRepository

    @Test
    fun `has no servlet on the classpath and runs as a plain context`() {
        assertThatThrownBy { Class.forName("jakarta.servlet.Servlet") }.isInstanceOf(ClassNotFoundException::class.java)
        assertThat(context).isInstanceOf(AnnotationConfigApplicationContext::class.java)
    }

    @Test
    fun `processes once and ignores duplicates`() {
        val event = ContractEvent("test-1", "C-1001", ContractEventType.SUSPENDED)
        assertThat(handler.handle(event)).isEqualTo(HandleResult.PROCESSED)
        assertThat(repository.findById("C-1001").status).isEqualTo(ContractStatus.SUSPENDED)
        assertThat(handler.handle(event)).isEqualTo(HandleResult.DUPLICATE)
    }

    @Test
    fun `dead-letters after max attempts`() {
        val event = ContractEvent("test-2", "unknown", ContractEventType.SUSPENDED)
        assertThat(handler.handle(event)).isEqualTo(HandleResult.DEAD_LETTERED)
        assertThat(handler.attemptsFor("test-2")).isEqualTo(3)
    }
}
