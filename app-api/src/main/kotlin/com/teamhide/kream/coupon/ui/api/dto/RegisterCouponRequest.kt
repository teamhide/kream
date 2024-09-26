package com.teamhide.kream.coupon.ui.api.dto

import com.teamhide.kream.coupon.domain.usecase.RegisterCouponCommand
import com.teamhide.kream.coupon.domain.vo.CouponDiscountType
import com.teamhide.kream.coupon.domain.vo.CouponPeriodType
import org.jetbrains.annotations.NotNull

data class RegisterCouponRequest(
    @field:NotNull
    val discountType: CouponDiscountType,

    @field:NotNull
    val discountValue: Int,

    @field:NotNull
    val quantity: Int,

    @field:NotNull
    val periodType: CouponPeriodType,

    @field:NotNull
    val period: Int,
) {
    fun toCommand(): RegisterCouponCommand {
        return RegisterCouponCommand(
            discountType = this.discountType,
            discountValue = this.discountValue,
            quantity = this.quantity,
            periodType = this.periodType,
            period = this.period,
        )
    }
}
