package com.teamhide.kream.coupon.domain.repository

import com.teamhide.kream.coupon.makeCoupon
import com.teamhide.kream.coupon.makeCouponGroup
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

internal class CouponRepositoryAdapterTest : StringSpec({
    val couponRepository = mockk<CouponRepository>()
    val couponGroupRepository = mockk<CouponGroupRepository>()
    val couponRepositoryAdapter = CouponRepositoryAdapter(
        couponGroupRepository = couponGroupRepository,
        couponRepository = couponRepository,
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
        sut.shouldNotBeNull()
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
        sut.shouldNotBeNull()
        sut.id shouldBe couponGroup.id
        sut.identifier shouldBe couponGroup.identifier
        sut.discountInfo shouldBe couponGroup.discountInfo
        sut.status shouldBe couponGroup.status
        sut.quantity shouldBe couponGroup.quantity
        sut.remainQuantity shouldBe couponGroup.remainQuantity
        sut.periodType shouldBe couponGroup.periodType
        sut.period shouldBe couponGroup.period
    }
})
