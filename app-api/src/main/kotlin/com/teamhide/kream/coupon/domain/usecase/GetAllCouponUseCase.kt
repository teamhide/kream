package com.teamhide.kream.coupon.domain.usecase

import com.teamhide.kream.coupon.domain.vo.CouponDiscountType
import com.teamhide.kream.coupon.domain.vo.CouponPeriodType

data class GetAllCouponQuery(val pageSize: Int, val offset: Int)

data class CouponGroupDto(
    val identifier: String,
    val discountType: CouponDiscountType,
    val discountValue: Int,
    val quantity: Int,
    val remainQuantity: Int,
    val periodType: CouponPeriodType,
    val period: Int,
)

interface GetAllCouponUseCase {
    fun execute(query: GetAllCouponQuery): List<CouponGroupDto>
}
