package io.github.khkim6040.sample.admin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.assertj.MockMvcTester
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@AutoConfigureMockMvc
class AdminApplicationTest {
    @Autowired lateinit var context: ApplicationContext
    @Autowired lateinit var mvc: MockMvcTester

    @Test
    fun `runs as a servlet web application`() {
        assertThat(context).isInstanceOf(WebApplicationContext::class.java)
    }

    @Test
    fun `lists seeded contracts`() {
        val result = mvc.get().uri("/admin/contracts").exchange()
        assertThat(result).hasStatusOk()
        assertThat(result).bodyJson().extractingPath("$.length()").isEqualTo(2)
    }

    @Test
    fun `suspends once then conflicts`() {
        assertThat(mvc.post().uri("/admin/contracts/C-1001/suspend").exchange()).hasStatusOk()
        val second = mvc.post().uri("/admin/contracts/C-1001/suspend").exchange()
        assertThat(second).hasStatus(HttpStatus.CONFLICT)
        assertThat(second).bodyJson().extractingPath("$.correlationId").isNotNull()
    }
}
