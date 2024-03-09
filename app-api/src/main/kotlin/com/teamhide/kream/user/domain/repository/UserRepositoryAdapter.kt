package com.teamhide.kream.user.domain.repository

import com.teamhide.kream.user.domain.model.User
import org.springframework.data.repository.findByIdOrNull
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

    fun findById(userId: Long): User? {
        return userRepository.findByIdOrNull(userId)
    }
}
