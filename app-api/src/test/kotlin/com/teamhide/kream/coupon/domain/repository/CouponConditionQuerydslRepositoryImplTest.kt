package com.teamhide.kream.coupon.domain.repository

import com.teamhide.kream.coupon.makeCouponCondition
import com.teamhide.kream.coupon.makeCouponGroup
import com.teamhide.kream.support.test.JpaRepositoryTest
import com.teamhide.kream.support.test.MysqlDbCleaner
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

@JpaRepositoryTest
class CouponConditionQuerydslRepositoryImplTest(
    private val couponGroupRepository: CouponGroupRepository,
    private val couponConditionRepository: CouponConditionRepository,
) : BehaviorSpec({
    listeners(MysqlDbCleaner())

    Given("findAllByCouponGroupIds") {
        When("CouponGroup ID목록으로 데이터를 조회하면") {
            val couponGroup1 = couponGroupRepository.save(makeCouponGroup())
            val couponGroup2 = couponGroupRepository.save(makeCouponGroup())
            val couponCondition1 = couponConditionRepository.save(makeCouponCondition(couponGroup = couponGroup1))
            val couponCondition2 = couponConditionRepository.save(makeCouponCondition(couponGroup = couponGroup2))
            val couponGroupIds = listOf(couponGroup1.id, couponGroup2.id)

            val result = couponConditionRepository.findAllByCouponGroupIds(couponGroupIds = couponGroupIds)

            Then("CouponCondition 목록이 리턴된다") {
                result.size shouldBe 2
                result[0].id shouldBe couponCondition1.id
                result[1].id shouldBe couponCondition2.id
            }
        }
    }
})
