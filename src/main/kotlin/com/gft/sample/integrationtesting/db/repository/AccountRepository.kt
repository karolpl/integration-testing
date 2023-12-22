package com.gft.sample.integrationtesting.db.repository

import com.gft.sample.integrationtesting.db.entity.AccountEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<AccountEntity, UUID> {
    fun existsByUserIdAndName(userId: UUID, name: String): Boolean
    fun findByUserIdAndName(userId: UUID, name: String): AccountEntity?
}
