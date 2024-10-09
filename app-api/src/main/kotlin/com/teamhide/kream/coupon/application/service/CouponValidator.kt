package com.teamhide.kream.coupon.application.service

import com.teamhide.kream.coupon.domain.model.CouponCondition
import com.teamhide.kream.coupon.domain.model.CouponGroup
import com.teamhide.kream.coupon.domain.usecase.ConditionContext
import org.springframework.stereotype.Component

@Component
class CouponValidator(
    private val couponConditionStrategyFactory: CouponConditionStrategyFactory,
) {
    fun isCouponGroupValid(
        couponGroup: CouponGroup,
        context: ConditionContext,
        couponGroupIdToConditionsMap: Map<Long, List<CouponCondition>>,
    ): Boolean {
        val conditions = couponGroupIdToConditionsMap.getOrDefault(couponGroup.id, emptyList())
        return conditions.all { condition -> isConditionSatisfied(condition, context) }
    }

    private fun isConditionSatisfied(condition: CouponCondition, context: ConditionContext): Boolean {
        val strategy = couponConditionStrategyFactory.getStrategy(conditionType = condition.conditionType)
        return strategy?.isSatisfied(condition = condition, context = context) ?: true
    }
}
