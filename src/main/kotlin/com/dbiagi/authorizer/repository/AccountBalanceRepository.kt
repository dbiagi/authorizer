package com.dbiagi.authorizer.repository

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.model.AccountBalance
import org.springframework.data.repository.CrudRepository

interface AccountBalanceRepository : CrudRepository<AccountBalance, Int> {
    fun findOneByAccountIdAndType(accountId: String, type: CreditType): AccountBalance?
}
