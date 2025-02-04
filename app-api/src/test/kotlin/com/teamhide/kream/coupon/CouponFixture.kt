package com.teamhide.kream.coupon

import com.teamhide.kream.coupon.domain.model.ConditionType
import com.teamhide.kream.coupon.domain.model.ConditionValueType
import com.teamhide.kream.coupon.domain.model.Coupon
import com.teamhide.kream.coupon.domain.model.CouponCondition
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
    id: Long = 0L,
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
    id: Long = 0L,
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
    userId: Long = 1L,
    coupon: Coupon = makeCoupon(),
): CouponHistory {
    return CouponHistory.issued(
        userId = userId,
        coupon = coupon,
    )
}

fun makeCouponCondition(
    id: Long = 0L,
    couponGroup: CouponGroup = makeCouponGroup(),
    conditionType: ConditionType = ConditionType.FIRST_DOWNLOAD,
    conditionValue: String = "true",
    conditionValueType: ConditionValueType = ConditionValueType.BOOLEAN,
): CouponCondition {
    return CouponCondition(
        id = id,
        couponGroup = couponGroup,
        conditionType = conditionType,
        conditionValue = conditionValue,
        conditionValueType = conditionValueType,
    )
}
