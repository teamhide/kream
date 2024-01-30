package com.teamhide.kream.user.domain.model

import com.teamhide.kream.user.domain.vo.Location
import java.time.LocalDateTime

class UserWithLocation(val userId: Long, val nickname: String, val location: Location, val stayedAt: LocalDateTime)
