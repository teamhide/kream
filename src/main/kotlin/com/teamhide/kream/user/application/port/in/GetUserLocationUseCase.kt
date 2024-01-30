package com.teamhide.kream.user.application.port.`in`

import com.teamhide.kream.user.domain.model.UserWithLocation

data class GetUserLocationQuery(val userId: Long)

interface GetUserLocationUseCase {
    fun execute(query: GetUserLocationQuery): UserWithLocation
}
