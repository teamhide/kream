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
    Given("AttemptPaymentService") {
        val command = makeAttemptPaymentCommand()

        When("PG결제에서 에러가 발생할 때 결제 요청을 진행하는 경우") {
            every { pgClient.attemptPayment(any()) } throws WebClientException(
                statusCode = HttpStatus.BAD_REQUEST, message = ""
            )

            Then("성공한다") {
                shouldThrow<WebClientException> { attemptPaymentService.execute(command = command) }
            }
        }

        When("PG결제가 정상일 때 특정 건에 대해 결제 요청을 진행하면") {
            every { pgClient.attemptPayment(any()) } returns AttemptPaymentResponse(paymentId = "paymentId")
            val sut = attemptPaymentService.execute(command = command)

            Then("성공한다") {
                sut shouldNotBe null
            }
        }
    }
})
