package com.teamhide.kream.user.adapter.out.persistence.jpa

import com.querydsl.jpa.impl.JPAQueryFactory
import com.teamhide.kream.user.domain.model.QUser

class UserQuerydslRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : UserQuerydslRepository {
    private val user = QUser.user

    override fun existByEmail(email: String): Boolean {
        val user = queryFactory
            .selectOne()
            .from(user)
            .where(user.email.eq(email))
            .fetchFirst()
        return user != null
    }
}
