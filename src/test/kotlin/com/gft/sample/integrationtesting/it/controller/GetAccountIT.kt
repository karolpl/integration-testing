package com.gft.sample.integrationtesting.it.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.gft.sample.integrationtesting.ACCOUNT_BALANCE
import com.gft.sample.integrationtesting.ACCOUNT_NAME
import com.gft.sample.integrationtesting.CURRENCY
import com.gft.sample.integrationtesting.USER_ID
import com.gft.sample.integrationtesting.db.entity.AccountEntity
import com.gft.sample.integrationtesting.db.repository.AccountRepository
import com.gft.sample.integrationtesting.common.starters.AuroraStarter
import com.gft.sample.integrationtesting.common.starters.SST
import com.gft.sample.integrationtesting.common.extractingAs
import com.gft.sample.openapi.integrationtesting.v1.model.Account
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
import org.springframework.test.web.servlet.MockMvc

@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetAccountIT(
    @Autowired
    override val objectMapper: ObjectMapper,
    @Autowired
    private val accountRepository: AccountRepository,
    @Autowired mockMvc: MockMvc,
) : AuroraStarter, SST {

    init {
        RestAssuredMockMvc.mockMvc(mockMvc)
        RestAssuredMockMvc.basePath = ""
    }

    @BeforeEach
    fun cleanUp() {
        accountRepository.deleteAll()
    }

    @Test
    fun `should get accounts from database`() {
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
        } When {
            get("/v1/accounts")
        } Then {
            status(HttpStatus.OK)
            extractingAs<List<Account>> {
                assertThat(it.size).isEqualTo(1)
                assertThat(it[0].userId).isEqualTo(USER_ID)
                assertThat(it[0].name).isEqualTo(ACCOUNT_NAME)
                assertThat(it[0].balance).isEqualTo(ACCOUNT_BALANCE)
                assertThat(it[0].currency).isEqualTo(CURRENCY)
            }
        }
    }
}
