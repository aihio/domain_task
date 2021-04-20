package com.example.domaintask

import com.example.domaintask.models.ApiResponse
import com.example.domaintask.models.Root
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import org.springframework.web.server.ResponseStatusException
import org.springframework.cache.annotation.CacheEvict


interface DomainInfoService {
    fun getDomainInfo(domainName: String): DomainInfoDTO
}

interface DomainPriceService {
    fun getDomainPrice(domainName: String): BigDecimal?
}

@Service
class DomainService(val domainInfoService: DomainInfoService, val domainPriceService: DomainPriceService) {

    fun getDomainInfo(domainName: String): DomainDTO {

        var domainPrice: BigDecimal? = null

        validateDomain(domainName)

        val domainInfo = domainInfoService.getDomainInfo(domainName)

        if (domainInfo.registrarName == null) {
            logger().info("The domain $domainName does not exist")
            domainPrice = domainPriceService.getDomainPrice(domainName)
        }

        return DomainDTO(domainInfo.registrarName, domainInfo.expiresDate, domainPrice)
    }

    private fun validateDomain(domainName: String) {

        val regex = "(?:[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?\\.)+[a-z0-9][a-z0-9-]{0,61}[a-z0-9]".toRegex()

        if (!regex.matches(domainName)) {
            logger().error("Domain $domainName is invalid!")
            throw  ResponseStatusException(HttpStatus.BAD_REQUEST, "Domain is invalid!")
        }
    }
}

@Service
class WhoIsXmlInfoService constructor(val restTemplate: RestTemplate) : DomainInfoService {

    @Value("\${whoisxmlapi.apiKey:}")
    lateinit var apiKey: String

    @Value("\${whoisxmlapi.api.uri:}")
    lateinit var serviceUri: String

    override fun getDomainInfo(domainName: String): DomainInfoDTO {
        logger().info("Getting domain info for $domainName")
        val domainInfo: Root? = try {
            restTemplate.getForObject(
                "$serviceUri/WhoisService?apiKey=$apiKey&domainName=$domainName&outputFormat=json",
                Root::class.java
            )
        } catch (e: Exception) {
            logger().error("DomainInfo service error: $e")
            throw ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "$e")
        }

        return DomainInfoDTO(
            domainInfo?.whoisRecord?.registrarName,
            domainInfo?.whoisRecord?.registryData?.expiresDate
        )
    }
}

@Service
class NamecheapService constructor(val pricesInfoService: NamecheapPricesInfoService) :
    DomainPriceService {

    override fun getDomainPrice(domainName: String): BigDecimal? {

        val domainPrices = pricesInfoService.getPriceInfo()

        val apiResponse: ApiResponse = try {
            val kotlinXmlMapper = XmlMapper(JacksonXmlModule().apply {
                setDefaultUseWrapper(false)
            }).registerKotlinModule()
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

            logger().info("Parsing xml response")
            kotlinXmlMapper.readValue(
                domainPrices,
                ApiResponse::class.java
            )
        } catch (e: Exception) {
            pricesInfoService.evictAllCacheValues()
            throw ResponseStatusException(HttpStatus.NO_CONTENT, "Error during parsing NameCheap API response")
        }

        if (apiResponse.errors != null) {
            pricesInfoService.evictAllCacheValues()
            logger().error(apiResponse.errors.error)
            throw  ResponseStatusException(HttpStatus.BAD_REQUEST, apiResponse.errors.error)
        }
        val price = apiResponse.commandResponse?.userGetPricingResult?.productType?.productCategory
            ?.product?.filter { product -> domainName.endsWith("." + product.name.toString()) }?.toList()

        if (!price.isNullOrEmpty()) {
            return price[0].price?.get(0)?.price
        }
        throw  ResponseStatusException(HttpStatus.NOT_FOUND, "Domain price for $domainName not found!")
    }
}

/**
 * Service to get XML with all domain prices from Namecheap API.
 * The response is cached.
 */
@Service
class NamecheapPricesInfoService(val restTemplate: RestTemplate) {

    @Value("\${namecheap.clientIp:}")
    lateinit var clientIp: String

    @Value("\${namecheap.userName:}")
    lateinit var userName: String

    @Value("\${namecheap.apiKey:}")
    lateinit var apiKey: String

    @Value("\${namecheap.api.uri:}")
    lateinit var apiUri: String

    @Cacheable("prices")
    fun getPriceInfo(): String? {
        logger().info("Getting domain price")
        return restTemplate.getForObject(
            "$apiUri/xml.response?ApiUser=$userName&ApiKey=$apiKey&UserName=$userName" +
                    "&Command=namecheap.users.getPricing&ClientIp=$clientIp&ProductType=DOMAIN&ProductCategory=DOMAINS&ActionName=REGISTER",
            String::class.java
        )
    }

    @CacheEvict(value = ["prices"], allEntries = true)
    fun evictAllCacheValues() {
        //evicts cache
    }

}

