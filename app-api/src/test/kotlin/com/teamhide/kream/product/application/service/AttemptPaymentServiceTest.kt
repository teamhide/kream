package com.teamhide.kream.product.application.service

import com.teamhide.kream.client.WebClientException
import com.teamhide.kream.client.makeAttemptPaymentCommand
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus

internal class AttemptPaymentServiceTest : BehaviorSpec({
    val pgClientAdapter = mockk<PgClientAdapter>()
    val attemptPaymentService = AttemptPaymentService(pgClientAdapter = pgClientAdapter)

    Given("PG결제에서 에러가 발생할 때") {
        val command = makeAttemptPaymentCommand()
        every {
            pgClientAdapter.attemptPayment(
                biddingId = command.biddingId,
                price = command.price,
                userId = command.userId
            )
        } returns Result.failure(WebClientException(message = "error", statusCode = HttpStatus.BAD_REQUEST))

        When("결제 요청을 진행하면") {
            Then("성공한다") {
                shouldThrow<WebClientException> { attemptPaymentService.execute(command = command) }
            }
        }
    }

    Given("특정 건에 대해") {
        val command = makeAttemptPaymentCommand()
        every { pgClientAdapter.attemptPayment(biddingId = command.biddingId, price = command.price, userId = command.userId) } returns Result.success("uuid")

        When("결제 요청을 진행하면") {
            val sut = attemptPaymentService.execute(command = command)

            Then("성공한다") {
                sut shouldNotBe null
            }
        }
    }
})
