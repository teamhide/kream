package com.teamhide.kream.coupon.application.service

import com.teamhide.kream.coupon.application.exception.AlreadyAcquireException
import com.teamhide.kream.coupon.application.exception.CouponOutOfStockException
import com.teamhide.kream.coupon.application.exception.InvalidIdentifierException
import com.teamhide.kream.coupon.application.exception.UnavailableCouponGroupException
import com.teamhide.kream.coupon.domain.model.Coupon
import com.teamhide.kream.coupon.domain.model.CouponGroup
import com.teamhide.kream.coupon.domain.model.CouponHistory
import com.teamhide.kream.coupon.domain.repository.CouponRepositoryAdapter
import com.teamhide.kream.coupon.domain.usecase.AcquireCouponCommand
import com.teamhide.kream.coupon.domain.usecase.AcquireCouponUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AcquireCouponService(
    private val couponRepositoryAdapter: CouponRepositoryAdapter,
    private val couponRedisAdapter: CouponRedisAdapter,
) : AcquireCouponUseCase {
    override fun execute(command: AcquireCouponCommand) {
        val identifier = command.identifier
        val userId = command.userId

        val couponGroup = getCouponGroup(identifier = identifier)
        tryAcquireCoupon(
            identifier = identifier,
            userId = userId,
            quantity = couponGroup.quantity,
        )
        saveCouponAcquisition(couponGroup = couponGroup, userId = userId)
    }

    private fun getCouponGroup(identifier: String): CouponGroup {
        val couponGroup = couponRepositoryAdapter.findCouponGroupByIdentifier(identifier)
            ?: throw InvalidIdentifierException()

        if (!couponGroup.isAvailable()) {
            throw UnavailableCouponGroupException()
        }

        return couponGroup
    }

    private fun tryAcquireCoupon(identifier: String, userId: Long, quantity: Int): IncreaseAndGetRemainCountDto {
        val acquireResponse = couponRedisAdapter
            .increaseAndGetRemainCount(identifier = identifier, userId = userId)

        if (acquireResponse.remainCount >= quantity) {
            if (acquireResponse.isObtain) {
                couponRedisAdapter.removeUserFromIssuedCoupon(identifier = identifier, userId = userId)
            }
            throw CouponOutOfStockException()
        }

        if (!acquireResponse.isObtain) {
            throw AlreadyAcquireException()
        }

        return acquireResponse
    }

    private fun saveCouponAcquisition(couponGroup: CouponGroup, userId: Long) {
        val coupon = Coupon.issue(couponGroup = couponGroup, userId = userId)
        couponRepositoryAdapter.saveCoupon(coupon)

        val couponHistory = CouponHistory.issued(userId = userId, coupon = coupon)
        couponRepositoryAdapter.saveCouponHistory(couponHistory)

        couponGroup.decreaseRemainQuantity()
    }
}
