package com.teamhide.kream.coupon.application.service

import com.teamhide.kream.coupon.application.exception.AlreadyAcquireException
import com.teamhide.kream.coupon.application.exception.CouponOutOfStockException
import com.teamhide.kream.coupon.application.exception.InvalidIdentifierException
import com.teamhide.kream.coupon.application.exception.UnavailableCouponGroupException
import com.teamhide.kream.coupon.domain.model.Coupon
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

        val couponGroup = couponRepositoryAdapter
            .findCouponGroupByIdentifier(identifier = identifier) ?: throw InvalidIdentifierException()
        if (!couponGroup.isAvailable()) {
            throw UnavailableCouponGroupException()
        }

        val acquireResponse = couponRedisAdapter
            .increaseAndGetRemainCount(identifier = identifier, userId = userId)
        if (acquireResponse.remainCount >= couponGroup.quantity) {
            if (acquireResponse.isObtain) {
                couponRedisAdapter.removeUserFromIssuedCoupon(identifier = identifier, userId = userId)
            }
            throw CouponOutOfStockException()
        }
        if (!acquireResponse.isObtain) {
            throw AlreadyAcquireException()
        }

        val coupon = Coupon.issue(couponGroup = couponGroup, userId = command.userId)
        couponRepositoryAdapter.saveCoupon(coupon = coupon)

        val couponHistory = CouponHistory.issued(userId = userId, coupon = coupon)
        couponRepositoryAdapter.saveCouponHistory(couponHistory = couponHistory)

        couponGroup.decreaseRemainQuantity()
    }
}
