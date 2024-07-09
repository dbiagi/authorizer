package com.dbiagi.authorizer.model

import com.dbiagi.authorizer.domain.CreditType
import jakarta.persistence.Entity
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import java.util.*

@Entity
data class Transaction(
    @Id
    val id: UUID? = null,

    val amount: Int,

    @Enumerated
    val creditType: CreditType,

    val merchant: String,

    val accountId: String,
)
