package com.example.domaintask

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import java.time.LocalDate

data class DomainInfoDTO(
    val registrarName: String?,
    val expiresDate: LocalDate?
)

data class DomainPriceDTO(
    val price: BigDecimal?,
    val currency: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DomainDTO(
    val registrarName: String?,
    val expiresDate: LocalDate?,
    val price: BigDecimal?,
    val currency: String?
)