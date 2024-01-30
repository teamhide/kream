package com.teamhide.kream.user.adapter.out.persistence

import com.teamhide.kream.user.adapter.out.persistence.jpa.UserRepository
import com.teamhide.kream.user.domain.model.User
import org.springframework.stereotype.Component

@Component
class UserRepositoryAdapter(
    private val userRepository: UserRepository,
) {
    fun existsByEmail(email: String): Boolean {
        return userRepository.existByEmail(email = email)
    }

    fun save(user: User): User {
        return userRepository.save(user)
    }
}
