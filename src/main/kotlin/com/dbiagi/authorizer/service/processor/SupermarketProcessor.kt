package com.dbiagi.authorizer.service.processor

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.TransactionRequest
import com.dbiagi.authorizer.service.TransactionService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SupermarketProcessor(
    private val transactionService: TransactionService,
) : AuthorizationProcessor {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun authorize(request: TransactionRequest) {
        logger.info("Processing supermarket transaction for account ${request.account}")
        transactionService.process(request, CreditType.SUPERMARKET)
    }

    override fun match(type: CreditType): Boolean = type == CreditType.SUPERMARKET
}
