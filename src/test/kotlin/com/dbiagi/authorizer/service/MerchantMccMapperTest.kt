package com.dbiagi.authorizer.service

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.config.MerchantNameListsConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class MerchantMccMapperTest {
    private val merchantNameListsConfig: MerchantNameListsConfig = mock()
    private val merchantMccMapper = MerchantMccMapper(merchantNameListsConfig)

    @Test
    fun `given a merchant name should map to RESTAURANT mcc type`() {
        // Given
        val mccCode = "1234"
        val merchantName = "Pizza"
        whenever(merchantNameListsConfig.restaurant).thenReturn(listOf("McDonald's", "Burger King", "KFC", "Pizza Hut", "Starbucks"))

        // When
        val mcc = merchantMccMapper.convert(mccCode, merchantName)

        // Then
        assertEquals(CreditType.FOOD, mcc.type)
    }

    @Test
    fun `given a merchant name should map to SUPERMARKET mcc type`() {
        // Given
        val mccCode = "1234"
        val merchantName = "Aldi"
        whenever(merchantNameListsConfig.supermarket).thenReturn(listOf("Walmart", "Carrefour", "Auchan", "Lidl", "Aldi"))

        // When
        val mcc = merchantMccMapper.convert(mccCode, merchantName)

        // Then
        assertEquals(CreditType.MEAL, mcc.type)
    }

    @Test
    fun `given a merchant name should map to MOBILITY mcc type`() {
        // Given
        val mccCode = "1234"
        val merchantName = "Uber"
        whenever(merchantNameListsConfig.mobility).thenReturn(listOf("Uber", "Lyft", "Didi", "Ola", "Grab"))

        // When
        val mcc = merchantMccMapper.convert(mccCode, merchantName)

        // Then
        assertEquals(CreditType.MOBILITY, mcc.type)
    }

    @Test
    fun `given a merchant name should map to UNKNOWN mcc type`() {
        // Given
        val mccCode = "1234"
        val merchantName = "Other"
        whenever(merchantNameListsConfig.mobility).thenReturn(listOf("Uber", "Lyft", "Didi", "Ola", "Grab"))

        // When
        val mcc = merchantMccMapper.convert(mccCode, merchantName)

        // Then
        assertEquals(CreditType.UNKNOWN, mcc.type)
    }
}
