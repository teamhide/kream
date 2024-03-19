package com.teamhide.kream.coupon.domain.repository

import com.teamhide.kream.coupon.domain.model.CouponGroup
import org.springframework.data.jpa.repository.JpaRepository

interface CouponGroupRepository : JpaRepository<CouponGroup, Long> {
    fun findByIdentifier(identifier: String): CouponGroup?
}
