package com.teamhide.kream.coupon.application.service

import com.teamhide.kream.coupon.domain.model.CouponCondition
import com.teamhide.kream.coupon.domain.model.CouponGroup
import com.teamhide.kream.coupon.domain.model.applyDefaultFilter
import com.teamhide.kream.coupon.domain.repository.CouponRepositoryAdapter
import com.teamhide.kream.coupon.domain.usecase.ConditionContext
import com.teamhide.kream.coupon.domain.usecase.CouponGroupDto
import com.teamhide.kream.coupon.domain.usecase.GetAllCouponQuery
import com.teamhide.kream.coupon.domain.usecase.GetAllCouponUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetAllCouponService(
    private val couponRepositoryAdapter: CouponRepositoryAdapter,
    private val couponConditionStrategyFactory: CouponConditionStrategyFactory,
) : GetAllCouponUseCase {
    override fun execute(query: GetAllCouponQuery): List<CouponGroupDto> {
        val coupons = couponRepositoryAdapter
            .findAllCouponGroupBy(pageSize = query.pageSize, offset = query.offset)

        val context = ConditionContext(userId = 1L, isFirstDownload = true)

        return coupons
            .applyDefaultFilter()
            .filter { couponGroup -> isCouponGroupValid(couponGroup, context) }
            .map { CouponGroupDto.from(couponGroup = it) }
    }

    private fun isCouponGroupValid(couponGroup: CouponGroup, context: ConditionContext): Boolean {
        val conditions = couponRepositoryAdapter.findAllConditionByCouponGroupId(couponGroupId = couponGroup.id)
        return conditions.all { condition -> isConditionSatisfied(condition, context) }
    }

    private fun isConditionSatisfied(condition: CouponCondition, context: ConditionContext): Boolean {
        val strategy = couponConditionStrategyFactory.getStrategy(conditionType = condition.conditionType)
        return strategy?.isSatisfied(condition = condition, context = context) ?: true
    }
}
