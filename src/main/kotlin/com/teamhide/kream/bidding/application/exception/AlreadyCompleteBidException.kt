package com.teamhide.kream.bidding.application.exception

import com.teamhide.kream.common.exception.CustomException
import org.springframework.http.HttpStatus

class AlreadyCompleteBidException : CustomException(
    statusCode = HttpStatus.BAD_REQUEST,
    errorCode = "BID__ALREADY_COMPLETE_BID",
    message = "",
)
