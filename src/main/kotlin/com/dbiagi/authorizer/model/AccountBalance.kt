package com.dbiagi.authorizer.model

import com.dbiagi.authorizer.domain.CreditType
import jakarta.persistence.Entity
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
data class AccountBalance (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Int,

    val accountId: String,

    val totalAmount: Int,

    @Enumerated
    val type: CreditType,

    val updatedAt: LocalDateTime
)
