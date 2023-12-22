package com.gft.sample.integrationtesting.exception

import org.springframework.http.HttpStatus

class RecordAlreadyExistsException(
    override val message: String,
    override val cause: Throwable? = null
) : RestException(message, HttpStatus.CONFLICT, cause)
