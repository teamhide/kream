package com.teamhide.kream.coupon.domain.usecase

import com.teamhide.kream.coupon.domain.model.ConditionType
import com.teamhide.kream.coupon.domain.model.CouponCondition

data class ConditionContext(
    val userId: Long,
    val isFirstDownload: Boolean,
)

interface CouponConditionStrategy {
    fun isSatisfied(condition: CouponCondition, context: ConditionContext): Boolean
    fun isSupport(conditionType: ConditionType): Boolean
}
