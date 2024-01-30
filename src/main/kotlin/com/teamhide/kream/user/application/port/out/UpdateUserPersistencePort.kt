package com.teamhide.kream.user.application.port.out

import com.teamhide.kream.user.domain.vo.Location
import com.teamhide.kream.user.domain.vo.UserStatus

interface UpdateUserPersistencePort {
    fun updateLocationById(userId: Long, location: Location): Long

    fun updateStatusById(userId: Long, status: UserStatus): Long
}
