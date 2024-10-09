package com.teamhide.kream.coupon.domain.repository

import com.teamhide.kream.coupon.domain.model.CouponGroup
import com.teamhide.kream.coupon.domain.vo.CouponGroupStatus
import com.teamhide.kream.coupon.makeCoupon
import com.teamhide.kream.coupon.makeCouponCondition
import com.teamhide.kream.coupon.makeCouponGroup
import com.teamhide.kream.coupon.makeCouponHistory
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

internal class CouponRepositoryAdapterTest : StringSpec({
    val couponRepository = mockk<CouponRepository>()
    val couponGroupRepository = mockk<CouponGroupRepository>()
    val couponHistoryRepository = mockk<CouponHistoryRepository>()
    val couponConditionRepository = mockk<CouponConditionRepository>()
    val couponRepositoryAdapter = CouponRepositoryAdapter(
        couponGroupRepository = couponGroupRepository,
        couponRepository = couponRepository,
        couponHistoryRepository = couponHistoryRepository,
        couponConditionRepository = couponConditionRepository,
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

    "모든 쿠폰그룹을 조회한다" {
        // Given
        val pageSize = 10
        val offset = 0
        val couponGroup1 = makeCouponGroup(id = 1L, remainQuantity = 0, status = CouponGroupStatus.ACTIVATED)
        val couponGroup2 = makeCouponGroup(id = 2L, remainQuantity = 10, status = CouponGroupStatus.ACTIVATED)
        val couponGroup3 = makeCouponGroup(id = 3L, remainQuantity = 10, status = CouponGroupStatus.DEACTIVATED)
        val couponGroups = mutableListOf(couponGroup1, couponGroup2, couponGroup3)
        val page = mockk<Page<CouponGroup>>()
        every { page.toList() } returns couponGroups
        every { couponGroupRepository.findAll(any<Pageable>()) } returns page

        // When
        val result = couponRepositoryAdapter.findAllCouponGroupBy(pageSize = pageSize, offset = offset)

        // Then
        result.size shouldBe 3

        result[0].id shouldBe couponGroup1.id
        result[0].remainQuantity shouldBe couponGroup1.remainQuantity
        result[0].status shouldBe couponGroup1.status

        result[1].id shouldBe couponGroup2.id
        result[1].remainQuantity shouldBe couponGroup2.remainQuantity
        result[1].status shouldBe couponGroup2.status

        result[2].id shouldBe couponGroup3.id
        result[2].remainQuantity shouldBe couponGroup3.remainQuantity
        result[2].status shouldBe couponGroup3.status
    }

    "쿠폰 그룹ID 목록으로 모든 CouponCondition을 조회한다" {
        // Given
        val couponGroupId = 1L
        val condition1 = makeCouponCondition(id = 1L)
        val condition2 = makeCouponCondition(id = 2L)
        every { couponConditionRepository.findAllByCouponGroupIds(any()) } returns listOf(condition1, condition2)

        // When
        val sut = couponRepositoryAdapter.findAllConditionByCouponGroupIds(couponGroupIds = listOf(couponGroupId))

        // Then
        sut.size shouldBe 2
        sut[0].id shouldBe condition1.id
        sut[1].id shouldBe condition2.id
    }
})
