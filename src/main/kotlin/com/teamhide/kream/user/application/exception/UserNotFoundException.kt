package com.teamhide.kream.user.application.exception

import com.teamhide.kream.common.exception.CustomException
import org.springframework.http.HttpStatus

class UserNotFoundException : CustomException(
    statusCode = HttpStatus.NOT_FOUND, errorCode = "USER__NOT_FOUND", message = "user not found",
)
