package com.gft.sample.integrationtesting.it.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.gft.sample.integrationtesting.ACCOUNT_BALANCE
import com.gft.sample.integrationtesting.ACCOUNT_DESCRIPTION
import com.gft.sample.integrationtesting.ACCOUNT_NAME
import com.gft.sample.integrationtesting.CURRENCY
import com.gft.sample.integrationtesting.USER_ID
import com.gft.sample.integrationtesting.db.entity.AccountEntity
import com.gft.sample.integrationtesting.db.repository.AccountRepository
import com.gft.sample.integrationtesting.common.starters.AuroraStarter
import com.gft.sample.integrationtesting.common.starters.SST
import com.gft.sample.integrationtesting.common.extractingAs
import com.gft.sample.integrationtesting.common.starters.MockServerStarter
import com.gft.sample.integrationtesting.mockAccountDetailsApi
import com.gft.sample.openapi.integrationtesting.v1.model.Account
import generated.model.AccountDetails
import io.restassured.http.ContentType
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.kotlin.extensions.Given
import io.restassured.module.mockmvc.kotlin.extensions.Then
import io.restassured.module.mockmvc.kotlin.extensions.When
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc

@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateAccountIT(
    @Autowired
    override val objectMapper: ObjectMapper,
    @Autowired
    private val accountRepository: AccountRepository,
    @Autowired mockMvc: MockMvc,
) : AuroraStarter, MockServerStarter, SST {

    init {
        RestAssuredMockMvc.mockMvc(mockMvc)
        RestAssuredMockMvc.basePath = ""
    }

    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun mockServerProperties(registry: DynamicPropertyRegistry) {
            registry.add("servers.url.account-details-service") { "${MockServerStarter.mockServerUrl}/account-details-service" }
        }
    }

    @BeforeEach
    fun cleanUp() {
        accountRepository.deleteAll()
        MockServerStarter.mockServerClient.reset()
    }

    @Test
    fun `should create account in database`() {
        mockAccountDetailsApi(
            ACCOUNT_NAME,
            AccountDetails()
                .accountName(ACCOUNT_NAME)
                .accountDescription(ACCOUNT_DESCRIPTION)
        )

        Given {
            contentType(ContentType.JSON)
            body(createAccountCreationRequest().toJsonString())
        } When {
            post("/v1/accounts")
        } Then {
            status(HttpStatus.CREATED)
            extractingAs<Account> {
                assertThat(it.userId).isEqualTo(USER_ID)
                assertThat(it.name).isEqualTo(ACCOUNT_NAME)
                assertThat(it.balance).isEqualTo(ACCOUNT_BALANCE)
                assertThat(it.currency).isEqualTo(CURRENCY)
            }
        }

        val records = accountRepository.findAll()
        assertThat(records.size).isEqualTo(1)

        records[0].also {
            assertThat(it.userId).isEqualTo(USER_ID)
            assertThat(it.name).isEqualTo(ACCOUNT_NAME)
            assertThat(it.balance).isEqualTo(ACCOUNT_BALANCE)
            assertThat(it.currency).isEqualTo(CURRENCY)
            assertThat(it.description).isEqualTo(ACCOUNT_DESCRIPTION) // description fetched from external API
            assertThat(it.id).isNotNull()
            assertThat(it.createdAt).isNotNull()
            assertThat(it.updatedAt).isNotNull()
        }
    }

    @Test
    fun `should return CONFLICT http status when account already exists in database`() {
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
        // then
        Given {
            contentType(ContentType.JSON)
            body(createAccountCreationRequest().toJsonString())
        } When {
            post("/v1/accounts")
        } Then {
            status(HttpStatus.CONFLICT)
        }
    }

    private fun createAccountCreationRequest() = Account(
        userId = USER_ID,
        name = ACCOUNT_NAME,
        balance = ACCOUNT_BALANCE,
        currency = CURRENCY
    )
}
