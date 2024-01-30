package com.teamhide.kream.user.domain.model

import com.teamhide.kream.common.exception.CustomException
import org.springframework.http.HttpStatus

class PasswordDoesNotMatchException : CustomException(
    statusCode = HttpStatus.BAD_REQUEST,
    errorCode = "USER__PASSWORD_DOES_NOT_MATCH",
    message = "password1 and password2 does not match"
)
