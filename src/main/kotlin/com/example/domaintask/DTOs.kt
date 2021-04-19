package com.example.domaintask

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import java.time.LocalDateTime

data class DomainInfoDTO(
    val registrarName: String?,
    val expiresDate: LocalDateTime?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DomainDTO(
    val registrarName: String?,
    val expiresDate: LocalDateTime?,
    val price: BigDecimal?
)