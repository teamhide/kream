package com.teamhide.kream.bidding.application.handler

import com.teamhide.kream.bidding.adapter.out.messaging.BiddingKafkaAdapter
import com.teamhide.kream.bidding.domain.event.BiddingCompletedEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class BiddingCompletedEventHandler(
    private val biddingKafkaAdapter: BiddingKafkaAdapter,
) {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(event: BiddingCompletedEvent) {
        biddingKafkaAdapter.sendBiddingCompleted(event = event)
    }
}
