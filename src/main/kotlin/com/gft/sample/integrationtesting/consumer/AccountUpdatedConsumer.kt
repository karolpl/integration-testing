package com.gft.sample.integrationtesting.consumer

import com.gft.sample.integrationtesting.config.INTERNAL_KAFKA_CONTAINER_FACTORY
import com.gft.sample.integrationtesting.consumer.model.AccountUpdatedEvent
import com.gft.sample.integrationtesting.service.AccountService
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class AccountUpdatedConsumer(
    private val accountService: AccountService
) {

    private val log = KotlinLogging.logger { }

    @KafkaListener(
        topics = ["\${spring.kafka.input.topic.account-updated.name}"],
        groupId = "\${spring.kafka.input.topic.account-updated.group-id}",
        containerFactory = INTERNAL_KAFKA_CONTAINER_FACTORY
    )
    fun consumeEvent(event: AccountUpdatedEvent) {
        log.info { "Received AccountUpdatedEvent with userId ${event.userId} and name ${event.name}" }
        accountService.updateAccount(event)
    }
}
