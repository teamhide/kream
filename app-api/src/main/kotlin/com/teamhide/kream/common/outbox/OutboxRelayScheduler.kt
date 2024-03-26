package com.teamhide.kream.common.outbox

import com.teamhide.kream.product.application.service.BiddingKafkaAdapter
import com.teamhide.kream.product.domain.event.BiddingCompletedEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.annotation.Profile
import org.springframework.data.domain.PageRequest
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger { }

@Component
class OutboxRelayScheduler(
    private val outboxRepository: OutboxRepository,
    private val biddingKafkaAdapter: BiddingKafkaAdapter,
) {
    @Async
    @Profile("!test")
    @Scheduled(fixedDelay = 1000)
    fun execute() {
        val pageRequest = PageRequest.of(0, 10)
        outboxRepository.findAllBy(pageable = pageRequest).forEach {
            when (it.aggregateType) {
                AggregateType.BIDDING_COMPLETED -> {
                    logger.info { "Relay BIDDING_COMPLETED Event" }
                    val event = AggregateTypeMapper.from<BiddingCompletedEvent>(payload = it.payload)
                    biddingKafkaAdapter.sendBiddingCompleted(event = event)
                }
            }
        }
    }
}
