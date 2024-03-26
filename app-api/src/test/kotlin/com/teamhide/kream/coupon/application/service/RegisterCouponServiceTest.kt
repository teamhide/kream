package com.teamhide.kream.coupon.application.service

import com.teamhide.kream.coupon.domain.repository.CouponGroupRepository
import com.teamhide.kream.coupon.domain.usecase.RegisterCouponCommand
import com.teamhide.kream.coupon.domain.vo.CouponDiscountType
import com.teamhide.kream.coupon.domain.vo.CouponGroupStatus
import com.teamhide.kream.coupon.domain.vo.CouponPeriodType
import com.teamhide.kream.support.test.IntegrationTest
import com.teamhide.kream.support.test.MysqlDbCleaner
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

@IntegrationTest
internal class RegisterCouponServiceTest(
    private val couponGroupRepository: CouponGroupRepository,
    private val registerCouponService: RegisterCouponService,
) : BehaviorSpec({
    listeners(MysqlDbCleaner())

    Given("특정 기간 동안 유효한") {
        val command = RegisterCouponCommand(
            discountType = CouponDiscountType.AMOUNT,
            discountValue = 10000,
            quantity = 100,
            periodType = CouponPeriodType.DAY,
            period = 30,
        )

        When("CouponGroup 생성을 요청하면") {
            val sut = registerCouponService.execute(command = command)

            Then("CouponGroup이 생성되었다") {
                val couponGroup = couponGroupRepository.findAll()[0]
                couponGroup.shouldNotBeNull()
                couponGroup.discountInfo.discountType shouldBe command.discountType
                couponGroup.discountInfo.discountValue shouldBe command.discountValue
                couponGroup.identifier.length shouldBe 32
                couponGroup.status shouldBe CouponGroupStatus.ACTIVATED
                couponGroup.quantity shouldBe command.quantity
                couponGroup.periodType shouldBe command.periodType
                couponGroup.period shouldBe command.period
            }

            Then("생성 결과를 리턴한다") {
                sut.identifier.length shouldBe 32
                sut.quantity shouldBe command.quantity
            }
        }
    }
})
