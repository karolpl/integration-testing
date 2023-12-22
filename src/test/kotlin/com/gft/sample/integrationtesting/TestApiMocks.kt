package com.gft.sample.integrationtesting

import com.gft.sample.integrationtesting.common.starters.SST
import com.gft.sample.integrationtesting.common.toRequest
import com.gft.sample.integrationtesting.common.respond
import com.gft.sample.integrationtesting.common.starters.MockServerStarter
import generated.model.AccountDetails
import org.mockserver.model.MediaType

private const val ACCOUNT_DETAILS_API_URL = "/account-details-service/account-details/{accountName}"

fun <T> T.mockAccountDetailsApi(
    accountName: String,
    responseBody: AccountDetails
) where T : SST, T : MockServerStarter {
    toRequest {
        withMethod("GET")
        withPath(ACCOUNT_DETAILS_API_URL)
        withPathParameter("accountName", accountName)
    } respond {
        withStatusCode(200)
        withBody(responseBody.toJsonString())
        withContentType(MediaType.APPLICATION_JSON)
    }
}
