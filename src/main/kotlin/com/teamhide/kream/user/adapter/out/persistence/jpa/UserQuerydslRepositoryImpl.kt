package com.teamhide.kream.user.adapter.out.persistence.jpa

import com.querydsl.jpa.impl.JPAQueryFactory
import com.teamhide.kream.common.geospatial.PointConverter
import com.teamhide.kream.user.domain.vo.UserStatus
import jakarta.persistence.EntityManager

class UserQuerydslRepositoryImpl(
    private val entityManager: EntityManager,
    private val queryFactory: JPAQueryFactory,
) : UserQuerydslRepository {
    private val userEntity = QUserEntity.userEntity

    override fun updateLocationById(userId: Long, lat: Double, lng: Double): Long {
        val count = queryFactory.update(userEntity)
            .set(userEntity.location, PointConverter.from(lat = lat, lng = lng))
            .where(userEntity.id.eq(userId))
            .execute()
        entityManager.flush()
        entityManager.clear()
        return count
    }

    override fun updateStatusById(userId: Long, status: UserStatus): Long {
        val count = queryFactory.update(userEntity)
            .set(userEntity.status, status)
            .where(userEntity.id.eq(userId))
            .execute()
        entityManager.flush()
        entityManager.clear()
        return count
    }
}
