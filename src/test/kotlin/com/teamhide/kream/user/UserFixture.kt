package com.teamhide.kream.user

import com.teamhide.kream.common.geospatial.PointConverter
import com.teamhide.kream.user.adapter.`in`.v1.RegisterUserRequest
import com.teamhide.kream.user.adapter.out.persistence.jpa.UserEntity
import com.teamhide.kream.user.adapter.out.persistence.jpa.UserFriendEntity
import com.teamhide.kream.user.application.port.`in`.AddFriendCommand
import com.teamhide.kream.user.application.port.`in`.RegisterUserCommand
import com.teamhide.kream.user.domain.model.User
import com.teamhide.kream.user.domain.vo.Location
import com.teamhide.kream.user.domain.vo.UserStatus
import java.time.LocalDateTime

const val EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MDE3NTk5MTAsImV4cCI6MTcwMTc1OTk5NywidXNlcl9pZCI6MX0.a3gyosESbCJ_-adDmkPUUa7hrdx2zQe1xebUV252jb8"
const val USER_ID_1_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MDE3NjQ3NzMsInVzZXJfaWQiOjF9.o2l9ZN4aCGhYMEslxwv6rtc-y7Oi8G_nz9OlfNk7kJk"

fun makeUserEntity(
    password: String = "password",
    email: String = "email",
    nickname: String = "nickname",
    status: UserStatus = UserStatus.ONLINE,
    lat: Double = 37.1234,
    lng: Double = 127.1234,
    stayedAt: LocalDateTime = LocalDateTime.now(),
    id: Long = 1L,
): UserEntity {
    return UserEntity(
        password = password,
        email = email,
        nickname = nickname,
        status = status,
        location = PointConverter.from(lat = lat, lng = lng),
        stayedAt = stayedAt,
        id = id,
    )
}

fun makeUser(
    password: String = "password",
    email: String = "email",
    nickname: String = "nickname",
    status: UserStatus = UserStatus.ONLINE,
    location: Location = Location(lat = 37.1234, lng = 127.1234),
    stayedAt: LocalDateTime = LocalDateTime.now(),
    id: Long = 1L,
): User {
    return User(
        password = password,
        email = email,
        nickname = nickname,
        status = status,
        location = location,
        stayedAt = stayedAt,
        id = id,
    )
}

fun makeRegisterUserCommand(
    password: String = "password",
    email: String = "hide@hide.net",
    nickname: String = "hide",
    lat: Double = 37.1234,
    lng: Double = 127.1234,
): RegisterUserCommand {
    return RegisterUserCommand(
        password = password,
        email = email,
        nickname = nickname,
        lat = lat,
        lng = lng,
    )
}

fun makeRegisterUserRequest(
    password: String = "password",
    email: String = "hide@hide.net",
    nickname: String = "hide",
    lat: Double = 37.1234,
    lng: Double = 127.1234,
): RegisterUserRequest {
    return RegisterUserRequest(
        password = password,
        email = email,
        nickname = nickname,
        lat = lat,
        lng = lng,
    )
}

fun makeUserFriendEntity(
    userId: Long = 1L,
    friendUserId: Long = 2L,
): UserFriendEntity {
    return UserFriendEntity(userId = userId, friendUserId = friendUserId)
}

fun makeAddFriendCommand(
    userId: Long,
    friendUserId: Long,
): AddFriendCommand {
    return AddFriendCommand(userId = userId, friendUserId = friendUserId)
}
