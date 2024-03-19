package com.teamhide.kream.coupon.application.service

import com.teamhide.kream.coupon.application.exception.AlreadyAcquireException
import com.teamhide.kream.coupon.application.exception.CouponOutOfStockException
import com.teamhide.kream.coupon.application.exception.InvalidIdentifierException
import com.teamhide.kream.coupon.application.exception.UnavailableCouponGroupException
import com.teamhide.kream.coupon.domain.repository.CouponRepositoryAdapter
import com.teamhide.kream.coupon.domain.vo.CouponGroupStatus
import com.teamhide.kream.coupon.makeAcquireCouponCommand
import com.teamhide.kream.coupon.makeCoupon
import com.teamhide.kream.coupon.makeCouponGroup
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.assertThrows

internal class AcquireCouponServiceTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val couponRepositoryAdapter = mockk<CouponRepositoryAdapter>()
    val couponRedisAdapter = mockk<CouponRedisAdapter>()
    val acquireCouponService = AcquireCouponService(
        couponRepositoryAdapter = couponRepositoryAdapter,
        couponRedisAdapter = couponRedisAdapter,
    )

    Given("존재하지 않는 쿠폰 그룹 식별자로") {
        val command = makeAcquireCouponCommand()
        every { couponRepositoryAdapter.findCouponGroupByIdentifier(any()) } returns null

        When("쿠폰 획득을 요청하면") {
            Then("예외가 발생한다") {
                assertThrows<InvalidIdentifierException> { acquireCouponService.execute(command = command) }
            }
        }
    }

    Given("쿠폰 그룹의 상태가 ACTIVATED가 아닐 때") {
        val command = makeAcquireCouponCommand()
        val couponGroup = makeCouponGroup(status = CouponGroupStatus.DEACTIVATED)

        every { couponRepositoryAdapter.findCouponGroupByIdentifier(any()) } returns couponGroup

        When("쿠폰 획득을 요청하면") {
            Then("예외가 발생한다") {
                assertThrows<UnavailableCouponGroupException> { acquireCouponService.execute(command = command) }
            }
        }
    }

    Given("발급된 쿠폰 개수가 총 개수와 같은 경우") {
        val command = makeAcquireCouponCommand()
        val couponGroup = makeCouponGroup(status = CouponGroupStatus.ACTIVATED, quantity = 100)
        val responseDto = IncreaseAndGetRemainCountDto(remainCount = 100, isObtain = true)

        every { couponRepositoryAdapter.findCouponGroupByIdentifier(any()) } returns couponGroup
        every { couponRedisAdapter.increaseAndGetRemainCount(any(), any()) } returns responseDto
        every { couponRedisAdapter.removeUserFromIssuedCoupon(any(), any()) } returns Unit

        When("쿠폰 획득을 요청하면") {
            Then("획득한 쿠폰은 회수 처리하고 예외가 발생한다") {
                assertThrows<CouponOutOfStockException> { acquireCouponService.execute(command = command) }
                verify(exactly = 1) { couponRedisAdapter.removeUserFromIssuedCoupon(any(), any()) }
            }
        }
    }

    Given("발급된 쿠폰 개수가 총 개수보다 큰 경우") {
        val command = makeAcquireCouponCommand()
        val couponGroup = makeCouponGroup(status = CouponGroupStatus.ACTIVATED, quantity = 100)
        val responseDto = IncreaseAndGetRemainCountDto(remainCount = 101, isObtain = true)

        every { couponRepositoryAdapter.findCouponGroupByIdentifier(any()) } returns couponGroup
        every { couponRedisAdapter.increaseAndGetRemainCount(any(), any()) } returns responseDto
        every { couponRedisAdapter.removeUserFromIssuedCoupon(any(), any()) } returns Unit

        When("쿠폰 획득을 요청하면") {
            Then("획득한 쿠폰은 회수 처리하고 예외가 발생한다") {
                assertThrows<CouponOutOfStockException> { acquireCouponService.execute(command = command) }
                verify(exactly = 1) { couponRedisAdapter.removeUserFromIssuedCoupon(any(), any()) }
            }
        }
    }

    Given("발급된 쿠폰 개수가 총 개수와 같지만 획득에 실패한 경우") {
        val command = makeAcquireCouponCommand()
        val couponGroup = makeCouponGroup(status = CouponGroupStatus.ACTIVATED, quantity = 100)
        val responseDto = IncreaseAndGetRemainCountDto(remainCount = 100, isObtain = false)

        every { couponRepositoryAdapter.findCouponGroupByIdentifier(any()) } returns couponGroup
        every { couponRedisAdapter.increaseAndGetRemainCount(any(), any()) } returns responseDto
        every { couponRedisAdapter.removeUserFromIssuedCoupon(any(), any()) } returns Unit

        When("쿠폰 획득을 요청하면") {
            Then("획득한 쿠폰은 회수 처리하지 않고 예외가 발생한다") {
                assertThrows<CouponOutOfStockException> { acquireCouponService.execute(command = command) }
                verify(exactly = 0) { couponRedisAdapter.removeUserFromIssuedCoupon(any(), any()) }
            }
        }
    }

    Given("발급된 쿠폰 개수가 총 개수보다 작지만 획득에 실패한 경우") {
        val command = makeAcquireCouponCommand()
        val couponGroup = makeCouponGroup(status = CouponGroupStatus.ACTIVATED, quantity = 100)
        val responseDto = IncreaseAndGetRemainCountDto(remainCount = 50, isObtain = false)

        every { couponRepositoryAdapter.findCouponGroupByIdentifier(any()) } returns couponGroup
        every { couponRedisAdapter.increaseAndGetRemainCount(any(), any()) } returns responseDto
        every { couponRedisAdapter.removeUserFromIssuedCoupon(any(), any()) } returns Unit

        When("쿠폰 획득을 요청하면") {
            Then("이미 획득한 쿠폰이므로 예외가 발생한다") {
                assertThrows<AlreadyAcquireException> { acquireCouponService.execute(command = command) }
                verify(exactly = 0) { couponRepositoryAdapter.saveCoupon(any()) }
            }
        }
    }

    Given("발급된 쿠폰 개수가 총 개수보다 작고 획득에 성공한 경우") {
        val command = makeAcquireCouponCommand()
        val couponGroup = makeCouponGroup(status = CouponGroupStatus.ACTIVATED, quantity = 100)
        val responseDto = IncreaseAndGetRemainCountDto(remainCount = 50, isObtain = true)
        val coupon = makeCoupon()

        every { couponRepositoryAdapter.findCouponGroupByIdentifier(any()) } returns couponGroup
        every { couponRedisAdapter.increaseAndGetRemainCount(any(), any()) } returns responseDto
        every { couponRedisAdapter.removeUserFromIssuedCoupon(any(), any()) } returns Unit
        every { couponRepositoryAdapter.saveCoupon(any()) } returns coupon

        When("쿠폰 획득을 요청하면") {
            acquireCouponService.execute(command = command)

            Then("성공한다") {
                verify(exactly = 1) { couponRepositoryAdapter.saveCoupon(any()) }
            }
        }
    }
})
