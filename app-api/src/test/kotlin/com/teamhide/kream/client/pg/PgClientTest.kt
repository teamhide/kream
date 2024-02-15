package com.teamhide.kream.client.pg

import com.fasterxml.jackson.databind.ObjectMapper
import com.teamhide.kream.client.WebClientException
import com.teamhide.kream.client.makeAttemptPaymentRequest
import com.teamhide.kream.client.makeAttemptPaymentResponse
import com.teamhide.kream.client.makeCancelPaymentRequest
import com.teamhide.kream.support.test.FeignTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

@FeignTest
internal class PgClientTest(
    private val pgClient: PgClient,
) : StringSpec({
    var mockWebServer = MockWebServer()
    val objectMapper = ObjectMapper()

    beforeEach {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
    }

    afterEach {
        mockWebServer.close()
    }

    "결제를 시도한다" {
        // Given
        val request = makeAttemptPaymentRequest(biddingId = 1L, price = 20000, userId = 1L)
        val response = makeAttemptPaymentResponse(paymentId = "paymentId")
        mockWebServer.enqueue(
            MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(HttpStatus.OK.value())
                .setBody(objectMapper.writeValueAsString(response))
        )

        // When
        val sut = pgClient.attemptPayment(request = request)

        // Then
        sut.paymentId shouldBe response.paymentId
    }

    "결제를 취소한다" {
        // Given
        val request = makeCancelPaymentRequest(paymentId = "paymentId")
        mockWebServer.enqueue(
            MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(HttpStatus.OK.value())
                .setBody("")
        )

        // When, Then
        pgClient.cancelPayment(request = request)
    }

    "200응답이 아니고 Response body가 없는 경우 빈 값을 담아 WebClientException을 반환한다" {
        // Given
        val request = makeCancelPaymentRequest(paymentId = "paymentId")
        mockWebServer.enqueue(
            MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(HttpStatus.BAD_REQUEST.value())
                .setBody("")
        )

        // When, Then
        shouldThrow<WebClientException> { pgClient.cancelPayment(request = request) }
    }

    "200응답이 아닌 경우 Response body를 WebClientException에 담아 반환한다" {
        // Given
        val request = makeCancelPaymentRequest(paymentId = "paymentId")
        mockWebServer.enqueue(
            MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(HttpStatus.BAD_REQUEST.value())
                .setBody("body")
        )

        // When, Then
        shouldThrow<WebClientException> { pgClient.cancelPayment(request = request) }
    }
})
