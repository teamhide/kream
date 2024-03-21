package com.teamhide.kream.product.application.service

import com.ninjasquad.springmockk.MockkBean
import com.teamhide.kream.client.WebClientException
import com.teamhide.kream.client.makeAttemptPaymentCommand
import com.teamhide.kream.client.pg.AttemptPaymentResponse
import com.teamhide.kream.client.pg.PgClient
import com.teamhide.kream.support.test.IntegrationTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import org.springframework.http.HttpStatus

@MockkBean(value = [PgClient::class])
@IntegrationTest
internal class AttemptPaymentServiceTest(
    private val attemptPaymentService: AttemptPaymentService,
    private val pgClient: PgClient,
) : BehaviorSpec({
    Given("PG결제에서 에러가 발생할 때") {
        val command = makeAttemptPaymentCommand()
        every { pgClient.attemptPayment(any()) } throws WebClientException(
            statusCode = HttpStatus.BAD_REQUEST, message = ""
        )

        When("결제 요청을 진행하면") {
            Then("성공한다") {
                shouldThrow<WebClientException> { attemptPaymentService.execute(command = command) }
            }
        }
    }

    Given("특정 건에 대해") {
        val command = makeAttemptPaymentCommand()
        every { pgClient.attemptPayment(any()) } returns AttemptPaymentResponse(paymentId = "paymentId")

        When("결제 요청을 진행하면") {
            val sut = attemptPaymentService.execute(command = command)

            Then("성공한다") {
                sut shouldNotBe null
            }
        }
    }
})
