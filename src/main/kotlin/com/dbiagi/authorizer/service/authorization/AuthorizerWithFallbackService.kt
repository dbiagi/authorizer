package com.dbiagi.authorizer.service.authorization

import com.dbiagi.authorizer.domain.ResultCode
import com.dbiagi.authorizer.domain.TransactionRequest
import com.dbiagi.authorizer.domain.exception.InsufficientBalanceException
import com.dbiagi.authorizer.service.MccMapper
import com.dbiagi.authorizer.service.processor.AuthorizationProcessor
import com.dbiagi.authorizer.service.processor.CashProcessor
import org.springframework.stereotype.Service

@Service
class AuthorizerWithFallbackService(
    private val processors: List<AuthorizationProcessor>,
    private val mccMapper: MccMapper,
    private val fallbackProcessor: CashProcessor
) : AuthorizationService {
    override fun authorize(transactionRequest: TransactionRequest): ResultCode {
        val mcc = mccMapper.convert(transactionRequest.mcc, transactionRequest.merchant)
        val processor = processors.find { it.match(mcc.type) }

        if (processor == null) {
            fallback(transactionRequest)
        }

        return try {
            processor?.authorize(transactionRequest) ?: ResultCode.REJECTED
            ResultCode.APPROVED
        } catch (e: InsufficientBalanceException) {
            fallback(transactionRequest)
        }
    }

    private fun fallback(transactionRequest: TransactionRequest): ResultCode {
        return try {
            fallbackProcessor.authorize(transactionRequest)
            ResultCode.APPROVED
        } catch (e: InsufficientBalanceException) {
            ResultCode.REJECTED
        }
    }
}
