package com.teamhide.kream.coupon.ui.api.dto

import com.teamhide.kream.coupon.domain.usecase.RegisterCouponDto

data class RegisterCouponResponse(val identifier: String, val quantity: Int) {
    companion object {
        fun from(coupon: RegisterCouponDto): RegisterCouponResponse {
            return RegisterCouponResponse(
                identifier = coupon.identifier,
                quantity = coupon.quantity,
            )
        }
    }
}
