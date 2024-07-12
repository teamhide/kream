package com.teamhide.kream.coupon.ui.api.dto

import com.teamhide.kream.coupon.domain.usecase.CouponGroupDto

data class GetAllCouponResponse(
    val data: List<CouponGroupDto>
)
