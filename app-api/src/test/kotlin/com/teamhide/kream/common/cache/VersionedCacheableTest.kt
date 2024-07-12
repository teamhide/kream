package com.teamhide.kream.common.cache

import com.teamhide.kream.support.test.IntegrationTest
import com.teamhide.kream.support.test.RedisCleaner
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

private const val RETURN_VALUE = "return"
private const val CACHE_KEY = "hide"

@Service
class VersionedCacheTestService {
    @VersionedCacheable(value = [CACHE_KEY], key = "#userId + ':' + #name")
    fun run(userId: Long, name: String): String {
        return RETURN_VALUE
    }
}

@IntegrationTest
class VersionedCacheableTest(
    private val versionedCacheTestService: VersionedCacheTestService,
    private val redisTemplate: RedisTemplate<String, String>,
    @Value("\${server.deploy-version}") private val deployVersion: String,
) : BehaviorSpec({
    listeners(RedisCleaner())

    Given("메소드에 VersionedCacheable을 적용했을 때") {
        val userId = 1L
        val name = "hide"
        val ops = redisTemplate.opsForValue()

        When("메소드를 실행하면") {
            val sut = versionedCacheTestService.run(userId = userId, name = name)

            Then("결과가 반환된다") {
                sut shouldBe RETURN_VALUE
            }

            Then("레디스에 캐싱된다") {
                val key = "$CACHE_KEY::$userId:$name:$deployVersion"
                val cacheValue = ops.get(key)
                cacheValue.shouldNotBeNull()
                cacheValue shouldBe "\"$RETURN_VALUE\""
            }
        }
    }
})
