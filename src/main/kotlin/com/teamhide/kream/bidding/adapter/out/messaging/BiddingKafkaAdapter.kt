package com.teamhide.kream.bidding.adapter.out.messaging

import com.teamhide.kream.bidding.domain.event.BiddingCompletedEvent
import com.teamhide.kream.bidding.domain.event.BiddingCreatedEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class BiddingKafkaAdapter(
    private val biddingCreatedProducer: BiddingKafkaProducer<BiddingCreatedEvent>,
    private val biddingCompletedProducer: BiddingKafkaProducer<BiddingCompletedEvent>,

    @Value("\${spring.kafka.topic.bidding-completed}")
    private val biddingCompletedTopic: String,

    @Value("\${spring.kafka.topic.bidding-created}")
    private val biddingCreatedTopic: String,
) {
    fun sendBiddingCreated(event: BiddingCreatedEvent) {
        biddingCreatedProducer.send(topic = biddingCreatedTopic, key = event.productId.toString(), message = event)
    }

    fun sendBiddingCompleted(event: BiddingCompletedEvent) {
        biddingCompletedProducer.send(topic = biddingCompletedTopic, key = event.productId.toString(), message = event)
    }
}
