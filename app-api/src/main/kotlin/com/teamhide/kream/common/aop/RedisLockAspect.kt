package com.teamhide.kream.common.aop

import com.teamhide.kream.common.util.lock.LockAcquireFailException
import com.teamhide.kream.common.util.lock.RedisLock
import io.github.oshai.kotlinlogging.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RedissonClient
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
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
        val key = annotation.key

        val uniqueKey = parseSPEL(
            parameterNames = signature.parameterNames,
            args = joinPoint.args,
            key = key,
        )

        val lockKey = "$LOCK_PREFIX:$key:$uniqueKey"
        val lock = redissonClient.getLock(lockKey)
        val isLocked = try {
            lock.tryLock(waitTime, leaseTime, timeUnit)
        } catch (e: InterruptedException) {
            logger.error { "RedisLockAspect | tryLock fail. ex=$e" }
            throw LockAcquireFailException()
        }

        if (!isLocked) {
            logger.warn { "RedisLockAspect | tryLock fail" }
            throw LockAcquireFailException()
        }

        return try {
            lockTransaction.proceed(joinPoint = joinPoint)
        } finally {
            try {
                lock.unlock()
            } catch (e: IllegalMonitorStateException) {
                logger.warn { "RedisLockAspect | unlock fail. ex=$e" }
            }
        }
    }

    private fun parseSPEL(parameterNames: Array<String>, args: Array<Any>, key: String): Any {
        val parser = SpelExpressionParser()
        val context = StandardEvaluationContext()

        for (i in parameterNames.indices) {
            context.setVariable(parameterNames[i], args[i])
        }

        return try {
            parser.parseExpression(key).getValue(context) as Any
        } catch (e: Exception) {
            logger.error { "RedisLockAspect | $e" }
            throw LockAcquireFailException()
        }
    }
}
