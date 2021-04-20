package com.example.domaintask.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText
import java.math.BigDecimal

data class Price(val price: BigDecimal?, val duration: Int)


data class Product(
    @JsonProperty("Price")
    val price: List<Price>?,
    val name: String?
)

data class ProductCategory(
    @JsonProperty("Product")
    val product: List<Product>?,
    val name: String?
)


data class ProductType
    (val productCategory: ProductCategory?)

data class UserGetPricingResult
    (val productType: ProductType?)


data class CommandResponse
    (val userGetPricingResult: UserGetPricingResult?)

data class Errors(
    val error: String?
)

data class ApiResponse(
    val errors: Errors?,
    val commandResponse: CommandResponse?
)

