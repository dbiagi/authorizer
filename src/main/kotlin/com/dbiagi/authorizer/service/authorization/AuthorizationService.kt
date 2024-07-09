package com.dbiagi.authorizer.service.authorization

import com.dbiagi.authorizer.domain.ResultCode
import com.dbiagi.authorizer.domain.TransactionRequest

interface AuthorizationService {
    fun authorize(transactionRequest: TransactionRequest): ResultCode
}
