package com.teamhide.kream.user.application.port.`in`

data class IsFriendWithQuery(val userId: Long, val friendUserId: Long)

interface IsFriendWithUseCase {
    fun execute(query: IsFriendWithQuery): Boolean
}
