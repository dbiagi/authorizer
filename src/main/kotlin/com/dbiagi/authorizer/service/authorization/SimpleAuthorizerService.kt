package com.dbiagi.authorizer.service.authorization

import com.dbiagi.authorizer.domain.ResultCode
import com.dbiagi.authorizer.domain.TransactionRequest
import com.dbiagi.authorizer.service.MccMapper
import com.dbiagi.authorizer.service.processor.AuthorizationProcessor
import org.springframework.stereotype.Service

@Service
class SimpleAuthorizerService(
    private val processors: List<AuthorizationProcessor>,
    private val mccMapper: MccMapper
) : AuthorizationService {
    override fun authorize(transactionRequest: TransactionRequest): ResultCode {
        val mcc = mccMapper.convert(transactionRequest.mcc, transactionRequest.merchant)
        val processor = processors.find { it.match(mcc.type) }

        if (processor == null) {
            return ResultCode.UNPROCESSABLE
        }

        return processor.authorize(transactionRequest)
    }
}
