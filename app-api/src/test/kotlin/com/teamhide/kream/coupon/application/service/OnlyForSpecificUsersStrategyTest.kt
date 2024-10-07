package com.teamhide.kream.coupon.application.service

import com.teamhide.kream.coupon.domain.model.ConditionType
import com.teamhide.kream.coupon.domain.model.ConditionValueType
import com.teamhide.kream.coupon.domain.model.CouponCondition
import com.teamhide.kream.coupon.domain.usecase.ConditionContext
import com.teamhide.kream.coupon.makeCoupon
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class OnlyForSpecificUsersStrategyTest : BehaviorSpec({
    val onlyForSpecificUsersStrategy = OnlyForSpecificUsersStrategy()

    Given("isSatisfied") {
        When("context의 값이 condition에 부합하면") {
            val userId = 1L
            val coupon = makeCoupon()
            val condition = CouponCondition(
                coupon = coupon,
                conditionType = ConditionType.ONLY_FOR_SPECIFIC_USERS,
                conditionValue = "[$userId,2,3]",
                conditionValueType = ConditionValueType.LIST_LONG,
            )
            val context = ConditionContext(userId = userId, isFirstDownload = true)
            val sut = onlyForSpecificUsersStrategy.isSatisfied(condition = condition, context = context)

            Then("true를 반환한다") {
                sut shouldBe true
            }
        }
    }

    Given("isSupport") {
        When("conditionType이 ONLY_FOR_SPECIFIC_USERS 라면") {
            val sut = onlyForSpecificUsersStrategy.isSupport(conditionType = ConditionType.ONLY_FOR_SPECIFIC_USERS)

            Then("true를 반환한다") {
                sut shouldBe true
            }
        }
    }
})
