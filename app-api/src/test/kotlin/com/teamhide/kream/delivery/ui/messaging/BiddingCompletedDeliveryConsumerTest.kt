package com.teamhide.kream.delivery.ui.messaging

import com.teamhide.kream.delivery.domain.usecase.InitializeDeliveryUseCase
import com.teamhide.kream.product.domain.event.BiddingCompletedEvent
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

internal class BiddingCompletedDeliveryConsumerTest : BehaviorSpec({
    val initializeDeliveryUseCase = mockk<InitializeDeliveryUseCase>()
    val consumer = BiddingCompletedDeliveryConsumer(
        initializedDeliveryUseCase = initializeDeliveryUseCase,
    )

    Given("BiddingCompletedDeliveryConsumer") {
        val event = BiddingCompletedEvent(productId = 1L, biddingId = 1L)
        every { initializeDeliveryUseCase.execute(any()) } returns Unit

        When("입찰 완료 메시지를 컨슈밍하면") {
            consumer.listen(message = event)

            Then("배송 초기 정보를 설정한다") {
                verify(exactly = 1) { initializeDeliveryUseCase.execute(any()) }
            }
        }
    }
})
