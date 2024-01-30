package com.teamhide.kream.location.application.exception

import com.teamhide.kream.common.exception.CustomException
import org.springframework.http.HttpStatus

class GhostModeUserException : CustomException(
    statusCode = HttpStatus.BAD_REQUEST,
    errorCode = "LOCATION__USER_IS_GHOST_MODE",
    message = "user is ghost mode",
)
