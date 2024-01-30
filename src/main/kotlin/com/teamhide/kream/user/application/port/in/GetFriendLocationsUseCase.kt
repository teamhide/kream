package com.teamhide.kream.user.application.port.`in`

import com.teamhide.kream.user.domain.model.UserWithLocation

data class GetFriendLocationsQuery(val userId: Long)

interface GetFriendLocationsUseCase {
    fun execute(query: GetFriendLocationsQuery): List<UserWithLocation>
}
