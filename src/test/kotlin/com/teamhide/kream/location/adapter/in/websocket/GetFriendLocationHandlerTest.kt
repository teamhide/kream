package com.teamhide.kream.location.adapter.`in`.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.teamhide.kream.location.application.port.`in`.GetLocationsUseCase
import com.teamhide.kream.location.domain.model.RequestFriendLocation
import com.teamhide.kream.location.domain.vo.LocationRequestType
import com.teamhide.kream.user.domain.model.UserWithLocation
import com.teamhide.kream.user.domain.vo.Location
import io.kotest.core.spec.style.StringSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.time.LocalDateTime

class GetFriendLocationHandlerTest : StringSpec({
    val getLocationsUseCase = mockk<GetLocationsUseCase>()
    val objectMapper = ObjectMapper()
    val handler = GetFriendLocationHandler(
        getLocationsUseCase = getLocationsUseCase, objectMapper = objectMapper,
    )

    afterEach {
        clearAllMocks()
    }

    "세션에 userId가 없다면 위치를 전달하지 않는다" {
        // Given
        val message = TextMessage("")
        val session = mockk<WebSocketSession>()
        every { session.attributes } returns emptyMap()

        // When
        handler.handleMessage(session, message)

        // Then
        verify(exactly = 0) { session.sendMessage(any()) }
    }

    "RequestFriendLocation 클래스로 변환 시 오류가 발생하면 위치를 전달하지 않는다" {
        // Given
        val message = TextMessage("")
        val session = mockk<WebSocketSession>()
        every { session.attributes } returns mapOf("currentUserId" to 1L)

        // When
        handler.handleMessage(session, message)

        // Then
        verify(exactly = 0) { session.sendMessage(any()) }
    }

    "요청 타입이 REQUEST가 아니면 위치를 전달하지 않는다" {
        // Given
        val requestMessage = RequestFriendLocation(type = LocationRequestType.UPDATE, userId = 1L)
        val message = TextMessage(objectMapper.writeValueAsString(requestMessage))
        val session = mockk<WebSocketSession>()
        every { session.attributes } returns mapOf("currentUserId" to 1L)

        // When
        handler.handleMessage(session, message)

        // Then
        verify(exactly = 0) { session.sendMessage(any()) }
    }

    "요청 유저와 대상 유저가 동일하지 않으면 위치를 전달하지 않는다" {
        // Given
        val requestMessage = RequestFriendLocation(type = LocationRequestType.REQUEST, userId = 2L)
        val message = TextMessage(objectMapper.writeValueAsString(requestMessage))
        val session = mockk<WebSocketSession>()
        every { session.attributes } returns mapOf("currentUserId" to 1L)

        // When
        handler.handleMessage(session, message)

        // Then
        verify(exactly = 0) { session.sendMessage(any()) }
    }

    "친구들의 위치를 전달한다" {
        // Given
        val requestMessage = RequestFriendLocation(type = LocationRequestType.REQUEST, userId = 1L)
        val message = TextMessage(objectMapper.writeValueAsString(requestMessage))
        val session = mockk<WebSocketSession>()
        every { session.attributes } returns mapOf("currentUserId" to 1L)
        every { session.sendMessage(any()) } returns Unit
        val userLocation = UserWithLocation(
            userId = 2L,
            nickname = "hide",
            location = Location(lat = 12.12, lng = 37.37),
            stayedAt = LocalDateTime.now(),
        )
        every { getLocationsUseCase.execute(any()) } returns listOf(userLocation)

        // When
        handler.handleMessage(session, message)

        // Then
        verify(exactly = 1) { getLocationsUseCase.execute(any()) }
        verify(exactly = 1) { session.sendMessage(any()) }
    }
})
