package com.teamhide.kream.location.adapter.`in`.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.teamhide.kream.location.application.port.out.MessagingPort
import com.teamhide.kream.location.domain.model.UpdateUserLocation
import com.teamhide.kream.location.domain.vo.LocationRequestType
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

class UpdateLocationHandlerTest : StringSpec({
    val messagingPort = mockk<MessagingPort>()
    val objectMapper = ObjectMapper()
    objectMapper.registerKotlinModule()
    val handler = UpdateLocationHandler(
        objectMapper = objectMapper, messagingPort = messagingPort,
    )

    "세션에 userId가 없다면 메시지를 발송하지 않는다" {
        // Given
        val message = TextMessage("")
        val session = mockk<WebSocketSession>()
        every { session.attributes } returns emptyMap()

        // When
        handler.handleMessage(session, message)

        // Then
        verify(exactly = 0) { messagingPort.sendLocationUpdated(any()) }
    }

    "UpdateUserLocation 클래스로 변환 시 오류가 발생하면 메시지를 발송하지 않는다" {
        // Given
        val message = TextMessage("")
        val session = mockk<WebSocketSession>()
        every { session.attributes } returns mapOf("currentUserId" to 1L)

        // When
        handler.handleMessage(session, message)

        // Then
        verify(exactly = 0) { messagingPort.sendLocationUpdated(any()) }
    }

    "요청 타입이 UPDATE가 아니면 메시지를 발송하지 않는다" {
        // Given
        val updateMessage = UpdateUserLocation(type = LocationRequestType.REQUEST, userId = 1L, lat = 1.1, lng = 2.2)
        val message = TextMessage(objectMapper.writeValueAsString(updateMessage))
        val session = mockk<WebSocketSession>()
        every { session.attributes } returns mapOf("currentUserId" to 1L)

        // When
        handler.handleMessage(session, message)

        // Then
        verify(exactly = 0) { messagingPort.sendLocationUpdated(any()) }
    }

    "요청 유저와 대상 유저가 동일하지 않으면 메시지를 발송하지 않는다" {
        // Given
        val updateMessage = UpdateUserLocation(type = LocationRequestType.UPDATE, userId = 2L, lat = 1.1, lng = 2.2)
        val message = TextMessage(objectMapper.writeValueAsString(updateMessage))
        val session = mockk<WebSocketSession>()
        every { session.attributes } returns mapOf("currentUserId" to 1L)

        // When
        handler.handleMessage(session, message)

        // Then
        verify(exactly = 0) { messagingPort.sendLocationUpdated(any()) }
    }

    "유저의 위치 업데이트 요청을 메시지로 발송한다" {
        // Given
        val updateMessage = UpdateUserLocation(type = LocationRequestType.UPDATE, userId = 1L, lat = 1.1, lng = 2.2)
        val message = TextMessage(objectMapper.writeValueAsString(updateMessage))
        val session = mockk<WebSocketSession>()
        every { session.attributes } returns mapOf("currentUserId" to 1L)
        every { messagingPort.sendLocationUpdated(any()) } returns Unit

        // When
        handler.handleMessage(session, message)

        // Then
        verify(exactly = 1) { messagingPort.sendLocationUpdated(any()) }
    }
})
