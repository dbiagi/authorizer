package com.dbiagi.authorizer.service

import com.dbiagi.authorizer.domain.Mcc
import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.config.MccCodesConfig
import org.springframework.stereotype.Component

@Component
class MccMapper(
    private val merchantMccMapper: MerchantMccMapper,
    private val mccCodesConfig: MccCodesConfig
) {
    fun convert(mccCode: String, merchantName: String): Mcc {
        val type: CreditType = when (mccCode) {
            mccCodesConfig.restaurant -> CreditType.RESTAURANT
            mccCodesConfig.supermarket -> CreditType.SUPERMARKET
            mccCodesConfig.mobility -> CreditType.MOBILITY
            else -> CreditType.UNKNOWN
        }

        if (type != CreditType.UNKNOWN) {
            return Mcc(mccCode, type)
        }

        return merchantMccMapper.convert(mccCode, merchantName)
    }
}
