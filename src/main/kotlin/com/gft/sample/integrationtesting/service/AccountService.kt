package com.gft.sample.integrationtesting.service

import com.gft.sample.integrationtesting.consumer.model.AccountUpdatedEvent
import com.gft.sample.integrationtesting.db.repository.AccountRepository
import com.gft.sample.integrationtesting.exception.RecordAlreadyExistsException
import com.gft.sample.integrationtesting.exception.RecordNotFoundException
import com.gft.sample.integrationtesting.mapper.toAccount
import com.gft.sample.integrationtesting.mapper.toAccountEntity
import com.gft.sample.integrationtesting.producer.AccountDeletedProducer
import com.gft.sample.integrationtesting.service.client.AccountDetailsService
import com.gft.sample.openapi.integrationtesting.v1.model.Account
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val accountDeletedProducer: AccountDeletedProducer,
    private val accountDetailsService: AccountDetailsService
) {

    private val log = KotlinLogging.logger { }

    fun createAccount(account: Account): Account {
        return when {
            accountRepository.existsByUserIdAndName(account.userId, account.name) -> {
                throw RecordAlreadyExistsException("The account with given name and userId already exists!")
            }

            else -> {
                val accountDetails = accountDetailsService.getAccountDetails(account.name)
                accountRepository.saveAndFlush(account.toAccountEntity(accountDetails?.accountDescription)).toAccount()
            }
        }
    }

    fun getAccounts(): List<Account> {
        val accounts = accountRepository.findAll()
        return accounts.map { it.toAccount() }
    }

    fun updateAccount(event: AccountUpdatedEvent) {
        accountRepository.findByUserIdAndName(event.userId, event.name)
            ?.apply {
                balance = event.balance
            }?.run(accountRepository::save)
            ?: log.info { "Account doesn't exist for provided userId ${event.userId} and name ${event.name}" }
    }

    fun deleteAccount(accountId: UUID) {
        accountRepository.findById(accountId)
            .takeIf { it.isPresent }
            ?.also {
                accountRepository.deleteById(accountId)
                val accountEntity = it.get()
                accountDeletedProducer.publishAccountDeletedMessage(
                    AccountUpdatedEvent(
                        userId = accountEntity.userId!!,
                        name = accountEntity.name!!,
                        balance = accountEntity.balance!!,
                        isDeleted = true
                    )
                )
            }?: throw RecordNotFoundException("The account with given id doesn't exist.")
    }
}
