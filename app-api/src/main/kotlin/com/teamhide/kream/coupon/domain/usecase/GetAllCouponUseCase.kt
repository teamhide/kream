package com.teamhide.kream.coupon.domain.usecase

import com.teamhide.kream.coupon.domain.model.CouponGroup
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
) {
    companion object {
        fun from(couponGroup: CouponGroup): CouponGroupDto {
            return CouponGroupDto(
                identifier = couponGroup.identifier,
                discountType = couponGroup.discountInfo.discountType,
                discountValue = couponGroup.discountInfo.discountValue,
                quantity = couponGroup.quantity,
                remainQuantity = couponGroup.remainQuantity,
                periodType = couponGroup.periodType,
                period = couponGroup.period,
            )
        }
    }
}

interface GetAllCouponUseCase {
    fun execute(query: GetAllCouponQuery): List<CouponGroupDto>
}
