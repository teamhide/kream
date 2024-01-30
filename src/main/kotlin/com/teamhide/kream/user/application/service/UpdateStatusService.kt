package com.teamhide.kream.user.application.service

import com.teamhide.kream.user.application.port.`in`.UpdateStatusCommand
import com.teamhide.kream.user.application.port.`in`.UpdateStatusUseCase
import com.teamhide.kream.user.application.port.out.UpdateUserPersistencePort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UpdateStatusService(
    private val updateUserPersistencePort: UpdateUserPersistencePort,
) : UpdateStatusUseCase {
    override fun execute(command: UpdateStatusCommand) {
        updateUserPersistencePort.updateStatusById(userId = command.userId, status = command.status)
    }
}
