package com.teamhide.kream.coupon.application.service

import com.teamhide.kream.coupon.domain.model.ConditionType
import com.teamhide.kream.coupon.domain.model.CouponCondition
import com.teamhide.kream.coupon.domain.usecase.ConditionContext
import com.teamhide.kream.coupon.domain.usecase.CouponConditionStrategy
import org.springframework.stereotype.Component

@Component
class FirstDownloadStrategy : CouponConditionStrategy {
    override fun isSatisfied(condition: CouponCondition, context: ConditionContext): Boolean {
        return context.isFirstDownload == condition.getTypedValue<Boolean>()
    }

    override fun isSupport(conditionType: ConditionType): Boolean {
        return conditionType == ConditionType.FIRST_DOWNLOAD
    }
}
