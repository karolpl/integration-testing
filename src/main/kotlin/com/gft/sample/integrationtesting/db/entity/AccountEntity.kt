package com.gft.sample.integrationtesting.db.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "account")
data class AccountEntity(
    @Column(name = "user_id", nullable = false, unique = true)
    val userId: UUID? = null,
    
    @Column(name = "name", nullable = false, unique = false)
    var name: String? = null,
    
    @Column(name = "balance", nullable = false, unique = false)
    var balance: BigDecimal? = null,
    
    @Column(name = "currency", nullable = false, unique = false)
    val currency: String? = null,

    @Column(name = "description", nullable = true, unique = false)
    val description: String? = null,
) {
    @Id
    @GeneratedValue
    var id: UUID? = null
    
    @CreationTimestamp
    val createdAt: Instant? = null
    
    @UpdateTimestamp
    val updatedAt: Instant? = null
}