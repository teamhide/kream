package com.teamhide.kream.user.application.service

import com.teamhide.kream.user.adapter.out.persistence.UserRepositoryAdapter
import com.teamhide.kream.user.domain.model.User
import com.teamhide.kream.user.domain.usecase.GetUserByIdQuery
import com.teamhide.kream.user.domain.usecase.GetUserByIdUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetUserByIdService(
    private val userRepositoryAdapter: UserRepositoryAdapter,
) : GetUserByIdUseCase {
    override fun execute(query: GetUserByIdQuery): User? {
        return userRepositoryAdapter.findById(userId = query.userId)
    }
}
