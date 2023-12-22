package com.gft.sample.integrationtesting.common

import com.gft.sample.integrationtesting.common.starters.MockServerStarter.Companion.mockServerClient
import org.mockserver.client.ForwardChainExpectation
import org.mockserver.matchers.Times
import org.mockserver.mock.Expectation
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse

fun toRequest(
    times: Times = Times.unlimited(),
    block: HttpRequest.() -> HttpRequest
): ForwardChainExpectation = mockServerClient.`when`(HttpRequest.request().run(block), times)

infix fun ForwardChainExpectation.respond(block: HttpResponse.() -> HttpResponse): Array<Expectation> =
    respond(HttpResponse().run(block))