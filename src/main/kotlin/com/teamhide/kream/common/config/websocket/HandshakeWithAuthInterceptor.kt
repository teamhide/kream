package com.teamhide.kream.common.config.websocket

import com.teamhide.kream.common.util.jwt.DecodeTokenException
import com.teamhide.kream.common.util.jwt.TokenProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.server.HandshakeInterceptor
import java.lang.Exception

@Component
class HandshakeWithAuthInterceptor(
    @Value("\${jwt.secret-key}")
    private val secretKey: String,
) : HandshakeInterceptor {
    private var tokenProvider: TokenProvider = TokenProvider(secretKey = secretKey)

    companion object {
        const val SESSION_USER_ID_KEY = "currentUserId"

        fun isUserIdExist(session: WebSocketSession): Boolean {
            return SESSION_USER_ID_KEY in session.attributes
        }
    }

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        val httpRequest = (request as ServletServerHttpRequest).servletRequest
        val authorization = httpRequest.getHeader(HttpHeaders.AUTHORIZATION) ?: return false
        val token = extractToken(authorization) ?: return false

        return try {
            val payload = tokenProvider.decrypt(token)
            attributes[SESSION_USER_ID_KEY] = payload.userId
            return true
        } catch (e: DecodeTokenException) {
            false
        }
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?
    ) {
    }

    fun extractToken(authorization: String): String? {
        if (!authorization.startsWith("Bearer ")) {
            return null
        }
        return authorization.split(" ")[1]
    }
}
