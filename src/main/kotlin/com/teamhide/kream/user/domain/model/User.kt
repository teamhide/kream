package com.teamhide.kream.user.domain.model

import com.teamhide.kream.user.domain.vo.Location
import com.teamhide.kream.user.domain.vo.UserStatus
import java.time.LocalDateTime

class User(
    val password: String,
    val email: String,
    val nickname: String,
    val status: UserStatus,
    val location: Location,
    val stayedAt: LocalDateTime,
    val id: Long = 0L,
)
