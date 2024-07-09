package com.dbiagi.authorizer.domain.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app.merchant.lists")
data class MerchantNameListsConfig(
    var restaurant: List<String> = emptyList(),
    var supermarket: List<String> = emptyList(),
    var mobility: List<String> = emptyList(),
)
