package com.example.domaintask


import com.github.tomakehurst.wiremock.client.WireMock.*
import org.hamcrest.Matchers.containsString
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.web.servlet.MockMvc


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureWireMock(port = 6443)
@AutoConfigureMockMvc
internal class DomainTaskApplicationKtTest {

    @LocalServerPort
    var randomServerPort = 0

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `controller should return info about an existing domain`() {
        val testDomain = "test.us"

        stubFor(
            get(urlPathEqualTo("/WhoisService"))
                .willReturn(
                    aResponse().withHeader("Content-Type", "application/json")
                        .withBody(TestingConstants.JSON_WITH_NAME_AND_DATE)
                )
        )

        this.mockMvc.perform(get("/api/getDomainInfo?domainName=$testDomain")).andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andExpect(
                content()
                    .string(containsString("{\"registrarName\":\"ICANN\",\"expiresDate\":\"2010-08-30T04:00:00\"}"))
            )
    }

    @Test
    fun `controller should return bad request on invalid domain`() {
        val testDomain = "thi4афывп32пin"
        stubFor(
            get(urlPathEqualTo("/WhoisService"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json"))
        )
        this.mockMvc.perform(get("/api/getDomainInfo?domainName=$testDomain")).andDo(print())
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `controller should return not found on unexisting domain prince`() {
        val testDomain = "i.dont.exist"
        stubFor(
            get(urlPathEqualTo("/WhoisService"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json").withBody("{}"))
        )

        stubFor(
            get(urlPathEqualTo("/xml.response"))
                .willReturn(
                    aResponse().withHeader("Content-Type", "text/xml").withBody(
                        TestingConstants.XML_WITH_PRICES
                    )
                )
        )

        this.mockMvc.perform(get("/api/getDomainInfo?domainName=$testDomain")).andDo(print())
            .andExpect(status().isNotFound)

    }

    @Test
    fun `controller should return correct price for domain from price list`() {
        val testDomain = "iexist.test"
        stubFor(
            get(urlPathEqualTo("/WhoisService"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json").withBody("{}"))
        )

        stubFor(
            get(urlPathEqualTo("/xml.response"))
                .willReturn(
                    aResponse().withHeader("Content-Type", "text/xml").withBody(
                        TestingConstants.XML_WITH_PRICES
                    )
                )
        )

        this.mockMvc.perform(get("/api/getDomainInfo?domainName=$testDomain")).andDo(print())
            .andExpect(status().isOk)
    }

}