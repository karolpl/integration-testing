package com.gft.sample.openapi.integrationtesting.v1.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

/**
 * 
 * @param userId 
 * @param name 
 * @param balance 
 * @param currency
 * @param id 
 * @param createdAt 
 * @param updatedAt 
 */
data class Account(

    @get:NotNull 
    @JsonProperty("userId") val userId: java.util.UUID,

    @get:NotNull 
    @JsonProperty("name") val name: kotlin.String,

    @get:NotNull 
    @JsonProperty("balance") val balance: java.math.BigDecimal,

    @get:NotNull 
    @JsonProperty("currency") val currency: kotlin.String,

    @JsonProperty("id") val id: java.util.UUID? = null,

    @JsonProperty("createdAt") val createdAt: java.time.Instant? = null,

    @JsonProperty("updatedAt") val updatedAt: java.time.Instant? = null
) {

}

