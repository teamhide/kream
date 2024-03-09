package com.teamhide.kream.common.outbox

import com.fasterxml.jackson.databind.ObjectMapper
import com.teamhide.kream.product.application.service.BiddingKafkaAdapter
import com.teamhide.kream.product.domain.event.BiddingCompletedEvent
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

internal class OutboxRelaySchedulerTest : BehaviorSpec({
    val outboxRepository = mockk<OutboxRepository>()
    val biddingKafkaAdapter = mockk<BiddingKafkaAdapter>()
    val outboxRelayScheduler = OutboxRelayScheduler(
        outboxRepository = outboxRepository,
        biddingKafkaAdapter = biddingKafkaAdapter,
    )
    val objectMapper = ObjectMapper()

    Given("아웃박스 테이블에 이벤트가 존재할 때") {
        val biddingCompletedEvent = BiddingCompletedEvent(
            productId = 1L,
            biddingId = 1L,
        )
        val biddingCompletedOutbox = Outbox(
            aggregateType = AggregateType.BIDDING_COMPLETED,
            payload = objectMapper.writeValueAsString(biddingCompletedEvent),
        )
        every { outboxRepository.findAllBy(any()) } returns listOf(biddingCompletedOutbox)
        every { biddingKafkaAdapter.sendBiddingCompleted(any()) } returns Unit
        every { biddingKafkaAdapter.sendBiddingCreated(any()) } returns Unit

        When("스케줄링이 실행되면") {
            outboxRelayScheduler.execute()

            Then("카프카로 이벤트를 전송한다") {
                verify(exactly = 1) { biddingKafkaAdapter.sendBiddingCompleted(any()) }
            }
        }
    }
})
