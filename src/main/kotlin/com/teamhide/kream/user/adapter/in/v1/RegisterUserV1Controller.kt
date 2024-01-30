package com.teamhide.kream.user.adapter.`in`.v1

import com.teamhide.kream.common.response.ApiResponse
import com.teamhide.kream.user.application.port.`in`.RegisterUserCommand
import com.teamhide.kream.user.application.port.`in`.RegisterUserUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class RegisterUserRequest(
    val password: String,
    val email: String,
    val nickname: String,
    val lat: Double,
    val lng: Double,
)

data class RegisterUserResponse(
    val email: String,
    val nickname: String,
)

@RestController
@RequestMapping("/api/v1/user")
class RegisterUserV1Controller(
    private val useCase: RegisterUserUseCase,
) {
    @PostMapping("")
    fun registerUser(@RequestBody @Valid body: RegisterUserRequest): ApiResponse<RegisterUserResponse> {
        val command = with(body) {
            RegisterUserCommand(
                password = password,
                email = email,
                nickname = nickname,
                lat = lat,
                lng = lng,
            )
        }
        val user = useCase.execute(command = command)
        val response = with(user) {
            RegisterUserResponse(
                email = email,
                nickname = nickname,
            )
        }
        return ApiResponse.success(body = response, statusCode = HttpStatus.OK)
    }
}
