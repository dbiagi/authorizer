package com.dbiagi.authorizer.service.authorization

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.Mcc
import com.dbiagi.authorizer.domain.ResultCode
import com.dbiagi.authorizer.domain.exception.InsufficientBalanceException
import com.dbiagi.authorizer.fixture.TransactionRequestFixture
import com.dbiagi.authorizer.service.MccMapper
import com.dbiagi.authorizer.service.TransactionService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class AuthorizerWithFallbackServiceTest {
    private val transactionService: TransactionService = mock()
    private val mccMapper: MccMapper = mock()
    private val authorizerWithFallbackService = AuthorizerWithFallbackService(transactionService, mccMapper)

    @Test
    fun `given a transaction request should authorize`() {
        // Given
        val transactionRequest = TransactionRequestFixture.getTransaction()
        val mcc = Mcc("1234", CreditType.CASH)

        whenever(mccMapper.convert(transactionRequest.mcc, transactionRequest.merchant)).thenReturn(mcc)

        // When
        val result = authorizerWithFallbackService.authorize(transactionRequest)

        // Then
        verify(transactionService).process(transactionRequest, mcc.type)
        assertEquals(ResultCode.APPROVED, result)
    }

    @Test
    fun `given an unknown mcc request should fallback to cash processor`() {
        // Given
        val transactionRequest = TransactionRequestFixture.getTransaction()
        val mcc = Mcc("1234", CreditType.UNKNOWN)

        whenever(mccMapper.convert(transactionRequest.mcc, transactionRequest.merchant)).thenReturn(mcc)

        // When
        val result = authorizerWithFallbackService.authorize(transactionRequest)

        // Then
        verify(transactionService).process(transactionRequest, mcc.type)
        assertEquals(ResultCode.APPROVED, result)
    }

    @Test
    fun `given an insufficient balance should fallback to cash processor`() {
        // Given
        val transactionRequest = TransactionRequestFixture.getTransaction()
        val mcc = Mcc("1234", CreditType.CASH)

        whenever(mccMapper.convert(transactionRequest.mcc, transactionRequest.merchant)).thenReturn(mcc)
        whenever(transactionService.process(transactionRequest, mcc.type))
            .thenThrow(InsufficientBalanceException::class.java)
        doNothing().whenever(transactionService).process(transactionRequest, CreditType.CASH)

        // When
        val result = authorizerWithFallbackService.authorize(transactionRequest)

        // Then
        verify(transactionService).process(transactionRequest, mcc.type)
        verify(transactionService).process(transactionRequest, CreditType.CASH)
        assertEquals(ResultCode.APPROVED, result)
    }
}
