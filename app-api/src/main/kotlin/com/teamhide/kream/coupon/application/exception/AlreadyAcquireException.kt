package com.teamhide.kream.coupon.application.exception

import com.teamhide.kream.common.exception.CustomException
import org.springframework.http.HttpStatus

class AlreadyAcquireException : CustomException(
    statusCode = HttpStatus.BAD_REQUEST,
    errorCode = "COUPON__ALREADY_ACQUIRE",
    message = "",
)
