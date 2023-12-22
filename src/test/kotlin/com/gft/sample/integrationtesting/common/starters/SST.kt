package com.gft.sample.integrationtesting.common.starters

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.ObjectMapper

interface SST {
    val objectMapper: ObjectMapper

    fun <T> T.toJsonString(serializationInclusionOption: Include = Include.ALWAYS): String =
        objectMapper.setSerializationInclusion(serializationInclusionOption).writeValueAsString(this)
}
