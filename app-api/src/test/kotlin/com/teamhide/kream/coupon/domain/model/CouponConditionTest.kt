package com.teamhide.kream.coupon.domain.model

import com.teamhide.kream.coupon.makeCoupon
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class CouponConditionTest : BehaviorSpec({
    Given("생성 테스트") {
        When("ConditionValueType이 BOOLEAN일 때 ConditionValue가 Boolean타입이 아닌 경우") {
            val coupon = makeCoupon()

            Then("예외가 발생한다") {
                shouldThrow<InvalidConditionTypeException> {
                    CouponCondition(
                        coupon = coupon,
                        conditionType = ConditionType.FIRST_DOWNLOAD,
                        conditionValue = "abc",
                        conditionValueType = ConditionValueType.BOOLEAN,
                    )
                }
            }
        }

        When("ConditionValueType이 LIST_LONG 때 ConditionValue가 List<Int> 타입이 아닌 경우") {
            val coupon = makeCoupon()

            Then("예외가 발생한다") {
                shouldThrow<InvalidConditionTypeException> {
                    CouponCondition(
                        coupon = coupon,
                        conditionType = ConditionType.ONLY_FOR_SPECIFIC_USERS,
                        conditionValue = "abc",
                        conditionValueType = ConditionValueType.LIST_LONG,
                    )
                }
            }
        }

        When("ConditionValue가 ConditionValueType에 맞는 타입이라면") {
            val coupon = makeCoupon()

            Then("정상 생성된다") {
                CouponCondition(
                    coupon = coupon,
                    conditionType = ConditionType.FIRST_DOWNLOAD,
                    conditionValue = "true",
                    conditionValueType = ConditionValueType.BOOLEAN,
                )
            }
        }
    }

    Given("getTypedValue") {
        When("ConditionValue를 ConditionValueType에 맞게 변환요청하면") {
            val coupon = makeCoupon()

            val condition = CouponCondition(
                coupon = coupon,
                conditionType = ConditionType.FIRST_DOWNLOAD,
                conditionValue = "true",
                conditionValueType = ConditionValueType.BOOLEAN,
            )
            val sut = condition.getTypedValue<Boolean>()

            Then("변환된 값이 리턴된다") {
                sut shouldBe true
            }
        }

        When("LIST_LONG 타입을 변환하면") {
            val coupon = makeCoupon()

            val condition = CouponCondition(
                coupon = coupon,
                conditionType = ConditionType.ONLY_FOR_SPECIFIC_USERS,
                conditionValue = "[1,2,3]",
                conditionValueType = ConditionValueType.LIST_LONG,
            )
            val sut = condition.getTypedValue<List<Int>>()

            Then("변환된 값이 리턴된다") {
                sut shouldBe listOf(1, 2, 3)
            }
        }

        When("LIST_LONG 타입 변환 시 ConditionValue가 [ 로 시작하지 않는 경우") {
            Then("예외가 발생한다") {
                val coupon = makeCoupon()

                shouldThrow<InvalidConditionTypeException> {
                    CouponCondition(
                        coupon = coupon,
                        conditionType = ConditionType.ONLY_FOR_SPECIFIC_USERS,
                        conditionValue = "1,2,3]",
                        conditionValueType = ConditionValueType.LIST_LONG,
                    ).getTypedValue<List<Int>>()
                }
            }
        }

        When("LIST_LONG 타입 변환 시 ConditionValue가 ] 로 끝나지 않는 경우") {
            val coupon = makeCoupon()

            Then("예외가 발생한다") {
                shouldThrow<InvalidConditionTypeException> {
                    CouponCondition(
                        coupon = coupon,
                        conditionType = ConditionType.ONLY_FOR_SPECIFIC_USERS,
                        conditionValue = "[1,2,3",
                        conditionValueType = ConditionValueType.LIST_LONG,
                    ).getTypedValue<List<Int>>()
                }
            }
        }

        When("LIST_LONG 타입 변환 후 결과가 빈 리스트라면") {
            val coupon = makeCoupon()

            Then("예외가 발생한다") {
                shouldThrow<InvalidConditionTypeException> {
                    CouponCondition(
                        coupon = coupon,
                        conditionType = ConditionType.ONLY_FOR_SPECIFIC_USERS,
                        conditionValue = "[]",
                        conditionValueType = ConditionValueType.LIST_LONG,
                    ).getTypedValue<List<Int>>()
                }
            }
        }
    }
})
