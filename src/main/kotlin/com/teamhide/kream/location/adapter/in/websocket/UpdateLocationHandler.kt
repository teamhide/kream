package com.teamhide.kream.location.adapter.`in`.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.teamhide.kream.common.config.websocket.HandshakeWithAuthInterceptor
import com.teamhide.kream.location.application.port.out.MessagingPort
import com.teamhide.kream.location.domain.model.UpdateUserLocation
import com.teamhide.kream.location.domain.vo.LocationRequestType
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.lang.Exception

private val logger = KotlinLogging.logger { }

@Component
class UpdateLocationHandler(
    private val objectMapper: ObjectMapper,
    private val messagingPort: MessagingPort,
) : TextWebSocketHandler() {
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        if (!HandshakeWithAuthInterceptor.isUserIdExist(session = session)) {
            logger.error { "UpdateLocationWebSocketHandler | Current user id is null" }
            return
        }

        objectMapper.registerKotlinModule()
        val request: UpdateUserLocation
        try {
            request = objectMapper.readValue(message.asBytes(), UpdateUserLocation::class.java)
        } catch (e: Exception) {
            logger.error { "UpdateLocationWebSocketHandler | Invalid message" }
            return
        }

        if (request.type != LocationRequestType.UPDATE) {
            logger.error { "UpdateLocationWebSocketHandler | Invalid type=${request.type}" }
            return
        }

        val userId = session.attributes[HandshakeWithAuthInterceptor.SESSION_USER_ID_KEY]
        if (userId != request.userId) {
            logger.error { "UpdateLocationWebSocketHandler | Auth error. userId=$userId, requestUserId=${request.userId}" }
            return
        }

        messagingPort.sendLocationUpdated(message = request)
    }
}
