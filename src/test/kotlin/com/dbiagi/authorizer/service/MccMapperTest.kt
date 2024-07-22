package com.dbiagi.authorizer.service

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.Mcc
import com.dbiagi.authorizer.domain.config.MccCodesConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class MccMapperTest {
    private val merchantMccMapper: MerchantMccMapper = mock()
    private val mccCodesConfig: MccCodesConfig = mock()
    private val mccMapper = MccMapper(merchantMccMapper, mccCodesConfig)

    @Test
    fun `given a restaurant mcc code should map to the correct mcc type`() {
        // Given
        val mccCodes = listOf("1234")

        whenever(mccCodesConfig.food).thenReturn(mccCodes)

        // when
        val result = mccMapper.convert(mccCodes.first(), "Merchant Name")

        // Then
        assertEquals(CreditType.FOOD, result.type)
    }

    @Test
    fun `given a supermarket mcc code should map to the correct mcc type`() {
        // Given
        val mccCodes = listOf("1234")

        whenever(mccCodesConfig.meal).thenReturn(mccCodes)

        // when
        val result = mccMapper.convert(mccCodes.first(), "Merchant Name")

        // Then
        assertEquals(CreditType.MEAL, result.type)
    }

    @Test
    fun `given an unknown mcc code should fallback to merchantName converter`() {
        // Given
        val mccCodes = listOf("1234")
        val expectedResult = CreditType.MEAL
        whenever(mccCodesConfig.meal).thenReturn(listOf("other"))
        whenever(merchantMccMapper.convert(mccCodes.first(), "Merchant Name")).thenReturn(Mcc(mccCodes.first(), expectedResult))

        // when
        val result = mccMapper.convert(mccCodes.first(), "Merchant Name")

        // Then
        assertEquals(expectedResult, result.type)
    }
}
