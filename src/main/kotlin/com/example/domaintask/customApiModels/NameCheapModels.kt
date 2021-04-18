package com.example.domaintask.customApiModels

import com.fasterxml.jackson.annotation.JsonProperty

data class Price(var price: String?)


data class Product(
    @JsonProperty("Price")
    var price: List<Price>? = null,
    var name: String? = null
)

data class ProductCategory(
    @JsonProperty("Product")
    var product: List<Product>? = null,
    var name: String? = null
)


data class ProductType
    (var productCategory: ProductCategory? = null)

data class UserGetPricingResult
    (var productType: ProductType? = null)


data class CommandResponse
    (var userGetPricingResult: UserGetPricingResult? = null)


data class ApiResponse(
    var commandResponse: CommandResponse? = null
)

