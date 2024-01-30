package com.teamhide.kream.location.application.port.`in`

import com.teamhide.kream.location.domain.vo.UserLocation

data class SaveUserLocationHistoryCommand(
    val userId: Long,
    val location: UserLocation,
)
interface SaveUserLocationHistoryUseCase {
    fun execute(command: SaveUserLocationHistoryCommand)
}
