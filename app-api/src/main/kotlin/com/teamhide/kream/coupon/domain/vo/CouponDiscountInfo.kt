package com.teamhide.kream.coupon.domain.vo

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
class CouponDiscountInfo(
    @Column(name = "discount_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    val discountType: CouponDiscountType,

    @Column(name = "discount_value", nullable = false)
    val discountValue: Int,
)
