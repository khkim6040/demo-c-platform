package io.github.khkim6040.sample.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.assertj.MockMvcTester
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@AutoConfigureMockMvc
class ApiApplicationTest {
    @Autowired lateinit var context: ApplicationContext
    @Autowired lateinit var mvc: MockMvcTester
    @MockitoBean lateinit var quoteClient: QuoteClient

    private val validBody = """
        {"holderName":"Kim","insuredAmount":10000000,"startDate":"2026-01-01","endDate":"2027-01-01"}
    """.trimIndent()

    @Test
    fun `runs as a servlet web application`() {
        assertThat(context).isInstanceOf(WebApplicationContext::class.java)
    }

    @Test
    fun `creates and reads a contract with correlation header`() {
        val created = mvc.post().uri("/contracts").contentType(MediaType.APPLICATION_JSON).content(validBody).exchange()
        assertThat(created).hasStatus(HttpStatus.CREATED)
        assertThat(created.response.getHeader("X-Correlation-Id")).isNotBlank()
        val id = created.response.contentAsString.substringAfter("\"id\":\"").substringBefore("\"")

        val read = mvc.get().uri("/contracts/{id}", id).exchange()
        assertThat(read).hasStatusOk()
        assertThat(read).bodyJson().extractingPath("$.holderName").isEqualTo("Kim")
    }

    @Test
    fun `domain rule violation becomes 400 problem detail`() {
        val body = validBody.replace("10000000", "0")
        val result = mvc.post().uri("/contracts").contentType(MediaType.APPLICATION_JSON).content(body).exchange()
        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST)
        assertThat(result).bodyJson().extractingPath("$.correlationId").isNotNull()
    }

    @Test
    fun `unknown contract becomes 404`() {
        assertThat(mvc.get().uri("/contracts/nope").exchange()).hasStatus(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `quote multiplies premium by external rate`() {
        given(quoteClient.currentRate()).willReturn(1.5)
        val created = mvc.post().uri("/contracts").contentType(MediaType.APPLICATION_JSON).content(validBody).exchange()
        val id = created.response.contentAsString.substringAfter("\"id\":\"").substringBefore("\"")

        val quote = mvc.get().uri("/contracts/{id}/quote", id).exchange()
        assertThat(quote).hasStatusOk()
        assertThat(quote).bodyJson().extractingPath("$.monthlyPremium").isEqualTo(10000)
        assertThat(quote).bodyJson().extractingPath("$.quotedPremium").isEqualTo(15000)
    }
}
