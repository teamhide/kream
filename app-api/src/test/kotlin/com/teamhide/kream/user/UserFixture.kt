package com.teamhide.kream.user

import com.teamhide.kream.user.domain.model.User
import com.teamhide.kream.user.domain.usecase.RegisterUserCommand
import com.teamhide.kream.user.domain.vo.Address
import com.teamhide.kream.user.ui.api.RegisterUserRequest

const val EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MDE3NTk5MTAsImV4cCI6MTcwMTc1OTk5NywidXNlcl9pZCI6MX0.a3gyosESbCJ_-adDmkPUUa7hrdx2zQe1xebUV252jb8"
const val USER_ID_1_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxfQ.JWd_KhG7M3FTQOlUVujmAEQ9b3a0pU2a74b-3ehboAE"
const val USER_ID_2_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoyfQ.0xRiB1wfPN9nuJ_6UtDBJAxhAfuJ_qnUuo_Bgfhjlb0"

fun makeRegisterUserCommand(
    email: String = "h@id.e",
    nickname: String = "hide",
    password1: String = "password",
    password2: String = "password",
    baseAddress: String = "base",
    detailAddress: String = "detail",
): RegisterUserCommand {
    return RegisterUserCommand(
        email = email,
        nickname = nickname,
        password1 = password1,
        password2 = password2,
        baseAddress = baseAddress,
        detailAddress = detailAddress,
    )
}

fun makeUser(
    id: Long = 0L,
    email: String = "h@id.e",
    nickname: String = "hide",
    password: String = "password",
    address: Address = Address(base = "base", detail = "detail"),
): User {
    return User(id = id, email = email, nickname = nickname, password = password, address = address)
}

fun makeRegisterUserRequest(
    email: String = "h@id.e",
    nickname: String = "hide",
    password1: String = "password",
    password2: String = "password",
    baseAddress: String = "base",
    detailAddress: String = "detail",
): RegisterUserRequest {
    return RegisterUserRequest(
        email = email,
        nickname = nickname,
        password1 = password1,
        password2 = password2,
        baseAddress = baseAddress,
        detailAddress = detailAddress,
    )
}
