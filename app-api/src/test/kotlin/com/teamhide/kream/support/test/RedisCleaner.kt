package com.teamhide.kream.support.test

import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.TestContextManager

@SpringBootTest
@TestEnvironment
class RedisCleaner : TestListener {
    lateinit var redisTemplate: RedisTemplate<String, String>

    @Suppress("UNCHECKED_CAST")
    override suspend fun afterContainer(testCase: TestCase, result: TestResult) {
        val testContextManager = TestContextManager(this::class.java)
        testContextManager.prepareTestInstance(this)
        redisTemplate = testContextManager.testContext.applicationContext.getBean("redisTemplate", RedisTemplate::class.java) as RedisTemplate<String, String>
        redisTemplate.execute { connection -> connection.serverCommands().flushAll() }
    }
}
