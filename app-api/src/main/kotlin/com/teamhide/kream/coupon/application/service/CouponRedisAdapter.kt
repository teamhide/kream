package com.teamhide.kream.coupon.application.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

data class IncreaseAndGetRemainCountDto(val remainCount: Long, val isObtain: Boolean)

private const val COUPON_COUNT_KEY = "coupon_count"

@Component
class CouponRedisAdapter(
    private val redisTemplate: RedisTemplate<String, String>,
) {
    fun increaseAndGetRemainCount(identifier: String, userId: Long): IncreaseAndGetRemainCountDto {
        val key = makeKey(identifier = identifier).toByteArray()
        val targetUserId = userId.toString().toByteArray()

        val executeResult = redisTemplate.execute { operations ->
            operations.multi()
            operations.openPipeline()

            val setCommand = operations.setCommands()
            setCommand.sCard(key)
            setCommand.sAdd(key, targetUserId)

            operations.closePipeline()
            operations.exec()
        }
        return executeResult?.let {
            IncreaseAndGetRemainCountDto(
                remainCount = it[0] as? Long ?: 0L,
                isObtain = it.getOrNull(1) == 1L,
            )
        } ?: IncreaseAndGetRemainCountDto(remainCount = 0L, isObtain = false)
    }

    fun removeUserFromIssuedCoupon(identifier: String, userId: Long) {
        val ops = redisTemplate.opsForSet()
        val key = makeKey(identifier = identifier)
        ops.remove(key, userId.toString())
    }

    fun makeKey(identifier: String) = "$COUPON_COUNT_KEY:$identifier"
}
