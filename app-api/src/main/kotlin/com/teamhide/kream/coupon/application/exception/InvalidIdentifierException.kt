package com.teamhide.kream.coupon.application.exception

import com.teamhide.kream.common.exception.CustomException
import org.springframework.http.HttpStatus

class InvalidIdentifierException : CustomException(
    statusCode = HttpStatus.BAD_REQUEST,
    errorCode = "COUPON__INVALID_IDENTIFIER",
    message = "",
)
