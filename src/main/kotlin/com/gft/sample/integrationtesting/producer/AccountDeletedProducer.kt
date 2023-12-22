package com.gft.sample.integrationtesting.producer

import com.gft.sample.integrationtesting.consumer.model.AccountUpdatedEvent
import mu.KotlinLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class AccountDeletedProducer(
    @Value("\${spring.kafka.output.topic.account-deleted}")
    private val accountDeletedTopic: String,
    @Autowired
    private val kafkaTemplate: KafkaTemplate<String, AccountUpdatedEvent>,
) {
    private val log = KotlinLogging.logger {}

    fun publishAccountDeletedMessage(event: AccountUpdatedEvent) {
        kafkaTemplate.send(
            ProducerRecord(accountDeletedTopic, event)
        )
        log.info { "Message published" }
    }
}
