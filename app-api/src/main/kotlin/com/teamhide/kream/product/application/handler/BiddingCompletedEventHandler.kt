package com.teamhide.kream.product.application.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.teamhide.kream.common.outbox.AggregateType
import com.teamhide.kream.common.outbox.Outbox
import com.teamhide.kream.common.outbox.OutboxRepository
import com.teamhide.kream.product.domain.event.BiddingCompletedEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class BiddingCompletedEventHandler(
    private val outboxRepository: OutboxRepository,
    private val objectMapper: ObjectMapper,
) {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handle(event: BiddingCompletedEvent) {
        val outbox = Outbox(
            aggregateType = AggregateType.BIDDING_COMPLETED,
            payload = objectMapper.writeValueAsString(event),
        )
        outboxRepository.save(outbox)
    }
}
