package com.teamhide.kream.bidding.application.exception

import com.teamhide.kream.common.exception.CustomException
import org.springframework.http.HttpStatus

class BiddingNotFoundException : CustomException(
    statusCode = HttpStatus.NOT_FOUND,
    errorCode = "BIDDING__NOT_FOUND",
    message = "",
)
