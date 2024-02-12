package com.teamhide.kream.product.domain.usecase

import com.teamhide.kream.user.domain.model.User

interface UserExternalPort {
    fun findById(userId: Long): User?
}
