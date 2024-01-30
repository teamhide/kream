package com.teamhide.kream.product.domain.model

import com.teamhide.kream.common.exception.CustomException
import org.springframework.http.HttpStatus

class InvalidReleasePriceException : CustomException(
    statusCode = HttpStatus.BAD_REQUEST,
    errorCode = "PRODUCT__INVALID_RELEASE_PRICE",
    message = "",
)
