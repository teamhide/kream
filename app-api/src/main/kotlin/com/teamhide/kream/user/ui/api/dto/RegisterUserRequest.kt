package com.teamhide.kream.user.ui.api.dto

import com.teamhide.kream.user.domain.usecase.RegisterUserCommand

data class RegisterUserRequest(
    val email: String,
    val nickname: String,
    val password1: String,
    val password2: String,
    val baseAddress: String,
    val detailAddress: String,
) {
    fun toCommand(): RegisterUserCommand {
        return RegisterUserCommand(
            email = this.email,
            nickname = this.nickname,
            password1 = this.password1,
            password2 = this.password2,
            baseAddress = this.baseAddress,
            detailAddress = this.detailAddress,
        )
    }
}
