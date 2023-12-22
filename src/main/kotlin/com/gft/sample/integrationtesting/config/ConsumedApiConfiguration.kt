package com.gft.sample.integrationtesting.config

import com.gft.sample.integrationtesting.filter.AccountDetailsApiFilter
import generated.api.AccountDetailsApi
import generated.invoker.ApiClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConsumedApiConfiguration(
    @Value("\${servers.url.account-details-service}") private val accountDetailsServiceBaseUrl: String,
) {
    @Bean
    fun accountDetailsApi() =
        AccountDetailsApi(
            ApiClient(
                ApiClient.buildWebClientBuilder()
                    .filter(AccountDetailsApiFilter.exchangeFilterFunction("AccountDetailsApi")).build()
            ).apply { basePath = accountDetailsServiceBaseUrl }
        )
}