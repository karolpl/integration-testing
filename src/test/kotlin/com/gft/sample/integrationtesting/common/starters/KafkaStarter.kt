package com.gft.sample.integrationtesting.common.starters

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName

interface KafkaStarter {
    companion object {
        private val KAFKA_DOCKER_IMAGE =
            DockerImageName.parse("confluentinc/cp-kafka:6.2.1")
                .asCompatibleSubstituteFor("confluentinc/cp-kafka")

        private val kafka = KafkaContainer(KAFKA_DOCKER_IMAGE)
            .apply { start() }

        val bootstrapServers: String by lazy { kafka.bootstrapServers }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.kafka.bootstrapServers") { bootstrapServers }
        }
    }
}
