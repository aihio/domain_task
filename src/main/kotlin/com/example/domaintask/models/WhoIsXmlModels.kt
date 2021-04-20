package com.example.domaintask.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

/**
 * Models for whoisxmlapi.com
 * @see <a href="whoisxmlapi.com">whoisxmlapi.com</a>
 */
data class Root(@JsonProperty("WhoisRecord") val whoisRecord: WhoisRecord?)

@JsonIgnoreProperties(ignoreUnknown = true)
data class WhoisRecord(val registrarName: String?, val registryData: RegistryData?)

@JsonIgnoreProperties(ignoreUnknown = true)
data class RegistryData(val expiresDate: LocalDate?)