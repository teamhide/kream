package com.teamhide.kream.user.ui.api.dto

data class RegisterUserRequest(
    val email: String,
    val nickname: String,
    val password1: String,
    val password2: String,
    val baseAddress: String,
    val detailAddress: String,
)
