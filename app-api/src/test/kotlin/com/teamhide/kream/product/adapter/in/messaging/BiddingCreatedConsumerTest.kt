package com.teamhide.kream.product.adapter.`in`.messaging

import com.teamhide.kream.product.domain.usecase.SaveOrUpdateProductDisplayUseCase
import com.teamhide.kream.product.domain.vo.BiddingType
import com.teamhide.kream.product.makeBiddingCreatedEvent
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class BiddingCreatedConsumerTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val saveOrUpdateProductDisplayUseCase = mockk<SaveOrUpdateProductDisplayUseCase>()
    val biddingCreatedProductConsumer = BiddingCreatedProductConsumer(
        saveOrUpdateProductDisplayUseCase = saveOrUpdateProductDisplayUseCase,
    )

    Given("biddingType이 SALE이 아닌") {
        val event = makeBiddingCreatedEvent(biddingType = BiddingType.PURCHASE.name)

        When("메시지를 컨슈밍하면") {
            biddingCreatedProductConsumer.listen(message = event)

            Then("아무런 작업을 하지 않는다") {
                verify(exactly = 0) { saveOrUpdateProductDisplayUseCase.execute(any()) }
            }
        }
    }

    Given("biddingType이 biddingType VO가 아닌 값인") {
        val event = makeBiddingCreatedEvent(biddingType = "test")

        When("메시지를 컨슈밍하면") {
            biddingCreatedProductConsumer.listen(message = event)

            Then("아무런 작업을 하지 않는다") {
                verify(exactly = 0) { saveOrUpdateProductDisplayUseCase.execute(any()) }
            }
        }
    }

    Given("biddingType이 SALE인") {
        val event = makeBiddingCreatedEvent(biddingType = BiddingType.SALE.name)
        every { saveOrUpdateProductDisplayUseCase.execute(any()) } returns Unit

        When("메시지를 컨슈밍하면") {
            biddingCreatedProductConsumer.listen(message = event)

            Then("생성 또는 수정 메소드를 호출한다") {
                verify(exactly = 1) { saveOrUpdateProductDisplayUseCase.execute(any()) }
            }
        }
    }
})
