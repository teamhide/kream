package com.teamhide.kream.common.aop

import com.teamhide.kream.common.util.lock.LockAcquireFailException
import com.teamhide.kream.common.util.lock.RedisLock
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import java.lang.reflect.Method

data class TestCommand(val productId: Long)

internal class RedisLockAspectTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val redissonClient = mockk<RedissonClient>()
    val lockTransaction = mockk<LockTransaction>()
    val redisLockAspect = RedisLockAspect(redissonClient = redissonClient, lockTransaction = lockTransaction)

    Given("SPEL을 파싱에서 Exception이 발생할 때") {
        val joinPoint = mockk<ProceedingJoinPoint>()
        val methodSignature = mockk<MethodSignature>()
        val method = mockk<Method>()
        every { method.getAnnotation(RedisLock::class.java) } returns RedisLock(
            key = "abasd",
        )
        every { methodSignature.parameterNames } returns arrayOf("command")
        every { joinPoint.args } returns arrayOf(TestCommand(productId = 1))
        every { methodSignature.method } returns method
        every { joinPoint.signature } returns methodSignature

        When("락을 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<LockAcquireFailException> { redisLockAspect.proceed(joinPoint = joinPoint) }
            }
        }
    }

    Given("tryLock에서 InterruptedException이 발생할 때") {
        val joinPoint = mockk<ProceedingJoinPoint>()
        val methodSignature = mockk<MethodSignature>()
        val method = mockk<Method>()
        every { method.getAnnotation(RedisLock::class.java) } returns RedisLock(
            key = "#command.productId",
        )
        every { methodSignature.parameterNames } returns arrayOf("command")
        every { joinPoint.args } returns arrayOf(TestCommand(productId = 1))
        every { methodSignature.method } returns method
        every { joinPoint.signature } returns methodSignature

        val rLock = mockk<RLock>()
        every { rLock.tryLock(any(), any(), any()) } throws InterruptedException()
        every { redissonClient.getLock(any<String>()) } returns rLock

        When("락을 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<LockAcquireFailException> { redisLockAspect.proceed(joinPoint = joinPoint) }
            }
        }
    }

    Given("tryLock이 실패할 때") {
        val joinPoint = mockk<ProceedingJoinPoint>()
        val methodSignature = mockk<MethodSignature>()
        val method = mockk<Method>()
        every { method.getAnnotation(RedisLock::class.java) } returns RedisLock(
            key = "#command.productId",
        )
        every { methodSignature.parameterNames } returns arrayOf("command")
        every { joinPoint.args } returns arrayOf(TestCommand(productId = 1))
        every { methodSignature.method } returns method
        every { joinPoint.signature } returns methodSignature

        val rLock = mockk<RLock>()
        every { rLock.tryLock(any(), any(), any()) } returns false
        every { redissonClient.getLock(any<String>()) } returns rLock
        every { rLock.unlock() } returns Unit

        When("락을 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<LockAcquireFailException> { redisLockAspect.proceed(joinPoint = joinPoint) }
            }
        }
    }

    Given("tryLock이 성공할 때") {
        val joinPoint = mockk<ProceedingJoinPoint>()
        val methodSignature = mockk<MethodSignature>()
        val method = mockk<Method>()
        every { method.getAnnotation(RedisLock::class.java) } returns RedisLock(
            key = "#command.productId",
        )
        every { methodSignature.parameterNames } returns arrayOf("command")
        every { joinPoint.args } returns arrayOf(TestCommand(productId = 1))
        every { methodSignature.method } returns method
        every { joinPoint.signature } returns methodSignature

        val rLock = mockk<RLock>()
        every { rLock.tryLock(any(), any(), any()) } returns true
        every { redissonClient.getLock(any<String>()) } returns rLock
        every { rLock.unlock() } returns Unit
        every { lockTransaction.proceed(any()) } returns Unit

        When("락을 요청하면") {
            redisLockAspect.proceed(joinPoint = joinPoint)

            Then("성공한다") {
                verify(exactly = 1) { rLock.unlock() }
                verify(exactly = 1) { lockTransaction.proceed(any()) }
            }
        }
    }

    Given("unlock에서 에러가 발생할 때") {
        val joinPoint = mockk<ProceedingJoinPoint>()
        val methodSignature = mockk<MethodSignature>()
        val method = mockk<Method>()
        every { method.getAnnotation(RedisLock::class.java) } returns RedisLock(
            key = "#command.productId",
        )
        every { methodSignature.parameterNames } returns arrayOf("command")
        every { joinPoint.args } returns arrayOf(TestCommand(productId = 1))
        every { methodSignature.method } returns method
        every { joinPoint.signature } returns methodSignature

        val rLock = mockk<RLock>()
        every { rLock.tryLock(any(), any(), any()) } returns true
        every { redissonClient.getLock(any<String>()) } returns rLock
        every { rLock.unlock() } throws IllegalMonitorStateException()
        every { lockTransaction.proceed(any()) } returns Unit

        When("락을 요청하면") {
            redisLockAspect.proceed(joinPoint = joinPoint)

            Then("성공한다") {
                verify(exactly = 1) { lockTransaction.proceed(any()) }
                verify(exactly = 1) { rLock.unlock() }
            }
        }
    }
})
