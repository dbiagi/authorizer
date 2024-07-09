package com.dbiagi.authorizer.service

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.TransactionRequest
import com.dbiagi.authorizer.model.Transaction
import com.dbiagi.authorizer.repository.TransactionRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val accountBalanceService: AccountBalanceService
) {
    @Transactional
    fun process(request: TransactionRequest, type: CreditType) {
        val transaction = toTransaction(request, type)
        accountBalanceService.debit(request.account, type, request.amount)
        transactionRepository.save(transaction)
    }

    private fun toTransaction(request: TransactionRequest, creditType: CreditType): Transaction = Transaction(
        accountId = request.account,
        amount = request.amount,
        creditType = creditType,
        merchant = request.merchant
    )
}
