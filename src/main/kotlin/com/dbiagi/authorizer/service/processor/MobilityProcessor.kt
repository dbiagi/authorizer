package com.dbiagi.authorizer.service.processor

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.ResultCode
import com.dbiagi.authorizer.domain.TransactionRequest
import com.dbiagi.authorizer.domain.TransactionResponse
import com.dbiagi.authorizer.domain.exception.InsufficientBalanceException
import com.dbiagi.authorizer.service.TransactionService
import org.slf4j.LoggerFactory

class MobilityProcessor(
    private val transactionService: TransactionService
) : AuthorizationProcessor {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun authorize(request: TransactionRequest): ResultCode {
        logger.info("Processing mobility transaction for account ${request.account}")

        try {
            transactionService.process(request, CreditType.MOBILITY)
            logger.warn("Transaction approved for account ${request.account}")
            return ResultCode.APPROVED
        } catch (e: InsufficientBalanceException) {
            logger.warn("Transaction rejected for account ${request.account}")
            return ResultCode.REJECTED
        } catch (e: Exception) {
            logger.warn("Transaction unprocessable for account ${request.account}")
            return ResultCode.UNPROCESSABLE
        }
    }

    override fun match(type: CreditType): Boolean = type == CreditType.MOBILITY

    override fun fallback(): CreditType? = null
}
