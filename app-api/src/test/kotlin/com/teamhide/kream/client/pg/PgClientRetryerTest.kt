package com.teamhide.kream.client.pg

import com.teamhide.kream.client.WebClientException
import feign.Request
import feign.RetryableException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.mockk.mockk

internal class PgClientRetryerTest : StringSpec({
    "RetryableException이 발생하면 WebClientException으로 감싸진다" {
        // Given
        val retryer = PgClientRetryer()
        val mockRequest = mockk<Request>()
        val exc = RetryableException(500, "message", Request.HttpMethod.GET, 1L, mockRequest)

        // When
        shouldThrow<WebClientException> { retryer.continueOrPropagate(e = exc) }

        // Then
    }
})
