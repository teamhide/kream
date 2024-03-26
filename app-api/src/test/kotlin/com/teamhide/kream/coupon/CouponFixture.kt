package com.teamhide.kream.coupon

import com.teamhide.kream.coupon.domain.model.Coupon
import com.teamhide.kream.coupon.domain.model.CouponGroup
import com.teamhide.kream.coupon.domain.model.CouponHistory
import com.teamhide.kream.coupon.domain.usecase.AcquireCouponCommand
import com.teamhide.kream.coupon.domain.vo.CouponDiscountInfo
import com.teamhide.kream.coupon.domain.vo.CouponDiscountType
import com.teamhide.kream.coupon.domain.vo.CouponGroupStatus
import com.teamhide.kream.coupon.domain.vo.CouponPeriodType
import com.teamhide.kream.coupon.domain.vo.CouponStatus
import java.time.LocalDateTime

fun makeCouponGroup(
    id: Long = 1L,
    identifier: String = "identifier",
    discountType: CouponDiscountType = CouponDiscountType.AMOUNT,
    discountValue: Int = 1000,
    status: CouponGroupStatus = CouponGroupStatus.ACTIVATED,
    quantity: Int = 100,
    remainQuantity: Int = 100,
    periodType: CouponPeriodType = CouponPeriodType.DAY,
    period: Int = 30,
): CouponGroup {
    return CouponGroup(
        id = id,
        identifier = identifier,
        discountInfo = CouponDiscountInfo(discountType = discountType, discountValue = discountValue),
        status = status,
        quantity = quantity,
        periodType = periodType,
        period = period,
        remainQuantity = remainQuantity,
    )
}

fun makeCoupon(
    id: Long = 1L,
    couponGroup: CouponGroup = makeCouponGroup(),
    userId: Long = 1L,
    status: CouponStatus = CouponStatus.ISSUED,
    expiredAt: LocalDateTime = LocalDateTime.now().plusDays(30)
): Coupon {
    return Coupon(
        id = id,
        couponGroup = couponGroup,
        userId = userId,
        status = status,
        expiredAt = expiredAt,
    )
}

fun makeAcquireCouponCommand(
    identifier: String = "identifier",
    userId: Long = 1L,
): AcquireCouponCommand {
    return AcquireCouponCommand(
        identifier = identifier,
        userId = userId,
    )
}

fun makeCouponHistory(
    id: Long = 1L,
    userId: Long = 1L,
    coupon: Coupon = makeCoupon(),
    status: CouponStatus = CouponStatus.ISSUED,
): CouponHistory {
    return CouponHistory(
        id = id,
        userId = userId,
        coupon = coupon,
        status = status,
    )
}
