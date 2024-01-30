package com.teamhide.kream.user.adapter.out.persistence

import com.teamhide.kream.user.adapter.out.persistence.jpa.UserFriendEntity
import com.teamhide.kream.user.adapter.out.persistence.jpa.UserFriendRepository
import com.teamhide.kream.user.application.port.out.GetUserFriendPersistencePort
import com.teamhide.kream.user.application.port.out.SaveUserFriendPersistencePort
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class UserFriendRepositoryAdapter(
    private val userFriendRepository: UserFriendRepository,
) : GetUserFriendPersistencePort, SaveUserFriendPersistencePort {
    override fun existsByUserIdAndFriendUserId(userId: Long, friendUserId: Long): Boolean {
        return userFriendRepository.existsByUserIdAndFriendUserId(userId = userId, friendUserId = friendUserId)
    }

    override fun countsByUserIdLessThan(userId: Long, count: Int): Boolean {
        val pageable = Pageable.ofSize(count + 1)
        val friends = userFriendRepository.findAllByUserId(userId = userId, pageable = pageable)
        return friends.size < count
    }

    override fun findAllFriendUserIdsByUserId(userId: Long): List<Long> {
        val friends = userFriendRepository.findAllByUserId(userId = userId)
        return friends.map { it.friendUserId }
    }

    override fun isFriendWith(userId: Long, friendUserId: Long): Boolean {
        return userFriendRepository.findByUserIdAndFriendUserId(userId = userId, friendUserId = friendUserId) != null
    }

    override fun save(userId: Long, friendUserId: Long) {
        val userFriendEntity = UserFriendEntity(userId = userId, friendUserId = friendUserId)
        userFriendRepository.save(userFriendEntity)
    }
}
