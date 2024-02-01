package com.teamhide.kream.client.pg

import com.teamhide.kream.client.WebClientException
import feign.RetryableException
import feign.Retryer
import org.springframework.http.HttpStatus

class PgClientRetryer : feign.Retryer {
    override fun clone(): Retryer {
        return PgClientRetryer()
    }

    override fun continueOrPropagate(e: RetryableException) {
        throw WebClientException(statusCode = HttpStatus.BAD_REQUEST, message = e.message ?: "")
    }
}
