package com.example.domaintask.customApiModels

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Models for whoisxmlapi.com
 * @see <a href="whoisxmlapi.com">whoisxmlapi.com</a>
 */
data class Root(@JsonProperty("WhoisRecord") var whoisRecord: WhoisRecord? = null)

@JsonIgnoreProperties(ignoreUnknown = true)
data class WhoisRecord(var registrarName: String?, var registryData: RegistryData?)

@JsonIgnoreProperties(ignoreUnknown = true)
data class RegistryData(var expiresDate: String? = null)