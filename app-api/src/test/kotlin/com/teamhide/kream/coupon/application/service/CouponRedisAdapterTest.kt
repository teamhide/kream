package com.teamhide.kream.coupon.application.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate

@SpringBootTest
internal class CouponRedisAdapterTest(
    private val redisTemplate: RedisTemplate<String, String>,
    private val couponRedisAdapter: CouponRedisAdapter,
) : BehaviorSpec({
    beforeEach {
        redisTemplate.execute { connection -> connection.serverCommands().flushAll() }
    }

    Given("CouponRedisAdapter") {
        When("쿠폰을 획득하지 않은 유저라면") {
            val sut = couponRedisAdapter.increaseAndGetRemainCount(identifier = "uuid", userId = 3L)

            Then("현재 발급된 쿠폰 개수와 획득 여부를 리턴한다") {
                sut.remainCount shouldBe 0
                sut.isObtain shouldBe true
            }
        }

        When("쿠폰을 이미 획득하지 않은 유저라면") {
            val ops = redisTemplate.opsForSet()
            val identifier = "uuid"
            val key = couponRedisAdapter.makeKey(identifier = identifier)
            ops.add(key, "1")

            val sut = couponRedisAdapter.increaseAndGetRemainCount(identifier = identifier, userId = 1L)

            Then("현재 발급된 개수와 획득 여부를 리턴한다") {
                sut.remainCount shouldBe 1
                sut.isObtain shouldBe false
            }
        }

        When("발급된 쿠폰 목록에서 유저 1의 기록을 삭제 요청하면") {
            val ops = redisTemplate.opsForSet()
            val identifier = "uuid"
            val key = couponRedisAdapter.makeKey(identifier = identifier)
            ops.add(key, "1")

            couponRedisAdapter.removeUserFromIssuedCoupon(identifier = identifier, userId = 1L)

            Then("발급 목록에서 제외된다") {
                val members = ops.members(identifier)
                members shouldBe emptySet()
            }
        }
    }
})
