package com.dbiagi.authorizer.service.authorization

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.Mcc
import com.dbiagi.authorizer.domain.ResultCode
import com.dbiagi.authorizer.domain.TransactionRequest
import com.dbiagi.authorizer.domain.exception.InsufficientBalanceException
import com.dbiagi.authorizer.service.MccMapper
import com.dbiagi.authorizer.service.TransactionService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever

class SimpleAuthorizerServiceTest {
    private val transactionService: TransactionService = mock()
    private val mccMapper: MccMapper = mock()
    private val simpleAuthorizerService = SimpleAuthorizerService(transactionService, mccMapper)

    @Test
    fun `given a transaction request should authorize`() {
        // Given
        val transactionRequest = TransactionRequest(
            mcc = "1234",
            merchant = "Merchant Name",
            amount = 100,
            account = "1"
        )
        val mcc = Mcc("1234", CreditType.CASH)

        whenever(mccMapper.convert(transactionRequest.mcc, transactionRequest.merchant)).thenReturn(mcc)

        // When
        val result = simpleAuthorizerService.authorize(transactionRequest)

        // Then
        verify(transactionService).process(transactionRequest, mcc.type)
        assertEquals(ResultCode.APPROVED, result)
    }

    @Test
    fun `given an unknown mcc request should return unprocessable code`() {
        // Given
        val transactionRequest = TransactionRequest(
            mcc = "OTHER",
            merchant = "Merchant Name",
            amount = 100,
            account = "1"
        )
        val mcc = Mcc("1234", CreditType.UNKNOWN)

        whenever(mccMapper.convert(transactionRequest.mcc, transactionRequest.merchant)).thenReturn(mcc)
        whenever(transactionService.process(transactionRequest, mcc.type)).thenThrow(InsufficientBalanceException::class.java)

        // When
        val result = simpleAuthorizerService.authorize(transactionRequest)

        // Then
        assertEquals(ResultCode.REJECTED, result)
    }
}
