package com.gft.sample.integrationtesting.controller

import com.gft.sample.integrationtesting.service.AccountService
import com.gft.sample.openapi.integrationtesting.v1.api.AccountControllerApi
import com.gft.sample.openapi.integrationtesting.v1.model.Account
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.UUID

@RestController
class AccountController(
    private val accountService: AccountService
) : AccountControllerApi {

    override fun createAccount(account: Account): ResponseEntity<Account> {
        val createdAccount = accountService.createAccount(account)

        return ResponseEntity
            .created(buildResourceLocation(createdAccount.id.toString()))
            .body(createdAccount)
    }

    override fun getAccounts(): ResponseEntity<List<Account>> {
        val accounts = accountService.getAccounts()

        return ResponseEntity.ok(accounts)
    }

    override fun deleteAccount(accountId: UUID): ResponseEntity<Unit> {
        accountService.deleteAccount(accountId)
        return ResponseEntity.noContent().build()
    }

    private fun buildResourceLocation(accountId: String) =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{accountId}")
            .buildAndExpand(accountId)
            .toUri()
}