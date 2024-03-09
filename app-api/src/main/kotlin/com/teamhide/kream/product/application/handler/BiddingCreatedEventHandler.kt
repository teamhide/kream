package com.teamhide.kream.product.application.handler

import com.teamhide.kream.product.application.service.BiddingKafkaAdapter
import com.teamhide.kream.product.domain.event.BiddingCreatedEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class BiddingCreatedEventHandler(
    private val biddingKafkaAdapter: BiddingKafkaAdapter,
) {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(event: BiddingCreatedEvent) {
        biddingKafkaAdapter.sendBiddingCreated(event = event)
    }
}
