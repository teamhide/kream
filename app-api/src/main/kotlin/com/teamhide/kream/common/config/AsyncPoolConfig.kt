package com.teamhide.kream.common.config

import com.teamhide.kream.common.exception.AsyncExceptionHandler
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@EnableAsync
@Configuration
class AsyncPoolConfig : AsyncConfigurer {
    override fun getAsyncExecutor(): Executor = ThreadPoolTaskExecutor().apply {
        setThreadNamePrefix("kream-async")
        setWaitForTasksToCompleteOnShutdown(true)
        setAwaitTerminationSeconds(10)
        maxPoolSize = 10
        corePoolSize = 10
        queueCapacity = 100
        initialize()
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler = AsyncExceptionHandler()
}
