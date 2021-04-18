package com.example.domaintask.customApiModels

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class Price(var price: BigDecimal?, var duration: Int)


data class Product(
    @JsonProperty("Price")
    var price: List<Price>?,
    var name: String?
)

data class ProductCategory(
    @JsonProperty("Product")
    var product: List<Product>?,
    var name: String?
)


data class ProductType
    (var productCategory: ProductCategory?)

data class UserGetPricingResult
    (var productType: ProductType?)


data class CommandResponse
    (var userGetPricingResult: UserGetPricingResult?)


data class ApiResponse(
    var commandResponse: CommandResponse?
)

