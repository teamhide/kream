package com.teamhide.kream.client

import feign.Logger
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@EnableFeignClients(basePackages = ["com.teamhide.kream.client"])
class FeignClientConfig {
    @Bean
    @Profile(value = ["prod"])
    fun prodFeignLogger(): Logger.Level {
        return Logger.Level.NONE
    }

    @Bean
    @Profile(value = ["!prod"])
    fun defaultFeignLogger(): Logger.Level {
        return Logger.Level.FULL
    }
}
