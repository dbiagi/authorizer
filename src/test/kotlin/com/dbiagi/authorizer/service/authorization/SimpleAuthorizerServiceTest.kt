package com.dbiagi.authorizer.service.authorization

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.Mcc
import com.dbiagi.authorizer.domain.ResultCode
import com.dbiagi.authorizer.domain.TransactionRequest
import com.dbiagi.authorizer.service.MccMapper
import com.dbiagi.authorizer.service.processor.AuthorizationProcessor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SimpleAuthorizerServiceTest {
    private val processor: AuthorizationProcessor = mock()
    private val processors: List<AuthorizationProcessor> = listOf(processor)
    private val mccMapper: MccMapper = mock()
    private val simpleAuthorizerService = SimpleAuthorizerService(processors, mccMapper)

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
        whenever(processor.match(mcc.type)).thenReturn(true)
        whenever(processor.authorize(transactionRequest)).thenReturn(ResultCode.APPROVED)

        // When
        val result = simpleAuthorizerService.authorize(transactionRequest)

        // Then
        verify(processor).authorize(transactionRequest)
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
        whenever(processor.authorize(transactionRequest)).thenReturn(ResultCode.APPROVED)

        // When
        val result = simpleAuthorizerService.authorize(transactionRequest)

        // Then
        verify(processor, never()).authorize(transactionRequest)
        assertEquals(ResultCode.UNPROCESSABLE, result)
    }
}
