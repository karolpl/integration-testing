package com.gft.sample.integrationtesting.common.starters

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

interface AuroraStarter {
    companion object {
        private val POSTGRES_DOCKER_IMAGE =
            DockerImageName.parse("postgres:latest")
                .asCompatibleSubstituteFor("postgres")

        var postgresContainer: PostgreSQLContainer<Nothing> =
            PostgreSQLContainer<Nothing>(POSTGRES_DOCKER_IMAGE)
                .apply { start() }

        @JvmStatic
        @DynamicPropertySource
        fun postgresProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgresContainer.jdbcUrl }
            registry.add("spring.datasource.username") { postgresContainer.username }
            registry.add("spring.datasource.password") { postgresContainer.password }
            registry.add("spring.datasource.local") { true }
            registry.add("spring.jpa.hibernate.ddl-auto") { "create" }
        }
    }
}
