package com.teamhide.kream.common.aop

import com.teamhide.kream.common.util.lock.LockAcquireFailException
import com.teamhide.kream.common.util.lock.RedisLock
import io.github.oshai.kotlinlogging.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

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
        val method = signature.method
        val annotation = method.getAnnotation(RedisLock::class.java)
        val lockKey = generateLockKey(
            parameterNames = signature.parameterNames,
            args = joinPoint.args,
            key = annotation.key,
        )

        val lock = redissonClient.getLock(lockKey)
        return if (acquireLock(lock = lock, waitTime = annotation.waitTime, leaseTime = annotation.leaseTime, timeUnit = annotation.timeUnit)) {
            try {
                lockTransaction.proceed(joinPoint)
            } finally {
                releaseLock(lock = lock)
            }
        } else {
            throw LockAcquireFailException()
        }
    }

    private fun acquireLock(lock: RLock, waitTime: Long, leaseTime: Long, timeUnit: TimeUnit): Boolean {
        return try {
            lock.tryLock(waitTime, leaseTime, timeUnit)
        } catch (e: InterruptedException) {
            logger.error(e) { "RedisLockAspect | Failed to acquire lock" }
            throw LockAcquireFailException()
        }
    }

    private fun releaseLock(lock: RLock) {
        try {
            lock.unlock()
        } catch (e: IllegalMonitorStateException) {
            logger.warn(e) { "RedisLockAspect | Failed to release lock" }
        }
    }

    private fun generateLockKey(parameterNames: Array<String>, args: Array<Any>, key: String): String {
        val uniqueKey = parseSPEL(parameterNames, args, key)
        return "$LOCK_PREFIX:$key:$uniqueKey"
    }

    private fun parseSPEL(parameterNames: Array<String>, args: Array<Any>, key: String): Any {
        val parser = SpelExpressionParser()
        val context = StandardEvaluationContext()

        parameterNames.forEachIndexed { index, paramName ->
            context.setVariable(paramName, args[index])
        }

        return try {
            parser.parseExpression(key).getValue(context) as Any
        } catch (e: Exception) {
            logger.error { "RedisLockAspect | $e" }
            throw LockAcquireFailException()
        }
    }
}
