package com.gft.sample.openapi.integrationtesting.v1.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

/**
 * 
 * @param message Detailed information about the error
 */
data class ErrorResponse(

    @get:NotNull 
    @JsonProperty("message") val message: kotlin.String
) {

}

