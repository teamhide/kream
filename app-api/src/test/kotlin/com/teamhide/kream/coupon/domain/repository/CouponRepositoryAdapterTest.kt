package com.teamhide.kream.coupon.domain.repository

import com.teamhide.kream.coupon.makeCoupon
import com.teamhide.kream.coupon.makeCouponGroup
import com.teamhide.kream.coupon.makeCouponHistory
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

internal class CouponRepositoryAdapterTest : StringSpec({
    val couponRepository = mockk<CouponRepository>()
    val couponGroupRepository = mockk<CouponGroupRepository>()
    val couponHistoryRepository = mockk<CouponHistoryRepository>()
    val couponRepositoryAdapter = CouponRepositoryAdapter(
        couponGroupRepository = couponGroupRepository,
        couponRepository = couponRepository,
        couponHistoryRepository = couponHistoryRepository,
    )

    "identifier로 CouponGroup을 조회한다" {
        // Given
        val couponGroup = makeCouponGroup()
        every { couponGroupRepository.findByIdentifier(any()) } returns couponGroup

        // When
        val sut = couponRepositoryAdapter.findCouponGroupByIdentifier(identifier = couponGroup.identifier)

        // Then
        sut.shouldNotBeNull()
        sut.id shouldBe couponGroup.id
        sut.identifier shouldBe couponGroup.identifier
        sut.status shouldBe couponGroup.status
        sut.discountInfo shouldBe couponGroup.discountInfo
        sut.quantity shl couponGroup.quantity
        sut.periodType shouldBe couponGroup.periodType
        sut.period shouldBe couponGroup.period
    }

    "Coupon을 저장한다" {
        // Given
        val coupon = makeCoupon()
        every { couponRepository.save(any()) } returns coupon

        // When
        val sut = couponRepositoryAdapter.saveCoupon(coupon = coupon)

        // Then
        sut.id shouldBe coupon.id
        sut.couponGroup shouldBe coupon.couponGroup
        sut.userId shouldBe coupon.userId
        sut.status shouldBe coupon.status
        sut.expiredAt shouldBe coupon.expiredAt
    }

    "CouponGroup을 저장한다" {
        // Given
        val couponGroup = makeCouponGroup()
        every { couponGroupRepository.save(any()) } returns couponGroup

        // When
        val sut = couponRepositoryAdapter.saveCouponGroup(couponGroup = couponGroup)

        // Then
        sut.id shouldBe couponGroup.id
        sut.identifier shouldBe couponGroup.identifier
        sut.discountInfo shouldBe couponGroup.discountInfo
        sut.status shouldBe couponGroup.status
        sut.quantity shouldBe couponGroup.quantity
        sut.remainQuantity shouldBe couponGroup.remainQuantity
        sut.periodType shouldBe couponGroup.periodType
        sut.period shouldBe couponGroup.period
    }

    "CouponHistory를 저장한다" {
        // Given
        val couponHistory = makeCouponHistory()
        every { couponHistoryRepository.save(any()) } returns couponHistory

        // When
        val sut = couponRepositoryAdapter.saveCouponHistory(couponHistory = couponHistory)

        // Then
        sut.id shouldBe couponHistory.id
        sut.userId shouldBe couponHistory.userId
        sut.coupon shouldBe couponHistory.coupon
        sut.status shouldBe couponHistory.status
    }
})
