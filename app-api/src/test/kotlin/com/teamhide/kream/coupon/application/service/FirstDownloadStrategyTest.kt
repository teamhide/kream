package com.teamhide.kream.coupon.application.service

import com.teamhide.kream.coupon.domain.model.ConditionType
import com.teamhide.kream.coupon.domain.model.ConditionValueType
import com.teamhide.kream.coupon.domain.model.CouponCondition
import com.teamhide.kream.coupon.domain.usecase.ConditionContext
import com.teamhide.kream.coupon.makeCouponGroup
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class FirstDownloadStrategyTest : BehaviorSpec({
    val firstDownloadStrategy = FirstDownloadStrategy()

    Given("isSatisfied") {
        When("context의 값이 condition에 부합하면") {
            val couponGroup = makeCouponGroup()
            val condition = CouponCondition(
                couponGroup = couponGroup,
                conditionType = ConditionType.FIRST_DOWNLOAD,
                conditionValue = "true",
                conditionValueType = ConditionValueType.BOOLEAN,
            )
            val context = ConditionContext(userId = 1L, isFirstDownload = true)
            val sut = firstDownloadStrategy.isSatisfied(condition = condition, context = context)

            Then("true를 반환한다") {
                sut shouldBe true
            }
        }
    }

    Given("isSupport") {
        When("conditionType이 FIRST_DOWNLOAD라면") {
            val sut = firstDownloadStrategy.isSupport(conditionType = ConditionType.FIRST_DOWNLOAD)

            Then("true를 반환한다") {
                sut shouldBe true
            }
        }
    }
})
