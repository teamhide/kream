package com.teamhide.kream.common.exception

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.mockk
import org.springframework.http.HttpStatus
import java.lang.reflect.Method

class AsyncExceptionHandlerTest : BehaviorSpec({
    val handler = AsyncExceptionHandler()
    class TestException : CustomException(
        statusCode = HttpStatus.NOT_FOUND,
        errorCode = "TEST__ERROR",
        message = ""
    )

    Given("CustomException을 상속받은 예외가 발생하는 경우") {
        val exc = TestException()
        val method = mockk<Method>()

        When("핸들러가 동작하면") {
            Then("예외를 던지지 않는다") {
                handler.handleUncaughtException(ex = exc, method = method)
            }
        }
    }

    Given("CustomException을 상속받지 않은 예외가 발생하는 경우") {
        val exc = RuntimeException()
        val method = mockk<Method>()

        When("핸들러가 동작하면") {
            Then("예외를 던진다") {
                shouldThrow<RuntimeException> { handler.handleUncaughtException(ex = exc, method = method) }
            }
        }
    }
})
