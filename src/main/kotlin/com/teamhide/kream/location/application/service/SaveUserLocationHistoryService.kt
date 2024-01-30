package com.teamhide.kream.location.application.service

import com.teamhide.kream.location.application.port.`in`.SaveUserLocationHistoryCommand
import com.teamhide.kream.location.application.port.`in`.SaveUserLocationHistoryUseCase
import com.teamhide.kream.location.application.port.out.SaveLocationHistoryPersistencePort
import com.teamhide.kream.location.domain.model.UserLocationHistory
import org.springframework.stereotype.Service

@Service
class SaveUserLocationHistoryService(
    private val persistencePort: SaveLocationHistoryPersistencePort,
) : SaveUserLocationHistoryUseCase {
    override fun execute(command: SaveUserLocationHistoryCommand) {
        val history = command.let {
            UserLocationHistory(
                userId = it.userId,
                location = it.location,
            )
        }
        persistencePort.save(history = history)
    }
}
