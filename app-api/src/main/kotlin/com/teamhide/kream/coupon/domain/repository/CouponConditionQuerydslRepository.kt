package com.teamhide.kream.coupon.domain.repository

import com.teamhide.kream.coupon.domain.model.CouponCondition

interface CouponConditionQuerydslRepository {
    fun findAllByCouponGroupIds(couponGroupIds: List<Long>): List<CouponCondition>
}
