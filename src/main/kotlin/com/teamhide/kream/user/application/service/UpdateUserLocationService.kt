package com.teamhide.kream.user.application.service

import com.teamhide.kream.user.application.port.`in`.UpdateUserLocationCommand
import com.teamhide.kream.user.application.port.`in`.UpdateUserLocationUseCase
import com.teamhide.kream.user.application.port.out.UpdateUserPersistencePort
import com.teamhide.kream.user.domain.vo.Location
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UpdateUserLocationService(
    private val updateUserPersistencePort: UpdateUserPersistencePort,
) : UpdateUserLocationUseCase {
    override fun execute(command: UpdateUserLocationCommand): Long {
        return updateUserPersistencePort.updateLocationById(
            userId = command.userId, location = Location(lat = command.lat, lng = command.lng)
        )
    }
}
