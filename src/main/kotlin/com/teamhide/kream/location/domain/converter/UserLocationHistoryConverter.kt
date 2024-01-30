package com.teamhide.kream.location.domain.converter

import com.teamhide.kream.location.adapter.out.persistence.mongo.UserLocationHistoryEntity
import com.teamhide.kream.location.domain.model.UserLocationHistory

class UserLocationHistoryConverter private constructor() {
    companion object {
        fun from(locationHistory: UserLocationHistoryEntity): UserLocationHistory {
            with(locationHistory) {
                return UserLocationHistory(
                    id = id,
                    location = location,
                    userId = userId,
                )
            }
        }

        fun to(locationHistory: UserLocationHistory): UserLocationHistoryEntity {
            with(locationHistory) {
                return UserLocationHistoryEntity(
                    id = id,
                    location = location,
                    userId = userId,
                )
            }
        }
    }
}
