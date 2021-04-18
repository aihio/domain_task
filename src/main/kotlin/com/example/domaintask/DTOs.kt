package com.example.domaintask

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DomainInfoDTO(var registrarName: String?, var expiresDate: String?, var price: BigDecimal? = null)