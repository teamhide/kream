package com.teamhide.kream.coupon.application.service

import com.teamhide.kream.coupon.domain.model.ConditionType
import com.teamhide.kream.coupon.domain.model.CouponCondition
import com.teamhide.kream.coupon.domain.usecase.ConditionContext
import com.teamhide.kream.coupon.domain.usecase.CouponConditionStrategy
import org.springframework.stereotype.Component

@Component
class OnlyForSpecificUsersStrategy : CouponConditionStrategy {
    override fun isSatisfied(condition: CouponCondition, context: ConditionContext): Boolean {
        val targetUsers = condition.getTypedValue<List<Long>>()
        return context.userId in targetUsers
    }

    override fun isSupport(conditionType: ConditionType): Boolean {
        return conditionType == ConditionType.ONLY_FOR_SPECIFIC_USERS
    }
}
