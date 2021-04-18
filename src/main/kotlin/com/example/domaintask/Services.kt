package com.example.domaintask

import com.example.domaintask.customApiModels.Root
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

interface DomainInfoService {
    fun getDomainInfo(domainName: String): DomainInfoDTO
}

interface DomainPriceService {
    fun getDomainPrice(domainName: String): DomainInfoDTO
}

@Service
class WhoIsXmlInfoService constructor(val restTemplate: RestTemplate) : DomainInfoService {
    @Value("\${whoisxmlapi.apiKey:}")
    lateinit var apiKey: String

    override fun getDomainInfo(domainName: String): DomainInfoDTO {

        val domainInfo = restTemplate.getForObject(
            "https://www.whoisxmlapi.com/whoisserver/WhoisService?apiKey=$apiKey&domainName=$domainName&outputFormat=json",
            Root::class.java
        )

        return DomainInfoDTO(domainInfo?.whoisRecord?.registrarName, domainInfo?.whoisRecord?.registryData?.expiresDate)
    }
}

@Service
class NamecheapService : DomainPriceService {

    override fun getDomainPrice(domainName: String): DomainInfoDTO {
        TODO("Not yet implemented")
    }

}