package com.teamhide.kream.user.adapter.out.persistence.jpa

interface UserQuerydslRepository {
    fun existByEmail(email: String): Boolean
}
