package com.gft.sample.integrationtesting.it.consumer

import com.gft.sample.integrationtesting.ACCOUNT_BALANCE
import com.gft.sample.integrationtesting.ACCOUNT_BALANCE_2
import com.gft.sample.integrationtesting.ACCOUNT_NAME
import com.gft.sample.integrationtesting.CURRENCY
import com.gft.sample.integrationtesting.USER_ID
import com.gft.sample.integrationtesting.consumer.model.AccountUpdatedEvent
import com.gft.sample.integrationtesting.db.entity.AccountEntity
import com.gft.sample.integrationtesting.db.repository.AccountRepository
import com.gft.sample.integrationtesting.common.starters.AuroraStarter
import com.gft.sample.integrationtesting.common.starters.KafkaStarter
import com.gft.sample.integrationtesting.common.untilCompleted
import org.apache.kafka.clients.producer.ProducerRecord
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.await
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.test.annotation.DirtiesContext
import java.util.concurrent.TimeUnit

@SpringBootTest
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountUpdatedConsumerIT(
    @Value("\${spring.kafka.input.topic.account-updated.name}")
    private val accountUpdatedTopic: String,
    @Autowired
    private val kafkaTemplate: KafkaTemplate<String, AccountUpdatedEvent>,
    @Autowired
    private val accountRepository: AccountRepository
): AuroraStarter, KafkaStarter {

    @BeforeEach
    fun cleanUp() {
        accountRepository.deleteAll()
    }

    @Test
    fun `should consume event and update record in database`() {
        // given
        accountRepository.save(
            AccountEntity(
                userId = USER_ID,
                name = ACCOUNT_NAME,
                balance = ACCOUNT_BALANCE,
                currency = CURRENCY
            )
        )

        // when
        kafkaTemplate.send(
            ProducerRecord(
                accountUpdatedTopic,
                AccountUpdatedEvent(
                    userId = USER_ID,
                    name = ACCOUNT_NAME,
                    balance = ACCOUNT_BALANCE_2
                )
            )
        )

        // then
        await.atMost(3, TimeUnit.SECONDS).untilCompleted {
            val records = accountRepository.findAll()

            assertThat(records.size).isEqualTo(1)
            assertThat(records[0].userId).isEqualTo(USER_ID)
            assertThat(records[0].name).isEqualTo(ACCOUNT_NAME)
            assertThat(records[0].balance).isEqualTo(ACCOUNT_BALANCE_2)
        }
    }
}
