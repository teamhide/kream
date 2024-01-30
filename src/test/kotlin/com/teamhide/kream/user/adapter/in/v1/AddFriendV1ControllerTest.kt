package com.teamhide.kream.user.adapter.`in`.v1

import com.teamhide.kream.test.BaseIntegrationTest
import com.teamhide.kream.user.USER_ID_1_TOKEN
import com.teamhide.kream.user.adapter.out.persistence.jpa.UserFriendRepository
import com.teamhide.kream.user.application.exception.AlreadyFriendException
import com.teamhide.kream.user.application.exception.ExceedFriendLimitException
import com.teamhide.kream.user.makeUserFriendEntity
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.post

internal class AddFriendV1ControllerTest : BaseIntegrationTest() {
    private val URL = "/api/v1/user/friend"

    @Autowired
    lateinit var userFriendRepository: UserFriendRepository

    @Test
    fun `이미 친구로 추가된 유저라면 400을 리턴한다`() {
        // Given
        val userFriendEntity = makeUserFriendEntity(userId = 1L, friendUserId = 2L)
        userFriendRepository.save(userFriendEntity)
        val request = AddFriendRequest(friendUserId = 2L)
        val exc = AlreadyFriendException()

        // When, Then
        mockMvc.post(URL) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
            jsonRequest(request)
        }
            .andExpect {
                status { isBadRequest() }
                jsonPath("errorCode") { value(exc.errorCode) }
                jsonPath("message") { value(exc.message) }
            }

        val count = userFriendRepository.count()
        count shouldBe 1
    }

    @Test
    fun `기존 친구 수가 20명이 넘는 경우 400을 리턴한다`() {
        // Given
        for (i in 1..20) {
            userFriendRepository.save(makeUserFriendEntity(userId = 1L, friendUserId = i.toLong()))
        }
        val request = AddFriendRequest(friendUserId = 100L)
        val exc = ExceedFriendLimitException()

        // When, Then
        mockMvc.post(URL) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
            jsonRequest(request)
        }
            .andExpect {
                status { isBadRequest() }
                jsonPath("errorCode") { value(exc.errorCode) }
                jsonPath("message") { value(exc.message) }
            }

        val count = userFriendRepository.count()
        count shouldBe 20
    }

    @Test
    fun `유저를 친구로 추가하고 200을 리턴한다`() {
        // Given
        val request = AddFriendRequest(friendUserId = 123L)

        // When, Then
        mockMvc.post(URL) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
            jsonRequest(request)
        }
            .andExpect {
                status { isOk() }
            }

        val count = userFriendRepository.count()
        count shouldBe 1
    }
}
