package com.teamhide.kream.common.resilience4j

import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.ratelimiter.RateLimiterConfig
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class RateLimiterConfig {
    companion object {
        const val PG_ATTEMPT_PAYMENT = "PG_ATTEMPT_PAYMENT"
    }

    @Bean
    fun rateLimiterRegistry() = RateLimiterRegistry.ofDefaults()

    @Bean
    fun pgRateLimiter(rateLimiterRegistry: RateLimiterRegistry): RateLimiter {
        val config = RateLimiterConfig.custom()
            .limitForPeriod(100)
            .limitRefreshPeriod(Duration.ofSeconds(1))
            .timeoutDuration(Duration.ofSeconds(2))
            .build()
        return rateLimiterRegistry.rateLimiter(PG_ATTEMPT_PAYMENT, config)
    }
}
