package com.teamhide.kream.batch.outbox.relay.processor

import com.teamhide.kream.common.outbox.AggregateType
import com.teamhide.kream.common.outbox.AggregateTypeMapper
import com.teamhide.kream.common.outbox.Outbox
import com.teamhide.kream.product.adapter.out.messaging.BiddingKafkaAdapter
import com.teamhide.kream.product.domain.event.BiddingCompletedEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger { }

@Component
class RelayOutboxItemProcessor(
    private val biddingKafkaAdapter: BiddingKafkaAdapter,
) : ItemProcessor<Outbox, Outbox> {
    override fun process(item: Outbox): Outbox? {
        if (item.aggregateType != AggregateType.BIDDING_COMPLETED) {
            return null
        }
        logger.info { "Batch Relay BIDDING_COMPLETED Event" }
        val event = AggregateTypeMapper.from<BiddingCompletedEvent>(payload = item.payload)
        biddingKafkaAdapter.sendBiddingCompleted(event = event)
        return item
    }
}
