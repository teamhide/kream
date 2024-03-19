package com.teamhide.kream.coupon.domain.model

import com.teamhide.kream.coupon.domain.vo.CouponPeriodType
import com.teamhide.kream.coupon.makeCouponGroup
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class CouponTest : StringSpec({
    "periodType이 MONTH인 쿠폰 발급을 요청한다" {
        // Given
        val period = 2
        val couponGroup = makeCouponGroup(periodType = CouponPeriodType.MONTH, period = period)

        // When
        val sut = Coupon.issue(couponGroup = couponGroup, userId = 1L)

        // Then
        sut.expiredAt.minusMonths(period.toLong()).month shouldBe LocalDateTime.now().month
    }

    "periodType이 DAY인 쿠폰 발급을 요청한다" {
        // Given
        val period = 2
        val couponGroup = makeCouponGroup(periodType = CouponPeriodType.DAY, period = period)

        // When
        val sut = Coupon.issue(couponGroup = couponGroup, userId = 1L)

        // Then
        sut.expiredAt.minusDays(period.toLong()).dayOfMonth shouldBe LocalDateTime.now().dayOfMonth
    }
})
