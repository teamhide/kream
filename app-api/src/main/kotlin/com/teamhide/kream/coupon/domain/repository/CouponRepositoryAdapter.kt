package com.teamhide.kream.coupon.domain.repository

import com.teamhide.kream.coupon.domain.model.Coupon
import com.teamhide.kream.coupon.domain.model.CouponGroup
import org.springframework.stereotype.Component

@Component
class CouponRepositoryAdapter(
    private val couponGroupRepository: CouponGroupRepository,
    private val couponRepository: CouponRepository,
) {
    fun findCouponGroupByIdentifier(identifier: String): CouponGroup? = couponGroupRepository.findByIdentifier(identifier = identifier)

    fun saveCoupon(coupon: Coupon): Coupon = couponRepository.save(coupon)

    fun saveCouponGroup(couponGroup: CouponGroup): CouponGroup = couponGroupRepository.save(couponGroup)
}
