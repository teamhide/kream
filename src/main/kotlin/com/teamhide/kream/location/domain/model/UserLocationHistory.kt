package com.teamhide.kream.location.domain.model

import com.teamhide.kream.location.domain.vo.UserLocation
import org.bson.types.ObjectId

class UserLocationHistory(
    val id: ObjectId = ObjectId(),
    val userId: Long,
    val location: UserLocation,
)
