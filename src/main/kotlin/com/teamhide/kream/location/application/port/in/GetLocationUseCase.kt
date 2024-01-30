package com.teamhide.kream.location.application.port.`in`

import com.teamhide.kream.user.domain.model.UserWithLocation

data class GetLocationQuery(val userId: Long, val friendUserId: Long)

interface GetLocationUseCase {
    fun execute(query: GetLocationQuery): UserWithLocation
}
