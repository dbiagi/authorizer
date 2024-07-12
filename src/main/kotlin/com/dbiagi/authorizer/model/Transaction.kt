package com.dbiagi.authorizer.model

import com.dbiagi.authorizer.domain.CreditType
import jakarta.persistence.Entity
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.*

@Entity(name = "transactions")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    val amount: Int,

    @Enumerated
    val creditType: CreditType,

    val merchant: String,

    val accountId: String,
)
