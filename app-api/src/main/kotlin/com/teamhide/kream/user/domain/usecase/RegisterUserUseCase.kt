package com.teamhide.kream.user.domain.usecase

import com.teamhide.kream.user.domain.model.User

data class RegisterUserCommand(
    val email: String,
    val nickname: String,
    val password1: String,
    val password2: String,
    val baseAddress: String,
    val detailAddress: String,
)

interface RegisterUserUseCase {
    fun execute(command: RegisterUserCommand): User
}
