package com.teamhide.kream.coupon.application.exception

import com.teamhide.kream.common.exception.CustomException
import org.springframework.http.HttpStatus

class UnavailableCouponGroupException : CustomException(
    statusCode = HttpStatus.BAD_REQUEST,
    errorCode = "COUPON__UNAVAILABLE_COUPON_GROUP",
    message = "",
)
