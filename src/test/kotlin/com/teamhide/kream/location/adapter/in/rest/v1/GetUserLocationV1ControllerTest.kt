package com.teamhide.kream.location.adapter.`in`.rest.v1

import com.teamhide.kream.location.application.exception.GhostModeUserException
import com.teamhide.kream.location.application.exception.UserIsNotFriendException
import com.teamhide.kream.test.BaseIntegrationTest
import com.teamhide.kream.user.USER_ID_1_TOKEN
import com.teamhide.kream.user.adapter.out.persistence.jpa.UserFriendRepository
import com.teamhide.kream.user.adapter.out.persistence.jpa.UserRepository
import com.teamhide.kream.user.domain.vo.UserStatus
import com.teamhide.kream.user.makeUserEntity
import com.teamhide.kream.user.makeUserFriendEntity
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get

class GetUserLocationV1ControllerTest : BaseIntegrationTest() {
    private val URL = "/api/v1/locations/{userId}"

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userFriendRepository: UserFriendRepository

    @Test
    fun `친구 상태가 아닌 유저의 위치를 조회하면 400이 반환된다`() {
        // Given
        // 1번 유저와
        val userEntity1 = makeUserEntity()
        userRepository.save(userEntity1)
        // 2번 유저가 존재한다
        val userEntity2 = makeUserEntity()
        val savedUser2 = userRepository.save(userEntity2)

        val exc = UserIsNotFriendException()

        // When, Then
        // 2번 유저의 위치를 요청한다
        mockMvc.get(URL, savedUser2.id) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
        }
            .andExpect {
                status { isBadRequest() }
                jsonPath("errorCode") { value(exc.errorCode) }
                jsonPath("message") { value(exc.message) }
            }
    }

    @Test
    fun `유령 모드 상태인 유저의 위치를 조회하면 400이 반환된다`() {
        // Given
        // 1번 유저가 존재한다
        val userEntity1 = makeUserEntity()
        val savedUser1 = userRepository.save(userEntity1)
        // 2번 유저는 오프라인 상태로 존재한다
        val userEntity2 = makeUserEntity(status = UserStatus.GHOST)
        val savedUser2 = userRepository.save(userEntity2)

        // 1번 유저는 2번 유저를 친구로 등록해놓은 상태이다
        val relation = makeUserFriendEntity(userId = savedUser1.id, friendUserId = savedUser2.id)
        userFriendRepository.save(relation)

        val exc = GhostModeUserException()

        // When, Then
        // 2번 유저의 위치를 요청한다
        mockMvc.get(URL, savedUser2.id) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
        }
            .andExpect {
                status { isBadRequest() }
                jsonPath("errorCode") { value(exc.errorCode) }
                jsonPath("message") { value(exc.message) }
            }
    }

    @Test
    fun `특정 유저의 위치를 조회한다`() {
        // Given
        // 1번 유저와
        val userEntity1 = makeUserEntity()
        val savedUser1 = userRepository.save(userEntity1)
        // 2번 유저가 존재한다
        val userEntity2 = makeUserEntity(status = UserStatus.ONLINE)
        val savedUser2 = userRepository.save(userEntity2)

        // 1번 유저는 2번 유저를 친구로 등록해놓은 상태이다
        val relation = makeUserFriendEntity(userId = savedUser1.id, friendUserId = savedUser2.id)
        userFriendRepository.save(relation)

        // When, Then
        // 2번 유저의 위치를 요청한다
        mockMvc.get(URL, savedUser2.id) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
        }
            .andExpect {
                status { isOk() }
                jsonPath("userId") { value(savedUser1.id) }
                jsonPath("nickname") { value(savedUser1.nickname) }
                jsonPath("location.lat") { value(savedUser1.location.x) }
                jsonPath("location.lng") { value(savedUser1.location.y) }
                jsonPath("stayedAt") { isNotEmpty() }
            }
    }
}
