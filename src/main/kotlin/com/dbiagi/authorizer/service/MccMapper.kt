package com.dbiagi.authorizer.service

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.Mcc
import com.dbiagi.authorizer.domain.config.MccCodesConfig
import org.springframework.stereotype.Component

@Component
class MccMapper(
    private val merchantMccMapper: MerchantMccMapper,
    private val mccCodesConfig: MccCodesConfig
) {
    fun convert(mccCode: String, merchantName: String): Mcc {
        if (mccCode in mccCodesConfig.food) {
            return Mcc(mccCode, CreditType.FOOD)
        }

        if (mccCode in mccCodesConfig.meal) {
            return Mcc(mccCode, CreditType.MEAL)
        }

        return merchantMccMapper.convert(mccCode, merchantName)
    }
}
