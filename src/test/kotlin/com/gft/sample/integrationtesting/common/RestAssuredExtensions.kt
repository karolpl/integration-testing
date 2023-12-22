package com.gft.sample.integrationtesting.common

import io.restassured.common.mapper.TypeRef
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse

inline fun <reified T> ValidatableMockMvcResponse.extractingAs(block: (T) -> Unit) {
    extract().`as`(object : TypeRef<T>() {}).run(block)
}
