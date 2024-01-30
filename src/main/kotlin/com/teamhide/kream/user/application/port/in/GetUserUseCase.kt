package com.teamhide.kream.user.application.port.`in`

import com.teamhide.kream.user.domain.model.User

data class GetUserQuery(val userId: Long)

interface GetUserUseCase {
    fun execute(query: GetUserQuery): User
}
