package com.gft.sample.integrationtesting.filter

import com.gft.sample.integrationtesting.exception.ExternalApiCallException
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import reactor.core.publisher.Mono

object AccountDetailsApiFilter {

    fun exchangeFilterFunction(apiName: String): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor {
            Mono.just(it)
        }.andThen(
            ExchangeFilterFunction.ofResponseProcessor {
                val statusCode = it.statusCode()
                if (!statusCode.is2xxSuccessful) {
                    if (statusCode == HttpStatus.NOT_FOUND) throw ExternalApiCallException(
                        message = "$apiName: Account not found",
                        httpCode = HttpStatus.NOT_FOUND.value()
                    )
                    throw ExternalApiCallException(
                        message = "$apiName: General error",
                        httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value()
                    )
                }
                Mono.just(it)
            }
        )
    }
}
