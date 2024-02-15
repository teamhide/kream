package com.teamhide.kream.common.config

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.types.beInstanceOf
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler

internal class AsyncPoolConfigTest : StringSpec({
    val asyncPoolConfig = AsyncPoolConfig()

    "Async 전용 예외 핸들러를 얻어온다" {
        // Given, When
        val sut = asyncPoolConfig.asyncUncaughtExceptionHandler

        // Then
        sut should beInstanceOf<AsyncUncaughtExceptionHandler>()
    }
})
