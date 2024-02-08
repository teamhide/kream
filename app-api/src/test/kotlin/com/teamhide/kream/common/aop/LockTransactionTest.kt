package com.teamhide.kream.common.aop

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.aspectj.lang.ProceedingJoinPoint

class LockTransactionTest : StringSpec({
    val lockTransaction = LockTransaction()

    "새롭게 트랜잭션을 시작한다" {
        // Given
        val joinPoint = mockk<ProceedingJoinPoint>()
        every { joinPoint.proceed() } returns "abc"

        // When
        val sut = lockTransaction.proceed(joinPoint = joinPoint)

        // Then
        sut shouldBe "abc"
    }
})
