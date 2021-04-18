package com.example.domaintask

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus


@SpringBootApplication
class DomainTaskApplication

fun main(args: Array<String>) {
    runApplication<DomainTaskApplication>(*args)
}

@RestController
class DomainInfoApi(val domainInfoInfoService: DomainInfoService) {

    @GetMapping(path = ["/api/getDomainInfo"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(code = HttpStatus.OK)
    fun getDomainInfo(@RequestParam domainName: String): DomainInfoDTO {
        return domainInfoInfoService.getDomainInfo(domainName)
    }
}





