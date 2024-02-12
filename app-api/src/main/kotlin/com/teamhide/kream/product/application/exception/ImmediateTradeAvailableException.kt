package com.teamhide.kream.product.application.exception

import com.teamhide.kream.common.exception.CustomException
import org.springframework.http.HttpStatus

class ImmediateTradeAvailableException : CustomException(
    statusCode = HttpStatus.BAD_REQUEST,
    errorCode = "BIDDING__AVAILABLE_IMMEDIATE_TRADE",
    message = "",
)
