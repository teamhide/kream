package com.teamhide.kream.coupon.application.service

import com.teamhide.kream.coupon.domain.repository.CouponConditionRepository
import com.teamhide.kream.coupon.domain.repository.CouponGroupRepository
import com.teamhide.kream.coupon.domain.usecase.GetAllCouponQuery
import com.teamhide.kream.coupon.domain.vo.CouponGroupStatus
import com.teamhide.kream.coupon.makeCouponCondition
import com.teamhide.kream.coupon.makeCouponGroup
import com.teamhide.kream.support.test.IntegrationTest
import com.teamhide.kream.support.test.MysqlDbCleaner
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

@IntegrationTest
class GetAllCouponServiceTest(
    private val couponGroupRepository: CouponGroupRepository,
    private val getAllCouponService: GetAllCouponService,
    private val couponConditionRepository: CouponConditionRepository,
) : BehaviorSpec({
    listeners(MysqlDbCleaner())

    Given("GetAllCouponService") {
        When("쿠폰 그룹 리스트를 요청하면") {
            val couponGroup1 = makeCouponGroup(
                id = 1L, identifier = "first", remainQuantity = 0, status = CouponGroupStatus.ACTIVATED
            )
            val couponGroup2 = makeCouponGroup(
                id = 2L, identifier = "second", remainQuantity = 10, status = CouponGroupStatus.ACTIVATED
            )
            val couponGroup3 = makeCouponGroup(
                id = 3L, identifier = "third", remainQuantity = 10, status = CouponGroupStatus.DEACTIVATED
            )
            val couponGroup4 = makeCouponGroup(
                id = 4L, identifier = "fourth", remainQuantity = 10, status = CouponGroupStatus.ACTIVATED
            )
            couponGroupRepository.saveAll(listOf(couponGroup1, couponGroup2, couponGroup3, couponGroup4))

            val condition1 = makeCouponCondition(couponGroup = couponGroup2)
            val condition2 = makeCouponCondition(couponGroup = couponGroup4)
            couponConditionRepository.saveAll(listOf(condition1, condition2))

            val query = GetAllCouponQuery(pageSize = 10, offset = 0)
            val result = getAllCouponService.execute(query = query)

            Then("유효한 쿠폰만 리턴된다") {
                result.size shouldBe 2
                result[0].identifier shouldBe couponGroup2.identifier
                result[0].remainQuantity shouldBe couponGroup2.remainQuantity
                result[1].identifier shouldBe couponGroup4.identifier
                result[1].remainQuantity shouldBe couponGroup4.remainQuantity
            }
        }
    }
})
