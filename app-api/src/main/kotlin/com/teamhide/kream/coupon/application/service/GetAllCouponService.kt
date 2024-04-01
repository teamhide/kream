package com.teamhide.kream.coupon.application.service

import com.teamhide.kream.coupon.domain.repository.CouponRepositoryAdapter
import com.teamhide.kream.coupon.domain.usecase.CouponGroupDto
import com.teamhide.kream.coupon.domain.usecase.GetAllCouponQuery
import com.teamhide.kream.coupon.domain.usecase.GetAllCouponUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetAllCouponService(
    private val couponRepositoryAdapter: CouponRepositoryAdapter,
) : GetAllCouponUseCase {
    override fun execute(query: GetAllCouponQuery): List<CouponGroupDto> {
        val coupons = couponRepositoryAdapter
            .findAllCouponGroupBy(pageSize = query.pageSize, offset = query.offset)
        return coupons.filter {
            it.remainQuantity > 0 && it.isAvailable()
        }.map {
            CouponGroupDto(
                identifier = it.identifier,
                discountType = it.discountInfo.discountType,
                discountValue = it.discountInfo.discountValue,
                quantity = it.quantity,
                remainQuantity = it.remainQuantity,
                periodType = it.periodType,
                period = it.period,
            )
        }
    }
}
