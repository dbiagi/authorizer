package com.dbiagi.authorizer.domain

data class TransactionRequest(
    val account: String,
    val amount: Int,
    val mcc: String,
    val merchant: String
)
