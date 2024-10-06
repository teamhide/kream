package com.teamhide.kream.coupon.domain.model

import com.teamhide.kream.common.exception.CustomException
import org.springframework.http.HttpStatus

class InvalidConditionTypeException : CustomException(
    statusCode = HttpStatus.BAD_REQUEST,
    errorCode = "COUPON__INVALID_CONDITION_TYPE",
    message = "",
)
