package com.gft.sample.integrationtesting.consumer.model

import java.math.BigDecimal
import java.util.UUID

data class AccountUpdatedEvent(
    val userId: UUID,
    val name: String,
    val balance: BigDecimal,
    val isDeleted: Boolean? = null
)
