package com.teamhide.kream.user.application.port.out

import com.teamhide.kream.user.domain.model.User

interface SaveUserPersistencePort {
    fun save(user: User): User
}
