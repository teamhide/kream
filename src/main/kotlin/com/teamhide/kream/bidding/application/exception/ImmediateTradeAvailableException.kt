package com.teamhide.kream.bidding.application.exception

import com.teamhide.kream.common.exception.CustomException
import org.springframework.http.HttpStatus

class ImmediateTradeAvailableException : CustomException(
    statusCode = HttpStatus.BAD_REQUEST,
    errorCode = "BIDDING__AVAILABLE_IMMEDIATE_TRADE",
    message = "",
)
