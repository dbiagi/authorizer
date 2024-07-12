package com.dbiagi.authorizer.service.processor

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.ResultCode
import com.dbiagi.authorizer.domain.exception.InsufficientBalanceException
import com.dbiagi.authorizer.fixture.TransactionRequestFixture
import com.dbiagi.authorizer.service.TransactionService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class CashProcessorTest {
    private val transactionService: TransactionService = mock()
    private val cashProcessor = CashProcessor(transactionService)

    @Test
    fun `given a tranction request should return approved`() {
        // given
        val request = TransactionRequestFixture.getTransaction()

        // when
        val result = cashProcessor.authorize(request)

        // then
        assertEquals(ResultCode.APPROVED, result)
    }

    @Test
    fun `given an insufficient balance should return rejected`() {
        // given
        val request = TransactionRequestFixture.getTransaction()
        whenever(transactionService.process(request, CreditType.CASH)).thenThrow(InsufficientBalanceException::class.java)

        // when
        val result = cashProcessor.authorize(request)

        // then
        assertEquals(ResultCode.REJECTED, result)
    }

    @Test
    fun `given an exception should return unprocessable`() {
        // given
        val request = TransactionRequestFixture.getTransaction()
        whenever(transactionService.process(request, CreditType.CASH)).thenThrow(RuntimeException::class.java)

        // when
        val result = cashProcessor.authorize(request)

        // then
        assertEquals(ResultCode.UNPROCESSABLE, result)
    }
}
