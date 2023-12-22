package com.gft.sample.integrationtesting.exception

import org.springframework.http.HttpStatus

class RecordNotFoundException(
    override val message: String,
    override val cause: Throwable? = null
) : RestException(message, HttpStatus.NOT_FOUND, cause)
