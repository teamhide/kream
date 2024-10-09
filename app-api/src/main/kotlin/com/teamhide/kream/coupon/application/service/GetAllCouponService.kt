package com.teamhide.kream.coupon.application.service

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
    private val couponValidator: CouponValidator,
) : GetAllCouponUseCase {
    override fun execute(query: GetAllCouponQuery): List<CouponGroupDto> {
        val couponGroups = couponRepositoryAdapter
            .findAllCouponGroupBy(pageSize = query.pageSize, offset = query.offset)
        val couponGroupIds = couponGroups.map { it.id }

        val context = ConditionContext(userId = 1L, isFirstDownload = true)
        val allConditions = couponRepositoryAdapter.findAllConditionByCouponGroupIds(couponGroupIds = couponGroupIds)
        val couponGroupIdToConditionsMap = allConditions.groupBy { it.couponGroup.id }

        return couponGroups
            .applyDefaultFilter()
            .filter { couponGroup ->
                couponValidator.isCouponGroupValid(
                    couponGroup = couponGroup,
                    context = context,
                    couponGroupIdToConditionsMap = couponGroupIdToConditionsMap,
                )
            }
            .map { CouponGroupDto.from(couponGroup = it) }
    }
}
