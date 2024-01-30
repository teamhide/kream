package com.teamhide.kream.location.application.service

import com.teamhide.kream.location.application.exception.GhostModeUserException
import com.teamhide.kream.location.application.exception.UserIsNotFriendException
import com.teamhide.kream.location.application.port.`in`.GetLocationQuery
import com.teamhide.kream.location.application.port.`in`.GetLocationUseCase
import com.teamhide.kream.location.application.port.out.UserExternalPort
import com.teamhide.kream.user.domain.model.UserWithLocation
import com.teamhide.kream.user.domain.vo.UserStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetLocationService(
    private val userExternalPort: UserExternalPort,
) : GetLocationUseCase {
    override fun execute(query: GetLocationQuery): UserWithLocation {
        val userId = query.userId
        val friendUserId = query.friendUserId

        val isFriend = userExternalPort.isFriendWith(userId = userId, friendUserId = friendUserId)
        if (!isFriend) {
            throw UserIsNotFriendException()
        }

        val user = userExternalPort.getUser(userId = userId)
        if (user.status == UserStatus.GHOST) {
            throw GhostModeUserException()
        }
        return user.let {
            UserWithLocation(
                userId = it.id,
                nickname = it.nickname,
                location = it.location,
                stayedAt = it.stayedAt,
            )
        }
    }
}
