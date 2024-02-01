package com.teamhide.kream.client

import com.teamhide.kream.common.exception.CustomException
import org.springframework.http.HttpStatus

class WebClientException(
    statusCode: HttpStatus,
    errorCode: String = "WEB_CLIENT_EXCEPTION",
    message: String
) : CustomException(statusCode = statusCode, errorCode = errorCode, message = message)
