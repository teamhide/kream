package com.teamhide.kream.location.adapter.`in`.websocket

import com.teamhide.kream.location.domain.model.RequestFriendLocation
import com.teamhide.kream.location.domain.vo.LocationRequestType
import com.teamhide.kream.test.BaseIntegrationTest
import com.teamhide.kream.user.USER_ID_1_TOKEN
import com.teamhide.kream.user.adapter.out.persistence.jpa.UserFriendRepository
import com.teamhide.kream.user.adapter.out.persistence.jpa.UserRepository
import com.teamhide.kream.user.makeUserEntity
import com.teamhide.kream.user.makeUserFriendEntity
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.net.URI

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class GetFriendLocationHandlerIntegrationTest : BaseIntegrationTest() {
    @LocalServerPort
    private var port: Int = 0

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userFriendRepository: UserFriendRepository

    @Test
    fun `친구들의 위치를 요청하여 전달받는다`() {
        // Given
        // 1, 2, 3 유저가 존재한다
        val userEntity1 = makeUserEntity(id = 1L, lat = 1.1, lng = 1.1)
        val userEntity2 = makeUserEntity(id = 2L, lat = 2.2, lng = 2.2)
        val userEntity3 = makeUserEntity(id = 3L, lat = 3.3, lng = 3.3)
        userRepository.saveAll(listOf(userEntity1, userEntity2, userEntity3))

        // 1번 유저는 2, 3번과 친구 상태이다
        val friendEntity1 = makeUserFriendEntity(userId = 1L, friendUserId = 2L)
        val friendEntity2 = makeUserFriendEntity(userId = 1L, friendUserId = 3L)
        userFriendRepository.saveAll(listOf(friendEntity1, friendEntity2))

        val webSocketClient = StandardWebSocketClient()
        val handler = TextWebSocketHandler()
        val headers = WebSocketHttpHeaders().apply {
            add(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
        }
        val uri = URI("ws://localhost:$port/request-location")
        val request = RequestFriendLocation(type = LocationRequestType.REQUEST, userId = userEntity1.id)

        // When, Then
        val session = webSocketClient.execute(handler, headers, uri).get()
        val message = TextMessage(objectMapper.writeValueAsString(request))
        session.sendMessage(message)
        session.close()
    }
}
