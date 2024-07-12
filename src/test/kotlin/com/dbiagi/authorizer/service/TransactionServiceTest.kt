package com.dbiagi.authorizer.service

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.exception.InsufficientBalanceException
import com.dbiagi.authorizer.fixture.TransactionRequestFixture
import com.dbiagi.authorizer.repository.TransactionRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class TransactionServiceTest {
    private val transactionRepository: TransactionRepository = mock()
    private val balanceService: AccountBalanceService = mock()
    private val transactionService = TransactionService(transactionRepository, balanceService)

    @Test
    fun `given a transaction request should debit and save the transaction`() {
        // given
        val request = TransactionRequestFixture.getTransaction()
        val creditType = CreditType.MOBILITY

        // when
        val result = transactionService.process(request, creditType)

        // then
        verify(balanceService).debit(request.account, creditType, request.amount)
    }

    @Test
    fun `given an insufficient balance should not save transaction`() {
        // given
        val request = TransactionRequestFixture.getTransaction()
        val creditType = CreditType.MOBILITY
        whenever(balanceService.debit(request.account, creditType, request.amount)).thenThrow(InsufficientBalanceException::class.java)

        // when
        assertThrows<InsufficientBalanceException> {
            transactionService.process(request, creditType)
        }

        // then
        verify(balanceService, times(1)).debit(request.account, creditType, request.amount)
        verify(transactionRepository, never()).save(any())
    }
}
