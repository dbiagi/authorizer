package com.dbiagi.authorizer.service.authorization

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.Mcc
import com.dbiagi.authorizer.domain.ResultCode
import com.dbiagi.authorizer.domain.exception.InsufficientBalanceException
import com.dbiagi.authorizer.fixture.TransactionRequestFixture
import com.dbiagi.authorizer.service.MccMapper
import com.dbiagi.authorizer.service.processor.AuthorizationProcessor
import com.dbiagi.authorizer.service.processor.CashProcessor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class AuthorizerWithFallbackServiceTest {
    private val processor: AuthorizationProcessor = mock()
    private val cashProcessor: CashProcessor = mock()
    private val processors: List<AuthorizationProcessor> = listOf(processor)
    private val mccMapper: MccMapper = mock()
    private val authorizerWithFallbackService = AuthorizerWithFallbackService(processors, mccMapper, cashProcessor)

    @Test
    fun `given a transaction request should authorize`() {
        // Given
        val transactionRequest = TransactionRequestFixture.getTransaction()
        val mcc = Mcc("1234", CreditType.CASH)

        whenever(mccMapper.convert(transactionRequest.mcc, transactionRequest.merchant)).thenReturn(mcc)
        whenever(processor.match(mcc.type)).thenReturn(true)
        whenever(processor.authorize(transactionRequest)).thenReturn(ResultCode.APPROVED)

        // When
        val result = authorizerWithFallbackService.authorize(transactionRequest)

        // Then
        verify(processor).authorize(transactionRequest)
        assertEquals(ResultCode.APPROVED, result)
    }

    @Test
    fun `given an unknown mcc request should fallback to cash processor`() {
        // Given
        val transactionRequest = TransactionRequestFixture.getTransaction()
        val mcc = Mcc("1234", CreditType.UNKNOWN)

        whenever(mccMapper.convert(transactionRequest.mcc, transactionRequest.merchant)).thenReturn(mcc)
        whenever(processor.authorize(transactionRequest)).thenReturn(ResultCode.APPROVED)
        whenever(cashProcessor.authorize(transactionRequest)).thenReturn(ResultCode.APPROVED)

        // When
        val result = authorizerWithFallbackService.authorize(transactionRequest)

        // Then
        verify(cashProcessor).authorize(transactionRequest)
        assertEquals(ResultCode.APPROVED, result)
    }

    @Test
    fun `given an insufficient balance should fallback to cash processor`() {
        // Given
        val transactionRequest = TransactionRequestFixture.getTransaction()
        val mcc = Mcc("1234", CreditType.CASH)

        whenever(mccMapper.convert(transactionRequest.mcc, transactionRequest.merchant)).thenReturn(mcc)
        whenever(processor.match(mcc.type)).thenReturn(true)
        whenever(processor.authorize(transactionRequest)).thenThrow(InsufficientBalanceException::class.java)
        whenever(cashProcessor.authorize(transactionRequest)).thenReturn(ResultCode.APPROVED)

        // When
        val result = authorizerWithFallbackService.authorize(transactionRequest)

        // Then
        verify(processor).authorize(transactionRequest)
        verify(cashProcessor).authorize(transactionRequest)
        assertEquals(ResultCode.APPROVED, result)
    }
}
