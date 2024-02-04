package com.teamhide.kream.product.adapter.`in`.messaging

import com.teamhide.kream.bidding.domain.vo.BiddingType
import com.teamhide.kream.product.domain.usecase.SaveOrUpdateProductDisplayUseCase
import com.teamhide.kream.product.makeBiddingCreatedEvent
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class BiddingCreatedConsumerTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val saveOrUpdateProductDisplayUseCase = mockk<SaveOrUpdateProductDisplayUseCase>()
    val biddingCreatedConsumer = BiddingCreatedConsumer(
        saveOrUpdateProductDisplayUseCase = saveOrUpdateProductDisplayUseCase,
    )

    Given("biddingType이 PURCHASE가 아닌") {
        val event = makeBiddingCreatedEvent(biddingType = BiddingType.SALE.name)

        When("메시지를 컨슈밍하면") {
            biddingCreatedConsumer.listen(message = event)

            Then("아무런 작업을 하지 않는다") {
                verify(exactly = 0) { saveOrUpdateProductDisplayUseCase.execute(any()) }
            }
        }
    }

    Given("biddingType이 biddingType VO가 아닌 값인") {
        val event = makeBiddingCreatedEvent(biddingType = "test")

        When("메시지를 컨슈밍하면") {
            biddingCreatedConsumer.listen(message = event)

            Then("아무런 작업을 하지 않는다") {
                verify(exactly = 0) { saveOrUpdateProductDisplayUseCase.execute(any()) }
            }
        }
    }

    Given("biddingType이 PURCHASE인") {
        val event = makeBiddingCreatedEvent(biddingType = BiddingType.PURCHASE.name)
        every { saveOrUpdateProductDisplayUseCase.execute(any()) } returns Unit

        When("메시지를 컨슈밍하면") {
            biddingCreatedConsumer.listen(message = event)

            Then("아무런 작업을 하지 않는다") {
                verify(exactly = 1) { saveOrUpdateProductDisplayUseCase.execute(any()) }
            }
        }
    }
})