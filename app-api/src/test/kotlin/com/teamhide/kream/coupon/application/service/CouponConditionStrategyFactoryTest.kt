package com.teamhide.kream.coupon.application.service

import com.teamhide.kream.coupon.domain.model.ConditionType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.spyk

class CouponConditionStrategyFactoryTest : BehaviorSpec({
    val onlyForSpecificUsersStrategy = spyk<OnlyForSpecificUsersStrategy>()
    val firstDownloadStrategy = spyk<FirstDownloadStrategy>()
    val couponConditionStrategyFactory = CouponConditionStrategyFactory(
        strategies = listOf(onlyForSpecificUsersStrategy, firstDownloadStrategy),
    )

    Given("get") {
        When("ConditionType이 FIRST_DOWNLOAD인 경우") {
            val conditionType = ConditionType.FIRST_DOWNLOAD
            val sut = couponConditionStrategyFactory.getStrategy(conditionType = conditionType)

            Then("FirstDownloadStrategy를 리턴한다") {
                sut.shouldBeInstanceOf<FirstDownloadStrategy>()
            }
        }

        When("ConditionType이 ONLY_FOR_SPECIFIC_USERS인 경우") {
            val conditionType = ConditionType.ONLY_FOR_SPECIFIC_USERS
            val sut = couponConditionStrategyFactory.getStrategy(conditionType = conditionType)

            Then("OnlyForSpecificUsersStrategy를 리턴한다") {
                sut.shouldBeInstanceOf<OnlyForSpecificUsersStrategy>()
            }
        }
    }
})
