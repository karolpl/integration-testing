package com.gft.sample.integrationtesting.exception

import org.springframework.http.HttpStatus

abstract class RestException(
    override val message: String,
    open val statusCode: HttpStatus,
    override val cause: Throwable? = null
) : RuntimeException(message, cause)
