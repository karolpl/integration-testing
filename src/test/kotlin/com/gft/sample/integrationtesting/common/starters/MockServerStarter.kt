package com.gft.sample.integrationtesting.common.starters

import org.mockserver.client.MockServerClient
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

interface MockServerStarter {
    companion object {
        private const val PORT = 1080
        private val DOCKER_IMAGE =
            DockerImageName.parse("mockserver/mockserver:mockserver-5.11.2")

        var mockServerContainer: GenericContainer<Nothing> =
            GenericContainer<Nothing>(DOCKER_IMAGE)
                .apply { waitingFor(Wait.forHttp("/mockserver/status").withMethod("PUT").forStatusCode(200)) }
                .apply { withExposedPorts(PORT) }
                .apply { start() }

        val mockServerClient by lazy {
            MockServerClient(mockServerContainer.host, mockServerContainer.getMappedPort(PORT))
        }

        val mockServerUrl: String
            get() = "http://${mockServerContainer.containerIpAddress}:${mockServerContainer.getMappedPort(PORT)}"
    }
}
