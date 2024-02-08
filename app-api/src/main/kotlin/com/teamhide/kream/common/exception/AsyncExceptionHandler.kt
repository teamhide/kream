package com.teamhide.kream.common.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import java.lang.reflect.Method

private val logger = KotlinLogging.logger { }

class AsyncExceptionHandler : AsyncUncaughtExceptionHandler {
    override fun handleUncaughtException(ex: Throwable, method: Method, vararg params: Any?) {
        if (ex !is CustomException) {
            throw ex
        }
        logger.warn { "AsyncExceptionHandler | method=$method, exc=$ex" }
    }
}
