package com.teamhide.kream.product.application.exception

import com.teamhide.kream.common.exception.CustomException
import org.springframework.http.HttpStatus

class ProductBrandNotFoundException : CustomException(
    statusCode = HttpStatus.NOT_FOUND,
    errorCode = "PRODUCT__BRAND_NOT_FOUND",
    message = "",
)
