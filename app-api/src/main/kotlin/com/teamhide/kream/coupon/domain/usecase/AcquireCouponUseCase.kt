package com.teamhide.kream.coupon.domain.usecase

data class AcquireCouponCommand(val userId: Long, val identifier: String)

interface AcquireCouponUseCase {
    fun execute(command: AcquireCouponCommand)
}
