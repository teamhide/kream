package com.teamhide.kream.user.application.port.out

interface GetUserFriendPersistencePort {
    fun existsByUserIdAndFriendUserId(userId: Long, friendUserId: Long): Boolean

    fun countsByUserIdLessThan(userId: Long, count: Int): Boolean

    fun findAllFriendUserIdsByUserId(userId: Long): List<Long>

    fun isFriendWith(userId: Long, friendUserId: Long): Boolean
}
