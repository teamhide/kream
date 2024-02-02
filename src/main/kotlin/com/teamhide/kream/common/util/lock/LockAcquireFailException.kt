package com.teamhide.kream.common.util.lock

import com.teamhide.kream.common.exception.CustomException
import org.springframework.http.HttpStatus

class LockAcquireFailException : CustomException(
    statusCode = HttpStatus.REQUEST_TIMEOUT,
    errorCode = "LOCK__ACQUIRE_FAIL",
    message = "",
)
