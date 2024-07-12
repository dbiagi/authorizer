package com.dbiagi.authorizer.service

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.exception.InsufficientBalanceException
import com.dbiagi.authorizer.model.AccountBalance
import com.dbiagi.authorizer.repository.AccountBalanceRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import kotlin.test.assertContains

class AccountBalanceServiceTest {
    private val accountBalanceRepository: AccountBalanceRepository = mock()
    private val accountBalanceService = AccountBalanceService(accountBalanceRepository)

    @Test
    fun `given a debit request should debit the account balance`() {
        // Given
        val accountId = "1234"
        val type = CreditType.CASH
        val amount = 1000
        val accountBalance = AccountBalance(1, "1234", 1000, CreditType.CASH, LocalDateTime.now())
        whenever(accountBalanceRepository.findOneByAccountIdAndType(accountId, type)).thenReturn(accountBalance)

        // When
        accountBalanceService.debit(accountId, type, amount)

        // Then
        verify(accountBalanceRepository).save(any())
    }

    @Test
    fun `given a debit request higher than balance should throw insufficient balance exception`() {
        // Given
        val accountId = "1234"
        val type = CreditType.CASH
        val amount = 1000
        val accountBalance = AccountBalance(1, "1234", 500, CreditType.CASH, LocalDateTime.now())
        whenever(accountBalanceRepository.findOneByAccountIdAndType(accountId, type)).thenReturn(accountBalance)

        // When
        val exception = assertThrows<InsufficientBalanceException> {
            accountBalanceService.debit(accountId, type, amount)
        }

        // Then
        assertContains(exception.message ?: "", "Insufficient balance", true)
    }
}
