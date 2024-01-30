package com.teamhide.kream.user.application.port.`in`

data class UpdateUserLocationCommand(val userId: Long, val lat: Double, val lng: Double)

interface UpdateUserLocationUseCase {
    fun execute(command: UpdateUserLocationCommand): Long
}
