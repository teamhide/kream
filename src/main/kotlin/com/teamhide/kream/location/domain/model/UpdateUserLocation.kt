package com.teamhide.kream.location.domain.model

import com.teamhide.kream.location.domain.vo.LocationRequestType

data class UpdateUserLocation(
    val type: LocationRequestType,
    val userId: Long,
    val lat: Double,
    val lng: Double,
)
