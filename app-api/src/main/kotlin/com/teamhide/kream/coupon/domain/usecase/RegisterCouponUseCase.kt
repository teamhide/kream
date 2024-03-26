package com.teamhide.kream.coupon.domain.usecase

import com.teamhide.kream.coupon.domain.vo.CouponDiscountType
import com.teamhide.kream.coupon.domain.vo.CouponPeriodType

data class RegisterCouponCommand(
    val discountType: CouponDiscountType,
    val discountValue: Int,
    val quantity: Int,
    val periodType: CouponPeriodType,
    val period: Int,
)

data class RegisterCouponDto(val identifier: String, val quantity: Int)

interface RegisterCouponUseCase {
    fun execute(command: RegisterCouponCommand): RegisterCouponDto
}
