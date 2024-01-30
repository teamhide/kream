package com.teamhide.kream.location.application.port.`in`

data class UpdateLocationCommand(val userId: Long, val lat: Double, val lng: Double)

interface UpdateLocationUseCase {
    fun execute(command: UpdateLocationCommand): Long
}
