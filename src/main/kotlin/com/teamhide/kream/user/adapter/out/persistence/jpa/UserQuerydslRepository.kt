package com.teamhide.kream.user.adapter.out.persistence.jpa

import com.teamhide.kream.user.domain.vo.UserStatus

interface UserQuerydslRepository {
    fun updateLocationById(userId: Long, lat: Double, lng: Double): Long

    fun updateStatusById(userId: Long, status: UserStatus): Long
}
