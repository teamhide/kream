package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.domain.usecase.ProductUserPort
import com.teamhide.kream.user.domain.model.User
import com.teamhide.kream.user.domain.usecase.GetUserByIdQuery
import com.teamhide.kream.user.domain.usecase.GetUserByIdUseCase
import org.springframework.stereotype.Component

@Component
class ProductUserAdapter(
    private val getUserByIdUseCase: GetUserByIdUseCase,
) : ProductUserPort {
    override fun findById(userId: Long): User? {
        val query = GetUserByIdQuery(userId = userId)
        return getUserByIdUseCase.execute(query = query)
    }
}
