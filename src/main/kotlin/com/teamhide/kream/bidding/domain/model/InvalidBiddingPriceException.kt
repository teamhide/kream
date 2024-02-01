package com.teamhide.kream.bidding.domain.model

import com.teamhide.kream.common.exception.CustomException
import org.springframework.http.HttpStatus

class InvalidBiddingPriceException : CustomException(
    statusCode = HttpStatus.BAD_REQUEST,
    errorCode = "BIDDING__INVALID_BIDDING_PRICE",
    message = "",
)
