package com.dbiagi.authorizer.service.authorization

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.ResultCode
import com.dbiagi.authorizer.domain.TransactionRequest
import com.dbiagi.authorizer.domain.exception.InsufficientBalanceException
import com.dbiagi.authorizer.service.MccMapper
import com.dbiagi.authorizer.service.TransactionService
import org.springframework.stereotype.Service

@Service
class AuthorizerWithFallbackService(
    private val transactionService: TransactionService,
    private val mccMapper: MccMapper,
) : AuthorizationService {
    override fun authorize(transactionRequest: TransactionRequest): ResultCode {
        val mcc = mccMapper.convert(transactionRequest.mcc, transactionRequest.merchant)

        return try {
            transactionService.process(transactionRequest, mcc.type)
            ResultCode.APPROVED
        } catch (e: InsufficientBalanceException) {
            fallback(transactionRequest)
        }
    }

    private fun fallback(transactionRequest: TransactionRequest): ResultCode {
        return try {
            transactionService.process(transactionRequest, CreditType.CASH)
            ResultCode.APPROVED
        } catch (e: InsufficientBalanceException) {
            ResultCode.REJECTED
        }
    }
}
