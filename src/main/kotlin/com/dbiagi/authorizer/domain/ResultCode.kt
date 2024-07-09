package com.dbiagi.authorizer.domain

enum class ResultCode(val code: String) {
    APPROVED("00"),
    REJECTED("51"),
    UNPROCESSABLE("07"),
}
