package com.teamhide.kream.location.application.port.out

import com.teamhide.kream.location.domain.model.UserLocationHistory

interface SaveLocationHistoryPersistencePort {
    fun save(history: UserLocationHistory)
}
