package com.gft.sample.integrationtesting.exception

open class ExternalApiCallException(
    override val message: String,
    open val httpCode: Int
) : BaseException(message)
