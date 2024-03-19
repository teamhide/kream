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

    Given("쿠폰을 획득하지 않은 유저ID로") {
        When("쿠폰 획득 요청을 하면") {
            val sut = couponRedisAdapter.increaseAndGetRemainCount(identifier = "uuid", userId = 3L)

            Then("현재 발급된 쿠폰 개수와 획득 여부를 리턴한다") {
                sut.remainCount shouldBe 0
                sut.isObtain shouldBe true
            }
        }
    }

    Given("쿠폰을 이미 획득한 유저 ID로") {
        val ops = redisTemplate.opsForSet()
        val identifier = "uuid"
        val key = couponRedisAdapter.makeKey(identifier = identifier)
        ops.add(key, "1")

        When("쿠폰 획득 요청을 하면") {
            val sut = couponRedisAdapter.increaseAndGetRemainCount(identifier = identifier, userId = 1L)

            Then("현재 발급된 개수와 획득 여부를 리턴한다") {
                sut.remainCount shouldBe 1
                sut.isObtain shouldBe false
            }
        }
    }

    Given("발급된 쿠폰 목록에서") {
        val ops = redisTemplate.opsForSet()
        val identifier = "uuid"
        val key = couponRedisAdapter.makeKey(identifier = identifier)
        ops.add(key, "1")

        When("유저 1의 기록을 삭제 요청하면") {
            couponRedisAdapter.removeUserFromIssuedCoupon(identifier = identifier, userId = 1L)

            Then("발급 목록에서 제외된다") {
                val members = ops.members(identifier)
                members shouldBe emptySet()
            }
        }
    }
})
