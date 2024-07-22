package com.dbiagi.authorizer.service.processor

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.fixture.TransactionRequestFixture
import com.dbiagi.authorizer.service.TransactionService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify

class FoodProcessorTest {
    private val transactionService: TransactionService = mock()
    private val foodProcessor = FoodProcessor(transactionService)

    @Test
    fun `given a tranction request should return approved`() {
        // given
        val request = TransactionRequestFixture.getTransaction()

        // when
        foodProcessor.authorize(request)

        // then
        verify(transactionService).process(request, CreditType.FOOD)
    }
}
