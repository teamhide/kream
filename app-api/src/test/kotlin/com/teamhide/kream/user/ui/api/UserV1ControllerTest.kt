
package com.teamhide.kream.user.ui.api

import com.ninjasquad.springmockk.MockkBean
import com.teamhide.kream.support.test.RestControllerTest
import com.teamhide.kream.user.application.exception.UserAlreadyExistException
import com.teamhide.kream.user.domain.model.PasswordDoesNotMatchException
import com.teamhide.kream.user.domain.usecase.RegisterUserUseCase
import com.teamhide.kream.user.makeRegisterUserRequest
import com.teamhide.kream.user.makeUser
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.post

const val URL = "/api/v1/user"

@WebMvcTest(UserV1Controller::class)
internal class UserV1ControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var registerUserUseCase: RegisterUserUseCase

    @Test
    fun `password1과 password2가 동일하지 않다면 400이 리턴된다`() {
        // Given
        val request = makeRegisterUserRequest(password1 = "a", password2 = "b")
        val exc = PasswordDoesNotMatchException()
        every { registerUserUseCase.execute(any()) } throws PasswordDoesNotMatchException()

        // When, Then
        mockMvc.post(URL) {
            jsonRequest(request)
        }
            .andExpect {
                status { isBadRequest() }
                jsonPath("errorCode") { value(exc.errorCode) }
                jsonPath("message") { value(exc.message) }
            }
    }

    @Test
    fun `동일한 email을 가진 유저가 존재하면 400이 리턴된다`() {
        // Given
        val request = makeRegisterUserRequest()
        val exc = UserAlreadyExistException()
        every { registerUserUseCase.execute(any()) } throws UserAlreadyExistException()

        // When, Then
        mockMvc.post(URL) {
            jsonRequest(request)
        }
            .andExpect {
                status { isBadRequest() }
                jsonPath("errorCode") { value(exc.errorCode) }
                jsonPath("message") { value(exc.message) }
            }
    }

    @Test
    fun `성공적으로 회원가입을 진행한다`() {
        // Given
        val user = makeUser()
        val request = makeRegisterUserRequest(nickname = user.nickname, email = user.email)
        every { registerUserUseCase.execute(any()) } returns user

        // When, Then
        mockMvc.post(URL) {
            jsonRequest(request)
        }
            .andExpect {
                status { isOk() }
                jsonPath("email") { value(request.email) }
                jsonPath("nickname") { value(request.nickname) }
            }
    }
}
