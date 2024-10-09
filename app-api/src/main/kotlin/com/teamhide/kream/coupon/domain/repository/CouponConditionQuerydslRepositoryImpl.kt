package com.teamhide.kream.coupon.domain.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.teamhide.kream.coupon.domain.model.CouponCondition
import com.teamhide.kream.coupon.domain.model.QCouponCondition.couponCondition

class CouponConditionQuerydslRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : CouponConditionQuerydslRepository {
    override fun findAllByCouponGroupIds(couponGroupIds: List<Long>): List<CouponCondition> {
        return queryFactory
            .selectFrom(couponCondition)
            .where(couponCondition.id.`in`(couponGroupIds))
            .fetch()
    }
}
