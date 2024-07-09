package com.dbiagi.authorizer.service.processor

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.ResultCode
import com.dbiagi.authorizer.domain.TransactionRequest
import com.dbiagi.authorizer.domain.TransactionResponse

interface AuthorizationProcessor {
    fun authorize(request: TransactionRequest): ResultCode

    fun match(type: CreditType): Boolean

    fun fallback(): CreditType?
}
