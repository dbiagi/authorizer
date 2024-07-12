package com.dbiagi.authorizer.fixture

import com.dbiagi.authorizer.domain.TransactionRequest

object TransactionRequestFixture {
    fun getTransaction() = TransactionRequest(
        mcc = "1234",
        merchant = "Merchant Name",
        amount = 100,
        account = "1"
    )
}
