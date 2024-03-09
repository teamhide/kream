package com.teamhide.kream.user.domain.repository

interface UserQuerydslRepository {
    fun existByEmail(email: String): Boolean
}
