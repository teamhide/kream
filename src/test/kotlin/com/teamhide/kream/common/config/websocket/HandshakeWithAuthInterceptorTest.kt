package com.teamhide.kream.common.config.websocket

import com.teamhide.kream.user.USER_ID_1_TOKEN
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.web.socket.WebSocketHandler

class HandshakeWithAuthInterceptorTest : StringSpec({
    val interceptor = HandshakeWithAuthInterceptor(secretKey = "secretKey")

    "Authorization 헤더가 없다면 false를 반환한다" {
        // Given
        val serverHttpRequest = mockkClass(ServletServerHttpRequest::class)
        val httpServletRequest = mockk<HttpServletRequest>()
        every { httpServletRequest.getHeader(any()) } returns null
        every { serverHttpRequest.servletRequest } returns httpServletRequest
        val serverHttpResponse = mockk<ServerHttpResponse>()
        val webSocketHandler = mockk<WebSocketHandler>()
        val attributes = mutableMapOf<String, Any>()

        // When
        val sut = interceptor.beforeHandshake(
            request = serverHttpRequest,
            response = serverHttpResponse,
            wsHandler = webSocketHandler,
            attributes = attributes,
        )

        // Then
        sut shouldBe false
    }

    "Authorization 헤더가 Bearer로 시작하지 않으면 false를 반환한다" {
        // Given
        val serverHttpRequest = mockkClass(ServletServerHttpRequest::class)
        val httpServletRequest = mockk<HttpServletRequest>()
        every { httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION) } returns "ABC"
        every { serverHttpRequest.servletRequest } returns httpServletRequest
        val serverHttpResponse = mockk<ServerHttpResponse>()
        val webSocketHandler = mockk<WebSocketHandler>()
        val attributes = mutableMapOf<String, Any>()

        // When
        val sut = interceptor.beforeHandshake(
            request = serverHttpRequest,
            response = serverHttpResponse,
            wsHandler = webSocketHandler,
            attributes = attributes,
        )

        // Then
        sut shouldBe false
    }

    "토큰 복호화에서 에러가 발생하면 false를 반환한다" {
        // Given
        val serverHttpRequest = mockkClass(ServletServerHttpRequest::class)
        val httpServletRequest = mockk<HttpServletRequest>()
        every { httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION) } returns "Bearer 1234"
        every { serverHttpRequest.servletRequest } returns httpServletRequest
        val serverHttpResponse = mockk<ServerHttpResponse>()
        val webSocketHandler = mockk<WebSocketHandler>()
        val attributes = mutableMapOf<String, Any>()

        // When
        val sut = interceptor.beforeHandshake(
            request = serverHttpRequest,
            response = serverHttpResponse,
            wsHandler = webSocketHandler,
            attributes = attributes,
        )

        // Then
        sut shouldBe false
    }

    "정상 동작이면 true를 반환한다" {
        // Given
        val serverHttpRequest = mockkClass(ServletServerHttpRequest::class)
        val httpServletRequest = mockk<HttpServletRequest>()
        every { httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION) } returns "Bearer $USER_ID_1_TOKEN"
        every { serverHttpRequest.servletRequest } returns httpServletRequest
        val serverHttpResponse = mockk<ServerHttpResponse>()
        val webSocketHandler = mockk<WebSocketHandler>()
        val attributes = mutableMapOf<String, Any>()

        // When
        val sut = interceptor.beforeHandshake(
            request = serverHttpRequest,
            response = serverHttpResponse,
            wsHandler = webSocketHandler,
            attributes = attributes,
        )

        // Then
        sut shouldBe false
    }
})
