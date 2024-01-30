package com.teamhide.kream.location.application.port.out

import com.teamhide.kream.location.domain.vo.UserLocation
import com.teamhide.kream.user.domain.model.User
import com.teamhide.kream.user.domain.model.UserWithLocation

interface UserExternalPort {
    fun getFriendLocations(userId: Long): List<UserWithLocation>

    fun updateUserLocation(userId: Long, location: UserLocation): Long

    fun getUserLocation(userId: Long): UserWithLocation

    fun getUser(userId: Long): User

    fun isFriendWith(userId: Long, friendUserId: Long): Boolean
}
