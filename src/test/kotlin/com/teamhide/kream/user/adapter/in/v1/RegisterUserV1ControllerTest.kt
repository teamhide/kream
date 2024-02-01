
package com.teamhide.kream.user.adapter.`in`.v1

import com.teamhide.kream.support.test.BaseIntegrationTest
import com.teamhide.kream.user.adapter.out.persistence.jpa.UserRepository
import com.teamhide.kream.user.application.exception.UserAlreadyExistException
import com.teamhide.kream.user.domain.model.PasswordDoesNotMatchException
import com.teamhide.kream.user.makeRegisterUserRequest
import com.teamhide.kream.user.makeUser
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.post

const val URL = "/api/v1/user"

internal class RegisterUserV1ControllerTest : BaseIntegrationTest() {
    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun `password1과 password2가 동일하지 않다면 400이 리턴된다`() {
        // Given
        val request = makeRegisterUserRequest(password1 = "a", password2 = "b")
        val exc = PasswordDoesNotMatchException()

        // When, Then
        mockMvc.post(URL) {
            jsonRequest(request)
        }
            .andExpect {
                status { isBadRequest() }
                jsonPath("errorCode") { value(exc.errorCode) }
                jsonPath("message") { value(exc.message) }
            }

        val count = userRepository.count()
        count shouldBe 0
    }

    @Test
    fun `동일한 email을 가진 유저가 존재하면 400이 리턴된다`() {
        // Given
        val user = makeUser()
        userRepository.save(user)
        val request = makeRegisterUserRequest(nickname = user.nickname, email = user.email)
        val exc = UserAlreadyExistException()

        // When, Then
        mockMvc.post(URL) {
            jsonRequest(request)
        }
            .andExpect {
                status { isBadRequest() }
                jsonPath("errorCode") { value(exc.errorCode) }
                jsonPath("message") { value(exc.message) }
            }

        val count = userRepository.count()
        count shouldBe 1
    }

    @Test
    fun `성공적으로 회원가입을 진행한다`() {
        // Given
        val request = makeRegisterUserRequest(nickname = "hide", email = "hide@hide.com")

        // When, Then
        mockMvc.post(URL) {
            jsonRequest(request)
        }
            .andExpect {
                status { isOk() }
                jsonPath("email") { value(request.email) }
                jsonPath("nickname") { value(request.nickname) }
            }

        val count = userRepository.count()
        count shouldBe 1
    }
}
