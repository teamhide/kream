package com.teamhide.kream.user.application.service

import com.teamhide.kream.user.application.exception.UserNotFoundException
import com.teamhide.kream.user.application.port.`in`.GetUserQuery
import com.teamhide.kream.user.application.port.`in`.GetUserUseCase
import com.teamhide.kream.user.application.port.out.GetUserPersistencePort
import com.teamhide.kream.user.domain.model.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetUserService(
    private val getUserPersistencePort: GetUserPersistencePort,
) : GetUserUseCase {
    override fun execute(query: GetUserQuery): User {
        return getUserPersistencePort.findById(id = query.userId) ?: run {
            throw UserNotFoundException()
        }
    }
}
