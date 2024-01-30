package com.teamhide.kream.location.adapter.out.persistence

import com.teamhide.kream.location.adapter.out.persistence.mongo.UserLocationHistoryRepository
import com.teamhide.kream.location.application.port.out.SaveLocationHistoryPersistencePort
import com.teamhide.kream.location.domain.converter.UserLocationHistoryConverter
import com.teamhide.kream.location.domain.model.UserLocationHistory
import org.springframework.stereotype.Component

@Component
class UserLocationHistoryRepositoryAdapter(
    private val userLocationHistoryRepository: UserLocationHistoryRepository,
) : SaveLocationHistoryPersistencePort {
    override fun save(history: UserLocationHistory) {
        val historyEntity = UserLocationHistoryConverter.to(history)
        userLocationHistoryRepository.save(historyEntity)
    }
}
