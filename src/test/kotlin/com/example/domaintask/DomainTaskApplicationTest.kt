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
internal class DomainTaskApplicationTest {

    @LocalServerPort
    var randomServerPort = 0

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var pricesInfoService: NamecheapPricesInfoService

    @Test
    fun `controller should return info about an existing domain`() {
        val testDomain = "test.us"

        stubFor(
            get(urlPathEqualTo("/WhoisService"))
                .willReturn(
                    aResponse().withHeader(TestingConstants.CONTENT_TYPE, TestingConstants.APPLICATION_JSON)
                        .withBody(TestingConstants.RAW_JSON_WITH_NAME_AND_DATE)
                )
        )

        this.mockMvc.perform(get("/api/getDomainInfo?domainName=$testDomain")).andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().contentType(TestingConstants.APPLICATION_JSON))
            .andExpect(
                content()
                    .string(containsString("{\"registrarName\":\"ICANN\",\"expiresDate\":\"2010-08-30\"}"))
            )
    }

    @Test
    fun `controller should return bad request on invalid domain`() {
        val testDomain = "thi4афывп32пin"
        stubFor(
            get(urlPathEqualTo("/WhoisService"))
                .willReturn(aResponse().withHeader(TestingConstants.CONTENT_TYPE, TestingConstants.APPLICATION_JSON))
        )
        this.mockMvc.perform(get("/api/getDomainInfo?domainName=$testDomain")).andDo(print())
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `controller should return bad request on empty domain parameter`() {
        stubFor(
            get(urlPathEqualTo("/WhoisService"))
                .willReturn(aResponse().withHeader(TestingConstants.CONTENT_TYPE, TestingConstants.APPLICATION_JSON))
        )
        this.mockMvc.perform(get("/api/getDomainInfo")).andDo(print())
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `controller should return not found on unexisting domain prince`() {
        val testDomain = "i.dont.exist"
        stubFor(
            get(urlPathEqualTo("/WhoisService"))
                .willReturn(
                    aResponse().withHeader(TestingConstants.CONTENT_TYPE, TestingConstants.APPLICATION_JSON)
                        .withBody("{}")
                )
        )

        stubFor(
            get(urlPathEqualTo("/xml.response"))
                .willReturn(
                    aResponse().withHeader(TestingConstants.CONTENT_TYPE, "text/xml").withBody(
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
                .willReturn(
                    aResponse().withHeader(TestingConstants.CONTENT_TYPE, TestingConstants.APPLICATION_JSON)
                        .withBody("{}")
                )
        )

        stubFor(
            get(urlPathEqualTo("/xml.response"))
                .willReturn(
                    aResponse().withHeader(TestingConstants.CONTENT_TYPE, "text/xml").withBody(
                        TestingConstants.XML_WITH_PRICES
                    )
                )
        )

        this.mockMvc.perform(get("/api/getDomainInfo?domainName=$testDomain")).andDo(print())
            .andExpect(status().isOk).andExpect(content().string(containsString("{\"price\":55.98}")))
    }

    @Test
    fun `controller should return bad request for XML with error`() {
        val testDomain = "iexist.test"
        this.pricesInfoService.evictAllCacheValues()
        stubFor(
            get(urlPathEqualTo("/WhoisService"))
                .willReturn(
                    aResponse().withHeader(TestingConstants.CONTENT_TYPE, TestingConstants.APPLICATION_JSON)
                        .withBody("{}")
                )
        )

        stubFor(
            get(urlPathEqualTo("/xml.response"))
                .willReturn(
                    aResponse().withHeader(TestingConstants.CONTENT_TYPE, "text/xml").withBody(
                        TestingConstants.XML_WITH_USERNAME_ERROR
                    )
                )
        )

        this.mockMvc.perform(get("/api/getDomainInfo?domainName=$testDomain")).andDo(print())
            .andExpect(status().isBadRequest)
    }

}