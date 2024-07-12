package com.dbiagi.authorizer.service.processor

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.ResultCode
import com.dbiagi.authorizer.domain.TransactionRequest

interface AuthorizationProcessor {
    fun authorize(request: TransactionRequest)

    fun match(type: CreditType): Boolean
}
