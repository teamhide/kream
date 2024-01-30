package com.teamhide.kream.location.domain.model

import com.teamhide.kream.location.domain.vo.LocationRequestType

data class RequestFriendLocation(val type: LocationRequestType, val userId: Long)
