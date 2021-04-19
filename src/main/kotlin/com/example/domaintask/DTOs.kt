package com.example.domaintask

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal

data class DomainInfoDTO(
    var registrarName: String?,
    var expiresDate: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DomainDTO(
    var registrarName: String?,
    var expiresDate: String?,
    var price: BigDecimal?,
    var message: String? = null
)