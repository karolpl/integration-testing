package com.gft.sample.integrationtesting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IntegrationTestingApplication

fun main(args: Array<String>) {
	runApplication<IntegrationTestingApplication>(*args)
}
