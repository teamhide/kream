package com.teamhide.kream.location.application.port.out

import com.teamhide.kream.location.domain.model.UpdateUserLocation

interface MessagingPort {
    fun sendLocationUpdated(message: UpdateUserLocation)
}
