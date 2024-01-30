package com.teamhide.kream.location.application.service

import com.teamhide.kream.location.application.port.`in`.GetLocationsQuery
import com.teamhide.kream.location.application.port.`in`.GetLocationsUseCase
import com.teamhide.kream.location.application.port.out.UserExternalPort
import com.teamhide.kream.user.domain.model.UserWithLocation
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetLocationsService(
    private val userExternalPort: UserExternalPort,
) : GetLocationsUseCase {
    override fun execute(query: GetLocationsQuery): List<UserWithLocation> {
        return userExternalPort.getFriendLocations(userId = query.userId)
    }
}
