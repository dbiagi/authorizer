package com.dbiagi.authorizer.service

import com.dbiagi.authorizer.domain.Mcc
import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.config.MccCodesConfig
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class MccMapperTest {
    private val merchantMccMapper: MerchantMccMapper = mock()
    private val mccCodesConfig: MccCodesConfig = mock()
    private val mccMapper = MccMapper(merchantMccMapper, mccCodesConfig)

    @Test
    fun `given a restaurant mcc code should map to the correct mcc type`() {
        // Given
        val mccCode = "1234"
        whenever(mccCodesConfig.restaurant).thenReturn(mccCode)

        // when
        val result = mccMapper.convert(mccCode, "Merchant Name")

        // Then
        assertEquals(CreditType.RESTAURANT, result.type)
    }

    @Test
    fun `given a mobility mcc code should map to the correct mcc type`() {
        // Given
        val mccCode = "1234"
        whenever(mccCodesConfig.mobility).thenReturn(mccCode)

        // when
        val result = mccMapper.convert(mccCode, "Merchant Name")

        // Then
        assertEquals(CreditType.MOBILITY, result.type)
    }

    @Test
    fun `given a supermarket mcc code should map to the correct mcc type`() {
        // Given
        val mccCode = "1234"
        whenever(mccCodesConfig.supermarket).thenReturn(mccCode)

        // when
        val result = mccMapper.convert(mccCode, "Merchant Name")

        // Then
        assertEquals(CreditType.SUPERMARKET, result.type)
    }

    @Test
    fun `given an unknown mcc code should fallback to merchantName converter`() {
        // Given
        val mccCode = "1234"
        val expectedResult = CreditType.SUPERMARKET
        whenever(mccCodesConfig.supermarket).thenReturn("other")
        whenever(merchantMccMapper.convert(mccCode, "Merchant Name")).thenReturn(Mcc(mccCode, expectedResult))

        // when
        val result = mccMapper.convert(mccCode, "Merchant Name")

        // Then
        assertEquals(expectedResult, result.type)
    }
}
