package com.teamhide.kream.bidding.adapter.out.messaging

import com.teamhide.kream.bidding.domain.event.BiddingCompletedEvent
import com.teamhide.kream.bidding.domain.event.BiddingCreatedEvent
import com.teamhide.kream.common.kafka.KafkaBaseProducer
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class BiddingKafkaAdapter(
    private val biddingCreatedProducer: KafkaBaseProducer<BiddingCreatedEvent>,
    private val biddingCompletedProducer: KafkaBaseProducer<BiddingCompletedEvent>,

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
