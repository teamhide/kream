package com.teamhide.kream.client.pg

import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean

class PgClientConfig {
    @Bean
    fun errorDecoder(): ErrorDecoder {
        return PgClientErrorDecoder()
    }

    @Bean
    fun retryer(): feign.Retryer {
        return PgClientRetryer()
    }
}
