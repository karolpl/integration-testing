package com.gft.sample.integrationtesting.mapper

import com.gft.sample.integrationtesting.common.toAmountFormat
import com.gft.sample.integrationtesting.db.entity.AccountEntity
import com.gft.sample.openapi.integrationtesting.v1.model.Account

fun AccountEntity.toAccount(): Account {
    return Account(
        id = this.id,
        userId = this.userId!!,
        name = this.name!!,
        balance = this.balance!!.toAmountFormat(),
        currency = this.currency!!,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun Account.toAccountEntity(description: String?): AccountEntity {
    return AccountEntity(
        userId = this.userId,
        name = this.name,
        balance = this.balance.toAmountFormat(),
        currency = this.currency,
        description = description
    )
}
