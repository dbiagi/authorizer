package com.dbiagi.authorizer.service

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.exception.InsufficientBalanceException
import com.dbiagi.authorizer.model.AccountBalance
import com.dbiagi.authorizer.repository.AccountBalanceRepository
import org.springframework.stereotype.Service

@Service
class AccountBalanceService(
    private val balanceRepository: AccountBalanceRepository
) {
    fun debit(accountId: String, type: CreditType, amount: Int) {
        val balance = getBalance(accountId, type)
        if (amount > balance.totalAmount) {
            throw InsufficientBalanceException("Insufficient balance for account $accountId and type $type")
        }
        balanceRepository.save(balance.copy(totalAmount = balance.totalAmount - amount))
    }

    private fun getBalance(accountId: String, type: CreditType): AccountBalance =
        balanceRepository.findOneByAccountIdAndType(accountId, type)
            ?: throw Exception("Balance not found for account $accountId and type $type")
}
