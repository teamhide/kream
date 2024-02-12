package com.teamhide.kream.product.adapter.out.external

import com.teamhide.kream.product.domain.usecase.UserExternalPort
import com.teamhide.kream.user.domain.model.User
import com.teamhide.kream.user.domain.usecase.GetUserByIdQuery
import com.teamhide.kream.user.domain.usecase.GetUserByIdUseCase
import org.springframework.stereotype.Component

@Component
class UserExternalAdapter(
    private val getUserByIdUseCase: GetUserByIdUseCase,
) : UserExternalPort {
    override fun findById(userId: Long): User? {
        val query = GetUserByIdQuery(userId = userId)
        return getUserByIdUseCase.execute(query = query)
    }
}
