package com.dbiagi.authorizer.domain.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app.mcc.codes")
data class MccCodesConfig(
    var food: List<String> = emptyList(),
    var meal: List<String> = emptyList(),
)
