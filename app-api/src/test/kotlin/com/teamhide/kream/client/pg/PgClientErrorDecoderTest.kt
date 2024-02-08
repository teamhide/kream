package com.teamhide.kream.client.pg

import com.teamhide.kream.client.WebClientException
import feign.Response
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.io.IOException

class PgClientErrorDecoderTest : StringSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val response = mockk<Response>()
    val body = mockk<Response.Body>()
    val errorDecoder = PgClientErrorDecoder()

    "body가 null인 경우 빈 문자열을 리턴한다" {
        // Given
        every { response.status() } returns 400
        every { response.body() } returns null

        // When, Then
        val exc = shouldThrow<WebClientException> { errorDecoder.decode(methodKey = "key", response = response) }
        exc.message shouldBe ""
    }

    "body의 길이가 0인 경우 빈 문자열을 리턴한다" {
        // Given
        every { response.status() } returns 400
        every { body.length() } returns 0
        every { response.body() } returns body

        // When, Then
        val exc = shouldThrow<WebClientException> { errorDecoder.decode(methodKey = "key", response = response) }
        exc.message shouldBe ""
    }

    "Response body를 문자열로 변환하는 도중 빈 메시지를 가진 IOException이 발생하면 빈 문자열을 리턴한다" {
        // Given
        every { response.status() } returns 400
        every { response.body() } returns body
        every { body.length() } returns 20
        every { body.asInputStream() } throws IOException()

        // When, Then
        val exc = shouldThrow<WebClientException> { errorDecoder.decode(methodKey = "key", response = response) }
        exc.message shouldBe ""
    }

    "Response body를 문자열로 변환하는 도중 IOException이 발생하면 에러의 메시지를 리턴한다" {
        // Given
        every { response.status() } returns 400
        every { response.body() } returns body
        every { body.length() } returns 20
        every { body.asInputStream() } throws IOException("message")

        // When, Then
        val exc = shouldThrow<WebClientException> { errorDecoder.decode(methodKey = "key", response = response) }
        exc.message shouldBe "message"
    }
})
