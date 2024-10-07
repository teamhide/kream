package com.teamhide.kream.coupon.domain.repository

import com.teamhide.kream.coupon.domain.model.CouponCondition
import org.springframework.data.jpa.repository.JpaRepository

interface CouponConditionRepository : JpaRepository<CouponCondition, Long> {
    fun findAllByCouponGroupId(couponGroupId: Long): List<CouponCondition>
}
