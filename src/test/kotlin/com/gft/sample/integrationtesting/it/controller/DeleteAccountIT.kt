package com.gft.sample.integrationtesting.it.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.gft.sample.integrationtesting.ACCOUNT_BALANCE
import com.gft.sample.integrationtesting.ACCOUNT_NAME
import com.gft.sample.integrationtesting.CURRENCY
import com.gft.sample.integrationtesting.USER_ID
import com.gft.sample.integrationtesting.common.starters.AuroraStarter
import com.gft.sample.integrationtesting.common.starters.KafkaStarter
import com.gft.sample.integrationtesting.common.starters.SST
import com.gft.sample.integrationtesting.common.untilCompleted
import com.gft.sample.integrationtesting.consumer.model.AccountUpdatedEvent
import com.gft.sample.integrationtesting.db.entity.AccountEntity
import com.gft.sample.integrationtesting.db.repository.AccountRepository
import io.restassured.http.ContentType
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.kotlin.extensions.Given
import io.restassured.module.mockmvc.kotlin.extensions.Then
import io.restassured.module.mockmvc.kotlin.extensions.When
import org.apache.kafka.clients.consumer.Consumer
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.await
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import java.time.Duration
import java.util.UUID
import java.util.concurrent.TimeUnit

private const val DELETE_ENDPOINT_URL = "/v1/accounts/{accountId}"

@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeleteAccountIT(
    @Value("\${spring.kafka.output.topic.account-deleted}")
    private val accountDeletedTopic: String,
    @Autowired
    override val objectMapper: ObjectMapper,
    @Autowired
    private val accountRepository: AccountRepository,
    @Autowired
    mockMvc: MockMvc,
    @Autowired
    private val customConsumerFactory: ConsumerFactory<String, AccountUpdatedEvent>
) : AuroraStarter, KafkaStarter, SST {

    init {
        RestAssuredMockMvc.mockMvc(mockMvc)
        RestAssuredMockMvc.basePath = ""
    }

    @BeforeEach
    fun cleanUp() {
        accountRepository.deleteAll()
    }

    @Test
    fun `should delete account from database and publish event`() {
        // given
        val accountEntity = accountRepository.save(
            AccountEntity(
                userId = USER_ID,
                name = ACCOUNT_NAME,
                balance = ACCOUNT_BALANCE,
                currency = CURRENCY
            )
        )

        val consumer = customConsumerFactory
            .createConsumer("groupId123", "")
            .also { it.subscribe(listOf(accountDeletedTopic)) }

        // when
        // then
        Given {
            contentType(ContentType.JSON)
        } When {
            delete(DELETE_ENDPOINT_URL, accountEntity.id.toString())
        } Then {
            status(HttpStatus.NO_CONTENT)
        }

        val records = accountRepository.findAll()
        assertThat(records.size).isEqualTo(0)

        assertPublishedMessage(consumer)
    }

    @Test
    fun `should return NOT_FOUND http status when account does not exist in database`() {
        Given {
            contentType(ContentType.JSON)
        } When {
            delete(DELETE_ENDPOINT_URL, UUID.randomUUID().toString())
        } Then {
            status(HttpStatus.NOT_FOUND)
        }
    }

    private fun assertPublishedMessage(consumer: Consumer<String, AccountUpdatedEvent>) {
        await.atMost(5, TimeUnit.SECONDS).untilCompleted {
            val event = consumer
                .poll(Duration.ofSeconds(3))
                .records(accountDeletedTopic)
                .last().value()

            assertThat(event.userId).isEqualTo(USER_ID)
            assertThat(event.name).isEqualTo(ACCOUNT_NAME)
            assertThat(event.balance).isEqualTo(ACCOUNT_BALANCE)
            assertThat(event.isDeleted).isEqualTo(true)
        }
    }
}
