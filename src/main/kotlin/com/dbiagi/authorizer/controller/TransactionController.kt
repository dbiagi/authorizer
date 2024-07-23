package com.dbiagi.authorizer.controller

import com.dbiagi.authorizer.domain.TransactionRequest
import com.dbiagi.authorizer.domain.TransactionResponse
import com.dbiagi.authorizer.service.authorization.AuthorizerWithFallbackService
import com.dbiagi.authorizer.service.authorization.SimpleAuthorizerService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/transactions")
class TransactionController(
    private val simpleAuthorizerService: SimpleAuthorizerService,
    private val withFallbackAuthorizerService: AuthorizerWithFallbackService
) {
    val logger: Logger = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun simple(@RequestBody request: TransactionRequest): TransactionResponse {
        logger.info("Processing transaction request: $request")
        val resultCode = simpleAuthorizerService.authorize(request)

        return TransactionResponse(resultCode.code)
    }

    @PostMapping(params = ["fallback"])
    private fun withFallback(@RequestBody request: TransactionRequest): TransactionResponse {
        logger.info("Processing transaction request: $request with fallback")
        val resultCode = withFallbackAuthorizerService.authorize(request)

        return TransactionResponse(resultCode.code)
    }
}
