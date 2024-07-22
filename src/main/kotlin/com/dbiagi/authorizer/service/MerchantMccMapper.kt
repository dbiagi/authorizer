package com.dbiagi.authorizer.service

import com.dbiagi.authorizer.domain.CreditType
import com.dbiagi.authorizer.domain.Mcc
import com.dbiagi.authorizer.domain.config.MerchantNameListsConfig
import org.springframework.stereotype.Component

@Component
class MerchantMccMapper(
    private val merchantNameListsConfig: MerchantNameListsConfig
) {
    fun convert(mccCode: String, merchantName: String): Mcc {
        if (contains(merchantNameListsConfig.restaurant, merchantName)) {
            return Mcc(mccCode, CreditType.FOOD)
        }

        if (contains(merchantNameListsConfig.supermarket, merchantName)) {
            return Mcc(mccCode, CreditType.MEAL)
        }

        if (contains(merchantNameListsConfig.mobility, merchantName)) {
            return Mcc(mccCode, CreditType.MOBILITY)
        }

        return Mcc(mccCode, CreditType.UNKNOWN)
    }

    private fun contains(list: List<String>, merchantName: String): Boolean {
        return list.any { it.contains(merchantName, ignoreCase = true) }
    }
}
