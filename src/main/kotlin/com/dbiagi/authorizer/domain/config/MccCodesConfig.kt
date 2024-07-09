package com.dbiagi.authorizer.domain.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app.mcc.codes")
data class MccCodesConfig(
    var restaurant: String = "",
    var supermarket: String = "",
    var mobility: String = "",
)
