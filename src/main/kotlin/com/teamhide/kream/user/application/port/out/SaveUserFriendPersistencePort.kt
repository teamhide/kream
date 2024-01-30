package com.teamhide.kream.user.application.port.out

interface SaveUserFriendPersistencePort {
    fun save(userId: Long, friendUserId: Long)
}
