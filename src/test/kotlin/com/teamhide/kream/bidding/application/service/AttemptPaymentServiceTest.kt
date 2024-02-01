package com.teamhide.kream.bidding.application.service

import com.teamhide.kream.bidding.adapter.out.external.PgClientAdapter
import com.teamhide.kream.client.makeAttemptPaymentCommand
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk

class AttemptPaymentServiceTest : BehaviorSpec({
    val pgClientAdapter = mockk<PgClientAdapter>()
    val attemptPaymentService = AttemptPaymentService(pgClientAdapter = pgClientAdapter)

    Given("특정 건에 대해") {
        val command = makeAttemptPaymentCommand()
        every { pgClientAdapter.attemptPayment(biddingId = command.biddingId, price = command.price, userId = command.userId) } returns "uuid"

        When("결제 요청을 진행하면") {
            val sut = attemptPaymentService.execute(command = command)

            Then("성공한다") {
                sut shouldNotBe null
            }
        }
    }
})
