package com.gft.sample.integrationtesting.service.client

import generated.api.AccountDetailsApi
import generated.model.AccountDetails
import org.springframework.stereotype.Service

@Service
class AccountDetailsService(
    private val accountDetailsApi: AccountDetailsApi
) {
    fun getAccountDetails(accountName: String): AccountDetails? {
        return accountDetailsApi.getAccountDetails(accountName).block()
    }
}
