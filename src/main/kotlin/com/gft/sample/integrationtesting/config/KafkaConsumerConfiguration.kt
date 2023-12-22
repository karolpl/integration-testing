package com.gft.sample.integrationtesting.config

import com.gft.sample.integrationtesting.consumer.model.AccountUpdatedEvent
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer

const val INTERNAL_KAFKA_CONTAINER_FACTORY = "internalKafkaContainerFactory"
const val CONSUMER_PROPERTIES = "consumerProperties"

@EnableKafka
@Configuration
class KafkaConsumerConfiguration(
    private val kafkaProperties: KafkaProperties,
) {

    @Bean(CONSUMER_PROPERTIES)
    fun consumerProperties(): Map<String, Any> = mapOf(
        CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers,
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to ErrorHandlingDeserializer::class.java.name,
        JsonDeserializer.TRUSTED_PACKAGES to "*"
    )

    @Bean
    fun customConsumerFactory(
        @Qualifier(CONSUMER_PROPERTIES) consumerProperties: Map<String, Any>
    ): ConsumerFactory<String, AccountUpdatedEvent> =
        DefaultKafkaConsumerFactory(
            consumerProperties,
            StringDeserializer(),
            JsonDeserializer(AccountUpdatedEvent::class.java, false)
        )

    @Bean(INTERNAL_KAFKA_CONTAINER_FACTORY)
    fun kafkaListenerContainerFactory(
        customConsumerFactory: ConsumerFactory<String, AccountUpdatedEvent>,
    ): ConcurrentKafkaListenerContainerFactory<String, AccountUpdatedEvent> =
        ConcurrentKafkaListenerContainerFactory<String, AccountUpdatedEvent>().apply {
            consumerFactory = customConsumerFactory
        }
}
