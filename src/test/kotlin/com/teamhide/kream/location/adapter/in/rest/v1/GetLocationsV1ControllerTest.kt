package com.teamhide.kream.location.adapter.`in`.rest.v1

import com.teamhide.kream.test.BaseIntegrationTest
import com.teamhide.kream.user.USER_ID_1_TOKEN
import com.teamhide.kream.user.adapter.out.persistence.jpa.UserFriendRepository
import com.teamhide.kream.user.adapter.out.persistence.jpa.UserRepository
import com.teamhide.kream.user.makeUserEntity
import com.teamhide.kream.user.makeUserFriendEntity
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get

class GetLocationsV1ControllerTest : BaseIntegrationTest() {
    private val URL = "/api/v1/locations"

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userFriendRepository: UserFriendRepository

    @Test
    fun `userId에 해당하는 모든 친구의 위치를 리턴한다`() {
        // Given
        val userEntity1 = makeUserEntity(id = 1L)
        val userEntity2 = makeUserEntity(id = 2L, nickname = "hide", lat = 38.444, lng = 55.999)
        val userEntity3 = makeUserEntity(id = 3L, nickname = "john", lat = 37.111, lng = 127.5423)
        userRepository.saveAll(arrayListOf(userEntity1, userEntity2, userEntity3))
        val friendEntity1 = makeUserFriendEntity(userId = 1L, friendUserId = 2L)
        val friendEntity2 = makeUserFriendEntity(userId = 1L, friendUserId = 3L)
        userFriendRepository.saveAll(arrayListOf(friendEntity1, friendEntity2))

        // When, Then
        mockMvc.get(URL) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.locations.[0].userId") { value(userEntity2.id) }
                jsonPath("$.locations.[0].nickname") { value(userEntity2.nickname) }
                jsonPath("$.locations.[0].lat") { value(userEntity2.location.x) }
                jsonPath("$.locations.[0].lng") { value(userEntity2.location.y) }
                jsonPath("$.locations.[0].stayedAt") { isNotEmpty() }
                jsonPath("$.locations.[1].userId") { value(userEntity3.id) }
                jsonPath("$.locations.[1].nickname") { value(userEntity3.nickname) }
                jsonPath("$.locations.[1].lat") { value(userEntity3.location.x) }
                jsonPath("$.locations.[1].lng") { value(userEntity3.location.y) }
                jsonPath("$.locations.[1].stayedAt") { isNotEmpty() }
            }
    }
}
