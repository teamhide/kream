package com.teamhide.kream.location.application.port.`in`

import com.teamhide.kream.user.domain.model.UserWithLocation

data class GetLocationsQuery(val userId: Long)
interface GetLocationsUseCase {
    fun execute(query: GetLocationsQuery): List<UserWithLocation>
}
