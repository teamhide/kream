package com.teamhide.kream.common.aop

import com.teamhide.kream.common.util.lock.LockAcquireFailException
import com.teamhide.kream.common.util.lock.RedisLock
import io.github.oshai.kotlinlogging.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component

private const val LOCK_PREFIX = "lock:"
private val logger = KotlinLogging.logger { }

@Aspect
@Component
class RedisLockAspect(
    private val redissonClient: RedissonClient,
    private val lockTransaction: LockTransaction,
) {
    @Around("@annotation(com.teamhide.kream.common.util.lock.RedisLock)")
    fun proceed(joinPoint: ProceedingJoinPoint): Any {
        val signature = joinPoint.signature as MethodSignature
        val annotation = signature.method.getAnnotation(RedisLock::class.java)

        val timeUnit = annotation.timeUnit
        val waitTime = annotation.waitTime
        val leaseTime = annotation.leaseTime
        val key = "$LOCK_PREFIX${annotation.key.key}"

        val lock = redissonClient.getLock(key)

        val isLocked = try {
            lock.tryLock(waitTime, leaseTime, timeUnit)
        } catch (e: InterruptedException) {
            logger.error { "RedisLockAspect | ex=$e" }
            throw LockAcquireFailException()
        }

        return try {
            if (!isLocked) {
                logger.warn { "RedisLockAspect | tryLock fail" }
                throw LockAcquireFailException()
            }
            lockTransaction.proceed(joinPoint = joinPoint)
        } finally {
            try {
                lock.unlock()
            } catch (e: IllegalMonitorStateException) {
                logger.warn { "RedisLockAspect | unlock fail. ex=$e" }
            }
        }
    }
}
