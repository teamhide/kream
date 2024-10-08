package com.teamhide.kream.coupon.application.service

import com.teamhide.kream.coupon.domain.model.ConditionType
import com.teamhide.kream.coupon.domain.usecase.CouponConditionStrategy
import org.springframework.stereotype.Component

@Component
class CouponConditionStrategyFactory(
    private val strategies: List<CouponConditionStrategy>,
) {
    fun getStrategy(conditionType: ConditionType): CouponConditionStrategy? {
        return strategies.firstOrNull { it.isSupport(conditionType = conditionType) }
    }
}
