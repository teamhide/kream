package com.teamhide.kream.coupon.application.service

import com.teamhide.kream.coupon.domain.model.CouponGroup
import com.teamhide.kream.coupon.domain.repository.CouponRepositoryAdapter
import com.teamhide.kream.coupon.domain.usecase.RegisterCouponCommand
import com.teamhide.kream.coupon.domain.usecase.RegisterCouponDto
import com.teamhide.kream.coupon.domain.usecase.RegisterCouponUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class RegisterCouponService(
    private val couponRepositoryAdapter: CouponRepositoryAdapter,
) : RegisterCouponUseCase {
    override fun execute(command: RegisterCouponCommand): RegisterCouponDto {
        val couponGroup = CouponGroup.create(
            identifier = makeIdentifier(),
            discountType = command.discountType,
            discountValue = command.discountValue,
            quantity = command.quantity,
            periodType = command.periodType,
            period = command.period,
        )
        couponRepositoryAdapter.saveCouponGroup(couponGroup = couponGroup)
        return RegisterCouponDto(identifier = couponGroup.identifier, quantity = couponGroup.quantity)
    }

    private fun makeIdentifier(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }
}
