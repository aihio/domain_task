package com.example.domaintask

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api")
class DomainInfoApi(val domainService: DomainService) {

    @GetMapping(path = ["/getDomainInfo"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(code = HttpStatus.OK)
    fun getDomainInfo(@RequestParam domainName: String): DomainDTO {
        return domainService.getDomainInfo(domainName)
    }
}